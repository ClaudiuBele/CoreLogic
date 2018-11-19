package dk.sidereal.corelogic.platform.recyclerview

interface Comparable{

    /** Used to verify value integrity. Used in [androidx.recyclerview.widget.DiffUtil.Callback.areContentsTheSame]
     *
     */
    fun areContentsTheSame(otherComparable: Comparable?) : Boolean

    /** Used to verify type integrity. Used in [androidx.recyclerview.widget.DiffUtil.Callback.areItemsTheSame].
     * Do not use directly, use [Comparable.Companion.isObjectOfSameType] instead to cover null checks on first value
     *
     */
    fun isObjectOfSameType(otherComparable: Comparable?) : Boolean

    companion object {

        fun isObjectOfSameType(first: Comparable?, second: Comparable?) : Boolean {
            return if(first == null || second == null) {
                true
            } else {
                first::class.java.isAssignableFrom(second::class.java) ||
                        second::class.java.isAssignableFrom(first::class.java)
            }
        }

    }
}