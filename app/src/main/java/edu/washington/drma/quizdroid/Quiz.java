package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;

public class Quiz extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get the requested quiz overview
        Intent i = getIntent();
        int quizIndex = i.getIntExtra(MainActivity.QUIZ, 0);
        Fragment overview = Overview.newInstance(quizIndex);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_placeholder, overview);
        tx.commit();
    }
}
