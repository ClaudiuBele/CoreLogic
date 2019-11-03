package dk.sidereal.app.onboarding

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import dk.sidereal.corelogic.app.R
import dk.sidereal.corelogic.app.view.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(value = AndroidJUnit4::class)
@LargeTest
class OnboardingGoToPage2Test {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testGoToPage2() {
        // check onboarding
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_first_page))
        Espresso.onView(ViewMatchers.withId(R.id.next))
            .perform(ViewActions.click())
        // Check that the page has changed
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_second_page))
    }

}

