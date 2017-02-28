package edu.washington.drma.quizdroid;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class AlertReciever extends BroadcastReceiver {

    private static final String TAG = "AlertReciever";
    private Activity activity;

    public AlertReciever(Activity activity){
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Broadcast Received");
        Bundle bundle = intent.getExtras();
        String messageType = bundle.getString("messageType");
        showAlert(messageType);
    }

    public void showAlert(String messageType){
        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();

        if(messageType.compareTo("ERROR_BAD_URL") == 0){
            Log.d(TAG, "Download Failed, bad url");

            // Display relevant dialog
            alertDialog.setTitle("Bad URL");
            alertDialog.setMessage("You entered an invalid URL, please change the URL in preferences");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Do nothing, just close the dialog
                    // Msybe take the user to the prefernces page when this happens
                }
            });

        }else if(messageType.compareTo("ERROR_DOWNLOAD_FAIL") == 0){
            Log.d(TAG, "Download Failed");

            // Display relevant dialog
            alertDialog.setTitle("Download Failed");
            alertDialog.setMessage("Would you like to retry or quit the application and try again later?");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Retry", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "Retrying download");
                    QuizApp app = (QuizApp)activity.getApplication();
                    app.tryDownload();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "Quitting App");
                    // This doesn't seem to kill the service, maybe stop the download service onPause
                    activity.finishAffinity();
                }
            });

        }else if(messageType.compareTo("ERROR_AIRPLANE_MODE_ON") == 0){

            Log.d(TAG, "Airoplane Mode On");

            // Display relevant dialog
            alertDialog.setTitle("Airplane Mode On");
            alertDialog.setMessage("Would you like to turn off airplane mode and retry the download?");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "Going to settings");
                    Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                    activity.startActivity(intent);
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "No network detected");
                    // No network can't download file, tell the user in a toast
                    Toast toast = Toast.makeText(activity.getApplicationContext(),
                            "No internet connecton, can't download quiz",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }else if(messageType.compareTo("SUCCESS_REFRESH_UI") == 0){
            Log.d(TAG, "Download success, reloading UI");

            // Tell user
            Toast successToast = Toast.makeText(activity.getApplicationContext(),
                    "Downloading questions file",
                    Toast.LENGTH_SHORT);
            successToast.show();

            // TODO: Fix UI reloading
            View view = activity.findViewById(R.id.activity_main);
            view.invalidate();

        }else{
            Log.e(TAG, "An error with no type, this shouldn't happen");
        }

        alertDialog.show();
    }
}
