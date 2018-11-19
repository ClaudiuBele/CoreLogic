package dk.sidereal.corelogic.di.lifecycle

interface DiComponent {

    fun inject(injectedActivity: DiActivity)
    fun inject(injectedComponent: DiComponent)
}