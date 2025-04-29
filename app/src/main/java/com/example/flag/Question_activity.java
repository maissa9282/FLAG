package com.example.flag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Question_activity extends AppCompatActivity implements View.OnClickListener  {

    private TextView textViewTimer;
    private TextView textViewQuestionNumber;
    private ImageView imageViewQuestion;
    private Button buttonAnswer1, buttonAnswer2, buttonAnswer3;

    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;
    private long timeLeftInMillis;
    private CountDownTimer countDownTimer;
    private static final long COUNTDOWN_IN_MILLIS = 30000; // 30 seconds per question

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);

        textViewTimer = findViewById(R.id.textViewTimer);
        textViewQuestionNumber = findViewById(R.id.textViewQuestionNumber);
        imageViewQuestion = findViewById(R.id.imageViewQuestion);
        buttonAnswer1 = findViewById(R.id.buttonAnswer1);
        buttonAnswer2 = findViewById(R.id.buttonAnswer2);
        buttonAnswer3 = findViewById(R.id.buttonAnswer3);

        buttonAnswer1.setOnClickListener(this);
        buttonAnswer2.setOnClickListener(this);
        buttonAnswer3.setOnClickListener(this);

        loadQuestions();
        showNextQuestion();
    }

    private void loadQuestions() {
        // --- IMPORTANT: Add your questions here! ---
        // Replace 'R.drawable.image_name' with your actual image resource IDs in the res/drawable folder.
        // Example:
        questionList.add(new Question(R.drawable.spain, new String[]{"spain", "Austrelia", "japon"}, 0)); // Assuming Option A1 is correct
        questionList.add(new Question(R.drawable.suisse, new String[]{"Maroc", "suisse", "Tunisie"}, 1)); // Assuming Option B2 is correct
        questionList.add(new Question(R.drawable.taiwan, new String[]{"China", "Tutkie", "Taiwan"}, 2)); // Assuming Option C3 is correct
        // Add more questions as needed...

        // You might want to shuffle the questions for variety
        // Collections.shuffle(questionList);

        if (questionList.isEmpty()) {
            // Handle case with no questions loaded
            Toast.makeText(this, "Error: No questions loaded! Add questions in MainActivity.java", Toast.LENGTH_LONG).show();
            // Optionally finish the activity or show an error message
            finish();
        }
    }

    private void showNextQuestion() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (currentQuestionIndex < questionList.size()) {
            Question currentQuestion = questionList.get(currentQuestionIndex);

            imageViewQuestion.setImageResource(currentQuestion.getImageResId());
            buttonAnswer1.setText(currentQuestion.getOption1());
            buttonAnswer2.setText(currentQuestion.getOption2());
            buttonAnswer3.setText(currentQuestion.getOption3());

            textViewQuestionNumber.setText("Question: " + (currentQuestionIndex + 1) + "/" + questionList.size());

            timeLeftInMillis = COUNTDOWN_IN_MILLIS;
            startCountDown();

            currentQuestionIndex++;
        } else {
            finishQuiz();
        }
    }

    private void startCountDown() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                updateCountDownText();
                checkAnswer(-1); // Indicate time ran out
            }
        }.start();
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000);
        String timeFormatted = String.format(Locale.getDefault(), "Time: %02ds", seconds);
        textViewTimer.setText(timeFormatted);
    }

    @Override
    public void onClick(View v) {
        int selectedOptionIndex = -1;
        int id = v.getId();
        if (id == R.id.buttonAnswer1) {
            selectedOptionIndex = 0;
        } else if (id == R.id.buttonAnswer2) {
            selectedOptionIndex = 1;
        } else if (id == R.id.buttonAnswer3) {
            selectedOptionIndex = 2;
        }

        checkAnswer(selectedOptionIndex);
    }

    private void checkAnswer(int selectedOptionIndex) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Adjust index because currentQuestionIndex was already incremented in showNextQuestion
        Question question = questionList.get(currentQuestionIndex - 1);

        if (selectedOptionIndex == question.getCorrectAnswerIndex()) {
            score++;
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else if (selectedOptionIndex == -1) {
            Toast.makeText(this, "Time's up!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Wrong! Correct was: " + question.getOptions()[question.getCorrectAnswerIndex()], Toast.LENGTH_SHORT).show();
        }

        // Load the next question or finish
        showNextQuestion();
    }

    private void finishQuiz() {
        // Display final score or navigate to a results screen
        String resultMessage = "Quiz Finished! Your score: " + score + "/" + questionList.size();
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG).show();

        // Example: Disable buttons and show a final message
        buttonAnswer1.setEnabled(false);
        buttonAnswer2.setEnabled(false);
        buttonAnswer3.setEnabled(false);
        textViewQuestionNumber.setText("Quiz Complete!");
        // You could start a new Activity here to show detailed results
        // Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        // intent.putExtra("SCORE", score);
        // intent.putExtra("TOTAL_QUESTIONS", questionList.size());
        // startActivity(intent);
        // finish(); // Optional: close the quiz activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ensure the timer is cancelled when the activity is destroyed
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
