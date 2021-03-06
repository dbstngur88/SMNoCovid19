package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Date;
import java.util.Map;

public class TimeLineActivity extends AppCompatActivity {
    List<User> timeLineList = new ArrayList<>();
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private FirebaseUser currentUser;
    RecyclerView mRecyclerView;
    Context context;
    private DatabaseReference mDatabase;
    RecyclerView.LayoutManager layoutManager;

    TimeLineAdapter adapter;
    private static final String TAG = "TimeLineActivity";
    TextView txtBuilding;
    TextView txtFloor;
    TextView txtTime;
    TextView txtUserInfo;
    String userInfo;
    String userEmail;
    String fStoreStudentNumber;
    String fStoreName;
    String fStoreMajor;
    String userNumber;
    String indexNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 화면을 portrait(세로) 화면으로 고정하고 싶은 경우


        setContentView(R.layout.activity_time_line);

        //액션바 안보이게 지정
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();
        userEmail = currentUser.getEmail();
        txtBuilding = findViewById(R.id.txtBuilding);
        txtFloor = findViewById(R.id.txtFloor);
        txtTime = findViewById(R.id.txtTime);
        txtUserInfo = findViewById(R.id.txtUserInfo);
        //recyclerview 설정
        mRecyclerView = findViewById(R.id.timeline_view);
        layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        timeLineList = new ArrayList<>();
        //어뎁터 선언시 초기화

        adapter = new TimeLineAdapter(TimeLineActivity.this, timeLineList, context);
        mRecyclerView.setAdapter(adapter);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getUserNumber();
        //유저 정보 불러오기 메소드 실행

        //타임라인 불러오기 메소드 실행

    }
    public void getTimeLine() {
        mDatabase.child("timeline_"+fStoreStudentNumber)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        timeLineList.clear();
                        for(DataSnapshot s : snapshot.getChildren()){
                            User user = s.getValue(User.class);
                            timeLineList.add(user);
                        }
                        //timeline 갱신후 notify
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.w(TAG, error.toString());
                    }
                });
    }
    public void getUserNumber() {

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fStoreName = (String) document.get("name");
                                fStoreMajor = (String) document.get("major");
                                fStoreStudentNumber = (String)document.get("studentnumber");
                                txtUserInfo.setText(fStoreMajor + " "+fStoreName + " 님의 타임라인");
                                getTimeLine();
                            }
                        } else {
                            Toast.makeText(TimeLineActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
