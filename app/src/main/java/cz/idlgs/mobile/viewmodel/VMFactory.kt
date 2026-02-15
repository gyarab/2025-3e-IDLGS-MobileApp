package cz.idlgs.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

fun <VM : ViewModel> vmFactory(init: () -> VM) =
	object : ViewModelProvider.Factory {
		override fun <T : ViewModel> create(modelClass: Class<T>) =
			init() as T
	}