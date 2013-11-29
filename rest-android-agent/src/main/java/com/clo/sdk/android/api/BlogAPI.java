package com.clo.sdk.android.api;

import com.clo.sdk.android.Oauth2AccessToken;
import com.clo.sdk.android.WeiboParameters;
import com.clo.sdk.android.net.RequestListener;
/**
 * 该类封装了用户接口，详情请参考<a href="http://open.weibo.com/wiki/API%E6%96%87%E6%A1%A3_V2#.E7.94.A8.E6.88.B7">用户接口</a>
 * @author xiaowei6@staff.sina.com.cn
 */
public class BlogAPI extends WeiboAPI {
	public BlogAPI(Oauth2AccessToken accessToken) {
        super(accessToken);
    }

    private static final String SERVER_URL_PRIX = API_SERVER + "remote";
    
    /**
     * 返回当前用户timeline
     * @param listener
     * @param name
     */
    public void timeline(RequestListener listener,String name){
    	WeiboParameters params = new WeiboParameters();
    	params.add("name", name);
    	request( SERVER_URL_PRIX + "/main", params, HTTPMETHOD_GET, listener);
    }

}
