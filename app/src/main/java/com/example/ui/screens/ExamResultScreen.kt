package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.QuestionEntity
import com.example.ui.theme.ErrorRed
import com.example.ui.theme.ErrorRedContainer
import com.example.ui.theme.GoldPyq
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.SuccessGreenContainer
import com.example.ui.viewmodels.ExamViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamResultScreen(
    examViewModel: ExamViewModel,
    onDashboardClick: () -> Unit,
    onRetakeClick: () -> Unit
) {
    val lastAttempt by examViewModel.lastExamAttempt.collectAsState()
    val questions by examViewModel.examQuestions.collectAsState()
    val studentAnswers by examViewModel.studentAnswers.collectAsState()

    val attempt = lastAttempt

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AHSEC Examination Result", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onDashboardClick) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Dashboard")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        if (attempt == null) {
            Box(modifier = Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text("No exam session found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Score Summary Card (Authentic AHSEC 3D Evaluation Marksheet)
                item {
                    val division = when {
                        attempt.percentage >= 75f -> "DISTINCTION (STAR)"
                        attempt.percentage >= 60f -> "FIRST DIVISION"
                        attempt.percentage >= 45f -> "SECOND DIVISION"
                        attempt.percentage >= 30f -> "THIRD DIVISION"
                        else -> "NEEDS REVISION"
                    }
                    val stampColor = if (attempt.percentage >= 60f) SuccessGreen else if (attempt.percentage >= 30f) GoldPyq else ErrorRed

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(6.dp, RoundedCornerShape(18.dp)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "ASSAM HIGHER SECONDARY EDUCATION COUNCIL",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "CLASS 12 SCIENCE · EVALUATION REPORT",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Score Circle
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                                modifier = Modifier.size(96.dp),
                                border = androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${attempt.score}",
                                            style = MaterialTheme.typography.headlineMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Text(
                                            text = "out of ${attempt.totalQuestions}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Official Grade Stamp
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = stampColor.copy(alpha = 0.12f),
                                border = androidx.compose.foundation.BorderStroke(1.5.dp, stampColor)
                            ) {
                                Text(
                                    text = "★ $division · ${String.format(Locale.getDefault(), "%.1f%%", attempt.percentage)}",
                                    color = stampColor,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // Stats breakdown chips
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ResultMetricPill(
                                    label = "Correct",
                                    count = attempt.correctCount,
                                    color = SuccessGreen,
                                    bgColor = SuccessGreenContainer
                                )
                                ResultMetricPill(
                                    label = "Incorrect",
                                    count = attempt.incorrectCount,
                                    color = ErrorRed,
                                    bgColor = ErrorRedContainer
                                )
                                ResultMetricPill(
                                    label = "Unattempted",
                                    count = attempt.unattemptedCount,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    bgColor = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                }

                // Action Buttons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = onDashboardClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Dashboard")
                        }
                        Button(
                            onClick = onRetakeClick,
                            modifier = Modifier
                                .weight(1f)
                                .testTag("retake_exam_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Retake Test")
                        }
                    }
                }

                // Answer Review Header
                item {
                    Text(
                        text = "Detailed Answer Review (${questions.size} Questions)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                // Question Review Cards
                itemsIndexed(questions) { index, question ->
                    val studentChoice = studentAnswers[index]
                    val isCorrect = studentChoice?.equals(question.correctOption, ignoreCase = true) == true
                    val isUnattempted = studentChoice == null

                    QuestionReviewCard(
                        index = index + 1,
                        question = question,
                        studentChoice = studentChoice,
                        isCorrect = isCorrect,
                        isUnattempted = isUnattempted
                    )
                }
            }
        }
    }
}

@Composable
fun ResultMetricPill(label: String, count: Int, color: Color, bgColor: Color) {
    Surface(
        color = bgColor,
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$count",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = color
            )
        }
    }
}

@Composable
fun QuestionReviewCard(
    index: Int,
    question: QuestionEntity,
    studentChoice: String?,
    isCorrect: Boolean,
    isUnattempted: Boolean
) {
    val borderColor = when {
        isCorrect -> SuccessGreen.copy(alpha = 0.5f)
        isUnattempted -> MaterialTheme.colorScheme.outlineVariant
        else -> ErrorRed.copy(alpha = 0.5f)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Q$index · ${question.chapterTitle}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = when {
                        isCorrect -> SuccessGreenContainer
                        isUnattempted -> MaterialTheme.colorScheme.surfaceVariant
                        else -> ErrorRedContainer
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = when {
                                isCorrect -> Icons.Default.Check
                                isUnattempted -> Icons.Default.HelpOutline
                                else -> Icons.Default.Close
                            },
                            contentDescription = null,
                            tint = when {
                                isCorrect -> SuccessGreen
                                isUnattempted -> MaterialTheme.colorScheme.onSurfaceVariant
                                else -> ErrorRed
                            },
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = when {
                                isCorrect -> "Correct"
                                isUnattempted -> "Unanswered"
                                else -> "Incorrect"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isCorrect -> SuccessGreen
                                isUnattempted -> MaterialTheme.colorScheme.onSurfaceVariant
                                else -> ErrorRed
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = question.questionText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Options summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Your Answer: ${studentChoice ?: "None"}",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isCorrect) SuccessGreen else if (isUnattempted) MaterialTheme.colorScheme.onSurfaceVariant else ErrorRed,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Correct Answer: Option ${question.correctOption}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Explanation
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Explanation:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = question.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
