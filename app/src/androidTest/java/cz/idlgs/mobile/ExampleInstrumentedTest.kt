package cz.idlgs.mobile

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
	@Test
	fun useAppContext() {
		val appContext = InstrumentationRegistry.getInstrumentation().targetContext
		val packageName = appContext.packageName
		
		assertTrue(
			"Package name should be 'cz.idlgs.mobile' or 'cz.idlgs.mobile.dev', but was $packageName",
			packageName == "cz.idlgs.mobile" || packageName == "cz.idlgs.mobile.dev"
		)
	}
}