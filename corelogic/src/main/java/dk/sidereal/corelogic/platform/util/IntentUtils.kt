package dk.sidereal.corelogic.platform.util

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings


object IntentUtils {
    fun getToAppSettings(context: Context): Intent {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intent.data = uri
        return intent
    }

    fun getToPlaystore(context: Context): Intent {
        try {
            return Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=${context.packageName}")
            )
        } catch (anfe: ActivityNotFoundException) {
            return Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
            )
        }
    }

    fun getToWebsite(context: Context, url: String): Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(url))
    }


    fun goToAppSettings(context: Context) {
        getToAppSettings(context).let {
            context.startActivity(it)
        }
    }

    fun goToPlaystore(context: Context) {
        getToPlaystore(context).let {
            context.startActivity(it)
        }
    }

    fun goToWebsite(context: Context, url: String) {
        context.startActivity(getToWebsite(context, url))
    }
}