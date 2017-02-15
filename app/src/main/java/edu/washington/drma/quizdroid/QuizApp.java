package edu.washington.drma.quizdroid;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by dandrew on 2/10/2017.
 */

public class QuizApp extends Application {

    private DataRepository instance;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("QuizApp", "Application Created");
        Log.i("QuizApp", this.getFilesDir().getAbsolutePath());

        String filePath = getApplicationContext().getFilesDir() + "/questions.json";
        File questionsFile = new File(filePath);
        instance = new DataRepository(questionsFile);

        // /data/user/0/edu.washington.drma.quizdroid/files
        // Can't adb push to my device, no permission to
        // Log.i("QuizApp", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
    }

    public DataRepository getRepository(){
        return instance;
    }

}
