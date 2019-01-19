package dk.sidereal.corelogic.kotlin.ext

import kotlin.reflect.KClass

/** Class name formatted using [dk.sidereal.corelogic.util.StringFormattingUtils.toLowercaseWithUnderscores]
 * . Actually uses [Class.simpleTagName] which uses the StringFormattingUtils function to avoid adding kotlin reflect dependency
 * <p>
 *  A class with a name like "SignUpActivity" will result in a simple tag name of "sign_up_activity"
 *
 */
fun <T : Any> KClass<T>.simpleTagName() = java.simpleTagName()