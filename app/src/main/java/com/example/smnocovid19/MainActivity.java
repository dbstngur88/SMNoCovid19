package com.example.smnocovid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button button1, button2, button3, button4;
    Intent intent;
    private FirebaseAuth fAuth;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);

        fAuth = FirebaseAuth.getInstance();

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

        //윤수혁 개발 내역 연결 버튼
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });
    }
} //test commit 장동민
 // second test 장동민
// test commit 박민석
//second commit 박민석...