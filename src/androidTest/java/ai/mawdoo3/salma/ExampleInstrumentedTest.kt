package ai.mawdoo3.salma

import ai.mawdoo3.salma.ui.BotMainActivity
import android.app.Instrumentation
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("ai.mawdoo3.salma", appContext.packageName)
    }

    @Test
    fun launchApp() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext



        val activityScenarioAccounts: ActivityScenario<BotMainActivity> =
            ActivityScenario.launch(BotMainActivity::class.java)

        val monitor: Instrumentation.ActivityMonitor = InstrumentationRegistry.getInstrumentation().addMonitor(
            BotMainActivity::class.java.canonicalName, null, true
        )



        InstrumentationRegistry.getInstrumentation().waitForMonitor(monitor)


    }
}