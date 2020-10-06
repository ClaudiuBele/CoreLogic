package dk.sidereal.corelogic.platform

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    fun neverAskAgainSelected(activity: Activity, permission: String): Boolean {
        val prevShouldShowStatus = getRatinaleDisplayStatus(activity, permission)
        val currShouldShowStatus =
            activity.shouldShowRequestPermissionRationale(permission)
        return prevShouldShowStatus != currShouldShowStatus
    }

    fun setShouldShowStatus(context: Context, permission: String?) {
        val genPrefs: SharedPreferences =
            context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE)
        val editor = genPrefs.edit()
        editor.putBoolean(permission, true)
        editor.commit()
    }

    fun getRatinaleDisplayStatus(
        context: Context,
        permission: String?
    ): Boolean {
        val genPrefs: SharedPreferences =
            context.getSharedPreferences("GENERIC_PREFERENCES", Context.MODE_PRIVATE)
        return genPrefs.getBoolean(permission, false)
    }

    companion object {

        private val REQUEST_CODE = 10
    }
}
