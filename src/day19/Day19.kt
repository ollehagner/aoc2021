package day19

import common.Point3D
import readInput
import java.util.*
import kotlin.collections.HashSet

fun parseScanners3D(input: List<String>): List<Scanner> {
    return input
        .filter { it.isNotEmpty() }
        .fold(mutableListOf<MutableSet<Point3D>>()) { acc, line ->
            if (line.contains("scanner")) {
                acc.add(HashSet())
            } else {
                val values = line.split(",")
                acc.last().add(Point3D(values[0].toInt(), values[1].toInt(), values[2].toInt()))
            }
            acc
        }
        .map { Scanner(it) }
}

fun main() {

    fun part1(scanners: List<Scanner>) {
        val scannerQueue = LinkedList(scanners
            .drop(1)
            .map { it.allOrientations() })

        var mainScanner = scanners.first()

        while(scannerQueue.isNotEmpty()) {
            val scannerOrientationsToCheckForOverlap = scannerQueue.pop()!!
            val potentialOverlap = scannerOrientationsToCheckForOverlap
                .firstOrNull { it.overlaps(mainScanner) }
            if(potentialOverlap != null) {
                mainScanner = mainScanner.merge(potentialOverlap)
            } else {
                scannerQueue.addLast(scannerOrientationsToCheckForOverlap)
            }
        }

        println("Day 19, part 1. Total num of beacons : ${mainScanner.beaconVectors.size}")
    }

    fun part2(scanners: List<Scanner>) {
        val scannerQueue = LinkedList(scanners
            .drop(1)
            .map { it.allOrientations() })

        var mainScanner = scanners.first()
        val scannerPositions = mutableListOf<Point3D>(mainScanner.self)

        while(scannerQueue.isNotEmpty()) {
            val scannerOrientationsToCheckForOverlap = scannerQueue.pop()!!
            val potentialOverlap = scannerOrientationsToCheckForOverlap
                .firstOrNull { it.overlaps(mainScanner) }
            if(potentialOverlap != null) {
                mainScanner = mainScanner.merge(potentialOverlap)
                scannerPositions.add(mainScanner.otherScannerPosition(potentialOverlap))
            } else {
                scannerQueue.addLast(scannerOrientationsToCheckForOverlap)
            }
        }


        val maxDistance = scannerPositions.flatMap { a ->
            scannerPositions.map { b ->
                a.manhattanDistance(b)
            }
        }
            .maxOf { it }


        println("Day 19, part 2. Max manhattan distance between scanners : $maxDistance")
    }

    val testinput = readInput("day19/day19_test_2")
    val input = readInput("day19/day19")
    val scanners = parseScanners3D(input)

    part1(scanners)
    part2(scanners)

}


class Scanner(private val beacons: Set<Point3D>, val self: Point3D = Point3D(0, 0, 0)) {

    val beaconVectors: Map<Point3D, Set<Point3D>> = beacons.associateWith { beacon ->
        beacons
            .filter { it != beacon }
            .map { beacon.relativeTo(it) }
            .toSet()
    }

    override fun toString(): String {
        return "Scanner: $self\n" + beaconVectors.map { (point, vectors) -> "$point : $vectors" }
            .joinToString("\n")
    }

    fun transpose(vector: Point3D): Scanner {
        val transposedBeacons = beacons.map { it.move(vector) }.toSet()
        return Scanner(transposedBeacons, self.move(vector))
    }

    fun merge(otherScanner: Scanner): Scanner {
        val identicalPoints = findIdenticalPoints(otherScanner)!!
        val transposeVector = identicalPoints.second.relativeTo(identicalPoints.first)
        return Scanner(beacons + otherScanner.transpose(transposeVector).beacons)
    }

    fun otherScannerPosition(otherScanner: Scanner): Point3D {
        val identicalPoints = findIdenticalPoints(otherScanner)!!
        val transposeVector = identicalPoints.second.relativeTo(identicalPoints.first)
        return otherScanner.self.move(transposeVector)
    }


    fun overlaps(otherScanner: Scanner): Boolean {
        return findIdenticalPoints(otherScanner) != null
    }

    fun findIdenticalPoints(otherScanner: Scanner): Pair<Point3D, Point3D>? {
        return beaconVectors.flatMap { selfVector ->
            otherScanner.beaconVectors.map { otherVector ->
                Pair(selfVector, otherVector)
            }

        }.filter { it.first.value.intersect(it.second.value).size >= 2 }
            .map { Pair(it.first.key, it.second.key) }
            .firstOrNull()

    }

    fun allOrientations(): List<Scanner> {
        return listOf(beacons.toList())
        .flatMap {
            IntArray(3)
                .scan(it) { acc, _ -> acc.map { point -> point.rotateRoundXAxis() } }
                .toList()
        }
            .flatMap {
                IntArray(3)
                    .scan(it) { acc, _ -> acc.map { point -> point.rotateRoundYAxis() } }
                    .toList()
            }
            .flatMap {
                IntArray(3)
                    .scan(it) { acc, _ -> acc.map { point -> point.rotateRoundZAxis() } }
                    .toList()
            }
            .map { Scanner(it.toSet()) }

    }

}
