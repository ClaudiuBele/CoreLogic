package dk.sidereal.corelogic.platform

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dk.sidereal.corelogic.platform.lifecycle.CoreApplication

class PermissionsDelegate constructor(
    private val application: CoreApplication
) {

    fun hasPermission(permission: String): Boolean {
        val permissionCheckResult = ContextCompat.checkSelfPermission(
            application,
            permission
        )
        return permissionCheckResult == PackageManager.PERMISSION_GRANTED
    }

    fun hasCameraPermission(): Boolean {
        return hasPermission(Manifest.permission.CAMERA)
    }

    fun hasAudioRecordPermission(): Boolean {
        return hasPermission(Manifest.permission.RECORD_AUDIO)
    }

    fun hasGpsPermission(): Boolean {
        val statusFine = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val statusCoarse = hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        return statusFine && statusCoarse
    }

    companion object {

        private val REQUEST_CODE = 10
    }
}
