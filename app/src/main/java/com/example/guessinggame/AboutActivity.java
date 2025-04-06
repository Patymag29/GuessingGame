package com.example.guessinggame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
    }
    public void openMainMenu(View view) {
        Intent intent = new Intent(AboutActivity.this, MainMenuActivity.class);
        startActivity(intent);
    }

}
