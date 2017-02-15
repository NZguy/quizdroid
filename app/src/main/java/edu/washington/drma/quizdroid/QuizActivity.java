package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;

public class QuizActivity extends Activity
        implements OverviewFragment.OnFragmentInteractionListener,
                    QuestionFragment.OnFragmentInteractionListener,
                    AnswerFragment.OnFragmentInteractionListener  {

    //private int[] numberOfQuestions = {5, 2, 2}; // Describes amount of questions per different quiz
    private boolean isQuestion = false; // Flag to determine if current fragment is a question or answer
    private int topicIndex;
    private int questionIndex = 0; // The current question we are on
    //private int[] correctArray; // Tracks which questions the user got right

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Get the requested quiz overview
        Intent i = getIntent();
        topicIndex = i.getIntExtra(MainActivity.QUIZ, 0);
        Fragment overview = OverviewFragment.newInstance(topicIndex);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, 0, 0);
        tx.replace(R.id.fragment_placeholder, overview);
        tx.commit();
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
