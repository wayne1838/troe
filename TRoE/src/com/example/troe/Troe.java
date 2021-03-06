package com.example.troe;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.troe.stock.IStockRestApi;
import com.example.troe.stock.impl.StockRestApi;

import android.appwidget.*;
import android.content.*;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.*;

public class Troe extends AppWidgetProvider {
	String api_key = "93cbd973-be54-4c65-bdfb-670635d69b9e"; // OKCoin申请的apiKey
	String secret_key = "2A07EA26640CAB31A8415EFAFE822BE2"; // OKCoin
															// 申请的secret_key
	String url_prex = "https://www.okcoin.com"; // 注意：请求URL
												// 国际站https://www.okcoin.com ;
												// 国内站https://www.okcoin.cn
	// 宣告元件名
	private Button button01;

	private TextView textView01, textView02;

	// 宣告變數
	int ID;// test
	String date;
	/** ticker 參數 */
	Map<String, Object> ticker;
	/** ticker 買一價 */
	int buy;
	/** ticker 最高 */
	int high;
	/** ticker 最新 */
	int last;
	/** ticker 最低 */
	int low;
	/** ticker 賣一價 */
	int sell;
	/** ticker 成交量 */
	int vol;
	String json;
	JSONObject tickerinfo;
	/**
	 * get请求无需发送身份认证,通常用于获取行情，市场深度等公共信息
	 * 
	 */
	IStockRestApi stockGet = new StockRestApi(url_prex);

	/**
	 * post请求需发送身份认证，获取用户个人相关信息时，需要指定api_key,与secret_key并与参数进行签名，
	 * 此处对构造方法传入api_key与secret_key,在请求用户相关方法时则无需再传入， 发送post请求之前，程序会做自动加密，生成签名。
	 * 
	 */
	IStockRestApi stockPost = new StockRestApi(url_prex, api_key, secret_key);

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		if (okcoinInfo(context, appWidgetManager) != true) {
			return;
		}

	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		Log.i("i", "onDeleted()");
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Log.i("i", "onEnabled()");
		// 启动服务
		Intent intent = new Intent(context, UpdateTimeService.class);
		context.startService(intent);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Log.i("i", "onDisabled()");
		// 停止服务
		Intent intent = new Intent(context, UpdateTimeService.class);
		context.stopService(intent);
	}

	public boolean okcoinInfo(Context context, AppWidgetManager appWidgetManager) {
		RemoteViews myviews = new RemoteViews(context.getPackageName(),
				R.layout.troe);
		ComponentName mywidget = new ComponentName(context, Troe.class);

		// 網路api 防止讀取時間過長無回應用
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		json = null;
		// 现货行情
		try {

			json = stockGet.ticker("btc_usd");

			// {"date":"1419044631",
			// "ticker":{"buy":"1986.7","high":"1988.0","last":"1986.7",
			// "low":"1891.0","sell":"1987.36","vol":"105394.57499996"}}
			// json 字串如上例 需json解析
			String json2 = new String(json);
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
		myviews.setTextViewText(R.id.troeTx1, "BTC==" + json);
		// text2
		myviews.setTextViewText(R.id.troeTx2, "BTC==" + buy);
		appWidgetManager.updateAppWidget(mywidget, myviews);

		return true;

	}

}