package edu.washington.drma.quizdroid;

import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class QuizActivity extends AppCompatActivity
        implements OverviewFragment.OnFragmentInteractionListener,
                    QuestionFragment.OnFragmentInteractionListener,
                    AnswerFragment.OnFragmentInteractionListener  {

    //private int[] numberOfQuestions = {5, 2, 2}; // Describes amount of questions per different quiz
    private boolean isQuestion = false; // Flag to determine if current fragment is a question or answer
    private int topicIndex;
    private int questionIndex = 0; // The current question we are on
    //private int[] correctArray; // Tracks which questions the user got right
    AlertReciever alertReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Up button
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        // Get the requested quiz overview
        Intent i = getIntent();
        topicIndex = i.getIntExtra(MainActivity.QUIZ, 0);
        Fragment overview = OverviewFragment.newInstance(topicIndex);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 0, 0);
        tx.replace(R.id.fragment_placeholder, overview);
        tx.commit();

        // Register alert reciever
        alertReciever = new AlertReciever(this);
        registerReceiver(alertReciever, new IntentFilter("ALERT"));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(alertReciever);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_preferences:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                i = new Intent(QuizActivity.this, PreferencesActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBeginPressed(int topicIndex) {
        Log.i("drma.QuizActivity", "Begin pressed " + topicIndex);
        Fragment question = QuestionFragment.newInstance(topicIndex, questionIndex);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 0, 0);
        tx.replace(R.id.fragment_placeholder, question);
        tx.commit();
    }

    public void onSubmitPressed(){
//        int numberCorrect = app.getRepository().countCorrectAnswers(topicIndex);

        QuizApp app = (QuizApp)this.getApplication();
        boolean isLastQuestion = false;
        if((questionIndex + 1) == app.getRepository().getNumOfQuestions(topicIndex)){
            isLastQuestion = true;
        }

        Log.i("drma.QuizActivity", "Submit pressed " + app.getRepository().isQuestionCorrect(topicIndex, questionIndex));
        Fragment answer = AnswerFragment.newInstance(topicIndex, questionIndex, isLastQuestion);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 0, 0);
        tx.replace(R.id.fragment_placeholder, answer);
        tx.commit();
    }

    public void onNextPressed(){
        questionIndex++;

        Fragment question = QuestionFragment.newInstance(topicIndex, questionIndex);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 0, 0);
        tx.replace(R.id.fragment_placeholder, question);
        tx.commit();
    }

    public void onFinishPressed(){
        Intent i = new Intent(QuizActivity.this, MainActivity.class);
        startActivity(i);
    }
}
