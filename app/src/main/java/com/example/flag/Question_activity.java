package com.example.flag;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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
import java.util.Random;

public class Question_activity extends AppCompatActivity implements View.OnClickListener {

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
    private MediaPlayer timerMediaPlayer;
    private Button button_Skip;

    private Button buttonHint;

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
        button_Skip = findViewById(R.id.button_Skip);
        buttonHint=findViewById(R.id.buttonHint);

        button_Skip.setOnClickListener(this);
        buttonAnswer1.setOnClickListener(this);
        buttonAnswer2.setOnClickListener(this);
        buttonAnswer3.setOnClickListener(this);
        buttonHint.setOnClickListener(this);


        loadQuestions();
        showNextQuestion();
    }

    private void loadQuestions() {
        // --- IMPORTANT: Add your questions here! ---
        // Replace 'R.drawable.image_name' with your actual image resource IDs in the res/drawable folder.
        // Example:
        questionList.add(new Question(R.drawable.spain, new String[]{"Spain", "Austrelia", "Japon"}, 0)); // Assuming Option A1 is correct
        questionList.add(new Question(R.drawable.suisse, new String[]{"Maroc", "Suisse", "Tunisie"}, 1)); // Assuming Option B2 is correct
        questionList.add(new Question(R.drawable.taiwan, new String[]{"China", "Tutkie", "Taiwan"}, 2)); // Assuming Option C3 is correct
        questionList.add(new Question(R.drawable.algerie, new String[]{"Algeria", "Yamen", "Libie"}, 0)); // Assuming Option C3 is correct
        questionList.add(new Question(R.drawable.china, new String[]{"Japon", "China", "Brazil"}, 1)); // Assuming Option C3 is correct
        questionList.add(new Question(R.drawable.korea, new String[]{"Canada", "Tutkie", "Korea"}, 2)); // Assuming Option C3 is correct
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

        // --- أضف الكود هنا ---
        buttonAnswer1.setEnabled(true);
        buttonAnswer2.setEnabled(true);
        buttonAnswer3.setEnabled(true);


        // إعادة اللون الافتراضي (استخدم اللون المناسب لتصميمك)
        buttonAnswer1.setBackgroundColor(Color.parseColor("#FF9E80")); // اللون الذي استخدمته
        buttonAnswer2.setBackgroundColor(Color.parseColor("#FF9E80"));
        buttonAnswer3.setBackgroundColor(Color.parseColor("#FF9E80"));
        button_Skip.setEnabled(true);
        buttonHint.setEnabled(true);
        buttonHint.setAlpha(1.0f); // إعادة الشفافية إذا كنت تغيرها عند استخدام التلميح
        buttonAnswer1.setAlpha(1.0f); // إعادة شفافية أزرار الإجابات إذا كنت تغيرها
        buttonAnswer2.setAlpha(1.0f);
        buttonAnswer3.setAlpha(1.0f);

        boolean hintUsed = false; // <-- أضف السطر هنا لإعادة السماح باستخدام التلميح


        button_Skip.setEnabled(true);

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
        startTimerSound();
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
        int id = v.getId(); // الحصول على معرّف الزر الذي تم الضغط عليه

        if (id == R.id.buttonAnswer1) {
            // إذا كان زر الإجابة الأول، تحقق من الإجابة بالفهرس 0
            checkAnswer(0);
        } else if (id == R.id.buttonAnswer2) {
            // إذا كان زر الإجابة الثاني، تحقق من الإجابة بالفهرس 1
            checkAnswer(1);
        } else if (id == R.id.buttonAnswer3) {
            // إذا كان زر الإجابة الثالث، تحقق من الإجابة بالفهرس 2
            checkAnswer(2);
        } else if (id == R.id.button_Skip) {
            // إذا كان زر التخطي، استدعِ دالة التخطي
            skipQuestion();
        } else if (id == R.id.button_Skip) {
            // إذا كان زر التلميح، استدعِ دالة التلميح
            useHint();
        }
        // لا يوجد استدعاء لـ checkAnswer هنا في النهاية بعد الآن
    }


    private void checkAnswer(int selectedOptionIndex) {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
          stopTimerSound();
        // تعطيل الأزرار لمنع النقر مرة أخرى
        buttonAnswer1.setEnabled(false);
        buttonAnswer2.setEnabled(false);
        buttonAnswer3.setEnabled(false);

        // الحصول على السؤال الحالي (تذكر أن currentQuestionIndex تمت زيادته بالفعل)
        Question question = questionList.get(currentQuestionIndex - 1);
        int correctOptionIndex = question.getCorrectAnswerIndex();

        // تحديد الزر الصحيح وتلوينه بالأخضر
        Button correctButton;
        if (correctOptionIndex == 0) {
            correctButton = buttonAnswer1;
        } else if (correctOptionIndex == 1) {
            correctButton = buttonAnswer2;
        } else { // correctOptionIndex == 2
            correctButton = buttonAnswer3;
        }
        correctButton.setBackgroundColor(Color.GREEN);

        // التحقق من إجابة المستخدم
        if (selectedOptionIndex == correctOptionIndex) {
            // إجابة صحيحة
            score++;
            Toast.makeText(this, "Correct!", LENGTH_SHORT).show();
            playSound(R.raw.correct); // <-- تشغيل صوت الإجابة الصحيحة
        } else if (selectedOptionIndex == -1) {
            // الوقت انتهى (لا نلون أي زر بالأحمر)
            Toast.makeText(this, "Time's up!", LENGTH_SHORT).show();
            playSound(R.raw.timer); // <-- تشغيل صوت انتهاء الوقت
        } else {
            // إجابة خاطئة
            Toast.makeText(this, "Wrong!", LENGTH_SHORT).show();

            // تحديد الزر الذي اختاره المستخدم وتلوينه بالأحمر
            Button selectedButton;
            if (selectedOptionIndex == 0) {
                selectedButton = buttonAnswer1;
            } else if (selectedOptionIndex == 1) {
                selectedButton = buttonAnswer2;
            } else { // selectedOptionIndex == 2
                selectedButton = buttonAnswer3;
            }
            selectedButton.setBackgroundColor(Color.RED);
            playSound(R.raw.wrong); // <-- تشغيل صوت الإجابة الخاطئة
        }

        // تأخير بسيط ثم الانتقال للسؤال التالي
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showNextQuestion();
            }
        }, 1500); // تأخير لمدة 1.5 ثانية (1500 ميلي ثانية)
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
        stopTimerSound();
        // Ensure the timer is cancelled when the activity is destroyed
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }


    private void playSound(int soundResourceId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(this, soundResourceId);
        if (mediaPlayer != null) {
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release(); // تحرير الموارد بعد انتهاء التشغيل
                }
            });
            mediaPlayer.start();
        } else {
            // يمكنك إضافة رسالة خطأ هنا إذا لم يتم العثور على الملف الصوتي
            Toast.makeText(this, "Error loading sound", LENGTH_SHORT).show();
        }
    }

    private void startTimerSound() {
        stopTimerSound(); // أوقف أي صوت مؤقت سابق قد يكون قيد التشغيل
        timerMediaPlayer = MediaPlayer.create(this, R.raw.timer); // استخدم اسم ملف صوت المؤقت الخاص بك
        if (timerMediaPlayer != null) {
            timerMediaPlayer.setLooping(true); // اجعل الصوت يتكرر
            timerMediaPlayer.start();
        } else {
            Toast.makeText(this, "Error loading timer sound", LENGTH_SHORT).show();
        }
    }
    private void stopTimerSound() {
        if (timerMediaPlayer != null) {
            if (timerMediaPlayer.isPlaying()) {
                timerMediaPlayer.stop();
            }
            timerMediaPlayer.release(); // تحرير الموارد
            timerMediaPlayer = null;
        }
    }

    // --- ADDED --- دالة لمعالجة تخطي السؤال
    private void skipQuestion() {
        // 1. إيقاف المؤقت الحالي إذا كان يعمل
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        // 2. إيقاف صوت المؤقت إذا كان يعمل
        stopTimerSound();

        // 3. تعطيل جميع الأزرار التفاعلية فوراً
        disableButtons();

        // 4. عرض رسالة للمستخدم تفيد بأنه تم تخطي السؤال
        Toast.makeText(this, "Question Skipped", LENGTH_SHORT).show();

        // 5. الانتظار لفترة قصيرة ثم عرض السؤال التالي
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showNextQuestion(); // استدعاء دالة عرض السؤال التالي
            }
        }, 1500); // تأخير لمدة 1.5 ثانية (نفس التأخير المستخدم بعد الإجابة)
    }

    // --- ADDED --- دالة مساعدة لتعطيل جميع الأزرار التفاعلية
    private void disableButtons() {
        buttonAnswer1.setEnabled(false);
        buttonAnswer2.setEnabled(false);
        buttonAnswer3.setEnabled(false);
        button_Skip.setEnabled(false); // تعطيل زر التخطي أيضاً
        // buttonHint.setEnabled(false); // قم بتضمين هذا السطر فقط إذا كان لديك زر تلميح
    }

    // --- تأكد من إضافة هذه الدالة بالكامل --- 
    private void useHint() {
        Toast.makeText(this, "Hint Button Clicked!", Toast.LENGTH_SHORT).show(); // <-- أضف هذا السطر هنا

        boolean hintUsed = false;
        if (hintUsed || !buttonHint.isEnabled()) {
            if (hintUsed) {
                Toast.makeText(this, "Hint already used!", LENGTH_SHORT).show();
            }
            return;
        }

        hintUsed = true;
        buttonHint.setEnabled(false);
        buttonHint.setAlpha(0.5f); // Visually indicate used hint

        Question currentQuestion = questionList.get(currentQuestionIndex - 1);
        int correctOptionIndex = currentQuestion.getCorrectAnswerIndex();

        Random random = new Random();
        int optionToDisable;
        do {
            optionToDisable = random.nextInt(3);
        } while (optionToDisable == correctOptionIndex);

        Button buttonToDisable = null;
        if (optionToDisable == 0) {
            buttonToDisable = buttonAnswer1;
        } else if (optionToDisable == 1) {
            buttonToDisable = buttonAnswer2;
        } else { // optionToDisable == 2
            buttonToDisable = buttonAnswer3;
        }

        if (buttonToDisable != null) {
            buttonToDisable.setEnabled(false);
            buttonToDisable.setAlpha(0.5f); // Make it look disabled
            Toast.makeText(this, "One wrong option removed!", LENGTH_SHORT).show();
        }
    }
    
    
}