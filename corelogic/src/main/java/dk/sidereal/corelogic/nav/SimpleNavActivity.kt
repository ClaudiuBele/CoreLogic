package dk.sidereal.corelogic.nav

/** Quick wrapper of [NavActivity]
 *
 */
open class SimpleNavActivity(private val graphResId: Int, private val graphStartDestinations: List<Int>) :
    NavActivity() {

    override fun getNavHostFragment(): CoreNavHostFragment {
        return CoreNavHostFragment.create(graphResId)
    }

    override fun getStartDestinations(): List<Int> {
        return graphStartDestinations
    }

}