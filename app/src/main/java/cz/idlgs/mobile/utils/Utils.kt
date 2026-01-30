package cz.idlgs.mobile.utils

import android.content.Context
import android.content.Intent
import android.util.Patterns
import android.widget.Toast
import androidx.core.net.toUri

object Utils {
	val urlRegex = Patterns.WEB_URL.toRegex()
	var currentToast: Toast? = null

	fun Context.showToast(msg: String, long: Boolean = false) {
		currentToast?.cancel()
		currentToast = Toast.makeText(this, msg, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
		currentToast!!.show()
	}

	fun Context.openLinkInBrowser(url: String) {
		val intent = Intent(Intent.ACTION_VIEW)
			.apply { data = url.toUri() }
		startActivity(intent)
	}
}