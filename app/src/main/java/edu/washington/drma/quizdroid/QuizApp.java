package edu.washington.drma.quizdroid;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Environment;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.WindowManager;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by dandrew on 2/10/2017.
 */

public class QuizApp extends Application {

    private DataRepository instance;
    DownloadService downloadService;
    private boolean serviceBound = false;
    private String defaultDownloadURL;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("QuizApp", "Application Created");
        Log.i("QuizApp", this.getFilesDir().getAbsolutePath());

        String filePath = getApplicationContext().getFilesDir() + "/questions.json";
        File questionsFile = new File(filePath);
        instance = new DataRepository(questionsFile, getApplicationContext());

        // /data/user/0/edu.washington.drma.quizdroid/files
        // Can't adb push to my device, no permission to
        // Log.i("QuizApp", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());

        Intent intent = new Intent(this, DownloadService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        defaultDownloadURL = "http://tednewardsandbox.site44.com/questions.json";
        //defaultDownloadURL = "lol";
        String anotherURL = "https://raw.github.com/square/okhttp/master/README.md";
    }

    public DataRepository getRepository(){
        return instance;
    }

    public void setDownloadURL(String downloadURL){
        downloadService.setDownloadURL(downloadURL);
    }

    public void tryDownload(){
        downloadService.tryDownload();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // There has to be a way to shorten this o.o
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            downloadService = binder.getService();
            serviceBound = true;
            downloadService.setDownloadURL(defaultDownloadURL);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

}
