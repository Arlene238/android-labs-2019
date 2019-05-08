package com.example.soft1706081301317sweepacticity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SweepActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sweep_acticity);
        Button button = (Button)findViewById(R.id.save);
        final EditText editText = findViewById(R.id.username);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_MULTI_PROCESS).edit();
                editor.putString("username",editText.getText().toString());
                editor.commit();
                Intent intent = new Intent(SweepActivity.this,WeclomeActivity.class);
                startActivity(intent);
            }
        });
    }

}
