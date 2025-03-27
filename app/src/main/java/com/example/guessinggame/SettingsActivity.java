package com.example.guessinggame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnClearSettings = findViewById(R.id.btn_clearSettings);
        btnClearSettings.setOnClickListener(v -> clearPreferences()); //this on click listener is called to clear the game preference from the settings view when the button is clicked

        Button btnResetGame = findViewById(R.id.btn_reset_game);
        btnResetGame.setOnClickListener(v -> resetGame()); //this on click listener is called to reset the game from the settings view when the button is clicked

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(v -> saveSettings()); //this on click listener is called to save the game preferences from the settings view when the button is clicked
    }

    private void clearPreferences() { // This method will clear all fields when the button is clicked

        EditText editNumQuestions = findViewById(R.id.num_questions); //finds the input from the user to set how many questions will be answered
        Spinner spinnerThemes = findViewById(R.id.spinner_themes); //sets the theme

        editNumQuestions.setText(""); //sets the text to empty string
        spinnerThemes.setSelection(0); //sets the theme to the base
        Toast.makeText(this, "User preference was clear", Toast.LENGTH_SHORT).show(); //shows to the user a message
        restartMainActivity(); //call the restart activity method to go back to the game view
    }

    private void resetGame() {
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE); // the SharedPreferences is used to reset any stored data if necessary
        SharedPreferences.Editor editor = preferences.edit(); //ensures the preference values remain in a consistent state and control when they are committed to storage.
        editor.clear(); // Clear saved settings
        editor.apply(); // if any change is set for the user

        Toast.makeText(this, "Game has been reset!", Toast.LENGTH_SHORT).show(); //show a message to the user

        restartMainActivity(); // Restart MainActivity from scratch after reset has been clicked
    }

    private void saveSettings() {
        EditText editNumQuestions = findViewById(R.id.num_questions); //uses the value inputted by the user
        Spinner spinnerThemes = findViewById(R.id.spinner_themes); //uses the theme set by the user

        String numQuestionsStr = editNumQuestions.getText().toString().trim(); //only gets a digit
        int numQuestions = numQuestionsStr.isEmpty() ? 20 : Integer.parseInt(numQuestionsStr); // if the input is empty the default value will be 20
        String selectedTheme = spinnerThemes.getSelectedItem().toString(); // for example dark or light

        // Save settings to SharedPreferences
        SharedPreferences preferences = getSharedPreferences("GameSettings", MODE_PRIVATE); //retrieve and hold the contents of the preferences file 'name', returning a SharedPreferences through which you can retrieve and modify its values.
        SharedPreferences.Editor editor = preferences.edit(); //ensures the preference values remain in a consistent state and control when they are committed to storage.
        editor.putInt("numQuestions", numQuestions); //Set an int value in the preferences editor, to be written back once commit or apply are called.
        editor.putString("theme", selectedTheme); //Set a String value in the preferences editor, to be written back once commit or apply are called.
        editor.apply();

        Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show(); //show a message to the user

        restartMainActivity(); // Restart MainActivity from scratch after reset has been clicked
    }

    // Restart MainActivity after updating settings
    private void restartMainActivity() {
        Intent intent = new Intent(this, MainActivity.class); //It is basically a passive data structure holding an abstract description of an action to be performed.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); //it will resume the intent or start from the begin
        startActivity(intent);
        finish(); // Finish current SettingsActivity
    }
}



