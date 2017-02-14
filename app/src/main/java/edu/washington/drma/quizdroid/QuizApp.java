package edu.washington.drma.quizdroid;

import android.app.Application;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by dandrew on 2/10/2017.
 */

public class QuizApp extends Application {

    private DataRepository instance = new DataRepository();

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("QuizApp", "Application Created");
    }

    public DataRepository getRepository(){
        return instance;
    }

}
