package dk.sidereal.corelogic

import com.google.common.truth.Truth.assertThat
import dk.sidereal.corelogic.kotlin.ext.isLowerCaseWithUnderscores
import dk.sidereal.corelogic.kotlin.ext.toLowerCaseWithUnderscores
import org.junit.Test

class InputTest {

    /** Given example
     *
     *
        class EmailValidatorTest {
            @Test
            fun emailValidator_CorrectEmailSimple_ReturnsTrue() {
                assertThat(EmailValidator.isValidEmail("name@email.com")).isTrue()
            }
        }
     *
     *
     */

    @Test
    fun inputValidator_NonNullNumber() {
        val someInt: Int? = null
        assertThat(someInt).isEqualTo(null)
    }

    @Test
    fun inputValidator_StringWithLowercaseAndUnderscores_ReturnsFalse(){
        assertThat("Failing lowercase string".isLowerCaseWithUnderscores()).isFalse()
    }

    @Test
    fun inputValidator_StringWithLowercaseAndUnderscores_ReturnsTrue() {
        assertThat("Failing lowercaseString"
            .toLowerCaseWithUnderscores()
            .isLowerCaseWithUnderscores()).isTrue()
    }
}