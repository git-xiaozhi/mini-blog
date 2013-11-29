package com.clo.android.demo;

import java.io.IOException;
import java.util.ArrayList;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.json.JSONObject;



import com.androidquery.AQuery;
import com.clo.android.keep.AccessTokenKeeper;
import com.clo.sdk.android.Oauth2AccessToken;
import com.clo.sdk.android.WeiboException;
import com.clo.sdk.android.api.BlogAPI;
import com.clo.sdk.android.model.ListPage;
import com.clo.sdk.android.model.WebPost;
import com.clo.sdk.android.net.RequestListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;

public class ListActivity extends Activity {
	
	AQuery lv,textAq;
	SimpleAdapter adapter;
	ArrayList<JSONObject> list;
	public static final String TAG = "show";
	

	
	
	//通过handler机制更新UI
	private  Handler myHandler = new Handler() {  
        public void handleMessage(Message msg) {   
             switch (msg.what) {   
                  case 1:   
				/**采用aquery组件显示列表*/
				try {
					
					ObjectMapper mapper = new ObjectMapper();
					JavaType javaType  =  mapper.getTypeFactory().constructParametricType(ListPage.class, WebPost.class);
					ListPage<WebPost> posts =  (ListPage<WebPost>)mapper.readValue(msg.getData().getString("show"), javaType);
					
					
					final AQuery listAq = new AQuery(ListActivity.this);
					ArrayAdapter<WebPost> aa = new ArrayAdapter<WebPost>(ListActivity.this, R.layout.activit_user_list, posts.getList()){
						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							if(convertView == null){
								convertView = getLayoutInflater().inflate(R.layout.activit_user_list, null);
							}
							WebPost jo = getItem(position);
							AQuery aq = listAq.recycle(convertView);
							aq.id(R.id.imageView1).progress(R.id.progress).image(jo.getUser().getPortraitUrl());
							aq.id(R.id.showname).text(jo.getContent());
							aq.id(R.id.registerTime).text(jo.getTimeArg());
							return convertView;
						}	
					};
					lv.adapter(aa);
					//textAq.id(R.id.text).text(posts.toString());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
                	  
                      break;
             }   
             super.handleMessage(msg);   
        }   
   };
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activit_user);
		//setContentView(R.layout.activit_test);//测试用
		lv = new AQuery(this).id(R.id.listView1);
		//textAq = new AQuery(this).id(R.id.text);//测试用
		
		Oauth2AccessToken accessToken = AccessTokenKeeper.readAccessToken(this);
		BlogAPI blog = new BlogAPI(accessToken);
		//user.findUsers(new UsersListListener());
		blog.timeline(new UsersListListener(),"xiaozhi");//测试用
		
	}
	
	
	
	class UsersListListener implements RequestListener{

		@Override
		public void onComplete(String response) {
			//Log.i(TAG, "response :"+response);
			Message message = new Message();
            message.what = 1;
            Bundle bundle = new Bundle();
            bundle.putString("show", response);
            message.setData(bundle);
            myHandler.sendMessage(message);
			
		}

		@Override
		public void onIOException(IOException e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onError(WeiboException e) {
			// TODO Auto-generated method stub
			
		}
		
	}

}
