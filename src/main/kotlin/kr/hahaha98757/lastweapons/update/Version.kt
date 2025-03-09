package kr.hahaha98757.lastweapons.update

import kr.hahaha98757.lastweapons.update.VersionType.*

data class Version(val x: Int, val y: Int, val z: Int, val versionType: VersionType, val w: Int) {

    constructor(x: Int, y: Int, z: Int): this(x, y, z, RELEASE, 9999)

    constructor(): this(0, 0, 0)

    companion object {
        fun toVersion(str: String): Version {
            try {
                val versionType = if (str.contains("alpha")) ALPHA
                else if (str.contains("beta")) BETA
                else if (str.contains("pre")) PRE_RELEASE
                else if (str.contains("rc")) RELEASE_CANDIDATE
                else RELEASE

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
}

enum class VersionType(val str: String) {
    ALPHA("alpha"), BETA("beta"), PRE_RELEASE("pre"), RELEASE_CANDIDATE("rc"), RELEASE("")
}