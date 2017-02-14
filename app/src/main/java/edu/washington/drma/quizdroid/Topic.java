package edu.washington.drma.quizdroid;

import java.util.ArrayList;

/**
 * Created by dandrew on 2/13/2017.
 */

public class Topic {

    private String title;
    private String descShort;
    private String descLong;
    private ArrayList<Question> questionList;

    public Topic(String title, String descShort, String descLong){
        questionList = new ArrayList<Question>();
        this.title = title;
        this.descShort = descShort;
        this.descLong = descLong;
    }

    public void addQuestion(Question q){
        questionList.add(q);
    }

    public Question getQuestion(int questionNumber){
        return questionList.get(questionNumber);
    }

    public String getTitle(){
        return this.title;
    }

    public String getDescShort(){
        return this.descShort;
    }

    public String getDescLong(){
        return this.descShort;
    }

    public int countCorrect(){
        int numberCorrect = 0;
        for(int i = 0; i < questionList.size(); i++){
            if(questionList.get(i).isCorrect()){
                numberCorrect++;
            }
        }
        return numberCorrect;
    }

}
