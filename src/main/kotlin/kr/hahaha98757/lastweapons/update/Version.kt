package kr.hahaha98757.lastweapons.update

import kr.hahaha98757.lastweapons.update.VersionType.*

data class Version(val x: Int, val y: Int, val z: Int, val versionType: VersionType, val w: Int): Comparable<Version> {

    constructor(x: Int, y: Int, z: Int): this(x, y, z, RELEASE, 9999)

    constructor(): this(0, 0, 0)

    companion object {
        fun toVersion(str: String): Version {
            try {
                val versionType = when {
                    str.contains("alpha") -> ALPHA
                    str.contains("beta") -> BETA
                    str.contains("pre") -> PRE_RELEASE
                    str.contains("rc") -> RELEASE_CANDIDATE
                    else -> RELEASE
                }

                if (!str.contains("-")) {
                    val strArray = str.split(".")
                    return Version(strArray[0].toInt(), strArray[1].toInt(), strArray[2].toInt())
                }

                val w = str.split("-")[1].replace(Regex("[^0-9]"), "").toInt()

                val strArray = str.split("-")[0].split(".")
                return Version(strArray[0].toInt(), strArray[1].toInt(), strArray[2].toInt(), versionType, w)
            } catch (e: Exception) {
                return Version()
            }
        }
    }

    override fun toString(): String {
        if (versionType == RELEASE)
            return "$x.$y.$z"
        return "$x.$y.$z-${versionType.str}$w"
    }

    override fun compareTo(other: Version): Int {
        if (this.x > other.x) return 1
        if (this.x < other.x) return -1
        if (this.y > other.y) return 1
        if (this.y < other.y) return -1
        if (this.z > other.z) return 1
        if (this.z < other.z) return -1
        val thisType = when (this.versionType) {
            ALPHA -> 0
            BETA -> 1
            PRE_RELEASE -> 2
            RELEASE_CANDIDATE -> 3
            RELEASE -> 4
        }
        val otherType = when (other.versionType) {
            ALPHA -> 0
            BETA -> 1
            PRE_RELEASE -> 2
            RELEASE_CANDIDATE -> 3
            RELEASE -> 4
        }
        if (thisType > otherType) return 1
        if (thisType < otherType) return -1
        if (this.w > other.w) return 1
        if (this.w < other.w) return -1
        return 0
    }
}

enum class VersionType(val str: String) {
    ALPHA("alpha"), BETA("beta"), PRE_RELEASE("pre"), RELEASE_CANDIDATE("rc"), RELEASE("")
}