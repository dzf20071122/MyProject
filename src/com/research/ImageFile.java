package com.research;


import com.research.Entity.Login;
import com.research.adapter.FolderAdapter;
import com.research.util.Bimp;
import com.research.util.PublicWay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;


/**
 * 这个类主要是用来进行显示包含图片的文件夹
 *
 * @author king
 * @version 2014年10月18日  下午11:48:06
 */
public class ImageFile extends Activity {

	private FolderAdapter folderAdapter;
	private Button bt_cancel;
	private Context mContext;
	public static Login fCustomerVo;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.plugin_camera_image_file);
		PublicWay.activityList.add(this);
		mContext = this;
		FolderAdapter.activity = this;
		fCustomerVo = (Login)getIntent().getSerializableExtra("data");
		Log.i("Runt", "fCustomerVo:"+fCustomerVo);
		bt_cancel = (Button) findViewById(R.id.cancel);
		bt_cancel.setOnClickListener(new CancelListener());
		GridView gridView = (GridView) findViewById(R.id.fileGridView);
		TextView textView = (TextView) findViewById(R.id.headerTitle);
		textView.setText(R.string.photo);
		folderAdapter = new FolderAdapter(this);
		gridView.setAdapter(folderAdapter);
	}

	private class CancelListener implements OnClickListener {// 取消按钮的监听
		public void onClick(View v) {
			//清空选择的图片
			Bimp.tempSelectBitmap.clear();
			/*Intent intent = new Intent();
			intent.setClass(mContext, ChatMainActivity.class);
			intent.putExtra("data", fCustomerVo);
			startActivity(intent);*/
			finish();
		}
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/*Intent intent = new Intent();
			intent.setClass(mContext, ChatMainActivity.class);
			intent.putExtra("data", fCustomerVo);
			startActivity(intent);*/
			finish();
		}
		
		return true;
	}

}
