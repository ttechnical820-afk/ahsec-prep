package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY chapterId ASC, id ASC")
    fun getAllQuestions(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE subject = :subject ORDER BY chapterId ASC, id ASC")
    fun getQuestionsBySubject(subject: String): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE subject = :subject AND chapterId = :chapterId AND isPublished = 1 ORDER BY id ASC")
    fun getQuestionsByChapter(subject: String, chapterId: Int): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions WHERE subject = :subject AND chapterId = :chapterId AND isPublished = 1")
    suspend fun getQuestionsByChapterSync(subject: String, chapterId: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE subject = :subject AND isPublished = 1 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsForSubject(subject: String, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE subject = :subject AND chapterId = :chapterId AND isPublished = 1 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomQuestionsForChapter(subject: String, chapterId: Int, limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE isPyq = 1 AND pyqYear LIKE '%Repeated%' AND isPublished = 1 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRepeatedPyqs(limit: Int): List<QuestionEntity>

    @Query("SELECT * FROM questions WHERE subject = :subject AND isPyq = 1 AND isPublished = 1 ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomPyqQuestionsForSubject(subject: String, limit: Int): List<QuestionEntity>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionCount(): Int

    @Query("SELECT COUNT(*) FROM questions WHERE subject = :subject")
    suspend fun getQuestionCountBySubject(subject: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(question: QuestionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<QuestionEntity>)

    @Update
    suspend fun updateQuestion(question: QuestionEntity)

    @Delete
    suspend fun deleteQuestion(question: QuestionEntity)

    @Query("DELETE FROM questions WHERE id = :id")
    suspend fun deleteQuestionById(id: Long)
}

@Dao
interface ExamDao {
    @Query("SELECT * FROM exam_attempts WHERE studentEmail = :email ORDER BY timestamp DESC")
    fun getAttemptsForStudent(email: String): Flow<List<ExamAttemptEntity>>

    @Query("SELECT * FROM exam_attempts ORDER BY timestamp DESC")
    fun getAllAttempts(): Flow<List<ExamAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: ExamAttemptEntity): Long
}

@Dao
interface StudentDao {
    @Query("SELECT * FROM students WHERE email = :email LIMIT 1")
    suspend fun getStudentByEmail(email: String): StudentEntity?

    @Query("SELECT * FROM students ORDER BY registeredAt DESC")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: StudentEntity)
}

@Dao
interface ProgressDao {
    @Query("SELECT * FROM user_progress WHERE email = :email")
    fun getProgressForUser(email: String): Flow<List<UserProgressEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordProgress(progress: UserProgressEntity)
}
