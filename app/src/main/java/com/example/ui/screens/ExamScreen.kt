package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flip
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionEntity
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.GoldPyq
import com.example.ui.theme.GoldPyqContainer
import com.example.ui.theme.SuccessGreen
import com.example.ui.viewmodels.AuthViewModel
import com.example.ui.viewmodels.ExamViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ExamScreen(
    examViewModel: ExamViewModel,
    authViewModel: AuthViewModel,
    onExamSubmitted: () -> Unit,
    onExitExam: () -> Unit
) {
    val questions by examViewModel.examQuestions.collectAsState()
    val currentIndex by examViewModel.currentIndex.collectAsState()
    val studentAnswers by examViewModel.studentAnswers.collectAsState()
    val markedForReview by examViewModel.markedForReview.collectAsState()
    val timeRemaining by examViewModel.timeRemainingSeconds.collectAsState()
    val isSubmitted by examViewModel.isExamSubmitted.collectAsState()
    val currentUser by authViewModel.currentUser.collectAsState()

    var showPaletteSheet by remember { mutableStateOf(false) }
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }
    var showExitConfirmDialog by remember { mutableStateOf(false) }

    // 3D OMR Sheet Flip state
    var showOmrSheet by remember { mutableStateOf(false) }

    // 3D Question Turn Animation
    val questionPageRotation = remember { Animatable(0f) }
    var previousIndex by remember { mutableStateOf(currentIndex) }

    val density = LocalDensity.current.density

    LaunchedEffect(currentIndex) {
        if (currentIndex != previousIndex) {
            val direction = if (currentIndex > previousIndex) 1f else -1f
            previousIndex = currentIndex
            // Quick 3D flip effect on question change
            questionPageRotation.snapTo(direction * 22f)
            questionPageRotation.animateTo(
                targetValue = 0f,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
            )
        }
    }

    // 3D 180° Flip between Question Paper & OMR Response Sheet
    val omrFlipAngle by animateFloatAsState(
        targetValue = if (showOmrSheet) 180f else 0f,
        animationSpec = tween(durationMillis = 550, easing = FastOutSlowInEasing),
        label = "omr_3d_flip"
    )

    LaunchedEffect(isSubmitted) {
        if (isSubmitted) {
            onExamSubmitted()
        }
    }

    val currentQ = questions.getOrNull(currentIndex)
    val isMarked = markedForReview.contains(currentIndex)

    val minutes = timeRemaining / 60
    val seconds = timeRemaining % 60
    val timerText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    val isUrgent = timeRemaining < 300 // < 5 mins

    // Pulsing animation for urgent clock
    val urgentScale by animateFloatAsState(
        targetValue = if (isUrgent && (timeRemaining % 2 == 0)) 1.08f else 1.0f,
        animationSpec = tween(durationMillis = 400),
        label = "urgent_pulse"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.graphicsLayer {
                            scaleX = urgentScale
                            scaleY = urgentScale
                        }
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isUrgent) ErrorRed.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Timer,
                                    contentDescription = "Timer",
                                    tint = if (isUrgent) ErrorRed else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = timerText,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isUrgent) ErrorRed else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isUrgent) "Closing soon!" else "Official Time",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isUrgent) ErrorRed else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { showExitConfirmDialog = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Exit Exam"
                        )
                    }
                },
                actions = {
                    // 3D Flip to OMR Sheet / Booklet Toggle
                    OutlinedButton(
                        onClick = { showOmrSheet = !showOmrSheet },
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                        modifier = Modifier.padding(end = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Flip,
                            contentDescription = "3D Flip",
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (showOmrSheet) "Paper" else "3D OMR",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(
                        onClick = { showPaletteSheet = true },
                        modifier = Modifier.testTag("question_palette_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridOn,
                            contentDescription = "Question Palette"
                        )
                    }

                    Button(
                        onClick = { showSubmitConfirmDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .testTag("submit_exam_top_button")
                    ) {
                        Text("Submit", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = { examViewModel.goToQuestion(currentIndex - 1) },
                        enabled = currentIndex > 0,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Prev")
                    }

                    OutlinedButton(
                        onClick = { examViewModel.toggleMarkForReview(currentIndex) },
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isMarked) GoldPyq else MaterialTheme.colorScheme.onSurface
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(
                            imageVector = if (isMarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isMarked) GoldPyq else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isMarked) "Reviewing" else "Review")
                    }

                    Button(
                        onClick = {
                            if (currentIndex < questions.size - 1) {
                                examViewModel.goToQuestion(currentIndex + 1)
                            } else {
                                showSubmitConfirmDialog = true
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("exam_next_button")
                    ) {
                        Text(if (currentIndex < questions.size - 1) "Next" else "Finish Paper", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        if (currentQ == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("Preparing AHSEC Examination Paper...", fontWeight = FontWeight.SemiBold)
            }
        } else {
            // Container with 3D Flip Card
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background)
                    .graphicsLayer {
                        rotationY = omrFlipAngle
                        cameraDistance = 16f * density
                    }
            ) {
                if (omrFlipAngle <= 90f) {
                    // ==========================================
                    // FRONT FACE: AUTHENTIC AHSEC QUESTION PAPER
                    // ==========================================
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Official Board Exam Header Card
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            shadowElevation = 2.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "ASSAM HIGHER SECONDARY EDUCATION COUNCIL",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary,
                                    letterSpacing = 0.6.sp
                                )
                                Text(
                                    text = "HIGHER SECONDARY FINAL EXAMINATION 2026",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "STREAM: SCIENCE (PART I)",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "SERIES: SET-A · NEGATIVE: NIL",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        // Question Status & Repeated PYQ Badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "${currentIndex + 1}",
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Question ${currentIndex + 1} of ${questions.size}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            if (currentQ.isPyq) {
                                val isRepeated = currentQ.pyqYear?.contains("Repeated") == true
                                Surface(
                                    color = GoldPyqContainer,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, GoldPyq.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = if (isRepeated) "★ HIGH-YIELD REPEATED PYQ" else "★ ${currentQ.pyqYear}",
                                        color = GoldPyq,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }

                        // 3D Question Card with Page-Turn Animation
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer {
                                    rotationY = questionPageRotation.value
                                    cameraDistance = 16f * density
                                }
                                .shadow(4.dp, RoundedCornerShape(16.dp)),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Surface(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(6.dp),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    Text(
                                        text = "${currentQ.subject} · ${currentQ.chapterTitle}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Text(
                                    text = currentQ.questionText,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 25.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // 4 Realistic 3D Interactive Options with OMR Bubbles
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            val opts = listOf(
                                "A" to currentQ.optionA,
                                "B" to currentQ.optionB,
                                "C" to currentQ.optionC,
                                "D" to currentQ.optionD
                            )

                            opts.forEach { (letter, text) ->
                                val isSelected = studentAnswers[currentIndex] == letter

                                RealisticOmrOptionCard(
                                    letter = letter,
                                    text = text,
                                    isSelected = isSelected,
                                    onClick = {
                                        examViewModel.selectAnswer(currentIndex, letter)
                                    }
                                )
                            }
                        }
                    }
                } else {
                    // ==========================================
                    // BACK FACE: REALISTIC 3D PHYSICAL OMR SHEET
                    // ==========================================
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer {
                                rotationY = 180f // Compensate flip so text renders correctly
                            }
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // OMR Header with simulated barcode
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFFBFBF8), // Authentic off-white paper color
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF1E293B))
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // Simulated Barcode
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier.height(20.dp)
                                ) {
                                    val widths = listOf(2, 4, 1, 3, 2, 5, 1, 4, 2, 3, 1, 4, 3, 2, 4, 2, 5, 1, 3, 2, 4)
                                    widths.forEach { w ->
                                        Box(
                                            modifier = Modifier
                                                .width(w.dp)
                                                .height(20.dp)
                                                .background(Color.Black)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "AHSEC OMR RESPONSE SHEET · SERIES A",
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF0F172A),
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Candidate: ${currentUser?.fullName ?: "REGISTERED CANDIDATE"} · Roll: 0204-10482",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF334155)
                                )
                                Text(
                                    text = "• Tap any circle [A, B, C, D] to mark your answer in ink",
                                    fontSize = 10.sp,
                                    color = Color(0xFF64748B),
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                        }

                        // OMR Response Rows
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFFBFBF8),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            shadowElevation = 3.dp
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                questions.forEachIndexed { idx, q ->
                                    val selectedOpt = studentAnswers[idx]
                                    val isCurrent = idx == currentIndex

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 5.dp)
                                            .background(
                                                if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
                                                else Color.Transparent,
                                                RoundedCornerShape(6.dp)
                                            )
                                            .padding(horizontal = 6.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = String.format(Locale.getDefault(), "Q%02d", idx + 1),
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily.Monospace,
                                            color = if (isCurrent) MaterialTheme.colorScheme.primary else Color(0xFF1E293B),
                                            modifier = Modifier.width(36.dp)
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        // 4 OMR Bubbles
                                        listOf("A", "B", "C", "D").forEach { opt ->
                                            val isDarkened = selectedOpt == opt

                                            Box(
                                                modifier = Modifier
                                                    .padding(horizontal = 6.dp)
                                                    .size(30.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        if (isDarkened) Color(0xFF1E3A8A) // Dark royal blue ballpoint pen ink
                                                        else Color.White
                                                    )
                                                    .border(
                                                        width = 1.5.dp,
                                                        color = if (isDarkened) Color(0xFF1E3A8A) else Color(0xFF475569),
                                                        shape = CircleShape
                                                    )
                                                    .clickable {
                                                        examViewModel.selectAnswer(idx, opt)
                                                        examViewModel.goToQuestion(idx)
                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = opt,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = if (isDarkened) Color.White else Color(0xFF475569)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.weight(1f))

                                        if (selectedOpt != null) {
                                            Text(
                                                text = "Marked",
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = SuccessGreen
                                            )
                                        } else {
                                            Text(
                                                text = "Empty",
                                                fontSize = 10.sp,
                                                color = Color(0xFF94A3B8)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Button to flip back to question paper
                        Button(
                            onClick = { showOmrSheet = false },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Flip, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Flip Back to Question Booklet (3D)", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Question Palette Bottom Sheet
        if (showPaletteSheet) {
            ModalBottomSheet(
                onDismissRequest = { showPaletteSheet = false },
                sheetState = rememberModalBottomSheetState()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Question Palette (${questions.size} Questions)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        LegendItem(color = MaterialTheme.colorScheme.primary, label = "Answered")
                        LegendItem(color = GoldPyq, label = "Review")
                        LegendItem(color = MaterialTheme.colorScheme.surfaceVariant, label = "Unattempted")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        questions.indices.forEach { idx ->
                            val isAnswered = studentAnswers.containsKey(idx)
                            val isForReview = markedForReview.contains(idx)
                            val isCurrent = currentIndex == idx

                            val bgColor = when {
                                isForReview -> GoldPyq
                                isAnswered -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            }

                            val textColor = if (isAnswered || isForReview) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bgColor)
                                    .border(
                                        width = if (isCurrent) 2.5.dp else 0.dp,
                                        color = if (isCurrent) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        examViewModel.goToQuestion(idx)
                                        showPaletteSheet = false
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = textColor
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Submit Confirmation Dialog
        if (showSubmitConfirmDialog) {
            val answeredCount = studentAnswers.size
            val unattemptedCount = questions.size - answeredCount
            val reviewCount = markedForReview.size

            AlertDialog(
                onDismissRequest = { showSubmitConfirmDialog = false },
                title = { Text("Submit Examination Paper?") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Are you sure you want to finish and submit your AHSEC Class 12 answers for official evaluation?")
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("• Attempted: $answeredCount")
                        Text("• Unattempted: $unattemptedCount")
                        Text("• Marked for Review: $reviewCount")
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showSubmitConfirmDialog = false
                            examViewModel.submitExam(currentUser?.email ?: "student")
                        },
                        modifier = Modifier.testTag("confirm_submit_button")
                    ) {
                        Text("Confirm & Submit")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSubmitConfirmDialog = false }) {
                        Text("Continue Test")
                    }
                }
            )
        }

        // Exit Confirm Dialog
        if (showExitConfirmDialog) {
            AlertDialog(
                onDismissRequest = { showExitConfirmDialog = false },
                title = { Text("Leave Examination?") },
                text = { Text("Your ongoing exam timer will stop and progress for this session will not be saved.") },
                confirmButton = {
                    Button(
                        onClick = {
                            showExitConfirmDialog = false
                            onExitExam()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                    ) {
                        Text("Exit Test")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitConfirmDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

// ----------------------------------------------------
// REALISTIC 3D OMR OPTION CARD WITH INK-DARKENING ANIMATION
// ----------------------------------------------------
@Composable
fun RealisticOmrOptionCard(
    letter: String,
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scaleAnim by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1.0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "option_scale"
    )

    val inkElevation by animateFloatAsState(
        targetValue = if (isSelected) 4f else 1f,
        label = "ink_elevation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                scaleX = scaleAnim
                scaleY = scaleAnim
            }
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .testTag("option_$letter"),
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f) else MaterialTheme.colorScheme.surface,
        shadowElevation = inkElevation.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Realistic OMR Bubble with blue ink
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Color(0xFF1E3A8A) // Dark blue pen ink
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (isSelected) Color(0xFF1E3A8A) else MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            if (isSelected) {
                Surface(
                    shape = CircleShape,
                    color = SuccessGreen,
                    modifier = Modifier.size(20.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selected",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, style = MaterialTheme.typography.labelSmall)
    }
}
