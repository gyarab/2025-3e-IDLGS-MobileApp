package cz.idlgs.mobile.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import cz.idlgs.mobile.R
import cz.idlgs.mobile.data.MockData
import cz.idlgs.mobile.nav.UserNavGraph
import cz.idlgs.mobile.ui.components.CourseCard

@Destination<UserNavGraph>(start = true)
@Composable
fun ListScreen(modifier: Modifier = Modifier) {
	val (archivedCourses, normalCourses) = MockData.courses.partition { it.archived }

	Column {
		LazyVerticalStaggeredGrid(
			columns = StaggeredGridCells.Adaptive(320.dp),
			modifier = modifier.weight(1f),
			contentPadding = PaddingValues(8.dp),
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalItemSpacing = 8.dp
		) {
			item(span = StaggeredGridItemSpan.FullLine) {
				Row(
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically,
					modifier = Modifier.padding(12.dp)
				) {
					Icon(
						Icons.Default.GridView,
						null,
						tint = MaterialTheme.colorScheme.primary,
					)
					Spacer(Modifier.width(8.dp))
					Text(
						text = stringResource(R.string.courses_n_textbooks),
						fontSize = 24.sp,
						fontWeight = FontWeight.Bold,
						color = MaterialTheme.colorScheme.primary
					)
				}
			}

			items(normalCourses) { course ->
				CourseCard(course = course) {/* TODO */}
			}

			if (archivedCourses.isNotEmpty()) {
				item(span = StaggeredGridItemSpan.FullLine) {
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 24.dp, bottom = 8.dp),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = "Archived Courses and Textbooks",
							fontSize = 20.sp,
							fontWeight = FontWeight.Bold
						)
						Text(
							text = "Archived courses and textbooks are read-only. Everything is left as it was at the moment of archiving.",
							fontSize = 14.sp,
							color = Color.Gray,
							modifier = Modifier.padding(horizontal = 16.dp)
						)
					}
				}

				items(archivedCourses) { course ->
					CourseCard(course = course) { /* TODO */ }
				}
			} else {
				item(span = StaggeredGridItemSpan.FullLine) {
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.padding(vertical = 32.dp),
						contentAlignment = Alignment.Center
					) {
						Text("No archived courses or textbooks yet")
					}
				}
			}
		}

		BottomToolbar()
	}
}

@Composable
fun BottomToolbar() {
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
