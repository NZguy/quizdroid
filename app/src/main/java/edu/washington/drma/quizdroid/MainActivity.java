package edu.washington.drma.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final String QUIZ = "drma.quizIndex";
    private static final String TAG = "MainActivity";
    QuizApp app;
    AlertReciever alertReciever1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        app = (QuizApp)this.getApplication();

        // Progamatically create topic buttons
        LinearLayout container = (LinearLayout)findViewById(R.id.container);
        LinearLayout.LayoutParams buttonLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        String[] topicTitles = app.getRepository().getTopicTitles();
        for(int i = 0; i< topicTitles.length; i++){
            Button topicButton = new Button(this);
            topicButton.setLayoutParams(buttonLayout);
            topicButton.setTag(i);
            topicButton.setOnClickListener(new MyListener());
            topicButton.setText(topicTitles[i]);
            Drawable icon = ResourcesCompat.getDrawable(getResources(),app.getRepository().getTopicIconName(i), null);
            topicButton.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
            container.addView(topicButton);
        }


        // Register alert reciever
        alertReciever1 = new AlertReciever(this);
        registerReceiver(alertReciever1, new IntentFilter("ALERT"));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(alertReciever1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_preferences:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                i = new Intent(MainActivity.this, PreferencesActivity.class);
                startActivity(i);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    BroadcastReceiver alertReciever = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent){
            Log.d(TAG, "Broadcast Received");
            Bundle bundle = intent.getExtras();
            String messageType = bundle.getString("messageType");
            //showAlert(messageType);
        }
    };

    public void showAlert(String messageType){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

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
                    app.tryDownload();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Quit", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "Quitting App");
                    // This doesn't seem to kill the service, maybe stop the download service onPause
                    MainActivity.this.finishAffinity();
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
                    startActivity(intent);
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, "No network detected");
                    // No network can't download file, tell the user in a toast
                    Toast toast = Toast.makeText(MainActivity.this.getApplicationContext(),
                            "No internet connecton, can't download quiz",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }else{
            Log.e(TAG, "An error with no type, this shouldn't happen");
        }

        alertDialog.show();
    }

    public class MyListener implements View.OnClickListener{

        public MyListener(){

        }

        @Override
        public void onClick(View v){
            if(v.getTag() != null){
                int buttonTag = Integer.parseInt(v.getTag().toString());
                Intent i = new Intent(MainActivity.this, QuizActivity.class);
                i.putExtra(QUIZ, buttonTag);
                startActivity(i);
            }
        }

    }

}
