package com.example.troe;


import android.appwidget.*;
import android.content.*;
import android.widget.*;
public class  Troe extends AppWidgetProvider{
  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
  RemoteViews myviews=new RemoteViews(context.getPackageName(),R.layout.troe);
  ComponentName mywidget=new ComponentName(context,Troe.class);
  appWidgetManager.updateAppWidget(mywidget, myviews);
  }
}