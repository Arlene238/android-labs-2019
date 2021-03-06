package com.example.shiyan7;

import android.app.Activity;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Map;

public class MainActivity extends Activity{
    ShakeListener mShakeListener = null;
    Vibrator mVibrator;
    private RelativeLayout mImgUp;
    private RelativeLayout mImgDn;
    private SoundPool sndpool;
    private Map<Integer,Integer> loadSound;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        loadSound=Utils.loadSound(sndpool,this);
    }
    private void init(){
        mVibrator=(Vibrator)getApplication().getSystemService(VIBRATOR_SERVICE);
        mImgUp = (RelativeLayout)findViewById(R.id.shakeImgUp);
        mImgDn = (RelativeLayout)findViewById(R.id.shakeImgDown);
        sndpool=new SoundPool(2, AudioManager.STREAM_SYSTEM,5);
    }
    @Override
    protected void onResume(){
        super.onResume();
        mShakeListener = new ShakeListener(this);
        mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener(){
            public void onShake(){
                final String hint = "抱歉，暂时没有找到\n在同一时刻摇一摇的人。\n再试一次吧！";
                Utils.startAnim(mImgUp,mImgDn);
                mShakeListener.stop();
                sndpool.play(loadSound.get(0),(float) 1,(float) 1,0,0,(float) 1.2);
                startVibrato();
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        sndpool.play(loadSound.get(1),(float) 1,(float) 1,0,0,(float) 1.0);
                        Toast.makeText(getApplicationContext(),hint,10).show();
                        mVibrator.cancel();
                        mShakeListener.start();
                    }
                },2000);
            }
        });
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mShakeListener!=null){
            mShakeListener.stop();
        }
    }
    public void startVibrato(){
        mVibrator.vibrate(new long[]{ 500,200,500,200 },-1);
    }
    public void shake_activity_back(View v){
        this.finish();
    }
    public void linshi(View v) {
        Utils.startAnim(mImgUp, mImgDn);
    }
}
