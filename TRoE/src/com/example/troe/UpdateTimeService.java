package com.example.troe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.troe.stock.IStockRestApi;
import com.example.troe.stock.impl.StockRestApi;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

public class UpdateTimeService extends Service {

	private Timer timer;
	private TimerTask task = new TimerTask() {

		@Override
		public void run() {

			okcoinInof();
			//
			// Date date = new Date(System.currentTimeMillis());
			// SimpleDateFormat formate = new
			// SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			// String time = formate.format(date);
			// views.setTextViewText(R.id.tv_time, time);
			//
			// Intent intent = new Intent(getApplicationContext(),
			// MainActivity.class);
			// PendingIntent pendingIntent =
			// PendingIntent.getActivity(getApplicationContext(), 100, intent,
			// 0);
			// // 设置widget的点击事件
			// views.setOnClickPendingIntent(R.id.tv_time, pendingIntent);
			// 更新widget
			// appWidgetManager.updateAppWidget(componentName, views);

		}
	};

	/**
	 * OKCOIN weight 更新資訊
	 */
	public void okcoinInof() {
		String api_key = "93cbd973-be54-4c65-bdfb-670635d69b9e"; // OKCoin申请的apiKey
		String secret_key = "2A07EA26640CAB31A8415EFAFE822BE2"; // OKCoin
																// 申请的secret_key
		String url_prex = "https://www.okcoin.com"; // 注意：请求URL
													// 国际站https://www.okcoin.com
													// ;
													// 国内站https://www.okcoin.cn

		// 宣告變數
		String date = "";
		/** ticker 參數 */
		Map<String, Object> ticker;
		/** ticker 買一價 */
		int buy = 0;
		/** ticker 最高 */
		int high = 0;
		/** ticker 最新 */
		int last = 0;
		/** ticker 最低 */
		int low = 0;
		/** ticker 賣一價 */
		int sell = 0;
		/** ticker 成交量 */
		int vol = 0;
		
		String date_cn = "";
		/** ticker 買一價 */
		int buy_cn = 0;
		/** ticker 最高 */
		int high_cn = 0;
		/** ticker 最新 */
		int last_cn = 0;
		/** ticker 最低 */
		int low_cn = 0;
		/** ticker 賣一價 */
		int sell_cn = 0;
		/** ticker 成交量 */
		int vol_cn = 0;
		String json = null;
		JSONObject tickerinfo;
		/**
		 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
		 * 
		 */
		IStockRestApi stockGet = new StockRestApi(url_prex);
		IStockRestApi stockGetcn = new StockRestApi("https://www.okcoin.cn");
		/**
		 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
		 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入，
		 * 发送post请求之前，程序会做自动加密，生成签名。
		 * 
		 */
		IStockRestApi stockPost = new StockRestApi(url_prex, api_key,
				secret_key);
		IStockRestApi stockPostcn = new StockRestApi("https://www.okcoin.cn", api_key,
				secret_key);
		
		
		
		// 得到widget管理器
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(getApplicationContext());
		ComponentName mywidget = new ComponentName(getApplicationContext(),
				Troe.class);
		RemoteViews myviews = new RemoteViews(getPackageName(), R.layout.troe);

		// 網路api 防止讀取時間過長無回應用
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		// 现货行情
		try {

			json = stockGet.ticker("btc_usd");

			// {"date":"1419044631",
			// "ticker":{"buy":"1986.7","high":"1988.0","last":"1986.7",
			// "low":"1891.0","sell":"1987.36","vol":"105394.57499996"}}
			// json 字串如上例 需json解析
			JSONObject OkcoinUsd = new JSONObject(json);
			// 解析json取得ticker標籤的內容
			tickerinfo = OkcoinUsd.getJSONObject("ticker");
			// tickerinfo.getString("buy") 取得buy 標簽內容 "buy":"1986.7"
			date = OkcoinUsd.getString("date");
			buy = tickerinfo.getInt("buy");
			high = tickerinfo.getInt("high");
			last = tickerinfo.getInt("last");
			low = tickerinfo.getInt("low");
			sell = tickerinfo.getInt("sell");
			vol = tickerinfo.getInt("vol");
////////////////////////////////////////////////////btc_usd cn
			json = stockGetcn.ticker("btc_usd");
			
			JSONObject OkcoinCn = new JSONObject(json);
			tickerinfo = OkcoinCn.getJSONObject("ticker");
			date_cn = OkcoinUsd.getString("date");
			buy_cn = tickerinfo.getInt("buy");
			high_cn = tickerinfo.getInt("high");
			last_cn = tickerinfo.getInt("last");
			low_cn = tickerinfo.getInt("low");
			sell_cn = tickerinfo.getInt("sell");
			vol_cn = tickerinfo.getInt("vol");
			
			
			
			
			
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// text1
		myviews.setTextViewText(R.id.troeTx1, "BTC USD::date==" + date +" USD =="+buy);
		// text2
		myviews.setTextViewText(R.id.troeTx2, "BTC CN::date==" + date_cn +" CN =="+buy_cn);
		appWidgetManager.updateAppWidget(mywidget, myviews);

	}

	@Override
	public void onCreate() {
		super.onCreate();

		timer = new Timer();
		timer.schedule(task, 0, 5000);// 开始任务 美幾秒更新一次

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		timer.cancel();// 结束任务
	}
}