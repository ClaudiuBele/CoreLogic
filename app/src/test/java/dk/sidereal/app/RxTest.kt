package dk.sidereal.app.coroutine

import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.given
import dk.sidereal.app.test.TestScope
import dk.sidereal.corelogic.app.api.GithubService
import dk.sidereal.corelogic.app.api.model.RepositoryResponse
import dk.sidereal.corelogic.app.repo.DataRepository
import dk.sidereal.corelogic.app.repo.DataRepositoryImpl
import dk.sidereal.corelogic.kotlin.ManagedCoroutineScope
import dk.sidereal.corelogic.kotlin.TrampolineSchedulerProvider
import dk.sidereal.corelogic.kotlin.ext.to
import io.reactivex.Single
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.willReturn
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RxTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val managedCoroutineScope: ManagedCoroutineScope = TestScope(testDispatcher)
    private var schedulerProvider = TrampolineSchedulerProvider()
    private val service = GithubService.getRetrofit(Gson()).to { GithubService.getService(it) }

    private lateinit var presenter: DataRepository


    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        presenter = DataRepositoryImpl(service, managedCoroutineScope, schedulerProvider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun when_load_data_not_empty() {

        //when
        presenter.getSomeDataObs()

        //then
        assertEquals(presenter.receivedData(), true)
    }

}