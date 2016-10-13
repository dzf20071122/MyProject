package com.research.widget;



import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.research.R;

public class SelectAddPopupWindow extends PopupWindow {

	private View mMenuView;
	private LinearLayout mChatLayout,mAddFriendLayout,mShaoLayout,mPhotoLayout;

	public SelectAddPopupWindow(final Activity context,final OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.addxml, null);
		mChatLayout = (LinearLayout)mMenuView.findViewById(R.id.chat_layout);
		mChatLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				itemsOnClick.onClick(v);
				dismiss();
			}
		});
		
		mAddFriendLayout = (LinearLayout)mMenuView.findViewById(R.id.add_friend);
		mAddFriendLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				itemsOnClick.onClick(v);
				dismiss();
			}
		});
		
		mShaoLayout = (LinearLayout)mMenuView.findViewById(R.id.shao_layout);
		mShaoLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				itemsOnClick.onClick(v);
				dismiss();
			}
		});
		
		
		mPhotoLayout = (LinearLayout)mMenuView.findViewById(R.id.photo_share);
		mPhotoLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				itemsOnClick.onClick(v);
				dismiss();
			}
		});
		
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		//���ð�ť����
		//����SelectPicPopupWindow��View
		this.setContentView(mMenuView);
		//����SelectPicPopupWindow��������Ŀ�
		this.setWidth((w/2/*+20*/));
		//����SelectPicPopupWindow��������ĸ�
		this.setHeight(LayoutParams.WRAP_CONTENT);
		//����SelectPicPopupWindow��������ɵ��
		this.setFocusable(true);
		//����SelectPicPopupWindow�������嶯��Ч��
		this.setAnimationStyle(R.style.mystyle);
		//ʵ����һ��ColorDrawable��ɫΪ��͸��
		ColorDrawable dw = new ColorDrawable(0000000000);
		//����SelectPicPopupWindow��������ı���
		//this.setBackgroundDrawable(dw);
		this.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.no_top_arrow_bg));
		//mMenuView���OnTouchListener�����жϻ�ȡ����λ�������ѡ������������ٵ�����
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout2).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});
	}
}
