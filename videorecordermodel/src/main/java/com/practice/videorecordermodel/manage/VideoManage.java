package com.practice.videorecordermodel.manage;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Environment;
import android.view.SurfaceHolder;

import java.io.File;
import java.util.UUID;

/**
 * 〈录制视频管理〉
 * 〈功能详细描述〉
 *
 * @author XLC
 * @version [1.0, 2016/2/23]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class VideoManage implements MediaRecorder.OnErrorListener, MediaRecorder.OnInfoListener {

    private MediaRecorder mMediaRecorder;

    private static VideoManage mInstance;

    private String fileDir = Environment.getExternalStorageDirectory() + "/videoRecorder";
    ; //录制文件存放的文件夹路径

    private String mCurrentFileName = ""; //当前录制文件的路径

    private boolean isPrepared = false;

    private SurfaceHolder mSurfaceHolder;

    private Camera mCamera;

    //录制准备好了的回掉接口
    private videoPreparedListener mListener;

    @Override
    public void onInfo(MediaRecorder mr, int what, int extra) {

    }

    /**
     * 设置文件保存路径
     *
     * @param fileDir
     */
    public void setFileDir(String fileDir) {
        this.fileDir = fileDir;
    }


    public interface videoPreparedListener {
        void wellPrepared();
    }

    public void setVideoPreparedListener(videoPreparedListener listener) {
        mListener = listener;
    }

    private VideoManage() {
    }

    public static VideoManage getInstance() {
        if (mInstance == null) {
            synchronized (VideoManage.class) {
                mInstance = new VideoManage();
            }
        }
        return mInstance;
    }

    public void setSurfaceHolder(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        if (mSurfaceHolder != null) {
            initMSurfaceHolder();
        }
    }

    private SurfaceHolder.Callback mCallBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                initCamera();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            releaseCameraResource();
        }
    };

    private void initMSurfaceHolder() {
        mSurfaceHolder.addCallback(mCallBack);
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void prepareVideo() {
        try {
            isPrepared = false;
            File dir = new File(fileDir);
            //判断文件夹是否存在 如不存在就创建
            if (!dir.exists()) {
                dir.mkdir();
            }
            String fileName = generateFileName();

            File mFile = new File(dir, fileName);
            mCurrentFileName = mFile.getAbsolutePath();

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.reset();
            if (mCamera == null) {
                initCamera();
            }
            //取消录制后再次进行录制时必须加如下两步操作，不然会报错
            mCamera.lock();
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
            mMediaRecorder.setOnInfoListener(this);
            mMediaRecorder.setOnErrorListener(this);
            mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 摄像头为视频源
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 设置麦克风为音频源
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式为MP4
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频格式

            //根据屏幕分辨率设置录制尺寸
            CamcorderProfile profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
            mMediaRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);

            mMediaRecorder.setVideoFrameRate(30); // 视频帧频率,显著提高录像时的流畅度

            mMediaRecorder.setVideoEncodingBitRate(profile.videoFrameWidth * profile.videoFrameHeight);// 设置帧频率，然后就清晰了
            mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
            mMediaRecorder.setOutputFile(mFile.getAbsolutePath());

            mMediaRecorder.prepare();
            isPrepared = true;
            mMediaRecorder.start();
            if (mListener != null) {
                mListener.wellPrepared();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化摄像头
     */
    private void initCamera() {
        releaseCameraResource();
        try {
            mCamera = Camera.open();
            // setCameraParams();
            mCamera.setDisplayOrientation(90);
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            mCamera.unlock();
        } catch (Exception e) {
            e.printStackTrace();
            releaseCameraResource();
        }
        if (mCamera == null)
            return;


    }

    /**
     * 释放摄像头资源
     */
    private void releaseCameraResource() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    //录制出错处理
    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        if (mr != null) {
            mr.reset();
        }
    }

    /**
     * 随机产生文件名
     *
     * @return
     */
    private String generateFileName() {
        return UUID.randomUUID().toString() + ".mp4";
    }

    /**
     * 释放资源
     */
    public void release() {
        try {
//            releaseCameraResource();
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMediaRecorder = null;
    }

    /**
     * 删除刚刚录制的文件并释放资源
     */
    public void cancel() {
        release();
        File file = new File(mCurrentFileName);
        file.delete();
//        initCamera();
        mCurrentFileName = null;
    }

    /**
     * 获取当前录制文件的绝对路径
     *
     * @return
     */
    public String getCurrentFileName() {
        return mCurrentFileName;
    }

    /**
     * 获取当前录音准备状态
     *
     * @return
     */
    public boolean getPrepared() {
        return isPrepared;
    }
}
