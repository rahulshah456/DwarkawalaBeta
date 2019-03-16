package com.example.edward.dwarkawala;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import Models.AccountData;

import static com.example.edward.dwarkawala.CompleteAccount.ACCOUNT_DATA;
import static com.example.edward.dwarkawala.CompleteAccount.COMPLETED;
import static com.example.edward.dwarkawala.SplashActivity.PROGRESS;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final String FIREBASE_DATABASE_LOCATION = "Users";
    private static final int RequestSignInCode = 5;
    public TextInputEditText emailText,password;
    public CardView loginButton,merchantLogin,facebookLogin,googleLogin,emailLogin;
    public TextView createNewAccount;
    private StorageReference storageReference;
    private DatabaseReference databaseReference, accountReference;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Dialog progressDialog;
    private AccountData accountData;
    public String number;
    private String gPassword,gName,gEmail,gProfileUrl;
    public FirebaseUser latestUser;
    private PhoneAuthProvider phoneAuthProvider;
    public static GoogleApiClient googleApiClient;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

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


        emailText = (TextInputEditText) findViewById(R.id.phoneEditTextID);
        password = (TextInputEditText) findViewById(R.id.passwordID);
        loginButton = (CardView) findViewById(R.id.loginID);
        merchantLogin = (CardView) findViewById(R.id.merchantLoginID);
        facebookLogin = (CardView) findViewById(R.id.facebookLoginID);
        googleLogin = (CardView) findViewById(R.id.googleLoginID);
        emailLogin = (CardView) findViewById(R.id.emailLoginID);
        createNewAccount = (TextView) findViewById(R.id.newAccountID);

        storageReference = FirebaseStorage.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        phoneAuthProvider = PhoneAuthProvider.getInstance();
        preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        editor  = preferences.edit();

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
                startActivity(intent);

            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                SignInUser();

            }
        });


        googleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoogleSignIn();

            }
        });






    }

    public void GooglePopup(final FirebaseUser newUser) {

        final TextInputEditText password, phoneNumber;
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

        password = (TextInputEditText) playlistDialog.findViewById(R.id.passwordEditTextID);
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

                gPassword = password.getText().toString();



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

                startPhoneNumberVerification(number);
                verificationView.setVisibility(View.VISIBLE);
                verificationCode.requestFocus();
                //startCountDown(30000);


                verify.setVisibility(View.GONE);
                cont.setVisibility(View.VISIBLE);


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
                            Log.d(TAG,String.valueOf(user.getPhoneNumber()));



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

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> AuthResultTask) {

                        if (AuthResultTask.isSuccessful()){

                            // Getting Current Login user details.
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = mAuth.getCurrentUser().getUid();

                            progressDialog.dismiss();

                            gEmail = user.getEmail();
                            gName = user.getDisplayName();
                            gProfileUrl = String.valueOf(user.getPhotoUrl());
                            GooglePopup(user);



                        }else {
                            Toast.makeText(LoginActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                    }
                });
    }


    public void SignInUser(){


        mAuth.signInWithEmailAndPassword(emailText.getText().toString(),password.getText().toString())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Log.d(TAG,"Login Failed");
                        progressDialog.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Login Failed!", Snackbar.LENGTH_SHORT).show();

                    }
                })
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {


                        Log.d(TAG,"Login Success");
                        progressDialog.dismiss();

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);



                    }
                });


    }



    public class CreateNewUserTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {

                final String uploadId = databaseReference.push().getKey();

                AccountData accountData = new AccountData(gName,gEmail,
                    number ,gProfileUrl,String.valueOf(77.215),String.valueOf(44.6654),gPassword);

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


            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


}
