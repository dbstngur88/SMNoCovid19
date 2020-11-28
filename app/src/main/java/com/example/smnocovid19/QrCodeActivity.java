package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import static java.security.AccessController.getContext;

public class QrCodeActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private FirebaseUser currentUser;
    private Button btn_check, btn_cancel;
    private TextView textViewUserNumber, textViewBuildingFloor, textViewBuildingName;
    private IntentIntegrator qrScan;
    private DatabaseReference mDatabase;
    String fStoreStudentNumber;
    String userEmail;
    Intent intent;
    int a = 0;

    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String formatDate = sdfNow.format(date); // 현재시간을 가지고있음.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        textViewUserNumber = (TextView) findViewById(R.id.textViewUserNumber);
        textViewBuildingName = (TextView) findViewById(R.id.textViewBuildingName);
        textViewBuildingFloor = (TextView) findViewById(R.id.textViewBuildingFloor);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        currentUser = fAuth.getCurrentUser();
        userEmail = currentUser.getEmail();

        btn_check = findViewById(R.id.check);
        btn_cancel = findViewById(R.id.cancel);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        readUser();
        getUserInfo();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(QrCodeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getUserNumber = textViewUserNumber.getText().toString();
                String getBuildingFloor = textViewBuildingFloor.getText().toString();
                String getBuildingName = textViewBuildingName.getText().toString();
                String getUpdateTime = formatDate;

                HashMap result = new HashMap<>();
                result.put("학번", getUserNumber);
                result.put("층", getBuildingFloor);
                result.put("건물", getBuildingName);
                result.put("입장시간", getUpdateTime);

                a = a + 1;
                writeNewUser(a, getUserNumber, getBuildingFloor, getBuildingName, getUpdateTime);
                intent = new Intent(QrCodeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

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
                Toast.makeText(QrCodeActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(QrCodeActivity.this, "인식되었습니다 " , Toast.LENGTH_SHORT).show();

                try {
                    JSONObject obj = new JSONObject(result.getContents());

//                   textViewUserNumber.setText(obj.getString("studentnumber"));
                    textViewBuildingName.setText(obj.getString("buildingName"));
                    textViewBuildingFloor.setText(obj.getString("buildingFloor"));
                    String getsBuildingFloor = textViewBuildingFloor.getText().toString();
                    String getsBuildingName = textViewBuildingName.getText().toString();
                    writeNewUser(a, fStoreStudentNumber, getsBuildingFloor, getsBuildingName, formatDate);
//                    textViewUpdateTime.setText(obj.getString("updateDate"));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                    textViewBuildingName.setText(result.getContents());
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            intent = new Intent(QrCodeActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    private void writeNewUser(int userId, String getUserNumber, String getBuildingFloor, String getBuildingName, String getUpdateTime) {
        User user = new User(getBuildingFloor, getBuildingName, getUpdateTime);

        mDatabase.child("timeline_" + getUserNumber).child(getUpdateTime).setValue(user)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
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
    public void getUserInfo() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                fStoreStudentNumber = (String) document.get("studentnumber");
                                textViewUserNumber.setText(fStoreStudentNumber);
                            }
                        } else {
                            Toast.makeText(QrCodeActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

