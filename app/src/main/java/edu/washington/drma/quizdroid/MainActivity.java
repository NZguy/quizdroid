package edu.washington.drma.quizdroid;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    public static final String QUIZ = "drma.quizIndex";

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.action_main:
                // User chose the "Settings" item, show the app settings UI...
                i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                return true;

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
