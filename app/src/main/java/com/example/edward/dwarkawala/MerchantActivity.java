package com.example.edward.dwarkawala;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import Models.AccountData;

public class MerchantActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final String FIREBASE_DATABASE_LOCATION = "Users";
    private static final int RequestSignInCode = 5;
    public TextInputEditText phoneText,password;
    public CardView loginButton,merchantLogin,facebookLogin,googleLogin,pintrestLogin,contButton;
    public TextView createNewAccount,infoText;
    public ConstraintLayout mainContent,verificationContent;
    private StorageReference storageReference;
    private DatabaseReference databaseReference, accountReference;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private double latitude = 0;
    private double longitude = 0;
    private FirebaseAuth mAuth;
    private Dialog progressDialog;
    private AccountData accountData;
    public String number,phoneNumberData;
    private String gPassword,gName,gEmail,gProfileUrl;
    public FirebaseUser latestUser;
    private PhoneAuthProvider phoneAuthProvider;
    public static GoogleApiClient googleApiClient,googleLocationClient;
    private boolean mVerificationInProgress = false;
    private String mVerificationId,num;
    private FusedLocationProviderClient locationProviderClient;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Boolean inProgress = false;
    private Boolean isRegistered = false,isRegisteredGoogle = false;
    private Pinview verificationCode;
    private TextView resendCode,codeCountdown,numberText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_merchant);


        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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


        phoneText = (TextInputEditText) findViewById(R.id.phoneEditTextID);
        //password = (TextInputEditText) findViewById(R.id.passwordID);
        loginButton = (CardView) findViewById(R.id.loginID);
        merchantLogin = (CardView) findViewById(R.id.merchantLoginID);
        facebookLogin = (CardView) findViewById(R.id.facebookLoginID);
        googleLogin = (CardView) findViewById(R.id.googleLoginID);
        pintrestLogin = (CardView) findViewById(R.id.emailLoginID);
        contButton = (CardView) findViewById(R.id.continueID);
        infoText = (TextView) findViewById(R.id.infoTextID);
        createNewAccount = (TextView) findViewById(R.id.newAccountID);

        storageReference = FirebaseStorage.getInstance().getReference("Merchants");
        databaseReference = FirebaseDatabase.getInstance().getReference("Merchants");
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mainContent = (ConstraintLayout) findViewById(R.id.mainContentID);
        verificationContent = (ConstraintLayout) findViewById(R.id.constraintLayout);
        phoneAuthProvider = PhoneAuthProvider.getInstance();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(MerchantActivity.this);
        editor  = preferences.edit();


        phoneAuthProvider = PhoneAuthProvider.getInstance();
        resendCode = (TextView) findViewById(R.id.resendTextID);
        codeCountdown = (TextView) findViewById(R.id.countdownTextID);
        verificationCode = (Pinview) findViewById(R.id.otpViewID);
        numberText = (TextView) findViewById(R.id.numberTextID);


        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);



        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();


        googleApiClient = new GoogleApiClient.Builder(MerchantActivity.this)
                .enableAutoManage(MerchantActivity.this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } )
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MerchantActivity.this, CreateAccountActivity.class);
                intent.putExtra("type","merchant");
                startActivity(intent);

            }
        });
    }
}
