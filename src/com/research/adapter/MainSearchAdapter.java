package com.research.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.research.R;
import com.research.Entity.MainSearchEntity;
import com.research.global.FeatureFunction;
import com.research.global.ImageLoader;
import com.research.global.ResearchCommon;

/**
 * 主界面搜索adapter
 * @author dongli
 *
 */
public class MainSearchAdapter extends BaseAdapter implements SectionIndexer{
	private List<MainSearchEntity> list = null;
	private Context mContext;
	private ImageLoader mImageLoader;

	public MainSearchAdapter(Context mContext, List<MainSearchEntity> list) {
		this.mContext = mContext;
		this.list = list;
		this.mImageLoader = new ImageLoader();
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param list
	 */
	public void updateListView(List<MainSearchEntity> list){
		this.list = list;
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup arg2) {
		ViewHolder holder = null;
		final MainSearchEntity mContent = list.get(position);
		if (convertView == null || ((ViewHolder) convertView.getTag()).mTag != position) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
			holder.mHeadrIcon = (ImageView)convertView.findViewById(R.id.headerimage);
			holder.mNameTextView = (TextView)convertView.findViewById(R.id.username);

			holder.index = (TextView) convertView.findViewById(R.id.sortKey);
			holder.indexLayout = (RelativeLayout)convertView.findViewById(R.id.grouplayout);
			holder.mContentSplite = (ImageView)convertView.findViewById(R.id.content_splite);
			holder.mSignTextView = (TextView)convertView.findViewById(R.id.prompt);
			holder.newFriendsIcon= (TextView)convertView.findViewById(R.id.new_notify);
			holder.mGroupHeaderLayout = (LinearLayout)convertView.findViewById(R.id.group_header);
			//holder.contactLayout = (LinearLayout)convertView.findViewById(R.id.contact_layout);//select_contact_splite

			holder.mTag = position;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			holder.indexLayout.setVisibility(View.VISIBLE);
			holder.index.setVisibility(View.VISIBLE);
			holder.mContentSplite.setVisibility(View.VISIBLE);

			if(mContent.sortKey == null || mContent.sortKey.equals("")){
				holder.indexLayout.setVisibility(View.GONE);
				holder.index.setVisibility(View.GONE);
			}else{
				holder.index.setText(mContent.sortKey);
			}
		}else{
			holder.indexLayout.setVisibility(View.GONE);
			holder.index.setVisibility(View.GONE);
			holder.mContentSplite.setVisibility(View.VISIBLE);
		}
		int searchLength = mContent.searchContent.length();
		if(mContent.olwerModle == 1 || mContent.olwerModle == 3){//通讯录
			String name = mContent.remarkname;
			if(name == null || name.equals("")){
				name = mContent.nickname;
			}else {
				if(!name.contains(mContent.searchContent)){
					name = mContent.nickname;
				}
			}
			if(name!=null && !name.equals("")){
				List<Integer> location = getSubString(name, mContent.searchContent);
				if(location!=null && location.size()>0){
					SpannableStringBuilder mTrendNameSpan = new SpannableStringBuilder(name);
					for (int i = 0; i < location.size(); i++) {
						mTrendNameSpan.setSpan(new ForegroundColorSpan(Color.rgb(75, 163, 0)), location.get(i), location.get(i)+searchLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}
					holder.mNameTextView.setText(mTrendNameSpan);
				}
			}
			holder.mGroupHeaderLayout.setVisibility(View.GONE);
			holder.mHeadrIcon.setVisibility(View.VISIBLE);
			if(mContent.headSmall!=null && !mContent.headSmall.equals("")){
				holder.mHeadrIcon.setTag(mContent.headSmall);
				mImageLoader.getBitmap(mContext, holder.mHeadrIcon,null,mContent.headSmall,0,false,true);
			}
		}else if(mContent.olwerModle == 2){//聊天记录
		
			holder.mNameTextView.setText(mContent.nickname);
			if(mContent.content!=null && !mContent.content.equals("")){
				holder.mSignTextView.setVisibility(View.VISIBLE);

				List<Integer> location = getSubString(mContent.content,mContent.searchContent);
				if(location!=null && location.size()>0){
					SpannableStringBuilder mTrendNameSpan = new SpannableStringBuilder(mContent.content);
					for (int i = 0; i < location.size(); i++) {
						mTrendNameSpan.setSpan(new ForegroundColorSpan(Color.rgb(75, 163, 0)), location.get(i), location.get(i)+searchLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
					}
					holder.mSignTextView.setText(mTrendNameSpan);
				}
			}
		
			String[] headUrlArray;
			if(mContent.headSmall!=null && !mContent.headSmall.equals("")){
				headUrlArray = mContent.headSmall.split(",");
			}else{
				headUrlArray = new String[]{ResearchCommon.getLoginResult(mContext).headsmall};
			}


			String showGrouName="";
			List<String> headUrlList = new ArrayList<String>();

			if(headUrlArray != null && headUrlArray.length!= 0){
				int count = 4;
				if(headUrlArray.length < 4){
					count = headUrlArray.length;
				}

				//String name = "";


				if(holder.mGroupHeaderLayout.getChildCount() != 0){
					holder.mGroupHeaderLayout.removeAllViews();
				}

				if(count == 1){
					holder.mGroupHeaderLayout.setVisibility(View.GONE);
					holder.mHeadrIcon.setVisibility(View.VISIBLE);
					mImageLoader.getBitmap(mContext, holder.mHeadrIcon, null, headUrlArray[0], 0, false, true);
					headUrlList.add(headUrlArray[0]);
				}else {
					holder.mGroupHeaderLayout.setVisibility(View.VISIBLE);
					holder.mHeadrIcon.setVisibility(View.GONE);

					boolean single = count % 2 == 0 ? false : true;
					int row = !single ? count / 2 : count / 2 + 1;
					for (int i = 0; i < row; i++) {
						LinearLayout outLayout = new LinearLayout(mContext);
						outLayout.setOrientation(LinearLayout.HORIZONTAL);
						int width = FeatureFunction.dip2px(mContext, 23);
						outLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, width));
						int padding = FeatureFunction.dip2px(mContext, 1);
						if(single && i == 0){
							LinearLayout layout = new LinearLayout(mContext);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
							layout.setPadding(padding, padding, padding, padding);
							layout.setLayoutParams(params);
							ImageView imageView = new ImageView(mContext);
							imageView.setImageResource(R.drawable.contact_default_header);
							mImageLoader.getBitmap(mContext, imageView, null, headUrlArray[0], 0, false, true);
							headUrlList.add(headUrlArray[0]);
							imageView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
							layout.addView(imageView);
							outLayout.setGravity(Gravity.CENTER_HORIZONTAL);
							outLayout.addView(layout);
						}else {
							for (int j = 0; j < 2; j++) {
								LinearLayout layout = new LinearLayout(mContext);
								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, width);
								layout.setPadding(padding, padding, padding, padding);
								layout.setLayoutParams(params);
								ImageView imageView = new ImageView(mContext);
								imageView.setImageResource(R.drawable.contact_default_header);
								if(single){
									headUrlList.add(headUrlArray[(2 * i + j - 1)]);
									mImageLoader.getBitmap(mContext, imageView, null, headUrlArray[(2 * i + j - 1)], 0, false, true);
								}else {
									headUrlList.add(headUrlArray[(2 * i + j)]);
									mImageLoader.getBitmap(mContext, imageView, null, headUrlArray[(2 * i + j)], 0, false, true);
								}
								imageView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
								layout.addView(imageView);
								outLayout.addView(layout);
							}
						}

						holder.mGroupHeaderLayout.addView(outLayout);
					}
				}
			}
		}

		
		//}



		return convertView;

	}



	final static class ViewHolder {
		public int mTag;
		public ImageView mHeadrIcon,mContentSplite;
		public TextView mNameTextView,mSignTextView;
		public LinearLayout mGroupHeaderLayout;

		public TextView index;
		public RelativeLayout indexLayout,contactLayout;
		public TextView newFriendsIcon;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		if(list.get(position)!=null && list.get(position).sortKey!=null
				&& !list.get(position).sortKey.equals("")){
			return list.get(position).sortKey.charAt(0);
		}
		return 0;
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			if(list.get(i)!=null ){
				String sortStr = list.get(i).sortKey;
				if(sortStr!=null && !sortStr.equals("")){
					char firstChar = sortStr.toUpperCase().charAt(0);
					if (firstChar == section) {
						return i;
					}
				}

			}

		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	public List<Integer>  getSubString(String str,String key){
		if((str == null || str.equals("")) || (key == null || key.equals(""))){
			return null;
		}
	/*	str = str.toUpperCase();*/
		List<Integer> locationList = new ArrayList<Integer>();
		int index = 0;
		while((index=str.indexOf(key,index))!=-1){
			locationList.add(index);
			index = index+key.length();
		}
		return locationList;
	}


}