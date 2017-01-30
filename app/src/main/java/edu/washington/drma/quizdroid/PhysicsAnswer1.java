package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PhysicsAnswer1 extends Activity {

    int numberCorrect;
    final int questionNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physics_answer1);

        Intent intent = getIntent();
        numberCorrect = intent.getIntExtra("numberCorrect", 0);
        String userAnswer = intent.getStringExtra("userAnswer");
        String correctAnswer = intent.getStringExtra("correctAnswer");

        TextView textAnswer = (TextView) findViewById(R.id.textAnswer);
        TextView textNumberCorrect = (TextView) findViewById(R.id.textNumberCorrect);

        textAnswer.setText("Your answer was \"" + userAnswer + "\", and the correct answer was \"" + correctAnswer + "\"");
        textNumberCorrect.setText("You've gotten " + numberCorrect + "/" + questionNumber + " correct");

        Button btnNext = (Button) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PhysicsAnswer1.this, MainActivity.class);
                intent.putExtra("numberCorrect", numberCorrect);
                startActivity(intent);
            }
        });
    }
}
