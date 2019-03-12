package com.example.edward.dwarkawala;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import java.io.File;

public class CompleteAccount extends AppCompatActivity {

    public static final String TAG   = CompleteAccount.class.getSimpleName();
    private CardView profilePic,getStarted,googleSync,facebookSync;
    private TextInputEditText name,email,password;
    private Bundle bundle;
    private TextView phone;
    private String phoneNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_account);

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

        bundle = getIntent().getExtras();
        phone = (TextView) findViewById(R.id.phoneNumberID);
        profilePic = (CardView) findViewById(R.id.profilePicID);
        getStarted = (CardView) findViewById(R.id.startedID);
        googleSync = (CardView) findViewById(R.id.googleLoginID);
        facebookSync = (CardView) findViewById(R.id.facebookLoginID);
        name = (TextInputEditText) findViewById(R.id.nameEditTextID);
        email = (TextInputEditText) findViewById(R.id.emailEditTextID);
        password = (TextInputEditText) findViewById(R.id.passwordEditTextID);


        if (bundle!=null){

            phoneNumber = bundle.getString("phone");
            phone.setText(phoneNumber);
        }


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
