package edu.washington.drma.quizdroid;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
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
        handler = new Handler(handlerThread.getLooper()){

            @Override
            public void handleMessage(Message msg){
                Bundle bundle = msg.getData();
                String messageType = bundle.getString("messageType");
                if(messageType.compareTo("SUCCESS") == 0){
                    // Success, send a notification, UI wull be updated by the repository
                    Log.d(TAG, "Download Succeded");

                }else if(messageType.compareTo("ERROR_BAD_URL") == 0){
                    // Failure

                    // Stop future downloads for now, will restart if user enters a new url
                    handler.removeCallbacks(runDownload);

                    // Alert the user of this
                    Intent intent = new Intent("ALERT");
                    intent.putExtra("messageType", messageType);
                    //TODO: Make local broadcast work
                    //LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
                    sendBroadcast(intent);

                }else if(messageType.compareTo("ERROR_DOWNLOAD_FAIL") == 0){
                    // Failure

                    // Stop future downloads for now, will restart if user wants to
                    handler.removeCallbacks(runDownload);

                    // Alert the user
                    Intent intent = new Intent("ALERT");
                    intent.putExtra("messageType", messageType);
                    sendBroadcast(intent);
                }
            }
        };

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

        this.tryDownload();
    }

    public void tryDownload(){
        // There many be a better way to do this checking but this works for now

        // Tell user
        Toast startToast = Toast.makeText(this.getApplicationContext(),
                "Downloading questions file",
                Toast.LENGTH_SHORT);
        startToast.show();

        // Check if airplane mode is on first
        if(!isAirplaneModeOn(this.getApplicationContext())){

            // Check if we are connected to the internet
            if(isConnected()){

                // Stops previous running of downloads and starts them again with the newest URL
                if(runDownload == null){
                    runDownload = new DownloadFileRunnable(handler);
                }
                handler.removeCallbacks(runDownload);
                handler.post(runDownload);

            }else{
                // No connection, pop a toast
                Toast connectionToast = Toast.makeText(this.getApplicationContext(),
                        "No internet connecton, can't download quiz",
                        Toast.LENGTH_SHORT);
                connectionToast.show();
            }

        }else{
            // Airplane mode is on alert the user
            // Alert the user
            Intent intent = new Intent("ALERT");
            intent.putExtra("messageType", "ERROR_AIRPLANE_MODE_ON");
            sendBroadcast(intent);

        }

        /**
         * Would be nice to retry the download when connection comes back online, maybe have a
         * retry button. Or to automatically retry.
         */

    }

    @SuppressWarnings("deprecation")
    private boolean isAirplaneModeOn(Context context) {
        boolean isOn = false;

        // Use different code depending on the api version
        if(android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN){
            isOn = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        }else{
            isOn = Settings.System.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }

        return isOn;
    }

    private boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    // This is so much easier than an alarm manager, is this in another thread?
    private class DownloadFileRunnable implements Runnable{

        Handler myHandler;

        DownloadFileRunnable(Handler h){
            myHandler = h;
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
                sendCaseMessage("ERROR_DOWNLOAD_FAIL");
                myHandler.removeCallbacks(runDownload);
            } catch (IllegalArgumentException e){
                e.printStackTrace();
                // Tell the user that this is an invalid url
                sendCaseMessage("ERROR_BAD_URL");
                myHandler.removeCallbacks(runDownload);
            }

            if(!data.isEmpty()){
                // If we get here that means the data is good, an error would have thrown with a failed download
                // Would need more validation for a final project
                Log.d(TAG, "Response got ");
                // Save the data to the file

                try {
                    // Put data in the right file
                    FileOutputStream outputStream = openFileOutput("questions.json", Context.MODE_PRIVATE);
                    // This line below makes a black filter cover the screen as if an alert were popped.
                    // TODO: Fix the above error
                    outputStream.write(data.getBytes());
                    outputStream.close();

                    // Make repository recreate its data
                    QuizApp app = (QuizApp)getApplicationContext();
                    app.getRepository().initializeData();

                    // Tell the app that we have finished
                    sendCaseMessage("SUCCESS");

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            handler.postDelayed(runDownload, (12 * 1000)); //TODO: Change this to longer value
        }

        private void sendCaseMessage(String messageType){
            Message message = myHandler.obtainMessage();
            Bundle bundle = message.getData();
            bundle.putString("messageType", messageType);
            message.setData(bundle);
            myHandler.sendMessage(message);
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
     * Send Data from background thread to ui thread
     * http://androidshortnotes.blogspot.com/2013/02/thread-concept-in-android.html
     * https://www.intertech.com/Blog/android-non-ui-to-ui-thread-communications-part-3-of-5/
     *
     * My Plan
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
