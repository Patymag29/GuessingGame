package com.example.guessinggame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {

    Questions[] questions = new Questions[20];
    int questionIndex = 0;
    Button op1, op2, op3, op4, settings;
    TextView questionTitle, scoreCounter;
    ImageView imgFlag;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        questionTitle = findViewById(R.id.question_Title);
        op1 = findViewById(R.id.btn_option1);
        op2 = findViewById(R.id.btn_option2);
        op3 = findViewById(R.id.btn_option3);
        op4 = findViewById(R.id.btn_option4);
        settings = findViewById(R.id.btn_Settings);
        imgFlag = findViewById(R.id.img_flag);
        scoreCounter = findViewById(R.id.tv_score2);

        loadQuestionsFromJSON();
        Collections.shuffle(Arrays.asList(questions)); // 🔀 Randomiza as perguntas
        loadQuestion();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadQuestion();
        updateScore();
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        score = savedInstanceState.getInt("SCORE");
        questionIndex = savedInstanceState.getInt("QUESTION");
        Parcelable[] parcelableArray = savedInstanceState.getParcelableArray("QUESTIONS");

        if (parcelableArray != null) {
            for (int i = 0; i < parcelableArray.length; i++) {
                questions[i] = (Questions) parcelableArray[i];
            }
        }
        loadQuestion();
        updateScore();
    }

    public void nextQuestion(View view) {
        if (questionIndex == 19) {
            Toast.makeText(this, "It's the final question", Toast.LENGTH_SHORT).show();
        } else {
            questionIndex++;
            loadQuestion();
            if (questions[questionIndex].answered) {
                Toast.makeText(this, "Question already answered!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void previousQuestion(View view) {
        if (questionIndex == 0) {
            Toast.makeText(this, "You're at the first question", Toast.LENGTH_SHORT).show();
        } else {
            questionIndex--;
            loadQuestion();
            if (questions[questionIndex].answered) {
                Toast.makeText(this, "Question already answered!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadQuestionsFromJSON() {
        try {
            InputStream is = getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            questions = new Questions[jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String imageName = obj.getString("image");
                int imageResId = getResources().getIdentifier(imageName, "drawable", getPackageName());

                JSONArray options = obj.getJSONArray("options");
                questions[i] = new Questions(
                        imageResId,
                        obj.getString("question"),
                        options.getString(0),
                        options.getString(1),
                        options.getString(2),
                        options.getString(3),
                        obj.getString("correctAnswer")
                );
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error on load questions: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadQuestion() {
        Questions current = questions[questionIndex];
        questionTitle.setText(current.questionAsk);
        imgFlag.setImageResource(current.imageResId);
        op1.setText(current.option1);
        op2.setText(current.option2);
        op3.setText(current.option3);
        op4.setText(current.option4);

        // Resetar a cor dos botões
        op1.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        op2.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        op3.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
        op4.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void updateScore() {
        scoreCounter.setText(Integer.toString(score));
    }

    public void pickAnswer(View view) {
        Button button = (Button) view;

        if (questions[questionIndex].answered) {
            Toast.makeText(this, "Question already answered !", Toast.LENGTH_SHORT).show();
            return;
        }

        String selectedAnswer = button.getText().toString();
        String correctAnswer = questions[questionIndex].correctAnswer;

        if (selectedAnswer.equals(correctAnswer)) {
            score++;
            button.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            button.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            Toast.makeText(this, "Incorrect, try again!", Toast.LENGTH_SHORT).show();
        }

        questions[questionIndex].answered = true;
        updateScore();
    }

    public void resetGame(View view) {
        score = 0;
        questionIndex = 0;

        for (Questions question : questions) {
            question.answered = false;
        }

        Collections.shuffle(Arrays.asList(questions)); // embaralhar novamente
        loadQuestion();
        updateScore();
    }

    public void openSettingsActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}
