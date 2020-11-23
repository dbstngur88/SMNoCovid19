package com.example.smnocovid19;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Time;
import java.util.List;

public class TimeLineAdapter extends ArrayAdapter {
    private Activity mContext;
    List<TimeLine> timeLineList;

    public TimeLineAdapter(Activity mContext, List<TimeLine> timeLineList) {
        super(mContext,R.layout.activity_time_line,timeLineList);
        this.mContext = mContext;
        this.timeLineList = timeLineList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View timeLineListView = inflater.inflate(R.layout.activity_time_line,null,true);
        TextView tvBuilding = timeLineListView.findViewById(R.id.txtBuilding);
        TextView tvBuildingFloor = timeLineListView.findViewById(R.id.txtFloor);
        TextView tvTime = timeLineListView.findViewById(R.id.txtTime);

        TimeLine timeLine = timeLineList.get(position);

        tvBuilding.setText(timeLine.getBuildingName());
        tvBuildingFloor.setText(timeLine.getBuildingFloor());
        tvTime.setText((CharSequence) timeLine.getUpdateDate());

        return timeLineListView;
    }


}
