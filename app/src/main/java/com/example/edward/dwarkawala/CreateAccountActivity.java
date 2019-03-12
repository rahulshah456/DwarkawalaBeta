package com.example.edward.dwarkawala;

import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import java.util.concurrent.TimeUnit;


public class CreateAccountActivity extends AppCompatActivity {

    public static final String TAG  = CreateAccountActivity.class.getSimpleName();
    private TextInputEditText phoneNumberText;
    private CardView sendOtp,continueBtn;
    private ConstraintLayout verificationView;
    private Pinview verificationCode;
    private TextView resendCode,codeCountdown,numberText;
    private PhoneAuthProvider phoneAuthProvider;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private String phoneNumber;
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;
    private FirebaseAuth mAuth;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            View decor = getWindow().getDecorView();
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            //if (shouldChangeStatusBarTintToDark) {
//                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            } else {
//                // We want to change tint color to white again.
//                // You can also record the flags in advance so that you can turn UI back completely if
//                // you have set other flags before, such as translucent or full screen.
//                decor.setSystemUiVisibility(0);
//            }
        }

        phoneAuthProvider = PhoneAuthProvider.getInstance();
        phoneNumberText = (TextInputEditText) findViewById(R.id.phoneEditTextID);
        sendOtp = (CardView) findViewById(R.id.otpID);
        continueBtn = (CardView) findViewById(R.id.validateID);
        verificationView = (ConstraintLayout) findViewById(R.id.constraintLayout);
        resendCode = (TextView) findViewById(R.id.resendTextID);
        codeCountdown = (TextView) findViewById(R.id.countdownTextID);
        verificationCode = (Pinview) findViewById(R.id.otpViewID);
        numberText = (TextView) findViewById(R.id.numberTextID);

        phoneNumberText.requestFocus();


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                Log.d(TAG, "onVerificationCompleted:" + phoneAuthCredential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    phoneNumberText.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.", Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + s);

                // Save verification ID and resending token so we can use them later
                mVerificationId = s;
                mResendToken = forceResendingToken;

                // [START_EXCLUDE]
                // Update UI
                //updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]

            }
        };


        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resendCode.getCurrentTextColor()==getResources().getColor(R.color.colorAccent)){

                    resendVerificationCode(phoneNumber,mResendToken);
                    resendCode.setTextColor(getResources().getColor(R.color.light));
                    startCountDown(30000);

                }else {

                    Snackbar.make(findViewById(android.R.id.content), "Wait till countdown.", Snackbar.LENGTH_SHORT).show();

                }



            }
        });



        sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumber = "+91" + phoneNumberText.getText().toString().trim();

                if (validatePhoneNumber(phoneNumber)){
                    startPhoneNumberVerification(phoneNumber);
                    numberText.setText(phoneNumber);
                    sendOtp.setVisibility(View.GONE);
                    verificationView.setVisibility(View.VISIBLE);
                    continueBtn.setVisibility(View.VISIBLE);
                    verificationCode.requestFocus();
                    startCountDown(30000);

                }


            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = verificationCode.getValue().toString().trim();
                verifyPhoneNumberWithCode(mVerificationId,code);


            }
        });

    }

    public void startCountDown(int milliSeconds){


        new CountDownTimer(milliSeconds, 1000) {

            public void onTick(long millisUntilFinished) {

                if (millisUntilFinished/1000>=10){
                    codeCountdown.setText("00:" + millisUntilFinished / 1000);
                }else {
                    codeCountdown.setText("00:0" + millisUntilFinished / 1000);
                }

            }

            public void onFinish() {
                codeCountdown.setText("00:00");
                resendCode.setTextColor(getResources().getColor(R.color.colorAccent));
            }
        }.start();
    }


    private boolean validatePhoneNumber(String phoneNumber) {

        if (TextUtils.isEmpty(phoneNumber)) {
            phoneNumberText.setError("Invalid phone number.");
            return false;
        }else if (TextUtils.getTrimmedLength(phoneNumber)<10) {
            phoneNumberText.setError("Invalid phone number.");
            return false;
        }

        return true;
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
    }


    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }

    // [START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Log.d(TAG,String.valueOf(user.getPhoneNumber()));

                            Intent intent = new Intent(CreateAccountActivity.this,CompleteAccount.class);
                            intent.putExtra("phone",user.getPhoneNumber());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);


                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                //verificationCode.setHint("000000");

                            }

                        }
                    }
                });
    }

}
