package com.example.ai

import android.util.Log
import com.example.data.local.QuestionEntity
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import org.json.JSONArray
import org.json.JSONObject

private const val TAG = "GeminiAiService"

class GeminiAiService {

    // Lazy initialization of standard Flash model
    private val standardModel by lazy {
        try {
            Firebase.ai.generativeModel("gemini-3.8-flash")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize generative model", e)
            null
        }
    }

    // Lazy initialization of structured generation model
    private val generatorModel by lazy {
        try {
            val mcqSchema = Schema.obj(
                properties = mapOf(
                    "questions" to Schema.array(
                        items = Schema.obj(
                            properties = mapOf(
                                "questionText" to Schema.string("The MCQ question statement"),
                                "optionA" to Schema.string("Option A"),
                                "optionB" to Schema.string("Option B"),
                                "optionC" to Schema.string("Option C"),
                                "optionD" to Schema.string("Option D"),
                                "correctOption" to Schema.string("Correct option letter: A, B, C, or D"),
                                "explanation" to Schema.string("Step-by-step derivation and reasoning"),
                                "whyIncorrect" to Schema.string("Brief explanation of why the other 3 options are incorrect"),
                                "difficulty" to Schema.string("EASY, MEDIUM, or HARD")
                            )
                        )
                    )
                )
            )

            val config = generationConfig {
                responseMimeType = "application/json"
                responseSchema = mcqSchema
            }

            Firebase.ai.generativeModel(
                modelName = "gemini-3.8-flash",
                generationConfig = config
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize structured generator model", e)
            null
        }
    }

    suspend fun getTutorExplanation(question: QuestionEntity, selectedOption: String?): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = buildString {
                    appendLine("You are an expert AHSEC Class 12 Science Tutor for Assam Board.")
                    appendLine("Explain the following ${question.subject} question clearly for a student preparing for board exams.")
                    appendLine("Chapter: ${question.chapterTitle}")
                    appendLine("Question: ${question.questionText}")
                    appendLine("A) ${question.optionA}")
                    appendLine("B) ${question.optionB}")
                    appendLine("C) ${question.optionC}")
                    appendLine("D) ${question.optionD}")
                    appendLine("Correct Option: ${question.correctOption}")
                    if (selectedOption != null) {
                        appendLine("Student chose: Option $selectedOption")
                    }
                    appendLine("Provide:")
                    appendLine("1. Core Concept & Formula used")
                    appendLine("2. Step-by-step mathematical or conceptual derivation")
                    appendLine("3. Why the correct option is right")
                    appendLine("4. Why the other options are wrong or common traps students fall into")
                    appendLine("Keep tone encouraging, concise, and focused on AHSEC board exam scoring.")
                }

                val model = standardModel ?: return@withContext Result.success(
                    getDefaultTutorExplanation(question, selectedOption)
                )

                val response = withTimeout(45_000L) {
                    model.generateContent(prompt)
                }

                val text = response.text
                if (!text.isNullOrBlank()) {
                    Result.success(text.trim())
                } else {
                    Result.success(getDefaultTutorExplanation(question, selectedOption))
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "AI Tutor explanation error: ${e.message}", e)
                Result.success(getDefaultTutorExplanation(question, selectedOption))
            }
        }
    }

    suspend fun generateSyllabusQuestions(
        subject: String,
        chapterId: Int,
        chapterTitle: String,
        count: Int
    ): Result<List<QuestionEntity>> {
        return withContext(Dispatchers.IO) {
            try {
                val model = generatorModel ?: return@withContext Result.failure(
                    Exception("Firebase AI Logic generator model is currently initializing. Please check network.")
                )

                val prompt = buildString {
                    appendLine("Generate exactly $count high-yield multiple choice questions (MCQs) for AHSEC Class 12 Science (Assam Higher Secondary Education Council).")
                    appendLine("Subject: $subject (Part I)")
                    appendLine("Chapter $chapterId: $chapterTitle")
                    appendLine("Ensure questions strictly conform to the latest NCERT/AHSEC Class 12 syllabus.")
                    appendLine("Include exact formulas, standard SI units, and rigorous options A, B, C, D.")
                    appendLine("Ensure only one option is unambiguously correct.")
                    appendLine("Mark these as newly created syllabus-based questions (do not fabricate PYQ years).")
                }

                val response = withTimeout(60_000L) {
                    model.generateContent(prompt)
                }

                val jsonText = response.text ?: throw IllegalStateException("Model returned empty text.")
                val rootObj = JSONObject(jsonText)
                val qArray = rootObj.getJSONArray("questions")
                val resultList = mutableListOf<QuestionEntity>()

                for (i in 0 until qArray.length()) {
                    val item = qArray.getJSONObject(i)
                    resultList.add(
                        QuestionEntity(
                            subject = subject,
                            chapterId = chapterId,
                            chapterTitle = chapterTitle,
                            questionText = item.getString("questionText"),
                            optionA = item.getString("optionA"),
                            optionB = item.getString("optionB"),
                            optionC = item.getString("optionC"),
                            optionD = item.getString("optionD"),
                            correctOption = item.optString("correctOption", "A").uppercase(),
                            explanation = item.optString("explanation", "Syllabus-aligned derivation."),
                            whyIncorrect = item.optString("whyIncorrect", ""),
                            isPyq = false,
                            pyqYear = null,
                            difficulty = item.optString("difficulty", "MEDIUM"),
                            isPublished = false, // Saved for Admin review before publishing
                            createdBy = "GEMINI_AI"
                        )
                    )
                }

                Result.success(resultList)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "Question generation failed: ${e.message}", e)
                Result.failure(e)
            }
        }
    }

    private fun getDefaultTutorExplanation(question: QuestionEntity, selectedOption: String?): String {
        return buildString {
            appendLine("📚 **Concept & Solution Breakdown**")
            appendLine()
            appendLine(question.explanation)
            appendLine()
            if (question.whyIncorrect.isNotBlank()) {
                appendLine("⚠️ **Option Analysis:**")
                appendLine(question.whyIncorrect)
            }
            if (selectedOption != null && selectedOption != question.correctOption) {
                appendLine()
                appendLine("💡 *Tutor Note:* You selected Option $selectedOption. Remember to carefully examine the units and signs before final calculation!")
            }
        }
    }
}
