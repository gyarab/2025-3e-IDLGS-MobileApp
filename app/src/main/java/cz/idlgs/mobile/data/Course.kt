package cz.idlgs.mobile.data

import androidx.compose.ui.graphics.Color
import java.util.UUID

enum class CourseType {
	TEXTBOOK, COURSE
}

data class Course(
	val id: Int,
	val name: String,
	val description: String,
	val subject: String,
	val textbookId: Int,
	val createdAt: String,
	val modifiedAt: String,
	val red: Int,
	val green: Int,
	val blue: Int,
	val uuid: String = UUID.randomUUID().toString(),
	val archived: Boolean = false,
	val treatLateAsAbsence: Boolean = false,

	val type: CourseType = CourseType.COURSE,
	val author: String = "",
	val grade: String? = null,
	val progress: Int? = null
) {
	val color: Color get() = Color(red, green, blue)
}

data class Grade(
	val id: Int,
	val percentage: Int,
	val courseId: Int,
	val userId: Int,
	val assignmentId: Int? = null
)

data class PercentageGradeValue(
	val id: Int,
	val courseId: Int,
	val min: Int,
	val max: Int,
	val name: String
)

data class Assignment(
	val id: Int,
	val deadline: String,
	val createdAt: String,
	val modifiedAt: String,
	val courseId: Int,
	val title: String,
	val description: String,
	val uuid: String = UUID.randomUUID().toString(),
	val authorId: Int? = null
)

data class AssignmentComment(
	val id: Int,
	val assignmentId: Int,
	val createdAt: String,
	val comment: String,
	val authorId: Int? = null,
	val uuid: String = UUID.randomUUID().toString()
)

data class CourseMessage(
	val id: Int,
	val courseId: Int,
	val createdAt: String,
	val content: String,
	val authorId: Int? = null,
	val uuid: String = UUID.randomUUID().toString()
)

data class CourseMessageComment(
	val id: Int,
	val courseMessageId: Int,
	val createdAt: String,
	val comment: String,
	val authorId: Int? = null,
	val uuid: String = UUID.randomUUID().toString()
)

data class Question(
	val id: Int,
	val type: String,
	val question: String,
	val answer: String,
	val courseId: Int,
	val reportCount: Int = 0,
	val ai: Boolean = false,
	val aitr: String = "",
	val uuid: String = UUID.randomUUID().toString()
)

data class CourseCode(
	val id: Int,
	val expiresAt: String,
	val usesRemaining: Int,
	val infinite: Boolean,
	val courseId: Int,
	val code: String
)

data class CourseLessonTemplate(
	val id: Int,
	val startTime: String,
	val endTime: String,
	val additionalNote: String,
	val uuid: String = UUID.randomUUID().toString()
)

data class CourseLesson(
	val id: Int,
	val courseId: Int,
	val title: String,
	val note: String,
	val order: Int,
	val uuid: String = UUID.randomUUID().toString()
)

object MockData {
	val courses = listOf(
		Course(
			id = 1,
			name = "ucebnice 2526",
			description = "Test ucebnice 1",
			subject = "Testing",
			textbookId = 1,
			createdAt = "",
			modifiedAt = "",
			red = 0x5D,
			green = 0x40,
			blue = 0x37,
			type = CourseType.TEXTBOOK,
			author = "IDLGS Administration",
			archived = false
		),
		Course(
			id = 2,
			name = "kurz 2026",
			description = "New course",
			subject = "sebastianism",
			textbookId = 2,
			createdAt = "",
			modifiedAt = "",
			red = 0x00,
			green = 0x69,
			blue = 0x5C,
			type = CourseType.COURSE,
			author = "sebastian",
			grade = "Grade 1",
			progress = 90,
			archived = false
		)
	)
}
