package com.example.smnocovid19;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TimeLineViewHolder extends RecyclerView.ViewHolder{
    TextView txtBuilding;
    TextView txtFloor;
    TextView txtTime;
    private TimeLineViewHolder.ClickListener mClickListener;
    View mView;

    public TimeLineViewHolder(@NonNull View itemView) {
        super(itemView);

        txtBuilding = itemView.findViewById(R.id.txtBuilding);
        txtFloor = itemView.findViewById(R.id.txtFloor);
        txtTime = itemView.findViewById(R.id.txtTime);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.onItemClick(v, getAdapterPosition());
            }
        });
}
    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnCLickListener(TimeLineViewHolder.ClickListener cLickListener) {
        mClickListener = cLickListener;
    }
}
