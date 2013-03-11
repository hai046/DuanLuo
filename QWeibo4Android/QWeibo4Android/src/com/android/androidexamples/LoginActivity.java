package com.android.androidexamples;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.R;
import com.android.utils.TokenStore;
import com.tencent.weibo.beans.OAuth;
import com.tencent.weibo.utils.Configuration;
import com.tencent.weibo.utils.OAuthClient;
import com.tencent.weibo.utils.Utils;

/**
 * login activity
 * 请首先修改beans/OAuth.java里的oauth_consumer_key和oauth_consumer_secret为自己的key和secret
 * 
 * SDK根据网上代码改写而成
 * 版权归所有参与者所有
 * @author hfahe
 */

public class LoginActivity extends Activity {
	
	public static final String appName = "海明月";
	public static OAuth oauth;
	public static OAuthClient auth;
	
	private Button beginOuathBtn;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.login);
				
		
		/* get ip */
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);   
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();   
		int ipAddress = wifiInfo.getIpAddress();	
		Configuration.wifiIp = Utils.intToIp(ipAddress);   
		
		oauth = new OAuth("QWeibo4android://OAuthActivity"); // 初始化OAuth请求令牌		
		
		String[] oauth_token_array = TokenStore.fetch(this);
		String oauth_token = oauth_token_array[0];
		String oauth_token_secret = oauth_token_array[1];
		 
		if(oauth_token != null && oauth_token_secret != null) { // 已经有access token
			Intent intent = new Intent(LoginActivity.this, OAuthActivity.class);
			intent.putExtra("oauth_token", oauth_token);
			intent.putExtra("oauth_token_secret", oauth_token_secret);			
			
			startActivity(intent);			
		} else {
	 
			// 点击开始oauth认证
			beginOuathBtn = (Button) findViewById(R.id.auth);
			beginOuathBtn.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					try {
						auth = new OAuthClient();// OAuth 认证对象
						
						// 获取request token
						oauth = auth.requestToken(oauth);
	
						if (oauth.getStatus() == 1) {
							System.out.println("Get Request Token failed!");
							return;
						} else {
							String oauth_token = oauth.getOauth_token();
							
							String url = "http://open.t.qq.com/cgi-bin/authorize?oauth_token="
								+ oauth_token;
							
							Log.d(appName, "AndroidExample url = "+url);
							
							Uri uri = Uri.parse(url);
							
							startActivity(new Intent(Intent.ACTION_VIEW, uri));
						}
					
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
				}
			});		
		}
	}  
}