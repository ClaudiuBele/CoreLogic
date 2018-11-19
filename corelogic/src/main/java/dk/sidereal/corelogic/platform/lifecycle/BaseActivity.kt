package dk.sidereal.corelogic.platform.lifecycle

import androidx.appcompat.app.AppCompatActivity

class BaseActivity : AppCompatActivity() {

    val baseApplication: BaseApplication
    get() = application as BaseApplication

    val controller = ActivityController(this)

}