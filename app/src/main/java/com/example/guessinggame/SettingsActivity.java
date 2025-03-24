package com.example.guessinggame;

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

        btnClearSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Clear all fields
                EditText editNumQuestions = findViewById(R.id.num_questions);
                Spinner spinnerThemes = findViewById(R.id.spinner_themes);

                editNumQuestions.setText("");
                spinnerThemes.setSelection(0);
            }
        });
    }

}
