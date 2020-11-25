package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import static java.security.AccessController.getContext;

public class QrCodeActivity extends AppCompatActivity {
    private Button btn_check;
    private TextView textViewUserNumber, textViewBuildingFloor, textViewBuildingName, textViewUpdateTime;
    private IntentIntegrator qrScan;
    private DatabaseReference mDatabase;
    int a = 0;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String formatDate = sdfNow.format(date); // 현재시간을 가지고있음.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        btn_check = findViewById(R.id.check);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        readUser();

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // String getUserNumber = textViewUserNumber.getText().toString();
                String getBuildingFloor = textViewBuildingFloor.getText().toString();
                String getBuildingName = textViewBuildingName.getText().toString();
                String getUpdateTime = formatDate;

                HashMap result = new HashMap<>();
               // result.put("학번", getUserNumber);
                result.put("층", getBuildingFloor);
                result.put("건물", getBuildingName);
                result.put("입장시간", getUpdateTime);

                a = a + 1;
                writeNewUser(a, getBuildingFloor, getBuildingName, getUpdateTime);

            }
        });

       // textViewUserNumber = (TextView) findViewById(R.id.textViewUserNumber);
        textViewBuildingFloor = (TextView) findViewById(R.id.textViewBuildingFloor);
        textViewBuildingName = (TextView) findViewById(R.id.textViewBuildingName);
        textViewUpdateTime = findViewById(R.id.textViewUpdateTime);

        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false);
        qrScan.setPrompt("사각형에 QR코드를 맞춰주세요");
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "취소하였습니다", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "인식되었습니다 " + result.getContents(), Toast.LENGTH_SHORT).show();
                try {
                    JSONObject obj = new JSONObject(result.getContents());
                    //textViewUserNumber.setText(obj.getString(""));
                    textViewBuildingFloor.setText(obj.getString("buildingFloor"));
                    textViewBuildingName.setText(obj.getString("buildingName"));
                    textViewUpdateTime.setText(obj.getString("updateDate"));
                } catch (JSONException e) {
                    e.printStackTrace();
                    // textViewUserNumber.setText(result.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void writeNewUser(int userId, String getBuildingFloor, String getBuildingName, String getUpdateTime) {
        User user = new User(getBuildingFloor, getBuildingName, getUpdateTime);

        mDatabase.child("timeline").child(String.valueOf(userId)).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(QrCodeActivity.this, "등록되었습니다", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QrCodeActivity.this,"등록을 실패했습니다", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void readUser() {
        mDatabase.child("timeline").child("1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(User.class) != null) {
                    User post = dataSnapshot.getValue(User.class);
                    Log.w("FireBaseData", "getData" + post.toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("FireBaseData", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }

}

