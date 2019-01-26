  package com.douglas.whatappclone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

  public class MainActivity extends AppCompatActivity {

    private EditText mphoneNumber;
    private EditText mCode;
    private Button mSend;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        userIsLoggedIn();

        mphoneNumber.findViewById(R.id.phoneNumber);
        mCode.findViewById(R.id.code);
        mSend.findViewById(R.id.verif);

        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                SignInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

        };


        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPhoneNumberVerification();
            }
        });

    }

      private void SignInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

          FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if(task.isSuccessful()){
                      userIsLoggedIn();
                  }
              }
          });
      }

      private void userIsLoggedIn(){
          FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
          if(user!=null){
              startActivity(new Intent(getApplicationContext(),MainPageActivity.class));
              finish();
              return;
          }
      }


    private void startPhoneNumberVerification(){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                mphoneNumber.getText().toString(),60,TimeUnit.SECONDS,this,mCallbacks);
    }
}
