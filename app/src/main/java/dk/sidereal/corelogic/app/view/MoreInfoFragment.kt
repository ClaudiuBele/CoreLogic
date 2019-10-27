package dk.sidereal.corelogic.app.view


import android.app.Application
import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable
import dk.sidereal.corelogic.app.R
import dk.sidereal.corelogic.app.api.model.RepositoryResponse
import dk.sidereal.corelogic.app.repo.DataRepository
import dk.sidereal.corelogic.kotlin.ext.observeOnAndroidLifecycle
import dk.sidereal.corelogic.platform.lifecycle.CoreFragment
import dk.sidereal.corelogic.platform.vm.StatefulViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 *
 */
class MoreInfoFragment : CoreFragment() {

    val viewModel: MoreInfoViewModel by sharedViewModel()
    val gson: Gson by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.button)?.setOnClickListener {
            viewModel.triggerClick()
        }

        viewModel.clicksRelay
            // receive updates on main thread
            .observeOn(AndroidSchedulers.mainThread())
            // auto dispose on OnDestroy tied to lifecycle of LifecycleOwner
            .autoDisposable(
                AndroidLifecycleScopeProvider.from(
                    viewLifecycleOwner,
                    Lifecycle.Event.ON_DESTROY
                )
            )
            .subscribe({ clicks ->
                context?.let {
                    view.findViewById<TextView>(R.id.title).text =
                        it.getString(R.string.more_info_title, clicks)
                }
            }, {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            })

        viewModel.githubRepoSubject
            .observeOnAndroidLifecycle(viewLifecycleOwner,
                onSuccess = { repoData ->
                    val reasonToUseGson = gson.toJson(repoData)
                    Snackbar.make(
                        view,
                        "${repoData.totalCount} repositories created with this app (not this app) ${reasonToUseGson.length}",
                        Snackbar.LENGTH_LONG
                    ).show()

                })
    }
}

class MoreInfoViewModel(
    private val dataRepository: DataRepository,
    private val application: Application
) : StatefulViewModel() {


    private var clicks = 0

    var githubRepoSubject = BehaviorRelay.create<RepositoryResponse>()

    init {
        viewModelScope.launch {
            val data = dataRepository.getSomeData()
            data.body()?.let {
                githubRepoSubject.accept(it)
            }
        }
    }

    // will only relay last value, seems like LiveData, in that the last value is also passed.
    /**
     *
     *
     * observer will receive all events.
    BehaviorRelay<Object> relay = BehaviorRelay.createDefault("default");
    relay.subscribe(observer);
    relay.accept("one");
    relay.accept("two");
    relay.accept("three");

    observer will receive the "one", "two" and "three" events, but not "zero"
    BehaviorRelay<Object> relay = BehaviorRelay.createDefault("default");
    relay.accept("zero");
    relay.accept("one");
    relay.subscribe(observer);
    relay.accept("two");
    relay.accept("three");
     *
     *
     */
    /** Relay posts last value to new observers, subject doesn't. Each useful for specific cases.
     * For state data to be shown when returning to the fragment, use relay to get a call back when
     * entering
     *
     */
    //TODO remove Relay, is Subject from RxJava
    var clicksRelay = BehaviorRelay.createDefault(clicks)
    var clicksSubject = PublishSubject.create<Int>()
    val viewEvents: Observable<Int> = clicksRelay

    companion object {
        const val STATE_CLICKS = "STATE_CLICKS"
    }

    override fun restoreState(state: Bundle?, timeSaved: Long?) {
        clicks = state?.getInt(STATE_CLICKS) ?: 0
        clicksRelay.accept(clicks)
        clicksSubject.onNext(clicks)
    }

    override fun saveInstanceState(outState: Bundle) {
        outState.putInt(STATE_CLICKS, clicks)
    }

    fun triggerClick() {
        clicks += 1
        clicksRelay.accept(clicks)
        clicksSubject.onNext(clicks)
        if (clicks == 10)
            clicksSubject.onError(IllegalStateException("too many clicks, buy paid version"))
    }

}
