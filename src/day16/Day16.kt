package day16

import readInput
import java.lang.IllegalStateException
import java.math.BigInteger

const val LITERAL_PACKET_TYPE_ID = 4

const val HEADER_SIZE = 6
const val LENGTH_TYPE_ID_SIZE = 1
const val SUBPACKET_LENGTH_SIZE = 15
const val SUBPACKET_NUMBER_SIZE = 11

typealias ValueAndRemainder = Pair<BigInteger, String>

fun main() {

    fun part1(input: String) {
        val outermostPacket = createPacket(input)
        println("Day 16 part 1. Version sum: ${outermostPacket.first.versionSum()}")
    }

    fun part2(input: String) {
        val outermostPacket = createPacket(input)
        println("Day 16 part 2. Total value: ${outermostPacket.first.value()}")
    }

    val testinput = "620080001611562C8802118E34".hexToBinaryString()
    val input = readInput("day16/day16").first().hexToBinaryString()

    part1(input)
    part2(input)

}

fun readLiteralValue(binaryString: String): ValueAndRemainder {
    val valueString = binaryString.chunked(5)
        .fold(Pair("", true)) { acc, value ->
            if (acc.second) Pair(
                acc.first + value.substring(1),
                value.first() == '1'
            ) else acc
        }
        .first
    return ValueAndRemainder(
        valueString.toBigInteger(2),
        binaryString.substring(valueString.length + valueString.length / 4)
    )
}

fun createLiteralPacket(binaryString: String): Pair<LiteralValuePacket, String> {
    val header = PacketHeader(binaryString)
    val valueAndRemainder = readLiteralValue(binaryString.substring(HEADER_SIZE))
    val remainder = if (valueAndRemainder.second.all { it == '0' }) "" else valueAndRemainder.second
    return Pair(LiteralValuePacket(header, valueAndRemainder.first), remainder)
}

fun createSubpacketsFixedLength(binaryString: String): List<Packet> {
    if (binaryString.length < HEADER_SIZE) {
        return emptyList()
    }

    val packetResult = createPacket(binaryString)
    return buildList {
        add(packetResult.first)
        addAll(createSubpacketsFixedLength(packetResult.second))
    }

}

fun createSubpacketsFixedNumber(
    binaryString: String,
    numOfPackets: Int
): Pair<List<Packet>, String> {
    var currentBinaryString = binaryString
    var operatorPackets = mutableListOf<Packet>()
    repeat(numOfPackets) {
        val result = createPacket(currentBinaryString)
        currentBinaryString = result.second
        operatorPackets.add(result.first)
    }
    return Pair(operatorPackets, currentBinaryString)
}

fun createPacket(binaryString: String): Pair<Packet, String> {
    val header = PacketHeader(binaryString)
    when (header.typeId) {
        LITERAL_PACKET_TYPE_ID -> {
            return createLiteralPacket(binaryString)
        }
        else -> {
            val lengthTypeId = binaryString.drop(HEADER_SIZE).take(LENGTH_TYPE_ID_SIZE)
            if (lengthTypeId == "0") {
                val totalPacketLength =
                    binaryString.drop(HEADER_SIZE + LENGTH_TYPE_ID_SIZE).take(SUBPACKET_LENGTH_SIZE)
                        .toInt(2)
                val remainder =
                    binaryString.drop(HEADER_SIZE + LENGTH_TYPE_ID_SIZE + SUBPACKET_LENGTH_SIZE)
                return Pair(
                    OperatorPacket(
                        header,
                        createSubpacketsFixedLength(remainder.take(totalPacketLength))
                    ), remainder.drop(totalPacketLength)
                )
            } else {
                val numOfSubpackets =
                    binaryString.drop(HEADER_SIZE + LENGTH_TYPE_ID_SIZE).take(SUBPACKET_NUMBER_SIZE)
                        .toInt(2)
                val result = createSubpacketsFixedNumber(
                    binaryString.drop(HEADER_SIZE + LENGTH_TYPE_ID_SIZE + SUBPACKET_NUMBER_SIZE),
                    numOfSubpackets
                )
                return Pair(OperatorPacket(header, result.first), result.second)
            }
        }
    }
}

fun String.hexToBinaryString(): String {
    return this.toBigInteger(16).toString(2)
}

data class PacketHeader(val version: Int, val typeId: Int) {
    constructor(packetAsString: String) : this(
        packetAsString.substring(0, 3).toInt(2),
        packetAsString.substring(3, 6).toInt(2)
    )
}

interface Packet {
    fun versionSum(): Int

    fun value(): BigInteger
}

data class LiteralValuePacket(val header: PacketHeader, val value: BigInteger) : Packet {
    override fun versionSum(): Int {
        return header.version
    }

    override fun value(): BigInteger {
        return value
    }


}

data class OperatorPacket(val header: PacketHeader, val subPackets: List<Packet>) : Packet {
    override fun versionSum(): Int {
        return subPackets
            .map { it.versionSum() }
            .sum() + header.version
    }

    override fun value(): BigInteger {
        return when (header.typeId) {
            0 -> subPackets.fold(BigInteger("0")) { acc, packet -> acc.add(packet.value()) }
            1 -> subPackets.fold(BigInteger("1")) { acc, packet -> acc.multiply(packet.value()) }
            2 -> subPackets.minOf { it.value() }
            3 -> subPackets.maxOf { it.value() }
            5 -> if (subPackets.first().value() > subPackets.last()
                    .value()
            ) BigInteger("1") else BigInteger("0")
            6 -> if (subPackets.first().value() < subPackets.last()
                    .value()
            ) BigInteger("1") else BigInteger("0")
            7 -> if (subPackets.first().value() == subPackets.last()
                    .value()
            ) BigInteger("1") else BigInteger("0")
            else -> throw IllegalStateException("Unknown type id ${header.typeId}")
        }
    }


}



