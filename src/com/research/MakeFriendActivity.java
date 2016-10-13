package com.research;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.llc.xlistview.XListView.IXListViewListener;
import com.research.R;
import com.research.Entity.ResearchJiaState;
import com.research.Entity.Login;
import com.research.Entity.UserList;
import com.research.adapter.BlockListAdapter;
import com.research.adapter.MakeFriendAdapter;
import com.research.adapter.SchoolMeetingMemberAdapter;
import com.research.fragment.ContactsFragment;
import com.research.global.GlobalParam;
import com.research.global.GlobleType;
import com.research.global.ResearchCommon;
import com.research.map.BMapApiApp;
import com.research.net.ResearchException;
import com.research.widget.SelectCreatPopupWindow;
import com.research.widget.SelectMakeFriendPopupWindow;

/**
 * 交友墙页面
 * 
 * @author dzf
 * 
 */
public class MakeFriendActivity extends BaseActivity implements
		OnClickListener, IXListViewListener {
	private ViewPager mPager;
	private List<View> listViews;
	private ImageView cursor;
	private TextView t1, t2, t3;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private ArrayList<Login> arrayList = new ArrayList<Login>();
	private int start = 0;
	private ArrayList<MakeFriendAdapter> pageAdapters = new ArrayList<MakeFriendAdapter>();
	private com.llc.xlistview.XListView currListview;
	private String refreshTime;

	private SelectMakeFriendPopupWindow menuWindow2; // 弹出框
	private RelativeLayout mTitleLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_friend_layout);
		mContext = this;

		refreshTime = getLastUpdateTime();

		InitImageView();
		InitTextView();
		InitViewPager();
		
		mTitleLayout = (RelativeLayout)findViewById(R.id.title_layout);
		setTitleContent(R.drawable.back_btn, R.drawable.search_icon_btn,
				R.string.make_friend_title_name);
		mLeftBtn.setOnClickListener(this);
		mRightBtn.setOnClickListener(this);

	}

	private void InitTextView() {
		t1 = (TextView) findViewById(R.id.text1);
		t2 = (TextView) findViewById(R.id.text2);
		t3 = (TextView) findViewById(R.id.text3);

		t1.setOnClickListener(this);
		t2.setOnClickListener(this);
		t3.setOnClickListener(this);
	}

	/*
	 * 处理消息
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {

			}
		}
	};

	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		View view1 = mInflater.inflate(R.layout.make_friend_list_item, null);
		View view2 = mInflater.inflate(R.layout.make_friend_list_item, null);
		View view3 = mInflater.inflate(R.layout.make_friend_list_item, null);

		listViews.add(view1);
		listViews.add(view2);
		listViews.add(view3);

		com.llc.xlistview.XListView listView1 = (com.llc.xlistview.XListView) view1
				.findViewById(R.id.contact_list);
		com.llc.xlistview.XListView listView2 = (com.llc.xlistview.XListView) view2
				.findViewById(R.id.contact_list);
		com.llc.xlistview.XListView listView3 = (com.llc.xlistview.XListView) view3
				.findViewById(R.id.contact_list);

		listView1.setRefreshTime(refreshTime);
		listView1.setRefreshTime(refreshTime);
		listView1.setRefreshTime(refreshTime);

		geneItems();

		MakeFriendAdapter simpleAdapter_Shu1 = new MakeFriendAdapter(mContext,
				arrayList, GlobleType.ADAPTER_FROM_MAKEFRIEND_TYPE);
		// new SimpleAdapter(this, arrayList,
		// R.layout.heard_name_item,
		// new String[]{"image", "userName"},
		// new int[]{R.id.head_img, R.id.user_name});
		MakeFriendAdapter simpleAdapter_Shu2 = new MakeFriendAdapter(mContext,
				arrayList, GlobleType.ADAPTER_FROM_MAKEFRIEND_TYPE);
		// new SimpleAdapter(this, arrayList,
		// R.layout.heard_name_item,
		// new String[]{"image", "userName"},
		// new int[]{R.id.head_img, R.id.user_name});
		MakeFriendAdapter simpleAdapter_Shu3 = new MakeFriendAdapter(mContext,
				arrayList, GlobleType.ADAPTER_FROM_MAKEFRIEND_TYPE);
		// new SimpleAdapter(this, arrayList,
		// R.layout.heard_name_item,
		// new String[]{"image", "userName"},
		// new int[]{R.id.head_img, R.id.user_name});

		pageAdapters.add(simpleAdapter_Shu1);
		pageAdapters.add(simpleAdapter_Shu2);
		pageAdapters.add(simpleAdapter_Shu3);

		listView1.setAdapter(simpleAdapter_Shu1);
		listView2.setAdapter(simpleAdapter_Shu2);
		listView3.setAdapter(simpleAdapter_Shu3);

		listView1.setPullLoadEnable(true);
		listView1.setXListViewListener(this);

		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.a)
				.getWidth();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;
		offset = (screenW / 3 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;
		int two = one * 2;

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void uploadImage2(final Activity context, View view) {
		if (menuWindow2 != null && menuWindow2.isShowing()) {
			menuWindow2.dismiss();
			menuWindow2 = null;
		}
		menuWindow2 = new SelectMakeFriendPopupWindow(MakeFriendActivity.this,
				itemsOnClick2);
		// 显示窗口

		// 计算坐标的偏移量
		int xoffInPixels = menuWindow2.getWidth() - view.getWidth() + 10;
		menuWindow2.showAsDropDown(view, -xoffInPixels, 0);
	}

	// 为弹出窗口实现监听类
	private OnClickListener itemsOnClick2 = new OnClickListener() {

		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.school_friend_layout:
				getFriendItems(1);
				MakeFriendAdapter mAdapter = pageAdapters.get(currIndex);
				mAdapter.notifyDataSetChanged();
				break;
			case R.id.county_friend_layout:
				getFriendItems(2);
				MakeFriendAdapter mAdapter2 = pageAdapters.get(currIndex);
				mAdapter2.notifyDataSetChanged();
				break;
			case R.id.fujin_friend_layout:
				getFriendItems(3);
				MakeFriendAdapter mAdapter3 = pageAdapters.get(currIndex);
				mAdapter3.notifyDataSetChanged();
				break;
			case R.id.every_friend_layout:
				getFriendItems(0);
				MakeFriendAdapter mAdapter4 = pageAdapters.get(currIndex);
				mAdapter4.notifyDataSetChanged();
				break;
			default:
				break;
			}
			if (menuWindow2 != null && menuWindow2.isShowing()) {
				menuWindow2.dismiss();
				menuWindow2 = null;
			}
		}
	};

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.left_btn:
			MakeFriendActivity.this.finish();
			break;
		case R.id.right_btn:
			uploadImage2(MakeFriendActivity.this, mTitleLayout);
			break;
		case R.id.text1:
			mPager.setCurrentItem(0);
			break;
		case R.id.text2:
			mPager.setCurrentItem(1);
			break;
		case R.id.text3:
			mPager.setCurrentItem(2);
			break;
		default:
			break;
		}
	}
	/**
	 * 获得校友信息
	 */
	private void getFriendItems(int type) {
		arrayList.clear();
		for (int i = 0; i < 10; i++) {
			Login aa = new Login();
			aa.nickname = "张三" + (start++);
			aa.gender = 1;
			aa.old = 24;
			aa.makeFriendXuanyan = "同一个学校的朋友吧！！！！！";
			Login mLogin = ResearchCommon.getLoginResult(mContext);
			mLogin.old = 24;
			mLogin.makeFriendXuanyan = "同一个学校的朋友吧！！！！！" + type;
			arrayList.add(mLogin);

		}
	}
	
	private void geneItems() {
		for (int i = 0; i < 10; i++) {
			Login aa = new Login();
			aa.nickname = "张三" + (start++);
			aa.gender = 1;
			aa.old = 24;
			aa.makeFriendXuanyan = "交个朋友吧！！！！！";
			Login mLogin = ResearchCommon.getLoginResult(mContext);
			mLogin.old = 24;
			mLogin.makeFriendXuanyan = "交个朋友吧！！！！！";
			arrayList.add(mLogin);

		}
	}
	
	private void updateDateList(){
		
	}

	/**
	 * 获得系统的最新时间
	 * 
	 * @return
	 */
	private String getLastUpdateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(System.currentTimeMillis());
	}

	private void onLoad() {
		currListview = (com.llc.xlistview.XListView) listViews.get(currIndex)
				.findViewById(R.id.contact_list);

		currListview.stopRefresh();
		currListview.stopLoadMore();
		// currListview.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				start = 0;
				arrayList.clear();
				geneItems();
				MakeFriendAdapter aa = new MakeFriendAdapter(mContext,
						arrayList, GlobleType.ADAPTER_FROM_MAKEFRIEND_TYPE);
				// new SimpleAdapter(MakeFriendActivity.this, arrayList,
				// R.layout.heard_name_item,
				// new String[]{"image", "userName"},
				// new int[]{R.id.head_img, R.id.user_name});
				pageAdapters.set(currIndex, aa);
				currListview = (com.llc.xlistview.XListView) listViews.get(
						currIndex).findViewById(R.id.contact_list);
				currListview.setAdapter(aa);

				currListview.setRefreshTime(refreshTime);
				refreshTime = getLastUpdateTime();

				onLoad();
			}
		}, 2000);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				geneItems();
				MakeFriendAdapter mAdapter = pageAdapters.get(currIndex);
				mAdapter.notifyDataSetChanged();
				onLoad();
			}
		}, 2000);
	}
}
