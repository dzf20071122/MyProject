package com.research.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.research.org.json.JSONArray;
import com.research.org.json.JSONException;
import com.research.org.json.JSONObject;

public class CommodityList implements Serializable{

	private static final long serialVersionUID = 1946436545154L;
	
	public List<Commodity> mCommodityList;
	public ResearchJiaState mState;
	public PageInfo mPageInfo;

	public CommodityList(){}
	
	public CommodityList(String reString){
		try {
			JSONObject json = new JSONObject(reString);
			if(!json.isNull("data")){
				JSONArray array = json.getJSONArray("data");
				if(array != null){
					mCommodityList = new ArrayList<Commodity>();
					List<Commodity> tempList = Commodity.constructCommodityList(array);
					if(tempList != null){
						mCommodityList.addAll(tempList);
					}
				}
			}
			
			if(!json.isNull("state")){
				mState = new ResearchJiaState(json.getJSONObject("state"));
			}
			
			if(!json.isNull("pageInfo")){
				mPageInfo = new PageInfo(json.getJSONObject("pageInfo"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
}
