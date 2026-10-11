package ru.mazenin.kotlin

import androidx.test.espresso.Espresso.*
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DownloaderUiTest {
    @get:Rule val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun initialUrlVisible() {
        onView(withId(R.id.url_input)).check(matches(withText("https://httpbin.org/image/png")))
    }

    @Test
    fun downloadButtonEnabled() {
        onView(withId(R.id.download_button)).check(matches(isEnabled()))
    }

    @Test
    fun urlCanBeEdited() {
        onView(withId(R.id.url_input))
            .perform(
                replaceText("https://example.com/photo.jpg"),
                androidx.test.espresso.action.ViewActions.closeSoftKeyboard(),
            )
            .check(matches(withText("https://example.com/photo.jpg")))
    }

    @Test
    fun emptyUrlShowsError() {
        onView(withId(R.id.url_input))
            .perform(clearText(), androidx.test.espresso.action.ViewActions.closeSoftKeyboard())
        onView(withId(R.id.download_button)).perform(click())
        onView(withId(R.id.status_text))
            .check(matches(withText(containsString("корректную HTTP(S)"))))
    }

    @Test
    fun historySectionVisible() {
        onView(withId(R.id.history_title))
            .check(matches(isDisplayed()))
            .check(matches(withText("История загрузок")))
    }
}
