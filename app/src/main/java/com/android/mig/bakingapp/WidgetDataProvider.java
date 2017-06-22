package com.android.mig.bakingapp;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.android.mig.bakingapp.utils.Preferences.PREFERENCE_FILE_KEY;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    List<String> mCollection = new ArrayList<>();
    Context mContext;

    public WidgetDataProvider(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
    }

    // called when RemoteViewsFactory is first created and when notifyWidgetViewDataChanged is called
    @Override
    public void onDataSetChanged() {
        getCollection();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mCollection.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
        remoteViews.setTextViewText(android.R.id.text1, mCollection.get(position));
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // true if items in list won't change
    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void getCollection(){
        mCollection.clear();
        Map<String, ?> preferencesMap = mContext.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE).getAll();
        for (Map.Entry<String, ?> entry : preferencesMap.entrySet()){
            mCollection.add(entry.getValue().toString());
        }
    }
}
