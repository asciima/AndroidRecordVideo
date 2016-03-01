package com.practice.vediorecorderapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import com.practice.videorecordermodel.manage.VideoManage;
import com.practice.videorecordermodel.view.RecordButton;

public class RecordingActivity extends AppCompatActivity implements RecordButton.onRecordingFinishListener {

    private SurfaceView mSurfaceView;

    private RecordButton mRecordButton;

    private VideoManage mVideoManage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        init();
    }

    private void init() {
        mSurfaceView = (SurfaceView) findViewById(R.id.mySurfaceView);
        mRecordButton = (RecordButton) findViewById(R.id.RecorderButton);
        mRecordButton.setRecordingFinishListener(this);
        mVideoManage = VideoManage.getInstance();
        //        mVideoManage.setFileDir(); //这个方法设置录制文件的存放路径
        mRecordButton.setLongestRecordingTime(7f);//设置视频录制的最大时间默认为6f
        mVideoManage.setSurfaceHolder(mSurfaceView.getHolder());
    }

    @Override
    public void onRecordingFinish(float seconds, String fileName) {
        Intent mIntent = new Intent();
        mIntent.putExtra("fileName", fileName);
        setResult(MainActivity.PLAY_CEDE, mIntent);
        finish();
    }

    /**
     * 获取屏幕分辨率
     *
     * @return
     */
    private DisplayMetrics getWindowDisplay() {
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        return mDisplayMetrics;
    }
}
