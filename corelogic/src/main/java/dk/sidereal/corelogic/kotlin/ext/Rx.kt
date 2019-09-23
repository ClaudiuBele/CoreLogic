package dk.sidereal.corelogic.kotlin.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

fun <T : Any> Observable<T>.observeOnAndroidLifecycle(
    lifecycleOwner: LifecycleOwner,
    onSuccess: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null
) {
    // receive updates on main thread
    observeOn(AndroidSchedulers.mainThread())
        // auto dispose on OnDestroy tied to lifecycle of LifecycleOwner
        .autoDisposable(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                Lifecycle.Event.ON_DESTROY
            )
        )
        .subscribe({ data ->
            onSuccess(data)
        }, {
            onError?.invoke(it)
        })

}