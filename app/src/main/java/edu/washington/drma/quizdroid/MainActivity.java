package edu.washington.drma.quizdroid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
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

public class MainActivity extends AppCompatActivity {

    public static final String QUIZ = "drma.quizIndex";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        QuizApp app = (QuizApp)this.getApplication();

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
        registerReceiver(alertReciever, new IntentFilter("ALERT"));
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(alertReciever);
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
            showAlert(messageType);
        }
    };

    public void showAlert(String messageType){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();

    if(messageType.compareTo("ERROR_BAD_URL") == 0){
        // Failure, send an orderedbroadcast
        //TODO: send a broadcast that triggers an alert
        Log.d(TAG, "Download Failed, bad url");
        Intent intent = new Intent("ALERT");
        intent.putExtra("messageType", messageType);
        //TODO: Make local broadcast work
        //LocalBroadcastManager.getInstance(DownloadService.this).sendBroadcast(intent);
        sendBroadcast(intent);

    }else if(messageType.compareTo("ERROR_DOWNLOAD_FAIL") == 0){
        // Failure, send an orderedbroadcast
        //TODO: send a broadcast that triggers an alert
        Log.d(TAG, "Download Failed");
    }

        alertDialog.setTitle("Alert");
        alertDialog.setMessage("Alert message to be shown");
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
