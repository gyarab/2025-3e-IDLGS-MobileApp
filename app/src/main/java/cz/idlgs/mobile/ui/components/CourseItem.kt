package cz.idlgs.mobile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cz.idlgs.mobile.data.Course
import cz.idlgs.mobile.data.CourseType

@Composable
fun CourseCard(
	course: Course,
	onClick: () -> Unit
) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.padding(8.dp),
		colors = CardDefaults.cardColors(containerColor = course.color),
		shape = RoundedCornerShape(8.dp),
		onClick = onClick
	) {
		Column(
			modifier = Modifier
				.padding(16.dp)
				.fillMaxWidth()
		) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Icon(
					imageVector = if (course.type == CourseType.TEXTBOOK) Icons.Default.AutoStories else Icons.Default.Description,
					contentDescription = null,
					tint = Color.White,
					modifier = Modifier.size(24.dp)
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = course.name,
					color = Color.White,
					fontWeight = FontWeight.Bold,
					fontSize = 18.sp
				)
				Spacer(Modifier.width(8.dp))
				Text(
					text = course.subject,
					color = Color.White.copy(alpha = 0.7f),
					fontSize = 14.sp
				)
			}

			Spacer(Modifier.height(8.dp))

			Row(verticalAlignment = Alignment.CenterVertically) {
				Icon(
					Icons.Default.PersonOutline,
					contentDescription = null,
					tint = Color.White.copy(alpha = 0.8f),
					modifier = Modifier.size(16.dp)
				)
				Spacer(Modifier.width(4.dp))
				Text(
					text = course.author,
					color = Color.White,
					fontSize = 12.sp,
					fontWeight = FontWeight.Bold
				)
			}
			if (course.grade != null || course.progress != null) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						Icons.Default.Grade,
						contentDescription = null,
						tint = Color.White.copy(alpha = 0.8f),
						modifier = Modifier.size(16.dp)
					)
					Spacer(Modifier.width(4.dp))
					Text(
						text = course.grade ?: "${course.progress}%",
						color = Color.White,
						fontSize = 12.sp
					)
				}
			}

			Spacer(Modifier.height(8.dp))
			Text(
				text = course.description,
				color = Color.White.copy(alpha = 0.8f),
				fontSize = 12.sp
			)

			if (course.grade != null || course.progress != null) {
				Spacer(Modifier.height(12.dp))
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.background(Color.Black.copy(alpha = 0.2f), MaterialTheme.shapes.small)
						.padding(8.dp)
				) {
					Text(
						text = course.grade ?: "${course.progress}%",
						color = Color.White,
						fontWeight = FontWeight.Bold,
						fontSize = 16.sp
					)
				}
			}
		}
	}
}

@Composable
fun AddNewCard(onClick: () -> Unit) {
	Card(
		modifier = Modifier
			.fillMaxWidth()
			.height(200.dp)
			.padding(8.dp),
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
		shape = RoundedCornerShape(8.dp),
		onClick = onClick
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			Surface(
				shape = CircleShape,
				color = Color.Transparent,
				border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
			) {
				Icon(
					Icons.Default.Add,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onSurface,
					modifier = Modifier
						.padding(8.dp)
						.size(32.dp)
				)
			}
			Spacer(Modifier.height(16.dp))
			Text(
				text = "Add New",
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onSurface,
				fontSize = 20.sp
			)
		}
	}
}
