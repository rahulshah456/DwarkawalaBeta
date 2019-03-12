package com.example.edward.dwarkawala;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    public TextInputEditText phoneNumber,password;
    public CardView loginButton,merchantLogin,facebookLogin,googleLogin,emailLogin;
    public TextView createNewAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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

        phoneNumber = (TextInputEditText) findViewById(R.id.phoneEditTextID);
        password = (TextInputEditText) findViewById(R.id.passwordID);
        loginButton = (CardView) findViewById(R.id.loginID);
        merchantLogin = (CardView) findViewById(R.id.merchantLoginID);
        facebookLogin = (CardView) findViewById(R.id.facebookLoginID);
        googleLogin = (CardView) findViewById(R.id.googleLoginID);
        emailLogin = (CardView) findViewById(R.id.emailLoginID);
        createNewAccount = (TextView) findViewById(R.id.newAccountID);



        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
                startActivity(intent);

            }
        });



    }
}
