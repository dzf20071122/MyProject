package com.research.control;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.research.R;
import com.research.action.AudioRecorderAction;
import com.research.action.AudioRecorderAction.RecorderListener;
import com.research.global.FeatureFunction;



public class ReaderImpl implements RecorderListener {

	private Context context;
	private Handler mhHandler;

	private ImageView mDialogBackground;
	private Dialog dialog;
	private AudioRecorderAction audioRecorder;
//	private static String mAudioName = "temp_audio.mp3";
	public static final String ACTION_RECORD_TOO_LONG = "com.teamchat.control.intent.action.ACTION_RECORD_TOO_LONG";
	public static final String ACTION_RECORD_AUTH_STOP = "com.teamchat.control.intent.action.ACTION_RECORD_AUTH_STOP";
	public boolean mIsStop = false;
	
	public ReaderImpl(Context context, Handler handler, AudioRecorderAction audioRecorder) {
		super();
		
		this.context = context;
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(ACTION_RECORD_TOO_LONG);
		filter.addAction(ACTION_RECORD_AUTH_STOP);
		context.registerReceiver(mReceiver, filter);
		this.mhHandler = handler;
		dialog = new Dialog(context, R.style.DialogPrompt);
		dialog.setContentView(R.layout.chat_voice_dialog);
		this.audioRecorder = audioRecorder;
		audioRecorder.setRecorderListener(this);
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(ACTION_RECORD_TOO_LONG)){
				mIsStop = true;
				cancelDg();
			}else if(intent.getAction().equals(ACTION_RECORD_AUTH_STOP)){
				mIsStop = true;
				dialog.cancel();
			}
		}
	};
	
	public void unregisterRecordReceiver(){
		context.unregisterReceiver(mReceiver);
	}

	@Override
	public void recording(final double amplitude) {
		
		mhHandler.post(new Runnable() {

			@Override
			public void run() {
				setDialogImg(amplitude);
			}
		});
	}

	@Override
	public void stop(String path) {
		
		Log.e("ReaderImpl", "stop");
		
		if(AudioRecorderAction.recordStart == AudioRecorderAction.RECOREDER_ON){
			return;
		}
		
		if (TextUtils.isEmpty(path)) {
			Toast.makeText(context, context.getString(R.string.record_time_too_short),
					Toast.LENGTH_SHORT).show();
			return;
		}
	}
	
	public Dialog getDialog(){
		return dialog;
	}

	@Override
	public void recordTime(float recordTime) {}
	
	// 显示录音时 dialog,并且开始录音
	public void showDg() {
		// 显示dialog
		dialog = new Dialog(context, R.style.DialogPrompt);
		dialog.setContentView(R.layout.chat_voice_dialog);
		mDialogBackground = (ImageView) dialog.findViewById(R.id.chat_voice);
		try {
			File file = new File(getAudioPath(context), getAudioName());
			File fileParent = file.getParentFile();
			if(!fileParent.exists()){
				fileParent.mkdirs();
			}
			audioRecorder.mIsStart = true;
			audioRecorder.startRecord(context, file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog.show();

	}

	// 录音结束,关闭dialog 效果
	public void cancelDg() {
		if(dialog.isShowing()){
			try {
				audioRecorder.mIsStart = false;
				audioRecorder.stopRecord();
			} catch (Exception e) {
				e.printStackTrace();
			}
			dialog.cancel();
		}
	}

	private void setDialogImg(double amplitude) {
		int index = 2333;
		if (0 <= amplitude && amplitude < index) {
			mDialogBackground.setImageResource(R.drawable.amp1);
		} else if (index <= amplitude && amplitude < index * 2) {
			mDialogBackground.setImageResource(R.drawable.amp2);
		} else if (index * 2 <= amplitude && amplitude < index * 3) {
			mDialogBackground.setImageResource(R.drawable.amp2);
		} else if (index * 3 <= amplitude && amplitude < index * 4) {
			mDialogBackground.setImageResource(R.drawable.amp3);
		} else if (index * 4 <= amplitude && amplitude < index * 5) {
			mDialogBackground.setImageResource(R.drawable.amp3);
		} else if (index * 5 <= amplitude && amplitude < index * 6) {
			mDialogBackground.setImageResource(R.drawable.amp4);
		} else if (index * 6 <= amplitude && amplitude < index * 7) {
			mDialogBackground.setImageResource(R.drawable.amp4);
		} else if (index * 7 <= amplitude && amplitude < index * 8) {
			mDialogBackground.setImageResource(R.drawable.amp5);
		} else if (index * 8 <= amplitude && amplitude < index * 9) {
			mDialogBackground.setImageResource(R.drawable.amp5);
		} else if (index * 9 <= amplitude && amplitude < index * 10) {
			mDialogBackground.setImageResource(R.drawable.amp6);
		} else if (index * 10 <= amplitude && amplitude < index * 11) {
			mDialogBackground.setImageResource(R.drawable.amp6);
		} else if (index * 11 <= amplitude && amplitude < index * 12) {
			mDialogBackground.setImageResource(R.drawable.amp7);
		}

	}

	@Override
	public void onStart() {
		
	}
	
	public float getReaderTime(){
		return audioRecorder.getRecordTime();
	}
	
	public static String getAudioName(){
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("'AUDIO'_yyyyMMdd_HHmmss");
		
		return format.format(date)+".mp3";
	}
	
	/**
	 * 获取外部内存路径.（SD卡不能使用时，会抛出Io异常）
	 * @param context
	 * @return
	 * 作者:fighter <br />
	 * 创建时间:2013-6-4<br />
	 * 修改时间:<br />
	 */
	public static File getAudioPath(Context context){
		File file = new File(FeatureFunction.getExternalCacheDir(context).getParentFile(), "voice");
		if(!file.exists()){
			file.mkdirs();
		}
		return file;
	}
}