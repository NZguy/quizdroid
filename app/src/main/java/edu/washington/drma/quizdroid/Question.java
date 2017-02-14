package edu.washington.drma.quizdroid;

/**
 * Created by dandrew on 2/13/2017.
 */

public class Question {

    private String questionText;
    private String[] answers;
    private int correctAnswer;
    private int userAnswer;

    public Question(String questionText, String[] answers, int correctAnswer){
        this.questionText = questionText;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
        this.userAnswer = -1;
    }

    public void setUserAnswer(int userAnswer){
        this.userAnswer = userAnswer;
    }

    public String getText(){
        return this.questionText;
    }

    public String[] getAnswers(){
        return this.answers;
    }

    public String getCorrectAnswer(){
        return this.answers[correctAnswer];
    }

    public String getUserAnswer(){
        return this.answers[userAnswer];
    }

    public boolean isCorrect(){
        if(userAnswer == correctAnswer){
            return true;
        }else{
            return false;
        }
    }

}
