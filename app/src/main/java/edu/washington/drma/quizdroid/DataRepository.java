package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import org.json.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by dandrew on 2/10/2017.
 */

public class DataRepository{

    private ArrayList<Topic> topics;
    private Context context;

    public DataRepository(File questionsFile){
        this.context = context;
        this.initializeData(questionsFile);
        // How do I get access to context in a non activity class without app crashing, for some
        // reason context is null

        /*
        How do I get access to context in a non activity class without app crashing? For some
        reason context is null.

        Whats the best way to handle all the try/catch segments in this code?
         */

//        Log.i("DataRepository", "DataRepo loaded");
//        Log.i("DataRepository", context.getFilesDir().getAbsolutePath());
    }

    public void initializeData(File questionsFile) {
        topics = new ArrayList<Topic>();
//        topics.add(new Topic("Math", "descShort", "descLong", R.drawable.ic_action_name));
//        topics.add(new Topic("Physics", "descShort", "descLong", R.drawable.ic_action_name));
//        String[] questions = {"answer1", "answer2", "answer3", "answer4"};
//        topics.get(0).addQuestion(new Question("question", questions, 2));
//        topics.get(0).addQuestion(new Question("question", questions, 2));
//        topics.get(0).addQuestion(new Question("question", questions, 3));
//        String[] questions2 = {"answer5", "answer6", "answer7", "answer8"};
//        topics.get(1).addQuestion(new Question("question", questions2, 2));

        String questionsJSON = readFileToString(questionsFile);
        Log.i("DataRepository", "JSON");
        Log.i("DataRepository", questionsJSON);

        // Source: http://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
        try {
            JSONArray jTopics = new JSONArray(questionsJSON);
            for(int i = 0; i < jTopics.length(); i++){
                JSONObject jTopic = jTopics.getJSONObject(i);
                topics.add(i, new Topic(jTopic.getString("title"),
                        jTopic.getString("desc"),
                        jTopic.getString("desc"),
                        R.drawable.ic_action_name));
                JSONArray jQuestions = jTopic.getJSONArray("questions");
                for(int j = 0; j < jQuestions.length(); j++){
                    JSONObject jQuestion = jQuestions.getJSONObject(j);
                    JSONArray jAnswers = jQuestion.getJSONArray("answers");
                    String[] stringAnswers = new String[jAnswers.length()];
                    for(int k = 0; k < jAnswers.length(); k++){
                        stringAnswers[k] = jAnswers.getString(k);
                    }
                    topics.get(i).addQuestion(new Question(jQuestion.getString("text"),
                            stringAnswers,
                            (jQuestion.getInt("answer") - 1)));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String readFileToString(File file){
        String ret = "";
        try {

            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }
            reader.close();
            ret = builder.toString();
            inputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
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
