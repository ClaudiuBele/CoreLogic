package dk.sidereal.corelogic.kotlin.ext

import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import dk.sidereal.corelogic.R
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

fun <T: Any> Observable<T>.observeOnAndroidLifecycle(
    lifecycleOwnert: LifecycleOwner,
    onSuccess: (T) -> Unit,
    onError: (Throwable) -> Unit
) {
        // receive updates on main thread
        observeOn(AndroidSchedulers.mainThread())
        // auto dispose on OnDestroy tied to lifecycle of LifecycleOwner
        .autoDisposable(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwnert,
                Lifecycle.Event.ON_DESTROY
            )
        )
        .subscribe({ data ->
            onSuccess(data)
        }, {
            onError(it)
        })

}