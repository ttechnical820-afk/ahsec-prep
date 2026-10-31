package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class QuestionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subject: String, // "PHYSICS" or "CHEMISTRY"
    val chapterId: Int,
    val chapterTitle: String,
    val questionText: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctOption: String, // "A", "B", "C", "D"
    val explanation: String,
    val whyIncorrect: String = "",
    val isPyq: Boolean = false,
    val pyqYear: String? = null, // e.g. "AHSEC 2024", "AHSEC 2023", "AHSEC 2022", "AHSEC 2020", "AHSEC 2019"
    val difficulty: String = "MEDIUM", // "EASY", "MEDIUM", "HARD"
    val isPublished: Boolean = true,
    val createdBy: String = "VERIFIED_AHSEC_CURATOR"
)

@Entity(tableName = "exam_attempts")
data class ExamAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val studentEmail: String,
    val subject: String,
    val chapterId: Int, // 0 for full syllabus mock
    val totalQuestions: Int,
    val correctCount: Int,
    val incorrectCount: Int,
    val unattemptedCount: Int,
    val score: Int,
    val percentage: Float,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "students")
data class StudentEntity(
    @PrimaryKey
    val email: String,
    val fullName: String,
    val address: String,
    val phone: String,
    val passwordHash: String,
    val role: String = "STUDENT", // "STUDENT" or "ADMIN"
    val registeredAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "user_progress", primaryKeys = ["email", "questionId"])
data class UserProgressEntity(
    val email: String,
    val questionId: Long,
    val lastAnswer: String,
    val isCorrect: Boolean,
    val attemptsCount: Int = 1,
    val updatedAt: Long = System.currentTimeMillis()
)
