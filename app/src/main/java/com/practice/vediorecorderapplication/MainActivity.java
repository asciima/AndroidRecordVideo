package com.practice.vediorecorderapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.practice.videorecordermodel.manage.MediaPlayerManage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int PLAY_CEDE = 1;

    private Button button;

    private String fileName;

    //存放文件路径
    private List<String> fileData;

    private ListView mListView;

    private MyAdapter myAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initListView();
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

    private void init() {

        //设置显示视频显示在SurfaceView上
        button = (Button) findViewById(R.id.press_to_recording);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordingActivity.class);
                startActivityForResult(intent, PLAY_CEDE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == PLAY_CEDE) {
            fileName = data.getStringExtra("fileName");
            fileData.add(fileName);
            myAdapter.notifyDataSetChanged();
         
        }

    }
}
