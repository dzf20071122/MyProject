package com.research;

import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.research.Entity.FriendsLoopItem;
import com.research.Entity.ResearchJiaState;
import com.research.global.GlobalParam;
import com.research.global.ResearchCommon;
import com.research.net.ResearchException;

/**
 * 相册详情评论页
 * @author dongli
 *
 */
public class AlbumCommentActivity extends BaseActivity{

	/**
	 * 定义全局变量
	 */
	private EditText mCommentEdit;

	private String mInputComment;
	private FriendsLoopItem mEntity;

	/*
	 * 导入控件
	 * (non-Javadoc)
	 * @see com.research.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.album_comment_view);
		mEntity = (FriendsLoopItem)getIntent().getSerializableExtra("item");
		initCompent();
	}

	/*
	 * 实例化控件
	 */
	private void initCompent(){
		setTitleContent(R.drawable.back_btn,R.drawable.send_map_btn,R.string.comment);
		mLeftBtn.setOnClickListener(this);
		mRightBtn.setOnClickListener(this);
		mCommentEdit = (EditText)findViewById(R.id.content);
	}

	/*
	 *实现评论功能
	 */
	private void comment(){
		if (!ResearchCommon.getNetWorkState()) {
			mBaseHandler.sendEmptyMessage(BASE_MSG_NETWORK_ERROR);
			return;
		}
		new Thread(){
			public void run() {
				try {

					ResearchCommon.sendMsg(mBaseHandler,BASE_SHOW_PROGRESS_DIALOG,
							mContext.getResources().getString(R.string.send_request));
					ResearchJiaState status = ResearchCommon.getResearchInfo().shareReply(mEntity.id,mEntity.uid,mInputComment);
					ResearchCommon.sendMsg(mHandler, GlobalParam.MSG_COMMENT_STATUS,status);
					mBaseHandler.sendEmptyMessage(BASE_HIDE_PROGRESS_DIALOG);
				} catch (ResearchException e) {
					e.printStackTrace();
					ResearchCommon.sendMsg(mBaseHandler, BASE_MSG_TIMEOUT_ERROR,
							mContext.getResources().getString(e.getStatusCode()));
				}catch (Exception e) {
					e.printStackTrace();
					mBaseHandler.sendEmptyMessage(BASE_HIDE_PROGRESS_DIALOG);
				}
			};
		}.start();
	}


	/*
	 * 处理消息
	 */
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GlobalParam.MSG_COMMENT_STATUS:
				ResearchJiaState status = (ResearchJiaState)msg.obj;
				if(status == null ){
					Toast.makeText(mContext, R.string.commit_data_error,Toast.LENGTH_LONG).show();
					return;
				}
				if(status.code!=0){
					Toast.makeText(mContext, status.errorMsg,Toast.LENGTH_LONG).show();
					return;
				}
				setResult(RESULT_OK);
				sendBroadcast(new Intent(FriensLoopActivity.MSG_REFRESH_MOVIINF));
				sendBroadcast(new Intent(GlobalParam.ACTION_REFRESH_MOVING_DETAIL));
				AlbumCommentActivity.this.finish();
				break;

			default:
				break;
			}
		}
		
	};
	
	
	/*
	 * 按钮点击事件
	 * (non-Javadoc)
	 * @see com.research.BaseActivity#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.left_btn:
			AlbumCommentActivity.this.finish();
			break;
		case R.id.right_btn:
			mInputComment = mCommentEdit.getText().toString();
			if(mInputComment == null || mInputComment.equals("")){
				Toast.makeText(mContext, R.string.please_write_commit, Toast.LENGTH_LONG).show();
				return;
			}
			comment();
			break;
		default:
			break;
		}
	}


}
