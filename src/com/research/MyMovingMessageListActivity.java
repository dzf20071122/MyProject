package com.research;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.research.Entity.NotifiyVo;
import com.research.adapter.MessageMovingAdapter;
import com.research.global.GlobalParam;
import com.research.global.ResearchCommon;

/**
 * 我的相册-消息列表
 * @author dongli
 *
 */
public class MyMovingMessageListActivity extends BaseActivity implements OnItemClickListener{

	private ListView mListView;
	private List<NotifiyVo> mListData = new ArrayList<NotifiyVo>();
	private MessageMovingAdapter mAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.moving_list_view);
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalParam.ACTION_REFRESH_MYALBUM_MESSAGE);
		registerReceiver(mReceiver, filter);
		initcompent();
	}
	
	BroadcastReceiver mReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent!=null){
				String action = intent.getAction();
				if(action.equals(GlobalParam.ACTION_REFRESH_MYALBUM_MESSAGE)){
					setListData();
				}
			}
		}
	};
	
	private void initcompent(){
		setRightTextTitleContent(R.drawable.back_btn,R.string.clear, R.string.message);
		mLeftBtn.setOnClickListener(this);
		mRightTextBtn.setOnClickListener(this);
		
		mListView = (ListView)findViewById(R.id.list);
		mListView.setDivider(getResources().getDrawable(R.drawable.splite));
		mListView.setCacheColorHint(0);
		mListView.setSelector(mContext.getResources().getDrawable(R.drawable.transparent_selector));
		mListView.setOnItemClickListener(this);
		setListData();
	}
	
	/*
	 * 设置消息列表
	 */
	private void setListData(){
		if(mListData ==null){
			mListData = new ArrayList<NotifiyVo>();
		}
		if(mListData!=null && mListData.size()>0){
			mListData.clear();
		}
		if(ResearchCommon.getMovingResult(mContext)!=null &&
				ResearchCommon.getMovingResult(mContext).size()>0){
			mListData.addAll( ResearchCommon.getMovingResult(mContext));
		}
		
		if(mAdapter!=null){
			mAdapter.notifyDataSetChanged();
		}else{
			if(mListData!=null && mListData.size()>0){
				mAdapter = new MessageMovingAdapter(mContext, mListData);
				mListView.setAdapter(mAdapter);
			}
		}
		
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.left_btn:
			MyMovingMessageListActivity.this.finish();
			break;
		case R.id.right_text_btn:
			ResearchCommon.saveMoving(mContext, null);
			setListData();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if( 0<=arg2 && arg2<mListData.size()){
			Intent detailIntent = new Intent();
			detailIntent.setClass(mContext, FriendsLoopDetailActivity.class);
			detailIntent.putExtra("shareId",mListData.get(arg2).shareId);
			startActivity(detailIntent);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
	
	

	
	
}
