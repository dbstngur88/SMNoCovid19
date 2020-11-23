package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class TimeLineActivity extends AppCompatActivity {
    ListView timeLineListview;
    List<TimeLine> timeLineList;

    DatabaseReference timeLineDbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        timeLineListview = findViewById(R.id.timeLineListView);
        timeLineList = new ArrayList<>();
        timeLineDbRef = FirebaseDatabase.getInstance().getReference("timeline");
        timeLineDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                timeLineList.clear(); //기존 배열리스트가 존재하지 않게 초기화
                for(DataSnapshot timelineDataSnap : snapshot.getChildren()) {
                    //반복문으로 데이터 List 추출
                    TimeLine timeLine = timelineDataSnap.getValue(TimeLine.class);
                    //TimeLine 객체에 데이터를 담는다.
                    timeLineList.add(timeLine);
                    //담은 데이터들을 배열리스트에 넣음
                }

                TimeLineAdapter adapter = new TimeLineAdapter(TimeLineActivity.this,timeLineList);
                timeLineListview.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}