package com.example.roselofinalexam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

data class Question(val question: String, val options: List<String>, val answer: String)

@Composable
fun MyApp() {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var quizFinished by remember { mutableStateOf(false) }

    val questions = listOf(
        Question(
            "What is the most iconic Porsche model?",
            listOf("911", "Cayenne", "Taycan", "Panamera"),
            "911"
        ),
        Question(
            "In what year was Porsche founded?",
            listOf("1948", "1950", "1965", "1931"),
            "1931"
        ),
        Question(
            "Which Porsche model is an all-electric sports car?",
            listOf("911", "Cayenne", "Taycan", "Macan"),
            "Taycan"
        ),
        Question(
            "What animal is featured on the Porsche logo?",
            listOf("Horse", "Eagle", "Lion", "Bull"),
            "Horse"
        ),
        Question(
            "Which Porsche model is known for being a luxury SUV?",
            listOf("Panamera", "Macan", "Cayenne", "Taycan"),
            "Cayenne"
        ),
        Question(
            "What does the 'P' in Porsche stand for?",
            listOf("Performance", "Pioneer", "Pride", "Porsche"),
            "Porsche"
        ),
        Question(
            "Which Porsche model is often used in racing?",
            listOf("911 GT3", "Cayenne", "Macan", "Taycan"),
            "911 GT3"
        ),
        Question(
            "What is the top speed of a Porsche 911 Turbo S?",
            listOf("305 km/h", "250 km/h", "290 km/h", "320 km/h"),
            "305 km/h"
        ),
        Question(
            "Which Porsche model was the first to feature hybrid technology?",
            listOf("918 Spyder", "Cayenne", "Panamera", "Taycan"),
            "Panamera"
        ),
        Question(
            "In which country is Porsche headquartered?",
            listOf("Germany", "USA", "France", "Italy"),
            "Germany"
        )
    )

    var currentScreen by remember { mutableStateOf(Screen.Start) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Porsche Cars Quiz",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.porsche_logo),
                contentDescription = "Porsche Logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 8.dp)
                    .shadow(10.dp)
            )
            Crossfade(
                targetState = currentScreen,
                animationSpec = tween(durationMillis = 500)
            ) { screen ->
                when (screen) {
                    Screen.Start -> StartScreen(
                        onStartQuiz = {
                            currentScreen = Screen.Quiz
                        }
                    )
                    Screen.Quiz -> QuizScreen(
                        question = questions[currentQuestionIndex],
                        onOptionSelected = { selectedOption ->
                            if (selectedOption == questions[currentQuestionIndex].answer) {
                                score++
                            }
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                currentScreen = Screen.Result
                            }
                        }
                    )
                    Screen.Result -> ResultScreen(
                        score = score,
                        onRestartQuiz = {
                            currentQuestionIndex = 0
                            score = 0
                            currentScreen = Screen.Start
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun OptionButton(option: String, onOptionSelected: (String) -> Unit) {
    Button(
        onClick = { onOptionSelected(option) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(Color.Gray)
    ) {
        Text(text = option, color = Color.White, fontSize = 18.sp)
    }
}

@Composable
fun StartScreen(onStartQuiz: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onStartQuiz() },
            colors = ButtonDefaults.buttonColors(Color.Gray),
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Start Quiz", color = Color.White, fontSize = 20.sp)
        }
    }
}

@Composable
fun QuizScreen(question: Question, onOptionSelected: (String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = question.question,
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .background(Color.Gray)
                .padding(16.dp)
        )
        question.options.forEach { option ->
            OptionButton(option = option, onOptionSelected = { selectedOption ->
                onOptionSelected(selectedOption)
            })
        }
    }
}

@Composable
fun ResultScreen(score: Int, onRestartQuiz: () -> Unit) {
    val resultMessage = when (score) {
        in 0..2 -> "Nice try!"
        in 3..5 -> "Great job!"
        in 6..8 -> "Well done!"
        in 9..10 -> "Perfect!"
        else -> ""
    }

    val carImages = listOf(
        R.drawable.porschecar1,
        R.drawable.porschecar2,
        R.drawable.porschecar3,
        R.drawable.porschecar4,
    )
    val randomCarImage = carImages.random()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Quiz Finished!",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = resultMessage,
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Your final score is $score",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )
        Image(
            painter = painterResource(id = randomCarImage),
            contentDescription = "Porsche Car",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(vertical = 8.dp)
        )
        Button(
            onClick = onRestartQuiz,
            colors = ButtonDefaults.buttonColors(Color.Gray),
            modifier = Modifier
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Restart Quiz", color = Color.White, fontSize = 20.sp)
        }
    }
}

enum class Screen {
    Start, Quiz, Result
}

@Preview(showBackground = true)
@Composable
fun MyAppPreview() {
    MyApp()
}
