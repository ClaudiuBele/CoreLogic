package dk.sidereal.corelogic

import android.content.Context
import com.google.common.truth.Truth.assertThat
import dk.sidereal.corelogic.platform.ext.getAppName
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.runners.MockitoJUnitRunner

private const val FAKE_STRING = "HELLO WORLD"

@RunWith(MockitoJUnitRunner::class)
class ContextTest {

    @Mock
    private lateinit var mockContext: Context

    @Test
    fun readStringFromContext_LocalizedString() {
        // Given a mocked Context injected into the object under test..., R.string.app_name used directly in
        // extension function mockContext.getAppName
        `when`(mockContext.getString(R.string.app_name))
            .thenReturn(FAKE_STRING)

        // ...when the string is returned from the object under test...
        val result: String = mockContext.getAppName()

        // ...then the result should be the expected one.
        assertThat(result).isEqualTo(FAKE_STRING)
    }
}