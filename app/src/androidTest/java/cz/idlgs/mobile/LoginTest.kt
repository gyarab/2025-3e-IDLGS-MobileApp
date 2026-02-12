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

		val loginText = context.getString(R.string.login)
		composeTestRule.onNodeWithText(loginText).performClick()

		// Wait for Login screen
		val loginButtonText = context.getString(R.string.log_in)
		composeTestRule.waitUntil(2000) {
			composeTestRule.onAllNodesWithText(loginButtonText)
				.fetchSemanticsNodes().isNotEmpty()
		}

		// Enter Email & Password
		val emailLabel = context.getString(R.string.email)
		val passwordLabel = context.getString(R.string.password)
		composeTestRule.onNodeWithText(emailLabel).performTextInput("user@example.com")
		composeTestRule.onNodeWithText(passwordLabel).performTextInput("password123")

		// Click Login
		composeTestRule.onNodeWithText(loginButtonText).performClick()

		// No errors?
		val invalidEmailText = context.getString(R.string.invalid_email_format)
		val emailNotFoundText = context.getString(R.string.email_not_found)
		composeTestRule.onNodeWithText(invalidEmailText).assertDoesNotExist()
		composeTestRule.onNodeWithText(emailNotFoundText).assertDoesNotExist()

		composeTestRule.waitUntil(3000) {
			composeTestRule.onAllNodesWithText(
				"Item", substring = true
			).fetchSemanticsNodes().isNotEmpty()
		}

		val profileText = context.getString(R.string.profile)
		val logoutText = context.getString(R.string.log_out)
		composeTestRule.onNodeWithText(profileText).assertIsDisplayed().performClick()
		composeTestRule.onNodeWithText(logoutText).assertIsDisplayed().performClick()
	}
	@Test
	fun forgotPasswordFlow() {

		val loginText = context.getString(R.string.login)
		composeTestRule.waitUntil(timeoutMillis = 2000) {
			composeTestRule.onAllNodesWithText(loginText)
				.fetchSemanticsNodes().isNotEmpty()
		}
		composeTestRule.onNodeWithText(loginText).assertIsDisplayed().performClick()

		// Wait for Login screen
		val loginButtonText = context.getString(R.string.log_in)
		composeTestRule.waitUntil(timeoutMillis = 2000) {
			composeTestRule.onAllNodesWithText(loginButtonText)
				.fetchSemanticsNodes().isNotEmpty()
		}

		// Click Login
		val forgotPasswordText = context.getString(R.string.forgot_password)
		composeTestRule.onNodeWithText(forgotPasswordText).performClick()

		// Enter Email
		val emailLabel = context.getString(R.string.email)
		composeTestRule.onNodeWithText(emailLabel).performTextInput("user@example.com")

		// No errors?
		val invalidEmailText = context.getString(R.string.invalid_email_format)
		val emailNotFoundText = context.getString(R.string.email_not_found)
		composeTestRule.onNodeWithText(invalidEmailText).assertDoesNotExist()
		composeTestRule.onNodeWithText(emailNotFoundText).assertDoesNotExist()

		// Click Send Reset Link
		val resetPasswordText = context.getString(R.string.send_reset_link)
		composeTestRule.onNodeWithText(resetPasswordText).performClick()

		// Wait for Message
		val notYetImplementedText = context.getString(R.string.not_yet_implemented)
		composeTestRule.waitUntil(3000) {
			composeTestRule.onAllNodesWithText(notYetImplementedText)
				.fetchSemanticsNodes().isNotEmpty()
		}
	}
}
