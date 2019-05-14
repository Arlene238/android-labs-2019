package edu.hzu.shiyan5;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class dlMainActivity extends AppCompatActivity {

    private EditText et_info;
    private EditText et_info2;
    private Button btn_save;
    private Button btn_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlactivity_main);

        et_info = (EditText) findViewById(R.id.et_info);
        et_info2= (EditText) findViewById(R.id.et_info2);
        btn_save = (Button) findViewById(R.id.btn_save);
        btn_read = (Button) findViewById(R.id.btn_read);
        btn_read.setOnClickListener(new ButtonListener());
        btn_save.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_save:
                    String hang="\n";
                    String saveinfo = et_info.getText().toString().trim();
                    String saveinfo2 = et_info2.getText().toString().trim();
                    FileOutputStream fos;
                    try {
                        fos = openFileOutput("data.txt", Context.MODE_APPEND);
                        fos.write(hang.getBytes());
                        fos.write(saveinfo.getBytes());
                        fos.write(hang.getBytes());
                        fos.write(saveinfo2.getBytes());
                        fos.write(hang.getBytes());
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(dlMainActivity.this, "数据保存成功", 0).show();
                    break;
                case R.id.btn_read:
                    String content = "";
                    try {
                        FileInputStream fis = openFileInput("data.txt");
                        byte[] buffer = new byte[fis.available()];
                        fis.read(buffer);
                        content = new String(buffer);
                        fis.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(dlMainActivity.this, "保存的数据是:" + content,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }
}
