package dk.sidereal.corelogic.platform.ext

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import dk.sidereal.corelogic.R

fun Context.getAppName(): String {
    return getString(R.string.app_name)
}

fun Context.hasPermission(permission: String): Boolean {
    val permissionCheckResult = ContextCompat.checkSelfPermission(
        this,
        permission
    )
    return permissionCheckResult == PackageManager.PERMISSION_GRANTED
}

fun Context.hasCameraPermission(): Boolean {
    return hasPermission(Manifest.permission.CAMERA)
}

fun Context.hasAudioRecordPermission(): Boolean {
    return hasPermission(Manifest.permission.RECORD_AUDIO)
}

fun Context.hasGpsPermission(): Boolean {
    val statusFine = hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    val statusCoarse = hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
    return statusFine && statusCoarse
}