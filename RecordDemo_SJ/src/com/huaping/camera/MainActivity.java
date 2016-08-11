package com.huaping.camera;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SurfaceHolder.Callback,OnClickListener{
	private Camera 				camera;
	private MediaRecorderUtils 	setMediaRecorder;
	private SurfaceView 		surfaceView;
	private MediaRecorder		mediaRecorder;
	private boolean 			isRecording=false;
	
	Button 		recordButton;
	Button 		playButton;
	TextView 	infoText;
	
	// 文件存储：文件夹名称
	String filefoldname = "epoint";
	// 文件存储路径
	String filepath;
	// 文件存储名称
	String filename     = "test.mp4";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainactiviy);
		
		surfaceView  = (SurfaceView)findViewById(R.id.surfaceView1);
		
		infoText	 = (TextView)findViewById(R.id.textView1);
		recordButton = (Button) findViewById(R.id.stop_recoder);
		playButton	 = (Button) findViewById(R.id.view_recoder);
		
		recordButton.setOnClickListener(this);
		playButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.stop_recoder:
			record();
			break;
		case R.id.view_recoder:
			play();
			break;
		default:
			break;
		}

	}
	
	// 视频录制
	private void record() {
		if (isRecording) {
			// 停止录制
			infoText.setText("点击开始录制...(正式版中无次对话框)");
			mediaRecorder.stop();
			mediaRecorder.reset();
			camera.lock();
			recordButton.setText("开始录制");
			isRecording = false;
		} else {
			// 开始录制
			infoText.setText("正在录制...(正式版中无次对话框)");
			try {
				filepath = Environment.getExternalStorageDirectory()+"/"+filefoldname;
				setMediaRecorder.initializeRecorder(filepath, filename);
			} catch (IOException e) {
				e.printStackTrace();
			}
			mediaRecorder = setMediaRecorder.getRecorder();
			mediaRecorder.start();
			recordButton.setText("停止录制");
			isRecording = true;
		}

	}
	
	// 播放视频
	private void play() {
		//查看录制结果Uri.parse()
		if(isRecording){
			Toast.makeText(getApplicationContext(), "请先停止录像！再查看！", Toast.LENGTH_SHORT).show();
			return ;
		}
		Uri data = Uri.fromFile(new File(filepath+"/"+filename)); 
		Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(data, "video/*");
        startActivity(intent);
	}

	
	

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		int camera_id 			= this.FindFrontCamera();
		this.camera				= Camera.open(camera_id);
		this.setMediaRecorder   = new MediaRecorderUtils(camera,camera_id,surfaceView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		this.camera.release();
		this.setMediaRecorder.getRecorder().release();
		this.isRecording=false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	

	// 获取前置摄像头
	private int FindFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); 
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                return camIdx;
            }
        }
        return -1;
    }

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d("andli","surfaceCreated");
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,int height) {
		Log.d("andli","surfaceChanged");
		try {
            camera.setPreviewDisplay(holder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d("andli","surfaceDestroyed");
	}
}
