package com.example.daystillapp

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ComponentName
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import java.util.*

class WidgetProvider : AppWidgetProvider() {

    private var timeTickReceiver: BroadcastReceiver? = null

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("WidgetProvider", "Widget enabled, registering time tick receiver.")
        registerTimeTickReceiver(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d("WidgetProvider", "Widget disabled, unregistering time tick receiver.")
        unregisterTimeTickReceiver(context)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("WidgetProvider", "onUpdate triggered")
        for (widgetId in appWidgetIds) {
            updateWidgetTime(context, appWidgetManager, widgetId)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateWidgetTime(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
        val currentTime = getCurrentTime()
        val currentDay = getCurrentDay()

        // Create RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Set the time and date
        views.setTextViewText(R.id.widget_time, currentTime) //12 hours
        views.setTextViewText(R.id.widget_day, currentDay)

        // Update the widget
        appWidgetManager.updateAppWidget(widgetId, views)
    }

    private fun getCurrentTime(): String {
        val dateTimeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateTimeFormat.format(Date())
    }

    private fun registerTimeTickReceiver(context: Context) {
        if (timeTickReceiver == null) {
            timeTickReceiver = object : BroadcastReceiver() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onReceive(context: Context, intent: Intent) {
                    if (Intent.ACTION_TIME_TICK == intent.action) {
                        Log.d("WidgetProvider", "ACTION_TIME_TICK received, updating widget.")
                        val appWidgetManager = AppWidgetManager.getInstance(context)
                        val appWidgetIds = appWidgetManager.getAppWidgetIds(
                            ComponentName(context, WidgetProvider::class.java)
                        )
                        for (widgetId in appWidgetIds) {
                            updateWidgetTime(context, appWidgetManager, widgetId)
                        }
                    }
                }
            }
            val filter = IntentFilter(Intent.ACTION_TIME_TICK)
            context.applicationContext.registerReceiver(timeTickReceiver, filter)
        }
    }

    private fun unregisterTimeTickReceiver(context: Context) {
        if (timeTickReceiver != null) {
            context.applicationContext.unregisterReceiver(timeTickReceiver)
            timeTickReceiver = null
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDay():String{
        val currentDay = LocalDate.now()
        val dayOfWeek = currentDay.dayOfWeek
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }

}

