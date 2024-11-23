package com.example.daystillapp

// Import statements for required Android and Java libraries
import android.app.PendingIntent
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

/**
 * WidgetProvider is a custom implementation of AppWidgetProvider.
 * It handles the lifecycle events of an App Widget and updates its content.
 */
class WidgetProvider : AppWidgetProvider() {

    // A BroadcastReceiver to listen for time ticks and update the widget
    private var timeTickReceiver: BroadcastReceiver? = null

    /**
     * Called when the first widget instance is added.
     * Registers the time tick receiver to update the widget periodically.
     */
    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        Log.d("WidgetProvider", "Widget enabled, registering time tick receiver.")
        registerTimeTickReceiver(context)
    }

    /**
     * Called when the last widget instance is removed.
     * Unregisters the time tick receiver to stop updates.
     */
    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        Log.d("WidgetProvider", "Widget disabled, unregistering time tick receiver.")
        unregisterTimeTickReceiver(context)
    }

    /**
     * Called whenever the widget needs to be updated (e.g., on schedule or after adding a widget).
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        Log.d("WidgetProvider", "onUpdate triggered")
        // Update each widget instance
        for (widgetId in appWidgetIds) {
            updateWidgetTime(context, appWidgetManager, widgetId)
        }
    }

    /**
     * Updates the widget's content with the current time and date.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateWidgetTime(context: Context, appWidgetManager: AppWidgetManager, widgetId: Int) {
        val currentTime = getCurrentTime() // Get the formatted current time
        val currentDay = getCurrentDate() // Get the formatted current date

        // Create RemoteViews object for the widget layout
        val views = RemoteViews(context.packageName, R.layout.widget_layout)

        // Set the time and date
        views.setTextViewText(R.id.widget_time, currentTime) // Set the current time
        views.setTextViewText(R.id.widget_day, currentDay)  // Set the current day

        // Create an Intent to launch the app's MainActivity
        val intent = Intent(context, MainActivity::class.java)

        // Wrap the Intent in a PendingIntent
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Set the PendingIntent as an onClick action for the widget layout
        views.setOnClickPendingIntent(R.id.widget_time, pendingIntent) // Set for time display
        views.setOnClickPendingIntent(R.id.widget_day, pendingIntent)  // Set for day display

        // Update the widget instance
        appWidgetManager.updateAppWidget(widgetId, views)
    }

    /**
     * Retrieves the current date as a formatted string.
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDate(): String {
        // Get the current date using LocalDate
        val localDate = LocalDate.now()

        // Extract the current month, day, and year
        val currentMonth = localDate.month.getDisplayName(TextStyle.FULL, Locale.getDefault())
        val currentYear = localDate.year
        val currentDay = localDate.dayOfMonth

        // Format and return the date string
        val currentDate = "Today's date is $currentMonth - $currentDay - $currentYear"
        return currentDate
    }

    private fun userInputDate() {

    }

    /**
     * Retrieves the current time as a formatted string (12-hour format with AM/PM).
     */
    private fun getCurrentTime(): String {
        // Use SimpleDateFormat to format the current time
        val dateTimeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateTimeFormat.format(Date())
    }

    /**
     * Registers a BroadcastReceiver to listen for time ticks (minute changes).
     */
    private fun registerTimeTickReceiver(context: Context) {
        if (timeTickReceiver == null) {
            // Define the BroadcastReceiver
            timeTickReceiver = object : BroadcastReceiver() {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onReceive(context: Context, intent: Intent) {
                    // Update the widget if the ACTION_TIME_TICK event is received
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
            // Register the receiver to listen for time ticks
            val filter = IntentFilter(Intent.ACTION_TIME_TICK)
            context.applicationContext.registerReceiver(timeTickReceiver, filter)
        }
    }

    /**
     * Unregisters the BroadcastReceiver for time ticks.
     */
    private fun unregisterTimeTickReceiver(context: Context) {
        if (timeTickReceiver != null) {
            context.applicationContext.unregisterReceiver(timeTickReceiver)
            timeTickReceiver = null
        }
    }

    /**
     * Retrieves the current day of the week as a formatted string.
     * (Currently unused but could be helpful for future features.)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getCurrentDay(): String {
        val currentDay = LocalDate.now()
        val dayOfWeek = currentDay.dayOfWeek
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
    }
}