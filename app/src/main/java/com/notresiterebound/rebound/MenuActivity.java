package com.notresiterebound.rebound;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    Button start;
    Button help;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        start = (Button)findViewById(R.id.start);
        help = (Button)findViewById(R.id.help);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Tag", "hi");
                Intent game = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(game);
            }
        });
    }
}
