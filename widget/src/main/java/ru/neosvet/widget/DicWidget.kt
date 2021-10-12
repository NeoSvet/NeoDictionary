package ru.neosvet.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.neosvet.dictionary.data.storage.DicStorage
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

class DicWidget : AppWidgetProvider() {
    companion object {
        const val FILE_NAME = "widget.txt"
        private const val PREF_NAME = "widget"
        private const val ACTION_MINUS = "minus"
        private const val ACTION_PLUS = "plus"
        private const val INDEX = "index"
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        appWidgetIds.forEach { id ->
            RemoteViews(
                context.packageName,
                R.layout.widget
            ).apply {
                val adapter = Intent(context, ListService::class.java)
                adapter.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
                setRemoteAdapter(R.id.lv_info, adapter)

                setOnClickPendingIntent(R.id.btn_prev, getPendingIntent(context, id, ACTION_MINUS))
                setOnClickPendingIntent(R.id.btn_next, getPendingIntent(context, id, ACTION_PLUS))

                appWidgetManager.updateAppWidget(id, this@apply)
                appWidgetManager.notifyAppWidgetViewDataChanged(id, R.id.lv_info)
            }
        }
    }

    private fun getPendingIntent(context: Context, widgetId: Int, action: String): PendingIntent {
        val intent = Intent(context, DicWidget::class.java)
        intent.action = action
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
        return PendingIntent.getBroadcast(context, widgetId, intent, PendingIntent.FLAG_CANCEL_CURRENT)
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        if (intent.action != ACTION_MINUS && intent.action != ACTION_PLUS)
            return

        val id = intent.extras?.let {
            it.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )
        } ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (id == AppWidgetManager.INVALID_APPWIDGET_ID)
            return

        val storage = DicStorage.get(context)
        scope.launch {
            val words = storage.wordDao.getAll()
            if (words.isEmpty())
                return@launch

            val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            var i = pref.getInt(INDEX, -1) +
                    if (intent.action == ACTION_MINUS) -1 else 1
            if (i >= words.size)
                i = 0
            else if (i < 0)
                i = words.size - 1

            val info = storage.infoDao.get(words[i].id)
            try {
                val f = File(context.filesDir.toString() + FILE_NAME)
                if (info.isEmpty()) {
                    f.delete()
                } else {
                    val bw = BufferedWriter(FileWriter(f))
                    info[0].title?.let {
                        bw.write(it)
                        bw.newLine()
                    }
                    info.forEach { item ->
                        item.description?.let {
                            bw.write(it)
                            bw.newLine()
                        }
                    }
                    bw.close()
                }
            } catch (e: Exception) {
            }

            val edit = pref.edit()
            edit.putInt(INDEX, i)
            edit.apply()

            onUpdate(context, AppWidgetManager.getInstance(context), intArrayOf(id))
        }
    }
}