package com.practice.vediorecorderapplication;

import android.content.Intent;
import android.media.CamcorderProfile;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.practice.videorecordermodel.manage.MediaPlayerManage;
import com.practice.videorecordermodel.manage.ScreenRecorder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public static final int PLAY_CEDE = 1;

    private static final int SCREEN_CODE = 2;

    private Button startRecordView, startRecordScreen;

    private String fileName;

    //存放文件路径
    private List<String> fileData;

    private ListView mListView;

    private MyAdapter myAdapter;

    //屏幕录制工具（android5.0推出的录制屏幕的工具）
    private MediaProjectionManager mMediaProjectionManager;

    private ScreenRecorder mScreenRecorder;

    private String mCurrentScreenFileName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecordView();
        initScreenRecorder();
        initListView();
    }

    /**
     * 初始化屏幕录制工具及按钮
     */
    private void initScreenRecorder() {
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
        startRecordScreen = (Button) findViewById(R.id.press_to_recording_screen);
        startRecordScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScreenRecorder != null) {
                    //录制屏幕结束
                    if (mCurrentScreenFileName != null && !mCurrentScreenFileName.isEmpty()) {
                        fileName = mCurrentScreenFileName;
                        fileData.add(fileName);
                        myAdapter.notifyDataSetChanged();
                    }
                    mScreenRecorder.quit();
                    mScreenRecorder = null;
                    startRecordScreen.setText("开始录制屏幕");
                } else {
                    Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
                    startActivityForResult(captureIntent, SCREEN_CODE);
                }
            }
        });
    }

    private void initListView() {
        mListView = (ListView) findViewById(R.id.myListView);
        fileData = new ArrayList<>();
        myAdapter = new MyAdapter(fileData, this);
        mListView.setAdapter(myAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaPlayerManage mediaPlayerManage = new MediaPlayerManage();
                Bundle bundle = new Bundle();
                bundle.putString("filePath", fileData.get(position));
                mediaPlayerManage.setArguments(bundle);
                mediaPlayerManage.show(getFragmentManager(), "mediaPlayerDialog");
            }
        });
    }

    /**
     * 初始化录制视频按钮
     */
    private void initRecordView() {
        //设置显示视频显示在SurfaceView上
        startRecordView = (Button) findViewById(R.id.press_to_recording);
        startRecordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
                startActivityForResult(intent, PLAY_CEDE);
            }
        });
    }


    /**
     * 录制完成的回掉
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLAY_CEDE) {                  //录制视频完成的回掉
            fileName = data.getStringExtra("fileName");
            fileData.add(fileName);
            myAdapter.notifyDataSetChanged();
        } else if (requestCode == SCREEN_CODE) {        //录制屏幕的回掉
            MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            if (mediaProjection == null) {
                Log.e("@@", "media projection is null");
                return;
            }
            // 录制视频尺寸
            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            final int width = profile.videoFrameHeight;
            final int height = profile.videoFrameWidth;
            File file = createMFile();
            final int bitrate = 6000000;
            mScreenRecorder = new ScreenRecorder(width, height, bitrate, 1, mediaProjection, file.getAbsolutePath());
            mScreenRecorder.start();
            startRecordScreen.setText("停止录制");
//            Toast.makeText(this, "Screen recorder is running...", Toast.LENGTH_SHORT).show();
            //后台运行
            moveTaskToBack(true);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mScreenRecorder != null) {
            mScreenRecorder.quit();
            mScreenRecorder = null;
        }
    }

    /**
     * 生成随机文件
     *
     * @return
     */
    private File createMFile() {
        //设置文件存放目录
        String fileDir = Environment.getExternalStorageDirectory() + "/videoRecorder";
        File dir = new File(fileDir);
        //判断文件夹是否存在 如不存在就创建
        if (!dir.exists()) {
            dir.mkdir();
        }
        String fileName = generateFileName();
        File mFile = new File(dir, fileName);
        mCurrentScreenFileName = mFile.getAbsolutePath();
        return mFile;
    }

    /**
     * 随机产生文件名
     *
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".mp4";
    }
}
