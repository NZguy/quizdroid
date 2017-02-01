package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;

public class Quiz extends Activity
        implements Overview.OnFragmentInteractionListener,
                    Question.OnFragmentInteractionListener,
                    Answer.OnFragmentInteractionListener  {

    private int[] numberOfQuestions = {5, 2, 2}; // Describes amount of questions per different quiz
    private boolean isQuestion = false; // Flag to determine if current fragment is a question or answer
    private int currentQuestion = 0; // The current question we are on
    private int[] correctArray; // Tracks which questions the user got right

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

    @Override
    public void onBeginPressed(int quizIndex) {
        Log.i("drma.Quiz", "Begin pressed " + quizIndex);
        Fragment question = Question.newInstance();
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_placeholder, question);
        tx.commit();

        correctArray = new int[numberOfQuestions[quizIndex]];
    }

    public void onSubmitPressed(String userAnswer, String correctAnswer){
        if (correctAnswer.equals(userAnswer)) {
            correctArray[currentQuestion] = 1;
        } else {
            correctArray[currentQuestion] = 0;
        }

        int numberCorrect = 0;
        for(int i = 0; i < correctArray.length; i++){
            numberCorrect += correctArray[i];
        }

        boolean isLastQuestion = false;
        if((currentQuestion + 1) == correctArray.length){
            isLastQuestion = true;
        }

        Log.i("drma.Quiz", "Submit pressed " + correctAnswer.equals(userAnswer));
        Fragment answer = Answer.newInstance(numberCorrect, (currentQuestion + 1), userAnswer, correctAnswer, isLastQuestion);
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_placeholder, answer);
        tx.commit();
    }

    public void onNextPressed(){
        currentQuestion++;

        Fragment question = Question.newInstance();
        FragmentTransaction tx = getFragmentManager().beginTransaction();
        tx.replace(R.id.fragment_placeholder, question);
        tx.commit();
    }

    public void onFinishPressed(){
        Intent i = new Intent(Quiz.this, MainActivity.class);
        startActivity(i);
    }
}
