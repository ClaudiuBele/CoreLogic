package dk.sidereal.app.coroutine

import com.google.gson.Gson
import com.nhaarman.mockitokotlin2.whenever
import dk.sidereal.app.test.TestScope
import dk.sidereal.corelogic.app.api.GithubService
import dk.sidereal.corelogic.app.repo.DataRepository
import dk.sidereal.corelogic.app.repo.DataRepositoryImpl
import dk.sidereal.corelogic.kotlin.ManagedCoroutineScope
import dk.sidereal.corelogic.kotlin.ext.to
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CoroutineTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val managedCoroutineScope: ManagedCoroutineScope = TestScope(testDispatcher)
    private val service = GithubService.getRetrofit(Gson()).to { GithubService.getService(it) }

    private lateinit var presenter: DataRepository

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        presenter = DataRepositoryImpl(service, managedCoroutineScope)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun when_load_data_throws_deprecated_api_then_present_upgrade_available() =
        runBlockingTest {
            presenter.getSomeData()
            Mockito.verify<DataRepository>(presenter).receivedData()
        }

}