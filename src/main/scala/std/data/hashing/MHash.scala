package std.data.hashing

import std.algebra.{Monoid, Order}
import std.data.Need
import std.evidence.Eq

/**
 *
 * https://www.youtube.com/watch?v=zQB1erzYxdI
 * https://www.rocq.inria.fr/secret/Jean-Pierre.Tillich/publications/HashingSL2.pdf
 * https://www.iacr.org/archive/crypto2000/18800288/18800288.pdf
 * http://lib.dr.iastate.edu/cgi/viewcontent.cgi?article=15807&context=rtd
 * https://www.youtube.com/watch?v=hJCv5KDMAFI
 * https://ticki.github.io/blog/designing-a-good-non-cryptographic-hash-function/
 */
final case class MHash(value: Int) extends Comparable[MHash] {
  import MHash._

  def |+|(that: MHash): MHash =
    MHash(combineRaw(this.value, that.value))

  def update(b: Byte): MHash = this |+| fromByte(b)
  def update(b: Short): MHash = this |+| fromShort(b)
  def update(b: Char): MHash = this |+| fromChar(b)
  def update(b: Int): MHash = this |+| fromInt(b)
  def update(b: Long): MHash = this |+| fromLong(b)
  def update(b: Array[Byte]): MHash = this |+| fromByteArray(b)

  override def toString: String =
    s"MHash(0x${java.lang.Integer.toHexString(value)})"
  override def hashCode(): Int = value
  override def clone(): AnyRef = this
  override def compareTo(o: MHash): Int =
    java.lang.Integer.compareUnsigned(this.value, o.value)
}
object MHash {
  val empty: MHash = MHash(0)

  def fromByte(x: Byte): MHash =
    MHash(table(x.toInt & 0xFF))

  def fromShort(x: Short): MHash = {
    val low = x & 0xFF
    val high = x >>> 8
    MHash(combineRaw(
      table(low),
      table(high)))
  }

  def fromChar(x: Char): MHash =
    fromShort(x.toShort)

  def fromInt(x: Int): MHash = {
    val a0 = x & 0xFF
    val a1 = (x >>> 8) & 0xFF
    val a2 = (x >>> 16) & 0xFF
    val a3 = (x >>> 24) & 0xFF

    val h1 = combineRaw(
      table(a0),
      table(a1))
    val h2 = combineRaw(
      table(a2),
      table(a3))
    MHash(combineRaw(h1, h2))
  }

  // FIXME: inline
  def fromLong(x: Long): MHash =
    fromInt(x.toInt) |+| fromInt((x >>> 32).toInt)

  def fromByteArray(array: Array[Byte]): MHash = {
    var r = 0
    var i = 0
    val length = array.length
    while (i < length) {
      r = combineRaw(r, table(array(i).toInt & 0xFF))
      i += 1
    }
    MHash(r)
  }

  private[std] def combineRaw(x: Int, y: Int): Int = {
    val x0 = x >>> 16
    val x1 = x & 0xFFFF
    val x3 = (x1 << 1) | 1
    val y0 = y >>> 16
    val y1 = y & 0xFFFF
    val z0 = (x0 + x3 * y0) & 0xFFFF
    val z1 = (x1 + x3 * y1) & 0xFFFF
    (z0 << 16) | z1
  }

  private[std] val table: Array[Int] = Array(
    0x8cc9b9a0, 0x58c0f709, 0x263225a8, 0x2be26ab7,
    0xc63fb5a9, 0x7de36137, 0x3baefe17, 0x80303abd,
    0x5d9b25c5, 0xf4812ce, 0xc41976ad, 0x49c8e38e,
    0x6d04db43, 0x266cd9a3, 0x26c3422d, 0xc896bde2,
    0x2eb35c50, 0x6dc94dce, 0x38ca2e1b, 0x7b8de4bd,
    0x854896ef, 0x35d99b6a, 0xd04b6080, 0x3c7a0ef,
    0xd43cc644, 0x9416c57a, 0x2baa99b1, 0xe8c52a8d,
    0x353e50d1, 0xfea54a70, 0xb3f14f5, 0xf946ef59,
    0xee1fdfb4, 0xa7289525, 0xd2824ab2, 0x3f13d312,
    0xc56ec7f, 0x5e9c3e59, 0xc3896183, 0x93b6c402,
    0xe500ae92, 0xbaa33f08, 0x97e0b4dd, 0x9d5a190a,
    0x84e8ebc3, 0x636242a2, 0x6a2deb80, 0x5c7620b3,
    0x6ed46065, 0xee8ce600, 0x21cbc23, 0xb8d831c2,
    0xcf5a3956, 0x916c6e86, 0x13e2cedd, 0xf1d4135e,
    0x430b391b, 0x4a2fcad5, 0x7cb159db, 0x2ed3503b,
    0x646a1d8d, 0x4f9ae788, 0x7a7eac2b, 0x2298b51,
    0x9352c86e, 0xe7224eaa, 0x3131c8e6, 0xa92c6c12,
    0x2a236e89, 0xb4d96a46, 0xa7dd5155, 0xd36c8174,
    0x58313631, 0xe061a8de, 0xca4c7b80, 0x2446b2bf,
    0x64d059d0, 0xd1df5566, 0x949a220, 0x2d2036a1,
    0x8ff1ee99, 0x332486ae, 0x8bdcc13b, 0x83ed69d5,
    0x5e48dda7, 0xe275cc66, 0x6a9aa174, 0x620b8c98,
    0xeea1eda3, 0x6e0c8443, 0xffd1c01e, 0xa22f00ae,
    0x980515fc, 0x7e695af4, 0xd8aa2485, 0xe3ade969,
    0x3a2e696e, 0x13b79289, 0x3536dbd6, 0x81c3450f,
    0xeaac75bf, 0x68d116f9, 0xfaed2fd0, 0x60f60157,
    0xcd83712b, 0xff93848b, 0x663f0c7e, 0xe64b29de,
    0x954d6f65, 0x737dc4ff, 0xab771f3f, 0xb0d37342,
    0x10f4b9b1, 0x34e0815, 0xde471b28, 0x5dddbb25,
    0x55b8c5f0, 0x72135a10, 0xa7f90b8e, 0x109db701,
    0xaf06f114, 0x8c032f36, 0x93f1a918, 0x1bc0ac8d,
    0xd3a1f3c1, 0x81a808b2, 0x2e03365b, 0xced289b,
    0xb9d5ee2e, 0xfb84e97f, 0x74ecf139, 0x91d834cf,
    0x487924e1, 0x8273d015, 0xddcbf2be, 0x709f24a1,
    0x42228336, 0x9098ec55, 0x470cc738, 0xaf29d3d5,
    0xe4c668f7, 0xa0064640, 0xf9aa1e8b, 0x3448d984,
    0xf8a1575f, 0x6e4998a6, 0x71af37f2, 0xaa6d23ac,
    0xc7861c42, 0xae7c216e, 0x6157c956, 0x147ee9d1,
    0x1da3037a, 0x2bdf5baf, 0x2e644a22, 0x4b40b3a9,
    0xb75798b4, 0xb1d0551b, 0xf68161b1, 0xf906d4f2,
    0xb9ff589b, 0xa985f807, 0xac18bb95, 0x90cfb9d,
    0x8500cf69, 0xf595e214, 0xe3c56df8, 0x42b6faa6,
    0xc51ceb24, 0x74dcdb8c, 0x4b22e301, 0x7b78ba11,
    0x47dd8267, 0x2249e810, 0xb7da59b2, 0x134354ad,
    0xcb42b981, 0x6a752f49, 0x1c3f8062, 0xa7c9dcc0,
    0x64ef42bb, 0x349d9864, 0x2c1a0196, 0xcda68673,
    0x222bdfa4, 0x5f4896b, 0x91d1527b, 0xc47cd3dc,
    0xcf8ce91a, 0x8aff1884, 0x58f3113d, 0xba1d1d2d,
    0x8510b6a0, 0xc919e33b, 0xfb20bf8, 0xe8ebe37f,
    0xcb8ff27c, 0x2a14323a, 0xbf75bd1c, 0x57bbebf3,
    0xea4e70fb, 0x898d7b97, 0xe84cdec2, 0x75e1ee8a,
    0x1674fa29, 0x7987e026, 0xa8f36189, 0xad0bef48,
    0x99948e58, 0x522aca48, 0xe0e86f0d, 0x8a591193,
    0x1ff7e09a, 0x360aaba5, 0x33898aa5, 0x946de2ce,
    0x4189540c, 0xecbb2fd0, 0x682c5bc6, 0xaef8f072,
    0x8d435f02, 0x89e95037, 0x86af62d7, 0x5e87d469,
    0xb127c71b, 0xb7a83c66, 0xa9caf024, 0x9943a9d6,
    0xe56d7116, 0x1019969e, 0xaf6424f0, 0x3cd9f962,
    0x19e48be5, 0xeea03e6d, 0x77769044, 0x83daad54,
    0x467c4cfe, 0xe06ce572, 0xfff57ee, 0xaa3690bd,
    0xe1ce4f9c, 0xfcb4afc7, 0x3ab80f36, 0x53e9e77c,
    0x258d93a, 0x14cda876, 0x74556574, 0xaefc5ef4,
    0x154c9356, 0xc17ef3eb, 0x27a5003d, 0x96868e60,
    0x7082c235, 0xec677cee, 0x8a1e947a, 0x376dd181)

  implicit val stdDataHashingMHashEq: Eq.Univ[MHash] =
    Eq.fromUniversalEquals[MHash]

  implicit val stdDataHashingMHashInstance: Monoid[MHash] with Hash[MHash] with Order[MHash] =
    new Monoid[MHash] with Hash[MHash] with Order[MHash] {
      override def empty: MHash = MHash.empty

      override def bytes(z: MHash, bytes: Array[Byte]): MHash =
        z.update(bytes)

      override def combine(x: MHash, y: MHash): MHash =
        x |+| y

      override def eqv(x: MHash, y: MHash): Boolean =
        x.value == y.value

      override def compare(x: MHash, y: MHash): Int =
        java.lang.Integer.compareUnsigned(x.value, y.value)
    }
}
