package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class MathQuestion2 extends Activity {

    int numberCorrect;
    final String correctAnswer = "RadioButton3";
    String userAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_math_question1);

        Intent intent = getIntent();
        numberCorrect = intent.getIntExtra("numberCorrect", 0);

        Button btnSubmit = (Button) findViewById(R.id.btnSumbit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(correctAnswer.equals(userAnswer)){
                    numberCorrect++;
                }
                Intent intent = new Intent(MathQuestion2.this, MathAnswer2.class);
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
