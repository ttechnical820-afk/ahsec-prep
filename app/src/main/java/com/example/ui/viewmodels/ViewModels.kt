package com.example.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.GeminiAiService
import com.example.data.local.ExamAttemptEntity
import com.example.data.local.QuestionEntity
import com.example.data.local.StudentEntity
import com.example.data.local.UserProgressEntity
import com.example.data.model.ChapterInfo
import com.example.data.model.Subject
import com.example.data.model.SyllabusCatalog
import com.example.data.repository.AuthRepository
import com.example.data.repository.ExamRepository
import com.example.data.repository.QuestionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ----------------------------------------------------
// AUTH VIEW MODEL
// ----------------------------------------------------
sealed interface AuthUiState {
    data object Idle : AuthUiState
    data object Loading : AuthUiState
    data class Success(val user: StudentEntity) : AuthUiState
    data class Error(val message: String) : AuthUiState
}

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _currentUser = MutableStateFlow<StudentEntity?>(null)
    val currentUser: StateFlow<StudentEntity?> = _currentUser.asStateFlow()

    fun register(name: String, address: String, phone: String, email: String, pass: String) {
        if (name.isBlank() || email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Please fill all required registration fields.")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.registerStudent(name, address, phone, email, pass)
            result.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _uiState.value = AuthUiState.Success(user)
                },
                onFailure = { err ->
                    _uiState.value = AuthUiState.Error(err.localizedMessage ?: "Registration failed")
                }
            )
        }
    }

    fun login(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) {
            _uiState.value = AuthUiState.Error("Please enter email and password.")
            return
        }
        viewModelScope.launch {
            _uiState.value = AuthUiState.Loading
            val result = authRepository.login(email, pass)
            result.fold(
                onSuccess = { user ->
                    _currentUser.value = user
                    _uiState.value = AuthUiState.Success(user)
                },
                onFailure = { err ->
                    _uiState.value = AuthUiState.Error(err.localizedMessage ?: "Login failed")
                }
            )
        }
    }

    fun logout() {
        _currentUser.value = null
        _uiState.value = AuthUiState.Idle
    }

    fun resetState() {
        _uiState.value = AuthUiState.Idle
    }
}

// ----------------------------------------------------
// MAIN APP VIEW MODEL (DASHBOARD & PRACTICE)
// ----------------------------------------------------
class MainViewModel(
    private val questionRepository: QuestionRepository,
    private val examRepository: ExamRepository,
    private val geminiService: GeminiAiService
) : ViewModel() {

    private val _selectedSubject = MutableStateFlow(Subject.PHYSICS)
    val selectedSubject: StateFlow<Subject> = _selectedSubject.asStateFlow()

    val allQuestions: StateFlow<List<QuestionEntity>> = questionRepository.allQuestions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Practice Mode State
    private val _practiceChapter = MutableStateFlow<ChapterInfo?>(null)
    val practiceChapter: StateFlow<ChapterInfo?> = _practiceChapter.asStateFlow()

    private val _practiceQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
    val practiceQuestions: StateFlow<List<QuestionEntity>> = _practiceQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedOption = MutableStateFlow<String?>(null)
    val selectedOption: StateFlow<String?> = _selectedOption.asStateFlow()

    private val _showExplanation = MutableStateFlow(false)
    val showExplanation: StateFlow<Boolean> = _showExplanation.asStateFlow()

    private val _aiTutorExplanation = MutableStateFlow<String?>(null)
    val aiTutorExplanation: StateFlow<String?> = _aiTutorExplanation.asStateFlow()

    private val _isAiLoading = MutableStateFlow(false)
    val isAiLoading: StateFlow<Boolean> = _isAiLoading.asStateFlow()

    init {
        viewModelScope.launch {
            questionRepository.ensureDatabaseSeeded()
        }
    }

    fun selectSubject(subject: Subject) {
        _selectedSubject.value = subject
    }

    fun startPractice(chapter: ChapterInfo) {
        _practiceChapter.value = chapter
        _currentQuestionIndex.value = 0
        _selectedOption.value = null
        _showExplanation.value = false
        _aiTutorExplanation.value = null

        viewModelScope.launch {
            val list = allQuestions.value.filter {
                it.subject.equals(chapter.subject.code, ignoreCase = true) &&
                        it.chapterId == chapter.id &&
                        it.isPublished
            }
            _practiceQuestions.value = list
        }
    }

    fun selectAnswer(option: String, userEmail: String?) {
        if (_selectedOption.value != null) return // Already answered
        _selectedOption.value = option
        _showExplanation.value = true

        val currentQ = _practiceQuestions.value.getOrNull(_currentQuestionIndex.value)
        if (currentQ != null && userEmail != null) {
            val isCorrect = option.equals(currentQ.correctOption, ignoreCase = true)
            viewModelScope.launch {
                examRepository.recordProgress(userEmail, currentQ.id, option, isCorrect)
            }
        }
    }

    fun retryQuestion() {
        _selectedOption.value = null
        _showExplanation.value = false
        _aiTutorExplanation.value = null
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < _practiceQuestions.value.size - 1) {
            _currentQuestionIndex.value++
            _selectedOption.value = null
            _showExplanation.value = false
            _aiTutorExplanation.value = null
        }
    }

    fun previousQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value--
            _selectedOption.value = null
            _showExplanation.value = false
            _aiTutorExplanation.value = null
        }
    }

    fun askAiTutor() {
        val currentQ = _practiceQuestions.value.getOrNull(_currentQuestionIndex.value) ?: return
        viewModelScope.launch {
            _isAiLoading.value = true
            val res = geminiService.getTutorExplanation(currentQ, _selectedOption.value)
            _aiTutorExplanation.value = res.getOrNull()
            _isAiLoading.value = false
        }
    }

    fun getStudentAttempts(email: String): StateFlow<List<ExamAttemptEntity>> {
        return examRepository.getStudentAttempts(email)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    fun getUserProgress(email: String): StateFlow<List<UserProgressEntity>> {
        return examRepository.getUserProgress(email)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }
}

// ----------------------------------------------------
// EXAM VIEW MODEL (EXAM MODE & RESULTS)
// ----------------------------------------------------
class ExamViewModel(
    private val questionRepository: QuestionRepository,
    private val examRepository: ExamRepository
) : ViewModel() {

    private val _examQuestions = MutableStateFlow<List<QuestionEntity>>(emptyList())
    val examQuestions: StateFlow<List<QuestionEntity>> = _examQuestions.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    private val _studentAnswers = MutableStateFlow<Map<Int, String>>(emptyMap())
    val studentAnswers: StateFlow<Map<Int, String>> = _studentAnswers.asStateFlow()

    private val _markedForReview = MutableStateFlow<Set<Int>>(emptySet())
    val markedForReview: StateFlow<Set<Int>> = _markedForReview.asStateFlow()

    private val _timeRemainingSeconds = MutableStateFlow(0)
    val timeRemainingSeconds: StateFlow<Int> = _timeRemainingSeconds.asStateFlow()

    private val _isExamSubmitted = MutableStateFlow(false)
    val isExamSubmitted: StateFlow<Boolean> = _isExamSubmitted.asStateFlow()

    private val _lastExamAttempt = MutableStateFlow<ExamAttemptEntity?>(null)
    val lastExamAttempt: StateFlow<ExamAttemptEntity?> = _lastExamAttempt.asStateFlow()

    private var timerJob: Job? = null
    private var totalDurationSeconds = 0

    private var activeStudentEmail: String = "student"

    fun startExam(
        subject: Subject,
        chapterId: Int = 0,
        questionCount: Int = 20,
        durationMinutes: Int = 30,
        userEmail: String = "student"
    ) {
        activeStudentEmail = userEmail
        timerJob?.cancel()
        _currentIndex.value = 0
        _studentAnswers.value = emptyMap()
        _markedForReview.value = emptySet()
        _isExamSubmitted.value = false
        _lastExamAttempt.value = null

        totalDurationSeconds = durationMinutes * 60
        _timeRemainingSeconds.value = totalDurationSeconds

        viewModelScope.launch {
            val questions = questionRepository.getExamQuestions(subject.code, chapterId, questionCount)
            _examQuestions.value = questions

            startTimer()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_timeRemainingSeconds.value > 0 && !_isExamSubmitted.value) {
                delay(1000L)
                _timeRemainingSeconds.value--
            }
            if (_timeRemainingSeconds.value <= 0 && !_isExamSubmitted.value) {
                // Auto-submit on time expiry
                submitExam(activeStudentEmail)
            }
        }
    }

    fun selectAnswer(index: Int, option: String) {
        val current = _studentAnswers.value.toMutableMap()
        current[index] = option
        _studentAnswers.value = current
    }

    fun toggleMarkForReview(index: Int) {
        val current = _markedForReview.value.toMutableSet()
        if (current.contains(index)) {
            current.remove(index)
        } else {
            current.add(index)
        }
        _markedForReview.value = current
    }

    fun goToQuestion(index: Int) {
        if (index in 0 until _examQuestions.value.size) {
            _currentIndex.value = index
        }
    }

    fun submitExam(userEmail: String) {
        if (_isExamSubmitted.value) return
        timerJob?.cancel()
        _isExamSubmitted.value = true

        val questions = _examQuestions.value
        val answers = _studentAnswers.value

        var correct = 0
        var incorrect = 0
        var unattempted = 0

        for (i in questions.indices) {
            val ans = answers[i]
            if (ans == null) {
                unattempted++
            } else if (ans.equals(questions[i].correctOption, ignoreCase = true)) {
                correct++
            } else {
                incorrect++
            }
        }

        val total = questions.size
        val score = (correct * 4) - incorrect // Standard board +4/-1 or raw +1
        val percentage = if (total > 0) (correct.toFloat() / total) * 100f else 0f
        val timeTaken = totalDurationSeconds - _timeRemainingSeconds.value

        val attempt = ExamAttemptEntity(
            studentEmail = userEmail,
            subject = questions.firstOrNull()?.subject ?: "ALL",
            chapterId = 0,
            totalQuestions = total,
            correctCount = correct,
            incorrectCount = incorrect,
            unattemptedCount = unattempted,
            score = correct,
            percentage = percentage,
            durationSeconds = timeTaken
        )

        _lastExamAttempt.value = attempt

        viewModelScope.launch {
            examRepository.saveExamAttempt(attempt)
        }
    }
}
