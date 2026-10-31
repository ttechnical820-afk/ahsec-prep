package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ai.GeminiAiService
import com.example.ai.configureAppCheck
import com.example.data.local.AppDatabase
import com.example.data.model.Subject
import com.example.data.repository.AuthRepository
import com.example.data.repository.ExamRepository
import com.example.data.repository.QuestionRepository
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExamResultScreen
import com.example.ui.screens.ExamScreen
import com.example.ui.screens.PracticeScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodels.AuthViewModel
import com.example.ui.viewmodels.ExamViewModel
import com.example.ui.viewmodels.MainViewModel

enum class AppScreen {
    DASHBOARD,
    PRACTICE,
    EXAM,
    EXAM_RESULT,
    AUTH
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Mandatory App Check configuration before UI or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        // Initialize Data & AI Dependencies
        val database = AppDatabase.getInstance(this)
        val questionRepository = QuestionRepository(this, database)
        val examRepository = ExamRepository(database)
        val authRepository = AuthRepository(database)
        val geminiService = GeminiAiService()

        // Create ViewModels
        val authViewModel = AuthViewModel(authRepository)
        val mainViewModel = MainViewModel(questionRepository, examRepository, geminiService)
        val examViewModel = ExamViewModel(questionRepository, examRepository)

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AhsecAppNavHost(
                        mainViewModel = mainViewModel,
                        examViewModel = examViewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun AhsecAppNavHost(
    mainViewModel: MainViewModel,
    examViewModel: ExamViewModel,
    authViewModel: AuthViewModel
) {
    var currentScreen by remember {
        mutableStateOf(if (authViewModel.currentUser.value != null) AppScreen.DASHBOARD else AppScreen.AUTH)
    }

    when (currentScreen) {
        AppScreen.DASHBOARD -> {
            DashboardScreen(
                mainViewModel = mainViewModel,
                authViewModel = authViewModel,
                onNavigateToPractice = { chapter ->
                    mainViewModel.startPractice(chapter)
                    currentScreen = AppScreen.PRACTICE
                },
                onNavigateToExamConfig = { subject, chapterId ->
                    examViewModel.startExam(
                        subject = subject,
                        chapterId = chapterId,
                        questionCount = 15,
                        durationMinutes = 20,
                        userEmail = authViewModel.currentUser.value?.email ?: "student"
                    )
                    currentScreen = AppScreen.EXAM
                },
                onNavigateToAuth = {
                    currentScreen = AppScreen.AUTH
                }
            )
        }

        AppScreen.PRACTICE -> {
            BackHandler {
                currentScreen = AppScreen.DASHBOARD
            }
            PracticeScreen(
                mainViewModel = mainViewModel,
                authViewModel = authViewModel,
                onBackClick = { currentScreen = AppScreen.DASHBOARD }
            )
        }

        AppScreen.EXAM -> {
            BackHandler {
                // Prevent accidental exit during timed test
            }
            ExamScreen(
                examViewModel = examViewModel,
                authViewModel = authViewModel,
                onExamSubmitted = {
                    currentScreen = AppScreen.EXAM_RESULT
                },
                onExitExam = {
                    currentScreen = AppScreen.DASHBOARD
                }
            )
        }

        AppScreen.EXAM_RESULT -> {
            BackHandler {
                currentScreen = AppScreen.DASHBOARD
            }
            ExamResultScreen(
                examViewModel = examViewModel,
                onDashboardClick = {
                    currentScreen = AppScreen.DASHBOARD
                },
                onRetakeClick = {
                    val last = examViewModel.lastExamAttempt.value
                    val sub = if (last?.subject == "CHEMISTRY") Subject.CHEMISTRY else Subject.PHYSICS
                    examViewModel.startExam(
                        subject = sub,
                        chapterId = last?.chapterId ?: 0,
                        questionCount = last?.totalQuestions ?: 15,
                        durationMinutes = 20,
                        userEmail = authViewModel.currentUser.value?.email ?: "student"
                    )
                    currentScreen = AppScreen.EXAM
                }
            )
        }

        AppScreen.AUTH -> {
            BackHandler(enabled = authViewModel.currentUser.value != null) {
                currentScreen = AppScreen.DASHBOARD
            }
            AuthScreen(
                authViewModel = authViewModel,
                onAuthSuccess = { currentScreen = AppScreen.DASHBOARD }
            )
        }
    }
}
