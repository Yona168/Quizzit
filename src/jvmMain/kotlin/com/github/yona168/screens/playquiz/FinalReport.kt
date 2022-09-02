package com.github.yona168.screens.playquiz

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.yona168.BoldText
import com.github.yona168.Centered
import com.github.yona168.SmallSpacer
import com.github.yona168.questions.ManyChoice
import com.github.yona168.questions.OptionsQuestion
import com.github.yona168.questions.Quiz
import com.github.yona168.questions.ShortAnswer

@Composable
fun FinalReport(quiz: Quiz, answers: List<Any?>, correctShortAnswerIndices: Set<Int>) {
    val correctIndices = quiz.questions.indices.filter { i ->
        when (val question = quiz.questions[i]) {
            is ShortAnswer -> correctShortAnswerIndices.contains(i)
            else -> question.answer == answers[i]
        }
    }
    val amountCorrect = correctIndices.size
    Centered {
        LazyColumn {
            item {
                    BoldText(
                        "You answered $amountCorrect out of ${quiz.questions.size} correctly (${(amountCorrect.toDouble() / quiz.questions.size.toDouble()) * 100}%)\n" +
                                "Incorrect answers are shown below",
                        fontSize = 25.sp
                    )
            }
            for (i in quiz.questions.indices) {
                if (i in correctIndices) {
                    continue
                }
                val question = quiz.questions[i]
                item {
                    SmallSpacer()
                    Card(modifier=Modifier.fillMaxWidth()){
                        Column {
                            BoldText("Question $i: ${question.question}", fontSize = 20.sp)
                            when (question) {
                                is ShortAnswer -> {
                                    Row {
                                        BoldText(
                                            "Your answer: "
                                        ); Text(answers[i] as String)
                                    }
                                    SmallSpacer()
                                    Row { BoldText("Correct answer: "); Text(question.answer) }
                                }

                                is OptionsQuestion -> {
                                    Row {
                                        Column {
                                            BoldText("Your Answer:")
                                            SmallSpacer()
                                            ReportOptionsCard(
                                                question = question,
                                                show = if (question is ManyChoice) answers[i] as Set<Int> else setOf(
                                                    answers[i] as Int
                                                )
                                            )
                                        }
                                        SmallSpacer()
                                        Column {
                                            BoldText("Correct Answer:")
                                            SmallSpacer()
                                            ReportOptionsCard(
                                                question = question,
                                                show = if (question is ManyChoice) question.answer else setOf(
                                                    question.answer as Int
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
@Composable
fun ReportOptionsCard(question: OptionsQuestion, show: Set<Int>) {
    for (i in question.options.indices) {
        Row {
            RadioButton(selected = show.contains(i), onClick = {})
            Text("\n${question.options[i]}")
        }
    }
}