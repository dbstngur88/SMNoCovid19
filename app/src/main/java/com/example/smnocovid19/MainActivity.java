package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    Button button1, button2, button3;
    Intent intent;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userEmail;
    String fStoreName;
    String fStoreMajor;

    TextView viewUserInfo;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("토큰정보:", token);
        setContentView(R.layout.activity_main);

        //액션바 안보이게 지정
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        viewUserInfo = findViewById(R.id.userInfo);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //userEmail에 유저의 이메일 값 저장
        currentUser = fAuth.getCurrentUser();
        userEmail = currentUser.getEmail();

        setUserInfo();

        intent = new Intent();
        
        //장동민 개발 내역 연결 버튼
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, QrCodeActivity.class);
                startActivity(intent);
            }
        });

        //박민석 개발 내역 연결 버튼
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, TimeLineActivity.class);
                startActivity(intent);
            }
        });

        //윤수혁 개발 내역 연결 버튼
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, CoronaMapActivity.class);
                startActivity(intent);
            }
        });

    }

    public void mClick(View view) {
        switch (view.getId()){
            case R.id.imgSignOut :      //로그아웃
                fAuth.getInstance().signOut();
                // Check if user is signed in (non-null) and update UI accordingly.
                currentUser = fAuth.getCurrentUser();
                if(currentUser == null){
                    intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("logout","logout");
                    startActivity(intent);
                    finish();
                }
                break;
        }
    }

    public void setUserInfo(){
        fStore.collection("users")     //TestCode, 수정필요(courseList ->competition)
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fStoreName = (String) document.get("name");
                                fStoreMajor = (String) document.get("major");

                                viewUserInfo.setText(fStoreMajor + "\n" + fStoreName + "님, 환영합니다.") ;
                            }
                        } else {
                            Toast.makeText(MainActivity.this,"유저 정보 출력 오류",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
} //test commit 장동민
 // second test 장동민
// test commit 박민석
//second commit 박민석...