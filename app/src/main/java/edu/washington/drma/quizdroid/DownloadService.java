package edu.washington.drma.quizdroid;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadService extends Service {

    private final IBinder binder = new LocalBinder();
    private final String TAG = "DownloadService";
    private String downloadURL;
    private Handler handler;
    private HandlerThread handlerThread;
    private DownloadFileRunnable runDownload;
    private OkHttpClient client;

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        // Handler to run thread
        handlerThread = new HandlerThread("MyHandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        // Client to get download
        client = new OkHttpClient();
    }

    @Override
    public void onDestroy(){
        handler.removeCallbacks(runDownload);
        handlerThread.quit();
    }

    public class LocalBinder extends Binder {
        DownloadService getService(){
            return DownloadService.this;
        }
    }

    public void setDownloadURL(String downloadURL){
        Log.d(TAG, "set download url");
        this.downloadURL = downloadURL;

        // Stops previous running of downloads and starts them again with the newest URL
        if(runDownload == null){
            runDownload = new DownloadFileRunnable();
        }
        handler.removeCallbacks(runDownload);
        handler.post(runDownload);
    }

    private void testMethod(){

    }

    // This is so much easier than an alarm manager, is this in another thread?
    private class DownloadFileRunnable implements Runnable{

        Handler myHandler;

        DownloadFileRunnable(){
            myHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void run(){



            Log.d(TAG, "Download URL is: " + downloadURL);
            String data = "";
            try {
                Request request = new Request.Builder()
                        .url(downloadURL)
                        .build();

                Response response = client.newCall(request).execute();
                data = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                // Tell the user that the download failed
            } catch (IllegalArgumentException e){
                e.printStackTrace();
                // Tell the user that this is an invalid url
            }

            if(data != null){
                // If we get here that means the data is good, an error would have thrown with a failed download
                Log.d(TAG, "Response: " + data);
                // Save the data to the file


                // Tell the app that we have finished


            }

            handler.postDelayed(runDownload, (12 * 1000));
        }
    }

    /**
     * Communicating with main app/activity once download is gotten
     * Best http://stackoverflow.com/questions/2463175/how-to-have-android-service-communicate-with-activity
     * Broadcast http://stackoverflow.com/questions/14896574/how-to-call-a-method-in-activity-from-a-service
     * Callback http://stackoverflow.com/questions/23586031/calling-activity-class-method-from-service-class
     * Bind/Callbacks http://stackoverflow.com/questions/20594936/communication-between-activity-and-service
     * http://stackoverflow.com/questions/36525508/communication-between-android-services-and-activities
     * http://stackoverflow.com/questions/9751088/how-do-i-display-a-dialog-in-android-without-an-activity-context(examples)
     * Dont create dialog from service http://stackoverflow.com/questions/8350385/launching-dialog-from-service
     * Android Docs
     * Bind https://developer.android.com/guide/components/bound-services.html
     * LocalBroadcastManager https://developer.android.com/reference/android/support/v4/content/LocalBroadcastManager.html
     *
     * Repeated Downloading
     * http://www.coderzheaven.com/2012/07/14/http-call-repeatedly-service-android/
     * https://code.tutsplus.com/tutorials/android-fundamentals-downloading-data-with-services--mobile-5740
     * http://stackoverflow.com/questions/25570024/repeat-service-task
     * AsyncTask or ExecutorService
     * DownloadManager or HttpURLConnection
     *
     * Download Backups
     * http://stackoverflow.com/questions/37601380/android-downloadmanager-backup-files-and-canceled-downloads
     *
     * My Plam
     * Use binder to call methods on the service from the activity
     * Use an orderedbroadcast in case the download fails, any active activity should see this broadcast
     * and show an alert that the user can respond to through a call to the app
     * Use a localbroadcast when the download is ready and data needs to be refreshed
     * Check if the user has connection connectivitymanager
     * Have a service that downloads the new file every hour (if the app is running), sleep the thread for an hour
     * Save file as new name then rename to proper filename once download is complete and validated
     * Tell the app to retrieve the new data once download is complete
     * Refresh main activity so users can see new questions
     */
}
