package cz.idlgs.mobile

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

	@get:Rule
	val composeTestRule = createAndroidComposeRule<MainActivity>()
	val context by lazy { composeTestRule.activity }

	@Test
	fun loginFlow() {
		// Profile
		val profileText = context.getString(R.string.profile)
		composeTestRule.onNodeWithText(profileText).performClick()

		// Wait for Login screen
		val loginButtonText = context.getString(R.string.log_in)
		composeTestRule.waitUntil(timeoutMillis = 2000) {
			composeTestRule.onAllNodesWithText(loginButtonText).fetchSemanticsNodes().isNotEmpty()
		}

		// Enter Email
		val emailLabel = context.getString(R.string.email)
		composeTestRule.onNodeWithText(emailLabel).performTextInput("user@example.com")

		// Enter Password
		val passwordLabel = context.getString(R.string.password)
		composeTestRule.onNodeWithText(passwordLabel).performTextInput("password123")

		// Click Login
		composeTestRule.onNodeWithText(loginButtonText).performClick()

		// No errors?
		val invalidEmailError = context.getString(R.string.invalid_email_format)
		val emailNotFoundError = context.getString(R.string.email_not_found)

		composeTestRule.onNodeWithText(invalidEmailError).assertDoesNotExist()
		composeTestRule.onNodeWithText(emailNotFoundError).assertDoesNotExist()
		Thread.sleep(1000)
	}
	@Test
	fun forgotPasswordFlow() {
		// Profile
		val profileText = context.getString(R.string.profile)
		composeTestRule.onNodeWithText(profileText).performClick()

		// Wait for Login screen
		val loginButtonText = context.getString(R.string.log_in)
		composeTestRule.waitUntil(timeoutMillis = 2000) {
			composeTestRule.onAllNodesWithText(loginButtonText).fetchSemanticsNodes().isNotEmpty()
		}

		// Click Login
		val forgotPasswordText = context.getString(R.string.forgot_password)
		composeTestRule.onNodeWithText(forgotPasswordText).performClick()

		// Enter Email
		val emailLabel = context.getString(R.string.email)
		composeTestRule.onNodeWithText(emailLabel).performTextInput("user@example.com")

		// No errors?
		val invalidEmailError = context.getString(R.string.invalid_email_format)
		val emailNotFoundError = context.getString(R.string.email_not_found)

		composeTestRule.onNodeWithText(invalidEmailError).assertDoesNotExist()
		composeTestRule.onNodeWithText(emailNotFoundError).assertDoesNotExist()

		// Click Send Reset Link
		val resetPasswordText = context.getString(R.string.send_reset_link)
		composeTestRule.onNodeWithText(resetPasswordText).performClick()
		Thread.sleep(1000)
	}
}
