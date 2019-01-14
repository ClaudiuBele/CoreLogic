package dk.sidereal.corelogic.kotlin.ext

import dk.sidereal.corelogic.util.StringFormattingUtils

fun String.toLowerCaseWithUnderscores(): String {
    return StringFormattingUtils.toLowercaseWithUnderscores(this)
}