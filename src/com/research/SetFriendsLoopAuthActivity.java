package com.research;

import com.research.DB.DBHelper;
import com.research.DB.UserTable;
import com.research.Entity.Login;
import com.research.Entity.LoginResult;
import com.research.Entity.ResearchJiaState;
import com.research.fragment.ContactsFragment;
import com.research.global.GlobalParam;
import com.research.global.ResearchCommon;
import com.research.map.BMapApiApp;
import com.research.net.ResearchException;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class SetFriendsLoopAuthActivity extends BaseActivity implements OnCheckedChangeListener {

	private ToggleButton mWatchMyLoopBtn,mWatchHeLoopBtn;

	private Login mLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.set_friends_loop_auth);
		mLogin = (Login) getIntent().getSerializableExtra("entity");
		initCompent();
	}

	private void initCompent(){
		setTitleContent(R.drawable.back_btn,0,R.string.set_friends_loop);
		mLeftBtn.setOnClickListener(this);
		mWatchMyLoopBtn = (ToggleButton)findViewById(R.id.tgll_no_watch_loop);
		mWatchHeLoopBtn= (ToggleButton)findViewById(R.id.tglloop);
		mWatchMyLoopBtn.setOnCheckedChangeListener(this);
		mWatchHeLoopBtn.setOnCheckedChangeListener(this);

		mWatchMyLoopBtn.setChecked(mLogin.fauth2 ==1?true:false);
		mWatchHeLoopBtn.setChecked(mLogin.fauth1 ==1?true:false);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.left_btn:
			SetFriendsLoopAuthActivity.this.finish();
			break;

		default:
			break;
		}
	}


	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.tgll_no_watch_loop://不让他看我的朋友圈
			if(isChecked && mLogin.fauth2==1){
				return;
			}
			if(!isChecked && mLogin.fauth2 == 0){
				return;
			}
			setFriendsLoopAuth(2);
			break;
		case R.id.tglloop://不看他的朋友圈
			if(isChecked && mLogin.fauth1==1){
				return;
			}
			if(!isChecked && mLogin.fauth1 == 0){
				return;
			}
			setFriendsLoopAuth(1);
			break;

		default:
			break;
		}
	}

	/**
	 * 设置朋友圈权限
	 * @param type true int  1-不看他（她）的朋友圈 2-不让他（她）看我的朋友圈
	 */
	private void setFriendsLoopAuth(final int type){
		if(!ResearchCommon.getNetWorkState()){
			mBaseHandler.sendEmptyMessage(BASE_MSG_NETWORK_ERROR);
			return;
		}
		new Thread(){
			public void run() {
				try {
					ResearchCommon.sendMsg(mBaseHandler, BASE_SHOW_PROGRESS_DIALOG, 
							mContext.getResources().getString(R.string.commit_dataing));
					ResearchJiaState state = ResearchCommon.getResearchInfo().setFriendCircleAuth(type,mLogin.uid);
					if(type == 1){
						ResearchCommon.sendMsg(mHandler,GlobalParam.MSG_CHECK_FRIENDS_LOOP_AUTH,state);
					}else if(type == 2){
						ResearchCommon.sendMsg(mHandler, GlobalParam.MSG_CHECK_STATE,state);
					}
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

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			case GlobalParam.MSG_LOAD_ERROR:
				hideProgressDialog();
				int excuteType = msg.arg1;
				String prompt = (String) msg.obj;
				if(prompt != null && !prompt.equals("")){
					Toast.makeText(mContext, prompt, Toast.LENGTH_LONG).show();
				}else {
					Toast.makeText(mContext,R.string.load_error,Toast.LENGTH_LONG).show();
				}

				break;
			case  GlobalParam.MSG_CHECK_STATE://不让其他用户查看登陆用户的朋友圈
				ResearchJiaState state = (ResearchJiaState)msg.obj;
				if(state == null || state.equals("")){
					Toast.makeText(mContext, R.string.load_error,Toast.LENGTH_LONG).show();
					return;
				}
				if(state.code == 0){
					if(mLogin.fauth2 == 0){
						mLogin.fauth2 = 1;
					}else if(mLogin.fauth2 == 1){
						mLogin.fauth2 = 0;
					}
					SQLiteDatabase db = DBHelper.getInstance(mContext).getWritableDatabase();
					UserTable table = new UserTable(db);
					table.update(mLogin);
					Intent intent = new Intent();
					intent.putExtra("entity", mLogin);
					setResult(2,intent);
				}else{
					Toast.makeText(mContext, state.errorMsg,Toast.LENGTH_LONG).show();
				}
				break;
			case GlobalParam.MSG_CHECK_FRIENDS_LOOP_AUTH://登陆用户不看其他用户的朋友圈
				ResearchJiaState loopState = (ResearchJiaState)msg.obj;
				if(loopState == null || loopState.equals("")){
					Toast.makeText(mContext, R.string.load_error,Toast.LENGTH_LONG).show();
					return;
				}
				if(loopState.code == 0){
					if(mLogin.fauth1 == 0){
						mLogin.fauth1 = 1;
					}else if(mLogin.fauth1 == 1){
						mLogin.fauth1 = 0;
					}
					SQLiteDatabase db = DBHelper.getInstance(mContext).getWritableDatabase();
					UserTable table = new UserTable(db);
					table.update(mLogin);
					Intent intent = new Intent();
					intent.putExtra("entity", mLogin);
					setResult(2,intent);
				}else{
					Toast.makeText(mContext, loopState.errorMsg,Toast.LENGTH_LONG).show();
				}
				break;
		
			}
		}
	};




}
