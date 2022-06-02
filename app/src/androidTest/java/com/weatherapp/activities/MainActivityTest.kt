package com.weatherapp.activities

import android.os.Build
import android.os.SystemClock.sleep
import androidx.lifecycle.Transformations.map
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import com.weatherapp.R
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    //TODO: Need to test if API data is displayed in UI correctly

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    //grants permissions for all test cases within class
    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_NETWORK_STATE
    )

    @Test
    fun whenPermissionsAreDenied() {
        denyPermission()
        onView(withId(R.id.action_refresh)).perform(click())
        onView(withText("Globe"))
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenPermissionsAreGranted() {
        grantPermission()
        onView(withId(R.id.action_refresh)).perform(click())
        onView(withText("US"))
            .check(matches(isDisplayed()))
    }

    //clicks positive button in permission dialog depending on the build version used
    private fun grantPermission() {
        val instrumentation = getInstrumentation()
        if (Build.VERSION.SDK_INT >= 23) {
            val allowPermission = UiDevice.getInstance(instrumentation).findObject(UiSelector().text(
                when {
                    Build.VERSION.SDK_INT == 23 -> "Allow"
                    Build.VERSION.SDK_INT <= 28 -> "ALLOW"
                    Build.VERSION.SDK_INT == 29 -> "Allow only while using the app"
                    Build.VERSION.SDK_INT == 32 -> "Only this time"
                    else -> "While using the app"
                }
            ))
            if (allowPermission.exists()) {
                allowPermission.click()
            }
        }

    }

    //clicks negative button in permission dialog depending on the build version used
    private fun denyPermission() {
        val instrumentation = getInstrumentation()
        if (Build.VERSION.SDK_INT >= 23) {
            val denyPermission = UiDevice.getInstance(instrumentation).findObject(UiSelector().text(
                when (Build.VERSION.SDK_INT) {
                    in 24..28 -> "DENY"
                    in 29..31 -> "Deny"
                    else -> "Don't allow"
                }
            ))
            denyPermission.click()
        }
    }

    //check visibility and functionality of UI buttons
    @Test
    fun checkButtons(){

        Thread.sleep(1500)
        //checks if please wait view and progress bar are displayed after clicking refresh button
        onView(withId(R.id.progressBar))
            .check(matches(isDisplayed()))

        onView(withId(R.id.please_wait))
            .check(matches(isDisplayed()))
        //click fahrenheit button
        onView(withId(R.id.buttonFahrenheit))
            .perform(click())

        //checks if fahrenheit button is displayed
        onView(withId(R.id.buttonFahrenheit))
            .check(matches(isDisplayed()))

        //click celsius button
        onView(withId(R.id.buttonCelsius))
            .perform(ViewActions.click())

        //checks if celsius button is displayed
        onView(withId(R.id.buttonCelsius))
            .check(matches(isDisplayed()))

        //checks if refresh button is displayed
        onView(withId(R.id.action_refresh))
            .check(matches(isDisplayed()))

        //click refresh button
        onView(withId(R.id.action_refresh))
            .perform(click())



    }

    //check visibility of icons
    @Test
    fun checkIcons(){
        //main weather icon
        onView(withId(R.id.iv_main))
            .check(matches(isDisplayed()))

        //humidity icon
        onView(withId(R.id.iv_humidity))
            .check(matches(isDisplayed()))

        //location icon
        onView(withId(R.id.iv_location))
            .check(matches(isDisplayed()))

        //min and max temp icon
        onView(withId(R.id.iv_min_max))
            .check(matches(isDisplayed()))

        //sunrise icon
        onView(withId(R.id.iv_sunrise))
            .check(matches(isDisplayed()))

        //sunset icon
        onView(withId(R.id.iv_sunset))
            .check(matches(isDisplayed()))

        //wind speed icon
        onView(withId(R.id.iv_wind))
            .check(matches(isDisplayed()))
    }
}
