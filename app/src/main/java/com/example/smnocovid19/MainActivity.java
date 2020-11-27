package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    LinearLayout qrScan, timeLine, coronaMap, accessDoc;
    Intent intent;
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userEmail;
    String fStoreName;
    String fStoreMajor;
    String fStorePhoneNum;
    String fStoreStudentNum;
    ImageView smCoronaLive;

    TextView viewUserInfo;
    //현재 로그인 된 유저 정보를 담을 변수
    private FirebaseUser currentUser;

    //DrawerView 관련 선언
    LinearLayout drawerView;
    DrawerLayout drawerLayout;
    TextView drawerName, drawerUserInfo, drawerIntroMsg;
    Button drawerAppInfo,drawerSignOut;
    ImageView openDrawerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d("토큰정보:", token);
        setContentView(R.layout.activity_main);

        //액션바 안보이게 지정
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //로그인이 자동으로 성공할 경우 성공 문자열 넣기
        intent = getIntent();
        String getLoginData = intent.getStringExtra("autoLogin");
        if(getLoginData != null){
            Toast.makeText(MainActivity.this, "자동 로그인 성공", Toast.LENGTH_SHORT).show();
        }

        qrScan = findViewById(R.id.qrCodeScan);
        timeLine = findViewById(R.id.timeline);
        coronaMap = findViewById(R.id.coronaMap);
        accessDoc = findViewById(R.id.document);

        viewUserInfo = findViewById(R.id.userInfo);
        smCoronaLive = findViewById(R.id.smcoronaimg);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //userEmail에 유저의 이메일 값 저장
        currentUser = fAuth.getCurrentUser();
        userEmail = currentUser.getEmail();

        setUserInfo();

        //drawerView 구현
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerView = findViewById(R.id.drawerView);
        openDrawerView = findViewById(R.id.openDrawerView);

        drawerName = drawerView.findViewById(R.id.name);
        drawerUserInfo = drawerView.findViewById(R.id.userInfo);
        drawerIntroMsg = drawerView.findViewById(R.id.introMsg);
        drawerAppInfo = drawerView.findViewById(R.id.btnAppInfo);
        drawerSignOut = drawerView.findViewById(R.id.btnLogout);

        drawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
            }
        });

        drawerSignOut.setOnClickListener(new View.OnClickListener() {
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

        drawerAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                box.setTitle("Application Info");
                box.setMessage("개발팀 : 군침이 사악\n개발 기간 : 2020.11.01 ~ 2020.11.28\n구현방법" +
                        "\n로그인,회원가입,비밀번호찾기 : FireBase 인증" +
                        "\n사용자 정보 저장 : FireBase FireStore\n좌측 바 : DrawerLayout" +
                        "\n메인페이지 : 각 버튼을 통해 이동" +
                        "\n지도 검색 : GoogleMaps" +
                        "\n\n시연에 참여해주셔서 감사합니다.");

                box.setPositiveButton("닫기",null);
                box.show();
            }
        });

        //선문대학교 코로나19 현황 페이지 링크
        smCoronaLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WebActivity.class);
                intent.putExtra("site_url","https://lily.sunmoon.ac.kr/Page/Story/VirusNotice.aspx");
                startActivity(intent);
            }
        });
        //qr코드 스캔 기능 intent
        qrScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, QrCodeActivity.class);
                startActivity(intent);
            }
        });

        //타임라인 기능 intent
        timeLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, TimeLineActivity.class);
                startActivity(intent);
            }
        });

        //코로나 맵 기능 intent
        coronaMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, CoronaMapActivity.class);
                startActivity(intent);
            }
        });

        //문진표 웹 intent
        accessDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,WebActivity.class);
                intent.putExtra("site_url","https://sws.sunmoon.ac.kr/Questionnaire_Guest.aspx?roleno=" + fStoreStudentNum);
                startActivity(intent);
            }
        });

        //드로어뷰 여는 코드, 가장 밑에 작성
        openDrawerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(drawerView);
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
                                fStorePhoneNum = (String) document.get("phonenum");
                                fStoreStudentNum = (String) document.get("studentnumber");

                                viewUserInfo.setText(fStoreMajor + "\n" + fStoreName + "님, 환영합니다.") ;
                                drawerName.setText(fStoreName + "님, 환영합니다.");
                                drawerUserInfo.setText("학과 : "+ fStoreMajor + "\n학번 : " + fStoreStudentNum + "\n연락처 : " + fStorePhoneNum);
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