package dk.sidereal.app.onboarding

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.filters.LargeTest
import dk.sidereal.corelogic.app.R
import dk.sidereal.corelogic.app.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(value = androidx.test.ext.junit.runners.AndroidJUnit4::class)
@LargeTest
class OnboardingGoToPage2Test {

    /**
     * Use [ActivityScenarioRule] to create and launch the activity under test before each test,
     * and close it after each test. This is a replacement for
     * [androidx.test.rule.ActivityTestRule].
     */
    @get:Rule var activityScenarioRule = activityScenarioRule<MainActivity>()


    @Test
    fun testGoToPage2() {
        // check onboarding
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_first_page))
        Espresso.onView(ViewMatchers.withId(R.id.next))
            .perform(ViewActions.click())
        // Check that the page has changed
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_second_page))
            .check(matches(isDisplayed()))
    }

}

