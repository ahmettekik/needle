package com.example.android.needle;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by jonfisk on 02/10/15.
 */
public class AnnouncementAdapter extends CursorAdapter {

    private static final long HOUR_IN_MILLIS = 60 * 60 * 1000;
    private static final long MINUTE_IN_MILLIS = 60 * 1000;


    public AnnouncementAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_ad, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView descriptionTextView = (TextView) view.findViewById(R.id.description_textView);
        String description = cursor.getString(AllFragment.COL_AD_DESC);
        descriptionTextView.setText(description);

        TextView remainingTimeTextView = (TextView) view.findViewById(R.id.remaining_time_textView);
        long timeAfterAnHour = cursor.getLong(AllFragment.COL_AD_DATE) + HOUR_IN_MILLIS;
        long remainingTime = (timeAfterAnHour - System.currentTimeMillis()) / MINUTE_IN_MILLIS;

        if(remainingTime > 45) {
            remainingTimeTextView.setTextColor(
                            context.getResources().getColor(R.color.dark_green)
            );
        } else if(remainingTime <= 44 && remainingTime > 15) {
            remainingTimeTextView.setTextColor(
                    context.getResources().getColor(R.color.needle_yellow)
            );
        } else {
            remainingTimeTextView.setTextColor(
                    context.getResources().getColor(R.color.fire_brick)
            );
        }

        remainingTimeTextView.setText(remainingTime + " minutes left...");

    }
}
