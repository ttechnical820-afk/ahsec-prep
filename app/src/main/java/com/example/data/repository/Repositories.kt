package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.StudentEntity
import com.example.data.local.UserProgressEntity
import com.example.data.seed.QuestionSeedData
import kotlinx.coroutines.flow.Flow
import org.json.JSONArray
import org.json.JSONObject

import android.content.Context

class QuestionRepository(private val context: Context, private val database: AppDatabase) {
    private val questionDao = database.questionDao()

    val allQuestions: Flow<List<QuestionEntity>> = questionDao.getAllQuestions()

    fun getQuestionsBySubject(subject: String): Flow<List<QuestionEntity>> {
        return questionDao.getQuestionsBySubject(subject)
    }

    fun getQuestionsByChapter(subject: String, chapterId: Int): Flow<List<QuestionEntity>> {
        return questionDao.getQuestionsByChapter(subject, chapterId)
    }

    suspend fun ensureDatabaseSeeded() {
        val count = questionDao.getQuestionCount()
        if (count < 1500) {
            try {
                val jsonString = context.assets.open("ahsec_questions.json").bufferedReader().use { it.readText() }
                parseAndImportJson(jsonString)
            } catch (e: Exception) {
                val initialList = QuestionSeedData.getInitialQuestions()
                questionDao.insertQuestions(initialList)
            }
        }
    }

    suspend fun getExamQuestions(subject: String, chapterId: Int, limit: Int, prioritizeRepeatedPyq: Boolean = true): List<QuestionEntity> {
        val questions = if (prioritizeRepeatedPyq) {
            val repeated = if (chapterId > 0) {
                questionDao.getRandomQuestionsForChapter(subject, chapterId, limit)
            } else {
                questionDao.getRepeatedPyqs(limit)
            }
            if (repeated.size < limit) {
                val backfill = if (chapterId > 0) {
                    questionDao.getRandomQuestionsForChapter(subject, chapterId, limit - repeated.size)
                } else {
                    questionDao.getRandomQuestionsForSubject(subject, limit - repeated.size)
                }
                (repeated + backfill).distinctBy { it.id }.take(limit)
            } else {
                repeated
            }
        } else {
            if (chapterId > 0) {
                questionDao.getRandomQuestionsForChapter(subject, chapterId, limit)
            } else {
                questionDao.getRandomQuestionsForSubject(subject, limit)
            }
        }
        return questions.ifEmpty {
            if (chapterId > 0) questionDao.getRandomQuestionsForChapter(subject, chapterId, limit)
            else questionDao.getRandomQuestionsForSubject(subject, limit)
        }
    }

    suspend fun addQuestion(question: QuestionEntity): Long {
        return questionDao.insertQuestion(question)
    }

    suspend fun updateQuestion(question: QuestionEntity) {
        questionDao.updateQuestion(question)
    }

    suspend fun deleteQuestion(id: Long) {
        questionDao.deleteQuestionById(id)
    }

    suspend fun parseAndImportJson(jsonText: String): Result<Int> {
        return try {
            val jsonArray = JSONArray(jsonText)
            val imported = mutableListOf<QuestionEntity>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val subject = obj.optString("subject", "PHYSICS").uppercase()
                val chapterId = obj.optInt("chapterId", 1)
                val chapterTitle = obj.optString("chapterTitle", "Chapter $chapterId")
                val text = obj.getString("questionText")
                val optA = obj.getString("optionA")
                val optB = obj.getString("optionB")
                val optC = obj.getString("optionC")
                val optD = obj.getString("optionD")
                val correct = obj.optString("correctOption", "A").uppercase()
                val explanation = obj.optString("explanation", "Verified AHSEC standard explanation.")
                val whyIncorrect = obj.optString("whyIncorrect", "")
                val isPyq = obj.optBoolean("isPyq", false)
                val pyqYear = if (obj.has("pyqYear") && !obj.isNull("pyqYear")) obj.getString("pyqYear") else null
                val difficulty = obj.optString("difficulty", "MEDIUM")

                imported.add(
                    QuestionEntity(
                        subject = subject,
                        chapterId = chapterId,
                        chapterTitle = chapterTitle,
                        questionText = text,
                        optionA = optA,
                        optionB = optB,
                        optionC = optC,
                        optionD = optD,
                        correctOption = correct,
                        explanation = explanation,
                        whyIncorrect = whyIncorrect,
                        isPyq = isPyq,
                        pyqYear = pyqYear,
                        difficulty = difficulty,
                        isPublished = true,
                        createdBy = "ADMIN_IMPORT"
                    )
                )
            }
            imported.chunked(100).forEach { questionDao.insertQuestions(it) }
            Result.success(imported.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun parseAndImportCsv(csvText: String): Result<Int> {
        return try {
            val lines = csvText.lines().filter { it.isNotBlank() }
            if (lines.isEmpty()) return Result.success(0)

            val imported = mutableListOf<QuestionEntity>()
            // Expecting: subject,chapterId,chapterTitle,questionText,optionA,optionB,optionC,optionD,correctOption,explanation,isPyq,pyqYear
            val startIndex = if (lines[0].contains("questionText", ignoreCase = true)) 1 else 0
            for (i in startIndex until lines.size) {
                val parts = lines[i].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)".toRegex())
                    .map { it.trim().removeSurrounding("\"") }
                if (parts.size >= 9) {
                    val subject = parts.getOrNull(0)?.uppercase() ?: "PHYSICS"
                    val chapterId = parts.getOrNull(1)?.toIntOrNull() ?: 1
                    val chapterTitle = parts.getOrNull(2) ?: "Chapter $chapterId"
                    val text = parts.getOrNull(3) ?: ""
                    val optA = parts.getOrNull(4) ?: ""
                    val optB = parts.getOrNull(5) ?: ""
                    val optC = parts.getOrNull(6) ?: ""
                    val optD = parts.getOrNull(7) ?: ""
                    val correct = parts.getOrNull(8)?.uppercase() ?: "A"
                    val explanation = parts.getOrNull(9) ?: "Standard AHSEC solution."
                    val isPyq = parts.getOrNull(10)?.toBooleanStrictOrNull() ?: false
                    val pyqYear = parts.getOrNull(11)

                    if (text.isNotBlank() && optA.isNotBlank()) {
                        imported.add(
                            QuestionEntity(
                                subject = subject,
                                chapterId = chapterId,
                                chapterTitle = chapterTitle,
                                questionText = text,
                                optionA = optA,
                                optionB = optB,
                                optionC = optC,
                                optionD = optD,
                                correctOption = correct,
                                explanation = explanation,
                                isPyq = isPyq,
                                pyqYear = pyqYear,
                                difficulty = "MEDIUM",
                                isPublished = true,
                                createdBy = "ADMIN_CSV_IMPORT"
                            )
                        )
                    }
                }
            }
            questionDao.insertQuestions(imported)
            Result.success(imported.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

class ExamRepository(private val database: AppDatabase) {
    private val examDao = database.examDao()
    private val progressDao = database.progressDao()

    fun getStudentAttempts(email: String): Flow<List<ExamAttemptEntity>> {
        return examDao.getAttemptsForStudent(email)
    }

    fun getAllAttempts(): Flow<List<ExamAttemptEntity>> {
        return examDao.getAllAttempts()
    }

    suspend fun saveExamAttempt(attempt: ExamAttemptEntity): Long {
        return examDao.insertAttempt(attempt)
    }

    fun getUserProgress(email: String): Flow<List<UserProgressEntity>> {
        return progressDao.getProgressForUser(email)
    }

    suspend fun recordProgress(email: String, questionId: Long, answer: String, isCorrect: Boolean) {
        progressDao.recordProgress(
            UserProgressEntity(
                email = email,
                questionId = questionId,
                lastAnswer = answer,
                isCorrect = isCorrect
            )
        )
    }
}

class AuthRepository(private val database: AppDatabase) {
    private val studentDao = database.studentDao()

    suspend fun registerStudent(
        fullName: String,
        address: String,
        phone: String,
        email: String,
        password: String
    ): Result<StudentEntity> {
        val existing = studentDao.getStudentByEmail(email.trim().lowercase())
        if (existing != null) {
            return Result.failure(Exception("An account with this email already exists."))
        }
        val student = StudentEntity(
            email = email.trim().lowercase(),
            fullName = fullName.trim(),
            address = address.trim(),
            phone = phone.trim(),
            passwordHash = password, // For demonstration & local storage
            role = "STUDENT"
        )
        studentDao.insertStudent(student)
        return Result.success(student)
    }

    suspend fun login(email: String, password: String): Result<StudentEntity> {
        val cleanEmail = email.trim().lowercase()

        val student = studentDao.getStudentByEmail(cleanEmail)
            ?: return Result.failure(Exception("No account found for $cleanEmail. Please register your student account first."))

        if (student.passwordHash != password) {
            return Result.failure(Exception("Incorrect password. Please try again."))
        }
        return Result.success(student)
    }

    fun getAllStudents(): Flow<List<StudentEntity>> {
        return studentDao.getAllStudents()
    }
}
