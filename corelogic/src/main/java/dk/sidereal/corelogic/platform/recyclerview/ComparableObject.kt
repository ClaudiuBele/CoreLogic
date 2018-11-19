package dk.sidereal.corelogic.platform.recyclerview

object ComparableObject{
    fun isObjectOfSameType(first: Comparable?, second: Comparable?) : Boolean {
        return if(first == null || second == null) {
            true
        } else {
            first::class.java.isAssignableFrom(second::class.java) ||
                    second::class.java.isAssignableFrom(first::class.java)
        }
    }

}