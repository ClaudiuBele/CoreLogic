package dk.sidereal.corelogic.di.lifecycle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DiActivity : AppCompatActivity() {

    internal val injectedApplication: DiApplication
        get() = getApplication() as DiApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}