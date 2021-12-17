package day16

const val LITERAL_PACKET_TYPE_ID = 4

const val HEADER_SIZE = 6
const val LENGTH_TYPE_ID_SIZE = 1
const val SUBPACKET_LENGTH_SIZE = 15
const val SUBPACKET_NUMBER_SIZE = 11

typealias ValueAndRemainder=Pair<Int, String>

fun main() {

    fun readLiteralValue(binaryString: String): ValueAndRemainder {
        val valueString = binaryString.chunked(5)
            .fold(Pair("", true)) { acc, value -> if(acc.second) Pair(acc.first + value.substring(1), value.first() == '1') else acc}
            .first
        return ValueAndRemainder(valueString.toInt(2), binaryString.substring(valueString.length + valueString.length / 4))
    }

    fun createLiteralPacket(binaryString: String): Pair<LiteralValuePacket, String> {
        val header = PacketHeader(binaryString)
        val valueAndRemainder = readLiteralValue(binaryString.substring(HEADER_SIZE))
        return Pair(LiteralValuePacket(header, valueAndRemainder.first), valueAndRemainder.second)
    }

    fun createSubpacketsFixedLength(binaryString: String): List<Packet> {
        if(binaryString.length < HEADER_SIZE) {
            return emptyList()
        }
        val header = PacketHeader(binaryString)
        println("Parse sub packets, type id: ${header.typeId}")
        when (header.typeId) {
            LITERAL_PACKET_TYPE_ID -> {
                val literalPacketAndRemainder: Pair<LiteralValuePacket, String> = createLiteralPacket(binaryString)
                return buildList {
                    add(literalPacketAndRemainder.first)
                    addAll(createSubpacketsFixedLength(literalPacketAndRemainder.second))
                }
            }
            else -> {
                return emptyList()
            }
        }
    }

    fun createSubpacketsFixedNumber(binaryString: String, numOfPackets: Int) : Pair<List<Packet>, String> {
        throw NotImplementedError("Soon")
    }

    fun createOperatorPacket(binaryString: String): Pair<OperatorPacket, String> {
        val header = PacketHeader(binaryString)
        val lengthTypeId = binaryString.drop(HEADER_SIZE).take(LENGTH_TYPE_ID_SIZE)
        if(lengthTypeId == "0") {
            val totalPacketLength = binaryString.drop(HEADER_SIZE + LENGTH_TYPE_ID_SIZE).take(SUBPACKET_LENGTH_SIZE).toInt(2)
            val remainder = binaryString.drop(HEADER_SIZE + LENGTH_TYPE_ID_SIZE + SUBPACKET_LENGTH_SIZE)
            return Pair(OperatorPacket(header, createSubpacketsFixedLength(remainder.take(totalPacketLength))), remainder.drop(totalPacketLength))
        } else {
            val numOfSubpackets = binaryString.drop(HEADER_SIZE + LENGTH_TYPE_ID_SIZE).take(SUBPACKET_NUMBER_SIZE).toInt(2)
            val result = createSubpacketsFixedNumber(binaryString.drop(HEADER_SIZE + LENGTH_TYPE_ID_SIZE + SUBPACKET_NUMBER_SIZE), numOfSubpackets)
            return Pair(OperatorPacket(header, result.first), result.second)
        }
    }

//    val testinput = readInput("day16/day16_test")
    val literalPacket = "D2FE28".hexToBinaryString()


    val operatorPacket = createOperatorPacket("00111000000000000110111101000101001010010001001000000000")
    println(operatorPacket)



}

fun String.hexToBinaryString(): String {
    return this.toInt(16).toString(2)
}

data class PacketHeader(val version: Int, val typeId: Int) {
    constructor(packetAsString: String) : this(packetAsString.substring(0, 3).toInt(2), packetAsString.substring(3, 6).toInt(2))
}

interface Packet {

}

data class LiteralValuePacket(val header: PacketHeader, val decimalValue: Int): Packet {


}

data class OperatorPacket(val header: PacketHeader, val subPackets: List<Packet>): Packet {

}



