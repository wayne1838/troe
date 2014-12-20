package com.example.troe;

import java.io.IOException;

import org.apache.commons.httpclient.HttpException;

import com.alibaba.fastjson.JSONObject;
import com.example.troe.stock.IStockRestApi;
import com.example.troe.stock.impl.StockRestApi;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class MainActivity extends ActionBarActivity  {
	String api_key = "93cbd973-be54-4c65-bdfb-670635d69b9e"; // OKCoin申请的apiKey
	String secret_key = "2A07EA26640CAB31A8415EFAFE822BE2"; // OKCoin
															// 申请的secret_key
	String url_prex = "https://www.okcoin.com"; // 注意：请求URL
												// 国际站https://www.okcoin.com ;
												// 国内站https://www.okcoin.cn

	@Override
	public void onCreate(Bundle savedInstanceState)  {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("安安安安安安安安安安安安 ");

		/**
		 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
		 * 
		 */
		IStockRestApi stockGet = new StockRestApi(url_prex);

		/**
		 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
		 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入，
		 * 发送post请求之前，程序会做自动加密，生成签名。
		 * 
		 */
		IStockRestApi stockPost = new StockRestApi(url_prex, api_key,
				secret_key);

		// 现货行情
		try {
			System.out.println("1");
			stockGet.ticker("btc_usd");
			System.out.println("OK");
		} catch (HttpException e) {
			System.out.println("2");
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("3");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("4");
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}


	
}
