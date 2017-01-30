package edu.washington.drma.quizdroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mathButton = (Button) findViewById(R.id.btnMath);
        Button physicsButton = (Button) findViewById(R.id.btnPhys);
        Button marvelButton = (Button) findViewById(R.id.btnMarvel);

        mathButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MathOverview.class);
                startActivity(intent);
            }
        });

        physicsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PhysicsOverview.class);
                startActivity(intent);
            }
        });

        marvelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MarvelOverview.class);
                startActivity(intent);
            }
        });
    }
}
