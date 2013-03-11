package com.android.androidexamples;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.R;
import com.android.utils.TokenStore;
import com.tencent.weibo.api.Friends_API;
import com.tencent.weibo.api.Statuses_API;
import com.tencent.weibo.api.T_API;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Configuration;
import com.tencent.weibo.utils.OAuthClient;

/**
 * main activity to access qweibo api 
 */

public class OAuthActivity extends Activity {

	private TextView authResultView;
	private OAuth oauth;
	private OAuthClient auth;
	private String clientIp;

	private Button postBtn;	
	private EditText postContentText;
	private TextView postContentResult;
	
	private Button postPicBtn;	
	private EditText postPicText;
	private TextView postPicResult;	
	
	private Button getTimeLineBtn;	
	private TextView getTimeLineResult;		
	
	//public String picPath = "/mnt/sdcard/PhotoSola/1309793574239.jpg";
	public String picPath = "/mnt/sdcard/mydesert.jpg";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		
		authResultView = (TextView) findViewById(R.id.authResult);
		
		postBtn = (Button) findViewById(R.id.postBtn);	
		postContentText = (EditText) findViewById(R.id.postContentText);	
		postContentResult = (TextView) findViewById(R.id.postContentResult);	
		
		postPicBtn = (Button) findViewById(R.id.postPicBtn);	
		postPicText = (EditText) findViewById(R.id.postPicText);	
		postPicResult = (TextView) findViewById(R.id.postPicResult);	
		
		getTimeLineBtn = (Button) findViewById(R.id.getTimeLineBtn);		
		getTimeLineResult = (TextView) findViewById(R.id.getTimeLineResult);
		
		Intent intent = getIntent();
		if(intent.hasExtra("oauth_token")) {
			String oauth_token = intent.getStringExtra("oauth_token");
			String oauth_token_secret = intent.getStringExtra("oauth_token_secret");
			
			setToken(oauth_token, oauth_token_secret);
		}
		
		/* 发表微博 */
		postBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg) {
				T_API tapi = new T_API();
				
				String content = postContentText.getText().toString();
				
				if(content == "") {
					content = "发表微博";
				}
				
				try {
					tapi.add(oauth, "json", content, clientIp, "", "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				postContentResult.setVisibility(View.VISIBLE);
			}
		});
		
		/* 发表图片 */
		postPicBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg) {
				T_API tapi = new T_API();
				
				String content = postPicText.getText().toString();
				
				if(content.equals("")) {
					content = "发表微博";
				}
				content="test";
				try {
					String s=tapi.add_pic(oauth, "json", content, clientIp, picPath);
					System.out.println(s);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				postPicResult.setVisibility(View.VISIBLE);
			}
		});	
		
		/* 获取TimeLine */
		getTimeLineBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg) {

				Friends_API friends=new Friends_API();
				Statuses_API sapi = new Statuses_API();
				String resultData = "";	
				
				try {
					//resultData = sapi.home_timeline(oauth, "json", "0", "0", "20");
					resultData =friends.user_fanslist(oauth, "json", "10", "0", "BlueX_Chan");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				getTimeLineResult.setText(resultData);
			}
		});				
	}
	
	/**
	 * call back from qweibo page
	 */
	public void onResume() {
		super.onResume();
		
		Uri uri = this.getIntent().getData();

		if(uri != null) {
			String oauth_verifier = uri.getQueryParameter("oauth_verifier");	
			String oauth_token = uri.getQueryParameter("oauth_token");	
			
			getToken(oauth_verifier, oauth_token);
		}
	}	
	
	/**
	 * get token from verifier code
	 * @param oauth_verifier
	 * @param oauth_token
	 */
	public void getToken(String oauth_verifier, String oauth_token) {
		oauth = LoginActivity.oauth;
		auth = LoginActivity.auth;
		
		oauth.setOauth_verifier(oauth_verifier);
		oauth.setOauth_token(oauth_token);
		
		clientIp = Configuration.wifiIp;
			
		try {
			oauth = auth.accessToken(oauth);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (oauth.getStatus() == 2) {
			System.out.println("Get Access Token failed!");
			return;
		} else {			
			Log.d(LoginActivity.appName, "OAuthActivity Oauth_token : " + oauth.getOauth_token());
			Log.d(LoginActivity.appName, "OAuthActivity Oauth_token_secret : " + oauth.getOauth_token_secret());
			
			// 已经拿到access token，可以使用oauth对象访问所有API了
			// 将access token存储到SharedPreferences里
			TokenStore.store(this, oauth);
		}
		
		authResultView.setText("Access token: " + oauth.getOauth_token() 
				+ "\nAccess token secret:" + oauth.getOauth_token_secret());						
	}
	
	public void setToken(String oauth_token, String oauth_token_secret) {
		oauth = LoginActivity.oauth;
		
		oauth.setOauth_token(oauth_token);
		oauth.setOauth_token_secret(oauth_token_secret);
		
		authResultView.setText("Access token: " + oauth.getOauth_token() 
				+ "\nAccess token secret:" + oauth.getOauth_token_secret());				
	}
}
