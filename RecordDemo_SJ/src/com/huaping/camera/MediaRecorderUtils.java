package com.huaping.camera;

import java.io.File;
import java.io.IOException;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.view.SurfaceView;

public class MediaRecorderUtils {
	private MediaRecorder 	recorder;
	private SurfaceView 	surfaceView;
	private int 			R_ID_Camera;
	private Camera 			camera;

	public MediaRecorderUtils(Camera camera, int R_ID_Camera,SurfaceView surfaceView) {
		this.camera = camera;
		this.R_ID_Camera = R_ID_Camera;
		this.surfaceView = surfaceView;
		this.recorder = new MediaRecorder();
	}
	// 参数一：文件存储路径 /sdcard/epoint
	// 参数二：文件存储名称 test
	public void initializeRecorder(String outputFileName, String mediaName) throws IllegalStateException, IOException {
		
		camera.unlock();
		recorder.setCamera(camera);
		//摄像头旁边的麦克风
		recorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		CamcorderProfile camcorderProfile = CamcorderProfile.get(R_ID_Camera,CamcorderProfile.QUALITY_HIGH);
		camcorderProfile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;
		recorder.setProfile(camcorderProfile);
		File file = new File(outputFileName);
		if(!file.exists()){
			file.mkdir();
		}
		recorder.setOutputFile(file.getAbsolutePath()+"/"+mediaName);
		recorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
		recorder.prepare();
	}

	
	public MediaRecorder getRecorder() {
		return recorder;
	}

}
