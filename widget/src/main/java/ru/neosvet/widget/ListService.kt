package ru.neosvet.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import java.io.BufferedReader
import java.io.File
import java.io.FileReader

class ListService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return DicFactory(applicationContext, intent)
    }

    inner class DicFactory(
        private val context: Context,
        intent: Intent
    ) : RemoteViewsFactory {
        private var data = mutableListOf<String>()
        var widgetId: Int = intent.getIntExtra(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        )

        override fun onCreate() {}

        override fun getCount() = data.size

        override fun getItemId(position: Int) = position.toLong()

        override fun getLoadingView(): RemoteViews? = null

        override fun getViewAt(position: Int): RemoteViews {
            val rView = RemoteViews(
                context.packageName,
                android.R.layout.simple_list_item_1
            )
            rView.setTextViewText(android.R.id.text1, data[position])
            return rView
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun hasStableIds(): Boolean {
            return true
        }

        override fun onDataSetChanged() {
            data.clear()
            try {
                val f = File(context.filesDir.toString() + DicWidget.FILE_NAME)
                val br = BufferedReader(FileReader(f))
                var s = br.readLine()
                while (s != null) {
                    data.add(s)
                    s = br.readLine()
                }
                br.close()
            } catch (e: Exception) {
            }
            if (data.isEmpty())
                data.add(context.getString(R.string.no_selected))
        }

        override fun onDestroy() {}
    }
}