package com.example.edward.dwarkawala;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.goodiebag.pinview.Pinview;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import Models.AccountData;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.edward.dwarkawala.CompleteAccount.ACCOUNT_DATA;
import static com.example.edward.dwarkawala.CompleteAccount.COMPLETED;
import static com.example.edward.dwarkawala.CompleteAccount.REQUEST_LOCATION;
import static com.example.edward.dwarkawala.CreateAccountActivity.REQUEST_CODE_ASK_PERMISSIONS;
import static com.example.edward.dwarkawala.SplashActivity.PROGRESS;

public class LoginActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_login);

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


        ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.CAMERA},
                REQUEST_CODE_ASK_PERMISSIONS);





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

        storageReference = FirebaseStorage.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        mainContent = (ConstraintLayout) findViewById(R.id.mainContentID);
        verificationContent = (ConstraintLayout) findViewById(R.id.constraintLayout);
        phoneAuthProvider = PhoneAuthProvider.getInstance();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
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


        googleApiClient = new GoogleApiClient.Builder(LoginActivity.this)
                .enableAutoManage(LoginActivity.this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } )
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        EnableLocationServices();


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
                    //phoneNumberText.setError("Invalid phone number.");
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


        createNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,CreateAccountActivity.class);
                intent.putExtra("type","customer");
                startActivity(intent);

            }
        });



        phoneText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                num = "+91"+s.toString();
                Log.d(TAG,num);
                isRegisteredUser(num);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phoneNumberData  = "+91" + phoneText.getText().toString();

                isRegisteredUser(phoneNumberData);
                //progressDialog.show();

                if (isRegistered){
                    //Toast.makeText(LoginActivity.this, "Registered Account!", Toast.LENGTH_SHORT).show();
                    mainContent.setVisibility(View.GONE);
                    loginButton.setVisibility(View.GONE);
                    merchantLogin.setVisibility(View.GONE);
                    infoText.setVisibility(View.GONE);
                    verificationContent.setVisibility(View.VISIBLE);
                    contButton.setVisibility(View.VISIBLE);

                    phoneText.setFocusable(false);
                    phoneText.setEnabled(false);



                    //verification code sending
                    startPhoneNumberVerification(num);
                    startCountDown(30000);



                }else {

                    Toast.makeText(LoginActivity.this, "Not Registered!", Toast.LENGTH_SHORT).show();
                }


            }
        });


        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (resendCode.getCurrentTextColor()==getResources().getColor(R.color.colorAccent)){

                    resendVerificationCode(num,mResendToken);
                    resendCode.setTextColor(getResources().getColor(R.color.light));
                    startCountDown(30000);

                }else {

                    Snackbar.make(findViewById(android.R.id.content), "Wait till countdown.", Snackbar.LENGTH_SHORT).show();

                }



            }
        });


        contButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String code = verificationCode.getValue().toString().trim();
                verifyPhoneNumberWithCode(mVerificationId,code);
            }
        });



        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignIn();

            }
        });


        facebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Snackbar.make(findViewById(android.R.id.content), "Can't Connect to Facebook.", Snackbar.LENGTH_SHORT).show();

                    }
                },500);

            }
        });



        pintrestLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Snackbar.make(findViewById(android.R.id.content), "Not Available!", Snackbar.LENGTH_SHORT).show();

                    }
                },500);

            }
        });



        merchantLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(LoginActivity.this,MerchantActivity.class);
                startActivity(intent);


            }
        });



        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        //TODO: UI updates.
                    }
                }
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);



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


    public void isRegisteredGoogleUser(final String email){

        Query query;
        query = FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE_DATABASE_LOCATION)
                .orderByKey();
        query.keepSynced(true);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    accountData = postSnapshot.getValue(AccountData.class);

                    if (accountData.getEmail().equals(email)){
                        isRegisteredGoogle = true;
                        return;
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void isRegisteredUser(final String phoneNumber){


        Query query;
        query = FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE_DATABASE_LOCATION)
                .orderByKey();
        query.keepSynced(true);


        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    accountData = postSnapshot.getValue(AccountData.class);

                    if (accountData.getPhoneNumber().equals(phoneNumber)){
                        isRegistered = true;
                        return;
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }



    private boolean checkLocationPermission()
    {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void EnableLocationServices() {

        if (googleLocationClient== null){
            //Initializing GoogleApiClient
            googleLocationClient = new GoogleApiClient.Builder(LoginActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleLocationClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleLocationClient.connect();



            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleLocationClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(LoginActivity.this, REQUEST_LOCATION);


                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }


    }


    public void GooglePopup(final FirebaseUser newUser) {


        if (checkLocationPermission()){
            LocationServices.getFusedLocationProviderClient(LoginActivity.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //TODO: UI updates.
                    if (location!=null){
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                    }

                }
            });
        }


        final TextInputEditText phoneNumber;
        ImageView profilePic;
        TextView name, email, buttonText;
        final Pinview verificationCode;
        TextView resendCode, codeCountdown;
        final CardView verify, cont;
        final ConstraintLayout verificationView;

        final Dialog playlistDialog = new Dialog(LoginActivity.this);
        playlistDialog.setContentView(R.layout.google_popup);
        Objects.requireNonNull(playlistDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        playlistDialog.setCancelable(false);


        phoneNumber = (TextInputEditText) playlistDialog.findViewById(R.id.phoneEditTextID);
        profilePic = (ImageView) playlistDialog.findViewById(R.id.profileImageView);
        name = (TextView) playlistDialog.findViewById(R.id.profileNameID);
        email = (TextView) playlistDialog.findViewById(R.id.profileMailID);
        verificationCode = playlistDialog.findViewById(R.id.otpViewID);
        resendCode = (TextView) playlistDialog.findViewById(R.id.resendTextID);
        codeCountdown = (TextView) playlistDialog.findViewById(R.id.countdownTextID);
        verify = (CardView) playlistDialog.findViewById(R.id.verifyID);
        buttonText = (TextView) playlistDialog.findViewById(R.id.buttonID);
        cont = (CardView) playlistDialog.findViewById(R.id.continueID);
        verificationView = (ConstraintLayout) playlistDialog.findViewById(R.id.constraintLayout);

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String code = verificationCode.getValue().toString().trim();
                verifyPhoneNumberWithCode(mVerificationId, code);



            }
        });


        Glide.with(this)
                .load(newUser.getPhotoUrl())
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(profilePic);

        name.setText(newUser.getDisplayName().toString());
        email.setText(newUser.getEmail().toString());



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                number = "+91" + phoneNumber.getText().toString().trim();

                if (phoneNumber.getText().length()>=10){

                    startPhoneNumberVerification(number);
                    verificationView.setVisibility(View.VISIBLE);
                    verificationCode.requestFocus();
                    //startCountDown(30000);
                    verify.setVisibility(View.GONE);
                    cont.setVisibility(View.VISIBLE);

                }else {

                    phoneNumber.setError("Invalid Number!");
                }







            }
        });


        playlistDialog.show();

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

                            num = String.valueOf(user.getPhoneNumber());

                            Log.d(TAG,String.valueOf(user.getPhoneNumber()));


                            progressDialog.show();
                            editor.putInt(PROGRESS,1);
                            editor.apply();
                            CreateNewUserTask newUserTask = new CreateNewUserTask();
                            newUserTask.execute();




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




    public void GoogleSignIn(){
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent, RequestSignInCode);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RequestSignInCode){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()){
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                GoogleUserAuth(googleSignInAccount);
            }

        }


        if (requestCode == REQUEST_LOCATION) {
            // Check for the integer request code originally supplied to startResolutionForResult()

            if (resultCode == RESULT_OK) {

                Log.d(TAG, "Location Services:ENABLED");

            } else {

                Log.d(TAG, "Location Services:DISABLED");
                finish();

            }
        }
    }



    public void GoogleUserAuth(GoogleSignInAccount account){
        final String email = account.getEmail().toString().trim();
        final String name = account.getDisplayName().toString().trim();
        final String profilePic = account.getPhotoUrl().toString().trim();

        progressDialog.setContentView(R.layout.progress_dialog);

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        Log.d(TAG, "Signing in with Google:" + account.getId());

        progressDialog.setCancelable(false);
        progressDialog.show();

        isRegisteredGoogleUser(account.getEmail());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> AuthResultTask) {

                        if (AuthResultTask.isSuccessful()){

                            inProgress = true;

                            // Getting Current Login user details.
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = mAuth.getCurrentUser().getUid();

                            if (isRegisteredGoogle){

                                CompleteUserTask completeUserTask = new CompleteUserTask();
                                completeUserTask.execute();

                            }else {

                                progressDialog.dismiss();
                                gEmail = user.getEmail();
                                gName = user.getDisplayName();
                                gProfileUrl = String.valueOf(user.getPhotoUrl());
                                GooglePopup(user);
                            }







                        }else {
                            Toast.makeText(LoginActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }



    public class CompleteUserTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {


            Query query;
            query = FirebaseDatabase.getInstance().getReference()
                    .child(FIREBASE_DATABASE_LOCATION)
                    .orderByKey();
            query.keepSynced(true);


            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        accountData = postSnapshot.getValue(AccountData.class);


                        if (isRegistered){

                            if (accountData.getPhoneNumber().equals(num)){

                                Gson gson = new Gson();
                                String accountJson = gson.toJson(accountData);
                                editor.putString(ACCOUNT_DATA, accountJson);
                                editor.apply();
                                editor.putInt(PROGRESS,1);
                                editor.apply();
                                inProgress = false;
                                progressDialog.dismiss();
                            }
                        }

                        if (isRegisteredGoogle){

                            if (accountData.getEmail().equals(mAuth.getCurrentUser().getEmail())){

                                Gson gson = new Gson();
                                String accountJson = gson.toJson(accountData);
                                editor.putString(ACCOUNT_DATA, accountJson);
                                editor.apply();
                                editor.putInt(PROGRESS,1);
                                editor.apply();
                                inProgress = false;
                                progressDialog.dismiss();
                            }
                        }


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });




            return COMPLETED;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    public class CreateNewUserTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {

                final String uploadId = databaseReference.push().getKey();
                Log.d(TAG,"PhoneNumber:" + num);
                AccountData accountData = new AccountData(gName,gEmail, num,gProfileUrl,String.valueOf(latitude),String.valueOf(longitude));
                databaseReference.child(uploadId).setValue(accountData);


                Gson gson = new Gson();
                String accountJson = gson.toJson(accountData);
                editor.putString(ACCOUNT_DATA, accountJson);
                editor.apply();
                editor.putInt(PROGRESS,1);
                editor.apply();

            return COMPLETED;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            progressDialog.dismiss();

            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }
}
