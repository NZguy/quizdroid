package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MarvelQuestion1 extends Activity {

    final String correctAnswer = "RadioButton3";
    String userAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_question1);

        Button btnSubmit = (Button) findViewById(R.id.btnSumbit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numberCorrect = 0;
                if(correctAnswer.equals(userAnswer)){
                    numberCorrect++;
                }
                Intent intent = new Intent(MarvelQuestion1.this, MarvelAnswer1.class);
                intent.putExtra("numberCorrect", numberCorrect);
                intent.putExtra("userAnswer", userAnswer);
                intent.putExtra("correctAnswer", correctAnswer);
                startActivity(intent);
            }
        });
    }

    public void onRadioClick(View view) {
        // Is the button now checked?
        userAnswer = ((RadioButton) view).getText().toString();
        Log.i("MathQuestion1", userAnswer);
        Button btnSubmit = (Button) findViewById(R.id.btnSumbit);
        btnSubmit.setVisibility(View.VISIBLE);
    }
}
