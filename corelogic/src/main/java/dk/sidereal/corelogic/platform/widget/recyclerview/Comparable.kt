package dk.sidereal.corelogic.platform.widget.recyclerview

interface Comparable {

    /** Used to verify value integrity. Used in [androidx.recyclerview.widget.DiffUtil.Callback.areContentsTheSame]
     *
     */
    fun areContentsTheSame(otherComparable: Comparable?): Boolean

    /** Used to verify type integrity. Used in [androidx.recyclerview.widget.DiffUtil.Callback.areItemsTheSame].
     * Do not use directly, use [Comparable.Companion.isObjectOfSameType] instead to cover null checks on first value
     *
     */
    fun isObjectOfSameType(otherComparable: Comparable?): Boolean


}
