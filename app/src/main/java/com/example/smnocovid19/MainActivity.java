package com.example.smnocovid19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button button1, button2;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);
        
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
    }
} //test commit 장동민
 // second test 장동민
// test commit 박민석
//second commit 박민석...