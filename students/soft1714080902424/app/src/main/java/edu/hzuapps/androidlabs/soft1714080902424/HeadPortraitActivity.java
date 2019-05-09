package edu.hzuapps.androidlabs.soft1714080902424;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class HeadPortraitActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = HeadPortraitActivity.class.getSimpleName();
    public static final String IMAGE_URL_PREFIX = "https://raw.githubusercontent.com/Kowaiking/android-labs-2019/master/students/soft1714080902424/app/src/main/res/drawable/";
    static String[] imageNames = {"head_portrait.jpg"};

    private Button CheckButton;
    private Button DownloadImageButton;
    private Button ShowImageButton;
    private Button NextButton;
    private TextView NetworkText;
    private ImageView imageView;

    private boolean Connected;

    private FileDownloader FileDownloader;

    private File PrivateRootDir;

    private File ImagesDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_portrait);

        CheckButton = (Button) findViewById(R.id.button_check_net);
        DownloadImageButton = (Button) findViewById(R.id.button_download_photo);
        ShowImageButton = (Button) findViewById(R.id.button_show_photo);
        NetworkText = (TextView) findViewById(R.id.textView_network);
        imageView = (ImageView) findViewById(R.id.imageview_show_photo);
        NextButton = findViewById(R.id.button_next);
        CheckButton.setOnClickListener(this);
        DownloadImageButton.setOnClickListener(this);
        ShowImageButton.setOnClickListener(this);

        NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HeadPortraitActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        PrivateRootDir = getFilesDir();

        ImagesDir = new File(PrivateRootDir, "images");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_check_net) {
            checkNetworkState();
        } else if (view.getId() == R.id.button_download_photo) {
            downloadImages();
        } else if (view.getId() == R.id.button_show_photo){
            showheadportrait();
        }
    }

    private void checkNetworkState() {

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Connected = true;
        } else {
            Connected = false;
        }

        String types = "";

        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiConn = networkInfo != null && networkInfo.isConnected();
        types += isWifiConn ? "Wi-Fi" : "";

        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileConn = networkInfo != null && networkInfo.isConnected();
        types += isMobileConn ? "流量" : "";

        NetworkText.setTextColor(Connected ? Color.GREEN : Color.RED);
        NetworkText.setText(Connected ? "网络正常 (" +types + ")" : "网络未连接!");
    }


    private void downloadImages() {

        FileDownloader = new FileDownloader(new FileDownloader.OnImageDownloadListener() {
            @Override
            public void onError(String error) {
                Toast.makeText(HeadPortraitActivity.this, error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onProgressChange(int percent) {
                Log.i(TAG, "当前进度 = " + percent);
            }

            @Override
            public void onComplete(Bitmap bitmap, String imageUrl) {

                final Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;

                String filename = imageUrl.replace(IMAGE_URL_PREFIX, "");
                final File imageFile = new File(ImagesDir, filename);

                FileDownloader.writeToDisk(imageFile, bitmap, new FileDownloader.OnBitmapSaveListener() {
                    @Override
                    public void onBitmapSaved() {
                        Toast.makeText(HeadPortraitActivity.this, "文件已保存: " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onBitmapSaveError(String error) {
                        Toast.makeText(HeadPortraitActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                }, format, false);
            }
        });

        for(String imageName : imageNames) {
            String imageUrl = IMAGE_URL_PREFIX + imageName;
            FileDownloader.download(imageUrl, true);
        }
    }

    private void showheadportrait() {
        Bitmap bitmap = getLoacalBitmap("/data/user/0/edu.hzuapps.androidlabs.soft1714080902424/files/images/head_portrait.jpg");
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}