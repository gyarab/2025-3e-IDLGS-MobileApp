package cz.idlgs.mobile.utils

import android.content.Context
import android.widget.Toast

object Utils {
	var currentToast: Toast? = null;
	fun Context.showToast(msg: String, long: Boolean = false) {
		currentToast?.cancel()
		currentToast = Toast.makeText(this, msg, if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
		currentToast!!.show()
	}
}