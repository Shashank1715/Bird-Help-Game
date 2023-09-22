package com.example.birdgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity {
    private TextView textViewResultInfo, textViewMyScore, textViewHighestScore;
    private Button buttonAgain;
    private int score;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        textViewResultInfo= findViewById(R.id.textViewResultInfo);
        textViewMyScore= findViewById(R.id.textViewMyScore);
        textViewHighestScore= findViewById(R.id.textViewHighestScore);
        buttonAgain= findViewById(R.id.buttonAgain);
        score= getIntent().getIntExtra("score",0);
        textViewMyScore.setText(""+score);

        sharedPreferences= this.getSharedPreferences("Score", Context.MODE_PRIVATE);
        int highestScore= sharedPreferences.getInt("highestScore",0);
        if(score>= 200)
        {
            textViewResultInfo.setText("You crack the highest score");
            textViewHighestScore.setText("Highest Score: "+score);
            sharedPreferences.edit().putInt("highestScore",score).apply();
        }
        else if(score>= highestScore)
        {
            textViewResultInfo.setText("Sorry you lost");
            textViewHighestScore.setText("Highest Score: "+score);
            sharedPreferences.edit().putInt("highestScore",score).apply();
        }
        else {
            textViewResultInfo.setText("Sorry you lost");
            textViewHighestScore.setText("Highest Score: "+score);
        }
        buttonAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    public  void onBackPressed(){
        Intent intent= new Intent(ResultActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}