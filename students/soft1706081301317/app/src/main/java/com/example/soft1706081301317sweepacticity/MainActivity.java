package com.example.soft1706081301317sweepacticity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TextView textView = findViewById(R.id.begin);
      TextView textView_2 = findViewById(R.id.help);
      textView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent  intent = new Intent(MainActivity.this,SweepActivity.class);
              startActivity(intent);
          }
      });
      textView_2.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent  intent = new Intent(MainActivity.this,HelpActivity.class);
              startActivity(intent);
          }
      });
    }
}

