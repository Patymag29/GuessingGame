package com.example.guessinggame;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private int numQuestions; // holds the amount of questions to be answered
    Questions[] questions; // array of Json data (must be assigned with value from settings)
    int questionIndex = 0;
    Button op1, op2, op3, op4, settings;
    private int totalQuestions; //hold the total number of question
    TextView questionTitle, scoreCounter, tvQuestionProgress; //sets the text on the text view dynamically
    ImageView imgFlag; // assignment 2
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //make references of components
        questionTitle = findViewById(R.id.question_Title);
        op1 = findViewById(R.id.btn_option1);
        op2 = findViewById(R.id.btn_option2);
        op3 = findViewById(R.id.btn_option3);
        op4 = findViewById(R.id.btn_option4);
        settings = findViewById(R.id.btn_Settings); // assignment 2
        imgFlag  = findViewById(R.id.img_flag); // assignment 2
        scoreCounter    = findViewById(R.id.tv_score2);
        tvQuestionProgress = findViewById(R.id.tv_question_progress);

        // Load user preferences in onCreate()
        // retrieve and hold the contents of the preferences file 'name', returning a SharedPreferences through which you can retrieve and modify its values.
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);

        numQuestions = preferences.getInt("numQuestions", 20); // assign to the global variable

        questions = new Questions[numQuestions]; //initialize the questions array

        String selectedTheme = preferences.getString("theme", "Default"); // Default theme TODO

        totalQuestions = numQuestions; //sets the number of questions based on the preferences

        // assignment 2
        loadQuestionsFromJSON();
        // assignment 2
        loadQuestion();

    }

    // this method is embedded from Android by default and now is overriding
    protected void onStart(){ // on loading the screen for the first time
        super.onStart();
        loadQuestion(); // method to load questions of game
        updateScore();
    }

   @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        score = savedInstanceState.getInt("SCORE"); // restore saved score (from onSaveInstanceState())
        questionIndex = savedInstanceState.getInt("QUESTION"); // restore saved question (from onSaveInstanceState())

        //PARCELABLE - This line retrieves an array of objects that were previously saved as Parcelable in onSaveInstanceState().
        Parcelable[] parcelableArray = savedInstanceState.getParcelableArray("QUESTIONS");

        // read this array and fill the array Questions[] destroyed when the screen was rotated
        if (parcelableArray != null){
            for (int i = 0; i < parcelableArray.length; i++){
                questions[i] = (Questions) parcelableArray[i];
            }
        }
        loadQuestion(); // method to load questions of game
        updateScore();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) { //this method will save the instance properly
        super.onSaveInstanceState(outState);
        outState.putInt("SCORE", score);
        outState.putInt("QUESTION", questionIndex);
        outState.putParcelableArray("QUESTIONS", questions);
    }

    // This method goes to the next Question and accessed by onclick button event
    public void nextQuestion(View view) { // questionIndex == 20 (must be assigned with value from settings)
        if (questionIndex >= numQuestions - 1) { //makes sure the questions are not going outside the array limit
            Toast.makeText(this, "It's The final question", Toast.LENGTH_SHORT).show();
        } else {
            questionIndex++;
            loadQuestion();
            if (questions[questionIndex].answered) {
                Toast.makeText(this, "Question already answered!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // This method goes back to the previous Question and accessed by onclick button event
    public void previousQuestion(View view){ // method used onclick button event "previous Button"
        if (questionIndex == 0){
            Toast.makeText(this, "You're at the first question", Toast.LENGTH_SHORT).show(); // when user tries to click on button "Previous" and he is on the first question
        } else {
            questionIndex--;
            loadQuestion();
            // Verify if the question has already answered
            if (questions[questionIndex].answered) {
                Toast.makeText(this, "Question already answered!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //assignment 2 - This method loads all questions only once and just keeps information of flags on memory
    private void loadQuestionsFromJSON() {
        try {
            InputStream is = getAssets().open("questions.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            questions = new Questions[jsonArray.length()];

            for (int i = 0; i < numQuestions && i < jsonArray.length(); i++) {
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
            questions = new Questions[0]; // starts a questions array to the first index
        }
    }

    // This method loads questions and stores on buttons
    private void loadQuestion() {
        if (questions != null && questions[questionIndex] != null) { //checks if there is a question to be loaded
        Questions current = questions[questionIndex]; // Assignment 2 - Get the current question from the questions array using the current index
        questionTitle.setText(current.questionAsk);
        imgFlag.setImageResource(current.imageResId); // Assignment 2 - Set the flag image according to the current question
        op1.setText(questions[questionIndex].option1);
        op2.setText(questions[questionIndex].option2);
        op3.setText(questions[questionIndex].option3);
        op4.setText(questions[questionIndex].option4);

            updateQuestionProgress(); // update question progress here!
        } else {
            Toast.makeText(this, "Error: No question loaded!", Toast.LENGTH_SHORT).show();
        }
    }
    private void updateQuestionProgress() { //helper method to keeps track of the number of questions
        String progress = getString(R.string.question_progress, questionIndex + 1, totalQuestions); //this is used to not concatenate directly on the code by using string placeholders
        tvQuestionProgress.setText(progress);
    }

    private void updateScore(){
        scoreCounter.setText(String.valueOf(score));
    }
    public void pickAnswer(View view) { // // method used onclick button event - linked to answer's buttons
        Button button = (Button) view;

        // Verify if the question has already answered and impedes the user answer again and then update score wrongly
        if (questions[questionIndex].answered) {
            Toast.makeText(this, "Question already answered !", Toast.LENGTH_SHORT).show();
            return;
        }

        if (questions[questionIndex] != null && button.getText().toString().equals(questions[questionIndex].correctAnswer)) { // correct Answer ? To be used with Json strings
            if (!questions[questionIndex].answered) { // if questions is answered
                score++;
            }
            questions[questionIndex].answered = true;
            Toast.makeText(this, "Correct !", Toast.LENGTH_SHORT).show();

            loadQuestion();
            updateScore();
        }
        else {
            Toast.makeText(this, "Incorrect, try again !", Toast.LENGTH_SHORT).show();
        }
    }

    // This method resets the Game and is called by 2 ways (activity_main and activity_settings)
    public void resetGame(View view) {
        score = 0;
        questionIndex = 0;
        // retrieve and hold the contents of the preferences file 'name', returning a SharedPreferences through which you can retrieve and modify its values.
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE);
        numQuestions = preferences.getInt("numQuestions", 20); //resets the number of questions

        // Reset the state of questions
        if (questions != null) {
            for (int i = 0; i < numQuestions; i++) {
                if (questions[i] != null) {
                    questions[i].answered = false;
                }
            }
        }
        loadQuestion();
        updateScore();
        updateQuestionProgress();
    }

    //assignment 2 - This method calls the activity_settings
    public void openSettingsActivity(View view) {
        Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(intent);
    }


} // end of MainActivity.java


