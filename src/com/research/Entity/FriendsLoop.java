package com.research.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.research.org.json.JSONArray;
import com.research.org.json.JSONException;
import com.research.org.json.JSONObject;



public class FriendsLoop implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public ResearchJiaState status;
	public String frontCover;
	public PageInfo pageInfo;
	public List<FriendsLoopItem> childList;


	public FriendsLoop() {
		super();
	}

	public FriendsLoop(String reString) {
		super();
		try {
			JSONObject parentObj = new JSONObject(reString);
			if(!parentObj.isNull("data")){
				
				JSONArray childJson = parentObj.getJSONArray("data");
				if(childJson != null && childJson.length()>0){
					childList = new ArrayList<FriendsLoopItem>();
					for (int i = 0; i<childJson.length(); i++) {
						childList.add(new FriendsLoopItem(childJson.getJSONObject(i)));
					}
				}
				


			}

			if(!parentObj.isNull("state")){
				this.status = new ResearchJiaState(parentObj.getJSONObject("state"));
			}
			if(!parentObj.isNull("pageInfo")){
				this.pageInfo = new PageInfo(parentObj.getJSONObject("pageInfo"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
