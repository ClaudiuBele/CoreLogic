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
    fun nullableInt_isNull() {
        val someInt: Int? = null
        assertThat(someInt).isEqualTo(null)
    }

    @Test
    fun stringWithLowercaseAndUnderscores_isRight(){
        assertThat("Failing lowercase string".isLowerCaseWithUnderscores()).isFalse()
    }

    @Test
    fun stringWithLowercaseAndUnderscores_isWrong() {
        assertThat("Failing lowercaseString"
            .toLowerCaseWithUnderscores()
            .isLowerCaseWithUnderscores()).isTrue()
    }
}