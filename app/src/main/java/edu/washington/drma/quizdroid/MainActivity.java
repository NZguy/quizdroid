package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    public static final String QUIZ = "drma.quizIndex";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        QuizApp app = (QuizApp)this.getApplication();

        //Have the buttons created programatically by checking the number of topics
        LinearLayout container = (LinearLayout)findViewById(R.id.activity_main);
        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] topicTitles = app.getRepository().getTopicTitles();
        for(int i = 0; i< topicTitles.length; i++){
            Button topicButton = new Button(this);
            topicButton.setLayoutParams(buttonLayout);
            topicButton.setTag(i);
            topicButton.setOnClickListener(new MyListener());
            topicButton.setText(topicTitles[i]);
            container.addView(topicButton);
        }
    }

    public class MyListener implements View.OnClickListener{

        public MyListener(){

        }

        @Override
        public void onClick(View v){
            if(v.getTag() != null){
                int buttonTag = Integer.parseInt(v.getTag().toString());
                Intent i = new Intent(MainActivity.this, QuizActivity.class);
                i.putExtra(QUIZ, buttonTag);
                startActivity(i);
            }
        }

    }

}
