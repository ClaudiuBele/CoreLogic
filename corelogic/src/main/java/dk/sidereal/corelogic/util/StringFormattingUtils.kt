package dk.sidereal.corelogic.util

object StringFormattingUtils {

    fun toLowercaseWithUnderscores(input: String): String {

        val trimmedInput = input.trim()
        val stringBuilder = StringBuilder(trimmedInput.length)
        trimmedInput.forEachIndexed { index, c ->
            val hasNextChar = index + 1 < trimmedInput.length
            val hasPrevChar = index - 1 >= 0
            when {
                c.isUpperCase() -> {
                    if(hasPrevChar) {
                        stringBuilder.append('_')
                    }
                    stringBuilder.append(c.toLowerCase())
                }
                c.isDigit() -> {
                    // add '_' before digit, if preceded by non-digit
                    if(hasPrevChar && !trimmedInput[index-1].isDigit()) {
                        stringBuilder.append('_')
                    }
                    stringBuilder.append(c)
                    if(hasNextChar && !trimmedInput[index+1].isDigit()) {
                        stringBuilder.append('_')
                    }
                }
                c == ' ' -> {
                    stringBuilder.append('_')
                }
                else -> {
                    stringBuilder.append(c)
                }
            }
        }
        return stringBuilder.toString()
    }

}