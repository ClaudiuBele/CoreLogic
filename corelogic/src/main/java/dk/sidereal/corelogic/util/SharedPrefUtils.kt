package dk.sidereal.corelogic.util

import android.content.SharedPreferences

interface SharedPrefManager<T : Any> {

    val key: String
    val sharedPreferences: SharedPreferences

    fun setValue(value: T)
    fun getValue(default: T?): T?

}

class BooleanPref(override val sharedPreferences: SharedPreferences, override val key: String) :
    SharedPrefManager<Boolean> {
    override fun setValue(value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun getValue(default: Boolean?): Boolean? {
        return sharedPreferences.getBoolean(key, default ?: false)
    }
}

class StringPref(override val sharedPreferences: SharedPreferences, override val key: String) :
    SharedPrefManager<String> {

    override fun setValue(value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getValue(default: String?): String? {
        return sharedPreferences.getString(key, default)
    }
}

class IntPref(override val sharedPreferences: SharedPreferences, override val key: String) :
    SharedPrefManager<Int> {

    override fun setValue(value: Int) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun getValue(default: Int?): Int? {
        return sharedPreferences.getInt(key, default ?: 0)
    }
}