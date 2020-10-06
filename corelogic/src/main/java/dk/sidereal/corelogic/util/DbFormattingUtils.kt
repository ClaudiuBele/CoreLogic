package dk.sidereal.corelogic.util

object DbFormattingUtils {

    fun getInCondition(paramName: String, values: List<String>?, notIn: Boolean = false): String? {
        if (values == null || values.isEmpty()) {
            return null
        }
        var keys = ""
        values.forEachIndexed { index, contentData ->
            keys += "'${contentData}'"
            if (index != values.lastIndex) {
                keys += ","
            }
        }
        return if (notIn) {
            "$paramName not in ($keys)"
        } else {
            "$paramName in ($keys)"
        }
    }

    fun getOrCondition(conditions: List<String>): String? {
        return if (conditions.isEmpty()) {
            null
        } else {
            var compositeQuery = "("
            conditions.forEachIndexed { index, s ->
                if (index != 0) {
                    compositeQuery += " OR "
                }
                compositeQuery += s
            }
            compositeQuery += ")"
            compositeQuery
        }
    }


}