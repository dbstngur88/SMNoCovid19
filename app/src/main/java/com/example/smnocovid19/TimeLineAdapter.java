package com.example.smnocovid19;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Time;
import java.util.List;

public class TimeLineAdapter extends RecyclerView.Adapter<TimeLineViewHolder> {
    TimeLineActivity timeLineActivity;
    Context mContext;
    List<TimeLine> timeLineList;

    public TimeLineAdapter(TimeLineActivity timeLineActivity,List<TimeLine> timeLineList, Context mContext ) {
        this.timeLineActivity = timeLineActivity;
        this.mContext = mContext;
        this.timeLineList = timeLineList;
    }

    @NonNull
    @Override
    public TimeLineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
       //timeline 레이아웃 생성
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.timeline_item,parent,false);
       //ViewHolder 객체 생성
        TimeLineViewHolder timeLineViewHolder = new TimeLineViewHolder(itemView);
        timeLineViewHolder.setOnCLickListener(new TimeLineViewHolder.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //아이템을 클릭할 때 동작
                String buildingName = timeLineList.get(position).getBuildingName();
                String buildingFloor = timeLineList.get(position).getBuildingFloor();
                String dateTime = timeLineList.get(position).getUpdateDate();
                Toast.makeText(timeLineActivity, buildingName+" "+ buildingFloor
                        +"\n" + dateTime, Toast.LENGTH_SHORT).show();
            }
        });
        return timeLineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TimeLineViewHolder holder, int position) {
        holder.txtBuilding.setText(timeLineList.get(position).getBuildingName());
        holder.txtFloor.setText(timeLineList.get(position).getBuildingFloor());
        holder.txtTime.setText(timeLineList.get(position).getUpdateDate());
    }

    @Override
    public int getItemCount() {
        return timeLineList.size();
    }
}
