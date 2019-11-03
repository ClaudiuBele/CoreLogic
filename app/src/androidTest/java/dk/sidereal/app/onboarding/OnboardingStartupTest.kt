package dk.sidereal.app.onboarding

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
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
class OnboardingStartupTest {

    @get:Rule
    var activityRule: ActivityTestRule<MainActivity>
            = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testInitialPage() {
        // check onboarding
        onView(withId(R.id.onboarding_first_page))
    }

}