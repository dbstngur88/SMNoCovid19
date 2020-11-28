package com.example.smnocovid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    EditText txtEmail,txtPW, txtPWCheck, txtName,txtStudentnumber, txtMajor,  txtSex, txtPhoneNum;
    Button btnPWCheck, btnRegister;
    Boolean boolPW;

    String userEmail;
    String userPW;
    String userName;
    String userPWCheck;
    String userStudentNumber;
    String userMajor;
    String userSex;
    String userPhoneNum;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 화면을 portrait(세로) 화면으로 고정하고 싶은 경우


        setContentView(R.layout.activity_register);

        //액션바 안보이게 지정
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //파이어베이스 정보 가져오기
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //전역변수 링크
        txtEmail = findViewById(R.id.email);
        txtPW = findViewById(R.id.password);
        txtPWCheck = findViewById(R.id.checkPW);
        txtName = findViewById(R.id.name);
        txtStudentnumber = findViewById(R.id.studentnumber);
        txtMajor = findViewById(R.id.major);
        txtSex = findViewById(R.id.sex);
        txtPhoneNum = findViewById(R.id.phonenum);

        btnPWCheck = findViewById(R.id.btnPWCheck);
        btnRegister = findViewById(R.id.btnRegister);

        boolPW = false;
        txtPhoneNum.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            userEmail = txtEmail.getText().toString();
                            userPW = txtPW.getText().toString();
                            userPWCheck = txtPWCheck.getText().toString();
                            userName = txtName.getText().toString();
                            userStudentNumber = txtStudentnumber.getText().toString();
                            userMajor = txtMajor.getText().toString();
                            userSex = txtSex.getText().toString();
                            userPhoneNum = txtPhoneNum.getText().toString();
                            if (TextUtils.isEmpty(userEmail)) {
                                Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (TextUtils.isEmpty(userPW)) {
                                Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (userPW.length() < 8) {
                                Toast.makeText(RegisterActivity.this, "비밀번호는 8자리 이상입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (TextUtils.isEmpty(userPWCheck)) {
                                Toast.makeText(RegisterActivity.this, "비밀번호 확인란을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (boolPW == false){
                                Toast.makeText(RegisterActivity.this,"비밀번호 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (TextUtils.isEmpty(userName)) {
                                Toast.makeText(RegisterActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (userEmail.indexOf('@') < 0) {
                                Toast.makeText(RegisterActivity.this, "이메일 형식이 올바르지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (TextUtils.isEmpty(userStudentNumber)) {
                                Toast.makeText(RegisterActivity.this, "학번을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (TextUtils.isEmpty(userMajor)) {
                                Toast.makeText(RegisterActivity.this, "학과를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (TextUtils.isEmpty(userSex)) {
                                Toast.makeText(RegisterActivity.this, "성별을 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (TextUtils.isEmpty(userPhoneNum)) {
                                Toast.makeText(RegisterActivity.this, "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            }else if (boolPW == true){
                                registerUser();
                            }else {
                                Toast.makeText(RegisterActivity.this, "알수 없는 오류", Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
    }



    public void mClick(View view) {
        switch (view.getId()){
            case R.id.btnPWCheck :
                userPW = txtPW.getText().toString();
                userPWCheck = txtPWCheck.getText().toString();
                if (TextUtils.isEmpty(userPW)) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(userPWCheck)) {
                    Toast.makeText(RegisterActivity.this, "비밀번호 확인란을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }else if (userPW.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "비밀번호는 8자리 이상입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else{
                    if (userPW.equals(userPWCheck)){
                        boolPW = true;
                        txtPW.setEnabled(false);
                        txtPWCheck.setEnabled(false);
                        btnPWCheck.setEnabled(false);
                        Toast.makeText(RegisterActivity.this, "확인 완료", Toast.LENGTH_SHORT).show();
                    }else {
                        boolPW = false;
                        Toast.makeText(RegisterActivity.this, "입력한 비밀번호를 다시 확인하세요.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnRegister :
                userEmail = txtEmail.getText().toString();
                userPW = txtPW.getText().toString();
                userPWCheck = txtPWCheck.getText().toString();
                userName = txtName.getText().toString();
                userStudentNumber = txtStudentnumber.getText().toString();
                userMajor = txtMajor.getText().toString();
                userSex = txtSex.getText().toString();
                userPhoneNum = txtPhoneNum.getText().toString();
                if (TextUtils.isEmpty(userEmail)) {
                    Toast.makeText(RegisterActivity.this, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(userPW)) {
                    Toast.makeText(RegisterActivity.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (userPW.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "비밀번호는 8자리 이상입니다. 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(userPWCheck)) {
                    Toast.makeText(RegisterActivity.this, "비밀번호 확인란을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (boolPW == false){
                    Toast.makeText(RegisterActivity.this,"비밀번호 중복확인을 해주세요.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(userName)) {
                    Toast.makeText(RegisterActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (userEmail.indexOf('@') < 0) {
                    Toast.makeText(RegisterActivity.this, "이메일 형식이 올바르지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(userStudentNumber)) {
                    Toast.makeText(RegisterActivity.this, "학번을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(userMajor)) {
                    Toast.makeText(RegisterActivity.this, "학과를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(userSex)) {
                    Toast.makeText(RegisterActivity.this, "성별을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(userPhoneNum)) {
                    Toast.makeText(RegisterActivity.this, "휴대폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }else if (boolPW == true){
                    registerUser();
                }else {
                    Toast.makeText(RegisterActivity.this, "알수 없는 오류", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    public void registerUser(){
        fAuth.createUserWithEmailAndPassword(userEmail,userPW)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "회원가입 완료", Toast.LENGTH_LONG).show();
                            FirebaseUser user = fAuth.getCurrentUser();
                            userEmail = fAuth.getCurrentUser().getEmail();
                            userPW = txtPW.getText().toString();
                            userName = txtName.getText().toString();
                            userStudentNumber = txtStudentnumber.getText().toString();
                            userMajor = txtMajor.getText().toString();
                            userSex = txtSex.getText().toString();
                            userPhoneNum = txtPhoneNum.getText().toString();
                            DocumentReference documentReference = fStore.collection("users").document(userEmail);
                            Map<String, Object> userMap = new HashMap<>();
                            userMap.put("email", userEmail);
                            userMap.put("password", userPW);
                            userMap.put("name", userName);
                            userMap.put("studentnumber", userStudentNumber);
                            userMap.put("major", userMajor);
                            userMap.put("sex", userSex);
                            userMap.put("phonenum",userPhoneNum);
                            documentReference.set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "successed. user Profile is created for" + userEmail);
                                }
                            });
                            fAuth.signOut();
                            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                            intent.putExtra("Email",userEmail);
                            intent.putExtra("Password",userPW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(RegisterActivity.this, "등록실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}