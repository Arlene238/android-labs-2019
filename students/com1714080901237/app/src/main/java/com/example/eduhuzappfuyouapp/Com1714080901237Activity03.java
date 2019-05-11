package com.example.eduhuzappfuyouapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.net.Uri;

import java.io.File;
import java.util.List;

public class Com1714080901237Activity03 extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = Com1714080901237Activity03.class.getSimpleName();
    public static final String IMAGE_URL_PREFIX = "http://g4.hexunimg.cn/2015-05-14/175800404.jpg";
     public static final String imageUri=" /data/user/0/com.example.eduhuzappfuyouapp/files/images/175800404.jpg";

    private Button mCheckButton;
    private Button mDownloadImageButton;
    private TextView mNetworkText;
    private ImageView mResultView;
    private TextView mShakeView;
    int ringValue = 40;
    boolean isOpen = false;

    private boolean mConnected;

    private NetworkFileDownloader mFileDownloader;

    // App的内部存储目录
    private File mPrivateRootDir;
    // “images”子目录
    private File mImagesDir;

    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity03_com1714080901237);

        mCheckButton = (Button) findViewById(R.id.button_check);
        mDownloadImageButton = (Button) findViewById(R.id.button_download_image);
        mNetworkText = (TextView) findViewById(R.id.text_network);
        mResultView = (ImageView) findViewById(R.id.text_result);
        mShakeView = (TextView) findViewById(R.id.text_s);

        mCheckButton.setOnClickListener(this);
        mDownloadImageButton.setOnClickListener(this);


        // 获取内部存储目录: files
        mPrivateRootDir = getFilesDir();
        // 获取内部存储子目录: files/images
        mImagesDir = new File(mPrivateRootDir, "images");

       SensorManager sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 通过传感器管理器 获取 本地所有的传感器
        List<Sensor> sensors = sm.getSensorList(Sensor.TYPE_ALL);

       for (Sensor s : sensors) {
           System.out.println("Sensor == " + s.toString());
       }
        // 获取指定的某一个传感器
        Sensor type_accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (type_accelerometer != null) {
            System.out.println("Sensor 获取指定的某一个传感器 " + type_accelerometer.toString());
        }
        // 注册传感器的监听器 （摇一摇）
        sm.registerListener(new SensorEventListener() {
            public void onSensorChanged(SensorEvent sensorEvent) {
                // 传感器数据变化，在该方法中我们可以获取传感器变化的值
                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                if (Math.abs(x) + Math.abs(y) + Math.abs(z) >= ringValue) {
                    if(isOpen) isOpen=false;
                    else isOpen=true;
                    if(isOpen)mShakeView.setText("摇动！！"+"\nX = "+x+"\nY = "+y+"\nZ = "+z+"\n提醒已开启");
                    else mShakeView.setText("摇动！！"+"\nX = "+x+"\nY = "+y+"\nZ = "+z+"\n提醒已关闭");
            }
        }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                System.out.println(sensor);
                System.out.println(accuracy);
            }
        }, type_accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }
        public void onClick (View view){
            if (view.getId() == R.id.button_check) {
                checkNetworkState(); // 检查网络
            } else if (view.getId() == R.id.button_download_image) {
                downloadImages(); // 下载图片
                mResultView.setImageURI(Uri.parse(imageUri));
            }
        }
        private void checkNetworkState () {
            // 取得连接管理器
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            // 检查当前激的网络
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                mConnected = true;
            } else {
                mConnected = false;
            }

            String types = "";

            // 检查Wi-Fi
            networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            boolean isWifiConn = networkInfo != null && networkInfo.isConnected();
            types += isWifiConn ? "Wi-Fi" : "";

            // 检查数据网络
            networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            boolean isMobileConn = networkInfo != null && networkInfo.isConnected();
            types += isMobileConn ? "流量" : "unkonw";
            mNetworkText.setTextColor(mConnected ? Color.GREEN : Color.RED);
            mNetworkText.setText(mConnected ? "网络正常 (" + types + ")" : "网络未连接!");
        }
        private void downloadImages () {
            // 创建下载器
            mFileDownloader = new NetworkFileDownloader(new NetworkFileDownloader.OnImageDownloadListener() {
                @Override
                public void onError(String error) {
                    Toast.makeText(Com1714080901237Activity03.this, error, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onProgressChange(int percent) {
                    Log.i(TAG, "当前进度 = " + percent);
                }

                @Override
                public void onComplete(Bitmap bitmap, String imageUrl) {
                    // 下载的图片格式为PNG
                    final Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
                    // 解析出原始文件名
                    String filename = imageUrl.replace(IMAGE_URL_PREFIX, "");
                    final File imageFile = new File(mImagesDir, filename);
                    // 将文件保存到磁盘中
                    NetworkFileDownloader.writeToDisk(imageFile, bitmap, new NetworkFileDownloader.OnBitmapSaveListener() {
                        @Override
                        public void onBitmapSaved() {
                            Toast.makeText(Com1714080901237Activity03.this, "文件已保存: " + imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                            Log.d("is2", "run ! uri=" + imageFile.getPath());
                            mResultView.setImageURI(Uri.fromFile(imageFile));
                        }

                        @Override
                        public void onBitmapSaveError(String error) {
                            Toast.makeText(Com1714080901237Activity03.this, error, Toast.LENGTH_LONG).show();
                        }
                    }, format, false);
                }
            });
            String imageUrl = IMAGE_URL_PREFIX;
            Log.d("is", "run !");
            mFileDownloader.download(imageUrl, true);

        }
    }
