# AndroidRecordVideo
安卓，录制短视频

HOW TO USE  
  1.首先声明权限：
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.RECORD_AUDIO" />
    <uses-permission android:name="android.permission.CAMERA" />
  2.创建布局
   <?xml version="1.0" encoding="utf-8"?>
   <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.practice.vediorecorderapplication.RecordingActivity">

    <SurfaceView
        android:id="@+id/mySurfaceView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginBottom="100dp" />

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_alignParentBottom="true"
        android:background="#000000">

        <com.practice.videorecordermodel.view.RecordButton
            android:id="@+id/RecorderButton"
            android:layout_width="wrap_content"
            android:layout_height="100dp"
            android:layout_centerInParent="true"
            android:background="#000000"
            android:text="按住拍"
            android:textColor="#ffffff"
            android:textSize="20sp" />
    </RelativeLayout>

</RelativeLayout>
  3.
  

