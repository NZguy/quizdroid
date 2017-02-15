package edu.washington.drma.quizdroid;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dandrew on 2/10/2017.
 */

public class DataRepository{

    ArrayList<Topic> topics;

    public DataRepository(){
        this.initializeData();
    }

    public void initializeData() {
        topics = new ArrayList<Topic>();
        topics.add(new Topic("Math", "descShort", "descLong", R.drawable.ic_action_name));
        topics.add(new Topic("Physics", "descShort", "descLong", R.drawable.ic_action_name));
        String[] questions = {"answer1", "answer2", "answer3", "answer4"};
        topics.get(0).addQuestion(new Question("question", questions, 2));
        topics.get(0).addQuestion(new Question("question", questions, 2));
        topics.get(0).addQuestion(new Question("question", questions, 3));
        String[] questions2 = {"answer5", "answer6", "answer7", "answer8"};
        topics.get(1).addQuestion(new Question("question", questions2, 2));
    }

    public int getNumOfTopics(){
        return topics.size();
    }

    public String[] getTopicTitles(){
        String[] topicNames = new String[topics.size()];
        for(int i = 0; i < topics.size(); i++){
            topicNames[i] = topics.get(i).getTitle();
        }
        return topicNames;
    }

    public String getTopicTitle(int topicNumber){
        return this.topics.get(topicNumber).getTitle();
    }

    public String getTopicDescShort(int topicNumber){
        return this.topics.get(topicNumber).getDescShort();
    }

    public String getTopicDescLong(int topicNumber){
        return this.topics.get(topicNumber).getDescLong();
    }

    public int getTopicIconName(int topicNumber){
        return this.topics.get(topicNumber).getIconName();
    }

    public int getNumOfQuestions(int topicNumber){
        return this.topics.get(topicNumber).getNumOfQuestions();
    }

    public int countCorrectAnswers(int topicNumber){
        return this.topics.get(topicNumber).countCorrect();
    }

    public void setQuestionUserAnswer(int topicNumber, int questionNumber, int userAnswer){
        this.topics.get(topicNumber).getQuestion(questionNumber).setUserAnswer(userAnswer);
    }

    public boolean isQuestionCorrect(int topicNumber, int questionNumber){
        return this.topics.get(topicNumber).getQuestion(questionNumber).isCorrect();
    }

    public String getQuestionText(int topicNumber, int questionNumber){
        return this.topics.get(topicNumber).getQuestion(questionNumber).getText();
    }

    public String[] getQuestionAnswers(int topicNumber, int questionNumber){
        return this.topics.get(topicNumber).getQuestion(questionNumber).getAnswers();
    }

    public String getQuestionCorrectAnswer(int topicNumber, int questionNumber){
        return this.topics.get(topicNumber).getQuestion(questionNumber).getCorrectAnswer();
    }

    public String getQuestionUserAnswer(int topicNumber, int questionNumber){
        return this.topics.get(topicNumber).getQuestion(questionNumber).getUserAnswer();
    }



}
