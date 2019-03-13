package com.example.edward.dwarkawala;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private static final String PREF_NAME = "dwarkawala.com";
    public static final String PROGRESS = "progress";
    public SharedPreferences loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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

        mAuth = FirebaseAuth.getInstance();
        loginProgress = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (mAuth.getCurrentUser()!=null){
                    if (loginProgress.contains(PROGRESS)){

                        int loginState = loginProgress.getInt(PROGRESS,0);

                        switch (loginState){
                            case 0:
                                //User successfully verified his phone number
                                Intent intent_one = new Intent(SplashActivity.this,CompleteAccount.class);
                                intent_one.putExtra("phone",mAuth.getCurrentUser().getPhoneNumber());
                                intent_one.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent_one);
                                break;
                            case 1:
                                //User successfully completed registration
                                Intent intent_two = new Intent(SplashActivity.this,MainActivity.class);
                                intent_two.putExtra("phone",mAuth.getCurrentUser().getPhoneNumber());
                                intent_two.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent_two);
                                break;
                        }

                    }else {

                        Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                    }

                }else {

                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }




            }
        },1500);





    }
}
