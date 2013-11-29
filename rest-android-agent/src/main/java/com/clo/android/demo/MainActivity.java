package com.clo.android.demo;

import java.text.SimpleDateFormat;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.clo.android.keep.AccessTokenKeeper;
import com.clo.sdk.android.Oauth2AccessToken;
import com.clo.sdk.android.Weibo;
import com.clo.sdk.android.WeiboAuthListener;
import com.clo.sdk.android.WeiboDialogError;
import com.clo.sdk.android.WeiboException;
import com.clo.sdk.android.util.KenoConfig;
import com.clo.sdk.android.util.Utility;
/**
 * 
 * @author liyan (liyan9@staff.sina.com.cn)
 */
public class MainActivity extends Activity {

    private Weibo mWeibo;
    private static final String CONSUMER_KEY = KenoConfig.getValue("client_ID");// 替换为开发者的appkey，例如"1646212860";
    private static final String REDIRECT_URL = KenoConfig.getValue("redirect_URI");
    //private Button authBtn, apiBtn, ssoBtn, cancelBtn;
    
    public static Oauth2AccessToken accessToken;
    public static final String TAG = "sinasdk";
    
    private AQuery mText,authBtn,cancelBtn,apiBtn;
    
    
    
    /**
     * SsoHandler 仅当sdk支持sso时有效，
     */
    //SsoHandler mSsoHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /**如果多组件则需要创建多实例，否则会相互覆盖掉*/
        mText = new AQuery(this).id(R.id.show);
        authBtn = new AQuery(this).id(R.id.auth);
        cancelBtn = new AQuery(this).id(R.id.apiCancel);
        apiBtn = new AQuery(this).id(R.id.findUsers);
        
        mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);
        
        /**点击按钮跳转*/
        apiBtn.clicked(this,"gotoUserListActivity");
        /**认证按钮，进行验证*/
        authBtn.clicked(this, "authorize");
        /**点击取消按钮*/
        cancelBtn.clicked(this, "cancle");


        MainActivity.accessToken = AccessTokenKeeper.readAccessToken(this);
        if (MainActivity.accessToken.isSessionValid()) {
            Weibo.isWifi = Utility.isWifi(this);
            apiBtn.visible();
            authBtn.invisible();
            cancelBtn.visible();
            String date = new java.text.SimpleDateFormat("yyyy/MM/dd hh:mm:ss")
                    .format(new java.util.Date(MainActivity.accessToken.getExpiresTime()));
            mText.text("access_token 仍在有效期内,无需再次登录: \naccess_token:" + MainActivity.accessToken.getToken() + "\n有效期：" + date);
        } else {
            //mText.setText("使用SSO登录前，请检查手机上是否已经安装新浪微博客户端，目前仅3.0.0及以上微博客户端版本支持SSO；如果未安装，将自动转为Oauth2.0进行认证");
        }

    }
    
    public void gotoUserListActivity(View v){
    	Intent intent = new Intent(MainActivity.this, ListActivity.class);
        startActivity(intent);//转向用户列表
    }
    
    public void authorize(View v){
    	Log.i(TAG, "response :"+v);
    	mWeibo.authorize(MainActivity.this, new AuthDialogListener());
    }
      
    public void cancle(View v){
        AccessTokenKeeper.clear(MainActivity.this);
        authBtn.visible();
        cancelBtn.invisible();
        apiBtn.invisible();
        mText.text("");
    }

    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    class AuthDialogListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            String token = values.getString("access_token");
            String expires_in = values.getString("expires_in");
            MainActivity.accessToken = new Oauth2AccessToken(token, expires_in);
            if (MainActivity.accessToken.isSessionValid()) {
                String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(MainActivity.accessToken.getExpiresTime()));
                mText.text("认证成功: \r\n access_token: " + token + "\r\n" + "expires_in: " + expires_in + "\r\n有效期：" + date);
                apiBtn.visible();
                authBtn.invisible();
                cancelBtn.visible();
                AccessTokenKeeper.keepAccessToken(MainActivity.this,accessToken);
                Toast.makeText(MainActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(WeiboDialogError e) {
            Toast.makeText(getApplicationContext(),
                    "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel() {
            Toast.makeText(getApplicationContext(), "Auth cancel",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(getApplicationContext(),
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * 下面两个注释掉的代码，仅当sdk支持sso时有效，
         */
        //if (mSsoHandler != null) {
         //   mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        //}
    }

}
