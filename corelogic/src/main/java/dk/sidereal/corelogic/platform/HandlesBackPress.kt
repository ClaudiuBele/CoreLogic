package dk.sidereal.corelogic.platform

interface HandlesBackPress {

    fun onBackPressedInternal(): Boolean

    fun onBackPressed(): Boolean
}