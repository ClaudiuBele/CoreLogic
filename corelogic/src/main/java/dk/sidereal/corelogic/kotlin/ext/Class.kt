package dk.sidereal.corelogic.kotlin.ext

import dk.sidereal.corelogic.util.StringFormattingUtils

/** Class name formatted using [dk.sidereal.corelogic.util.StringFormattingUtils.toLowercaseWithUnderscores]
 * <p>
 *  A class with a name like "SignUpActivity" will result in a simple tag name of "sign_up_activity"
 *
 */
fun <T> Class<T>.simpleTagName() = StringFormattingUtils.toLowercaseWithUnderscores(simpleName)