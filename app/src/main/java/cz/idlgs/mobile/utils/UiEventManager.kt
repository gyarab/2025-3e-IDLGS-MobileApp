package cz.idlgs.mobile.utils

import android.content.Context
import androidx.annotation.StringRes
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

sealed class UiEvent {
	data class ShowSnackbar(val message: UiText) : UiEvent()
	data class ShowToast(val message: UiText) : UiEvent()
}

sealed class UiText {
	data class Str(val value: String) : UiText()
	data class StrRes(@StringRes val resId: Int) : UiText()

	fun asString(context: Context): String = when (this) {
		is StrRes -> context.getString(resId)
		is Str -> value
	}
}

@Singleton
class UiEventManager @Inject constructor() {
	private val _events = MutableSharedFlow<UiEvent>()
	val events = _events.asSharedFlow()

	suspend fun showSnackbar(message: String) {
		_events.emit(UiEvent.ShowSnackbar(UiText.Str(message)))
	}

	suspend fun showSnackbar(@StringRes resId: Int) {
		_events.emit(UiEvent.ShowSnackbar(UiText.StrRes(resId)))
	}

	suspend fun showToast(message: String) {
		_events.emit(UiEvent.ShowToast(UiText.Str(message)))
	}

	suspend fun showToast(@StringRes resId: Int) {
		_events.emit(UiEvent.ShowToast(UiText.StrRes(resId)))
	}
}
