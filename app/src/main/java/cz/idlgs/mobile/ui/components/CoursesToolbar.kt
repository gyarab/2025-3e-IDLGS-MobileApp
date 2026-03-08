package cz.idlgs.mobile.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@Composable
fun CoursesToolbar() {
	var searchQuery by rememberSaveable { mutableStateOf("") }
	var isSearchExpanded by rememberSaveable { mutableStateOf(false) }
	val focusRequester = remember { FocusRequester() }
	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(isSearchExpanded) {
		if (isSearchExpanded) focusRequester.requestFocus()
		else keyboardController?.hide()
	}
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.height(IntrinsicSize.Max)
			.padding(4.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.End,
	) {
		AnimatedVisibility(
			visible = isSearchExpanded,
			enter = expandHorizontally(expandFrom = Alignment.End),
			exit = shrinkHorizontally(shrinkTowards = Alignment.End),
			modifier = Modifier.weight(1f, fill = false)
		) {
			OutlinedTextField(
				value = searchQuery,
				onValueChange = { searchQuery = it },
				placeholder = { Text("Search...") },
				leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
				trailingIcon = {
					IconButton(onClick = {
						if (searchQuery.isEmpty()) isSearchExpanded = false
						else searchQuery = ""
					}) {
						Icon(Icons.Default.Clear, contentDescription = "Clear")
					}
				},
				shape = MaterialTheme.shapes.medium,
				singleLine = true,
				modifier = Modifier
					.focusRequester(focusRequester)
					.fillMaxWidth()
					.padding(horizontal = 8.dp)
			)
		}

		Surface(
			shape = MaterialTheme.shapes.large,
			color = MaterialTheme.colorScheme.surface,
			tonalElevation = 4.dp,
			modifier = Modifier.fillMaxHeight()
		) {
			Row(
				modifier = Modifier.padding(4.dp),
				verticalAlignment = Alignment.CenterVertically
			) {
				if (!isSearchExpanded) {
					IconButton(
						onClick = { isSearchExpanded = true },
						shape = MaterialTheme.shapes.medium,
						modifier = Modifier
							.fillMaxHeight()
							.size(52.dp),
						colors = IconButtonDefaults.iconButtonColors(
							containerColor = MaterialTheme.colorScheme.primaryContainer
						),
					) {
						Icon(
							Icons.Default.Search, "Open Search"
						)
					}
					Spacer(Modifier.width(4.dp))
				}

				IconButton(
					onClick = { /* TODO */ },
					shape = MaterialTheme.shapes.medium,
					modifier = Modifier
						.fillMaxHeight()
						.size(52.dp),
					colors = IconButtonDefaults.iconButtonColors(
						containerColor = MaterialTheme.colorScheme.primaryContainer
					),
				) {
					Icon(
						Icons.Default.Add, null,
					)
				}
			}
		}
	}
}
