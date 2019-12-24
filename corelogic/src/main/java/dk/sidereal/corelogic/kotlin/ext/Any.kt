package dk.sidereal.corelogic.kotlin.ext

inline fun <T, R> T.to(converter: (T) -> R):R {
    return converter.invoke(this)
}

inline fun <T, R> T?.toNullable(converter: (T) -> R?):R? {
    return this?.let(converter)
}