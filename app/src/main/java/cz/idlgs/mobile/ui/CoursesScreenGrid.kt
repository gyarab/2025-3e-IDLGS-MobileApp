package cz.idlgs.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ramcosta.composedestinations.annotation.Destination
import cz.idlgs.mobile.R
import cz.idlgs.mobile.data.MockData
import cz.idlgs.mobile.nav.UserNavGraph
import cz.idlgs.mobile.ui.components.CourseCard
import cz.idlgs.mobile.ui.components.CoursesToolbar

@Destination<UserNavGraph>(start = true)
@Composable
fun CoursesScreenGrid(modifier: Modifier = Modifier) {
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

		CoursesToolbar()
	}
}
