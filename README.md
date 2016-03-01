# AndroidRecordVideo
# 安卓，录制短视频

# USAGE
# 1.首先声明权限：
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.CAMERA" />
# 2.添加SufaceView和自定义Button

 <SurfaceView
        android:id="@+id/mySurfaceView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginBottom="100dp" />

  <com.practice.videorecordermodel.view.RecordButton
            android:id="@+id/RecorderButton"
            android:layout_width="wrap_content"
            android:layout_height="100dp"
            android:layout_centerInParent="true"
            android:background="#000000"
            android:text="按住拍"
            android:textColor="#ffffff"
            android:textSize="20sp" />
            
# 3.初始化这些控件

        mSurfaceView = (SurfaceView) findViewById(R.id.mySurfaceView);
        mRecordButton = (RecordButton) findViewById(R.id.RecorderButton);
        mRecordButton.setRecordingFinishListener(this);
        mVideoManage = VideoManage.getInstance();
        //        mVideoManage.setFileDir(); //这个方法设置录制文件的存放路径
        mRecordButton.setLongestRecordingTime(7f);//设置视频录制的最大时间默认为6f
        mVideoManage.setSurfaceHolder(mSurfaceView.getHolder());
  

