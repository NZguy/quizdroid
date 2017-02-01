package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    public static final String QUIZ = "drma.quizIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mathButton = (Button) findViewById(R.id.btnMath);
        Button physicsButton = (Button) findViewById(R.id.btnPhys);
        Button marvelButton = (Button) findViewById(R.id.btnMarvel);

        mathButton.setOnClickListener(new MyListener());
        physicsButton.setOnClickListener(new MyListener());
        marvelButton.setOnClickListener(new MyListener());
    }

    public class MyListener implements View.OnClickListener{

        public MyListener(){

        }

        @Override
        public void onClick(View v){
            if(v.getTag() != null){
                int buttonTag = Integer.parseInt(v.getTag().toString());
                Intent i = new Intent(MainActivity.this, Quiz.class);
                i.putExtra(QUIZ, buttonTag);
                startActivity(i);
            }
        }

    }

}
