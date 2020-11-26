package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
public class TimeLineActivity extends AppCompatActivity {
    List<User> timeLineList = new ArrayList<>();
    RecyclerView mRecyclerView;
    Context context;

    RecyclerView.LayoutManager layoutManager;

    TimeLineAdapter adapter;
    private DatabaseReference timeLineDbRef;

    TextView txtBuilding;
    TextView txtFloor;
    TextView txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        txtBuilding = findViewById(R.id.txtBuilding);
        txtFloor = findViewById(R.id.txtFloor);
        txtTime = findViewById(R.id.txtTime);
        //recyclerview 설정
        mRecyclerView = findViewById(R.id.timeline_view);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        timeLineDbRef = FirebaseDatabase.getInstance().getReference("timeline/1");
        timeLineDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User timeLine = snapshot.getValue(User.class);
                timeLineList.clear(); //기존 배열리스트가 존재하지 않게 초기화
                //realtimeDB의 실시간 시간 설정
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = simpleDateFormat.format(new Date());
                timeLine.setUpdateTime(strDate);
                timeLineDbRef.setValue(timeLine);
                //담은 데이터들을 배열리스트에 넣음
                timeLineList.add(timeLine);

                //어댑터 연결
                adapter = new TimeLineAdapter(TimeLineActivity.this, timeLineList, context);
                mRecyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}