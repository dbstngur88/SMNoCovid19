package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class CoronaMapActivity extends AppCompatActivity {
    List<User> timeLineList = new ArrayList<>();
    RecyclerView recyclerPlace;
    RecyclerView.LayoutManager layoutManager;
    TimeLineAdapter placeAdapter;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseDatabase database;
    DatabaseReference myRef;
    private static final String TAG = "CoronaMapActivity";
    TextView txtBuilding;
    TextView txtFloor;
    TextView txtTime;
    Context context;

    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corona_map);

        //액션바 안보이게 지정
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();

        txtBuilding = findViewById(R.id.txtBuilding);
        txtFloor = findViewById(R.id.txtFloor);
        txtTime = findViewById(R.id.txtTime);

        database=FirebaseDatabase.getInstance();
        myRef=database.getReference();

        getTimeLine();

        //recyclerview 설정
        recyclerPlace = findViewById(R.id.timeline_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerPlace.setLayoutManager(layoutManager);
        timeLineList = new ArrayList<>();
        //어뎁터 선언시 초기화
        placeAdapter = new TimeLineAdapter(CoronaMapActivity.this, timeLineList, context);
        recyclerPlace.setAdapter(placeAdapter);

    }

    public void getTimeLine() {
        myRef.child("corona_timeline_2016244055")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        timeLineList.clear();
                        for(DataSnapshot s : snapshot.getChildren()){
                            User user = s.getValue(User.class);
                            timeLineList.add(user);
                        }
                        //timeline 갱신후 notify
                        placeAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, error.toString());
                    }
                });
    }

}