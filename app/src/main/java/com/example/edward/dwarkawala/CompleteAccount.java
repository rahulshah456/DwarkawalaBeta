package com.example.edward.dwarkawala;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.example.edward.dwarkawala.SplashActivity.PROGRESS;

public class CompleteAccount extends AppCompatActivity {

    public static final String TAG = CompleteAccount.class.getSimpleName();
    private static final int PICK_IMAGE_REQUEST = 5;
    private static final int CAPTURE_IMAGE_REQUEST = 10;
    public static final String COMPLETED = "completed";
    private CardView profilePic, getStarted, googleSync, facebookSync;
    private TextInputEditText name, email, password;
    private ImageView profileImage;
    private Bundle bundle;
    private TextView phone;
    private String phoneNumber;
    private Dialog progressDialog;
    private StorageReference storageReference;
    private DatabaseReference databaseReference, accountReference;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    private double latitude = 0;
    private double longitude = 0;
    private String location;
    private File actualImage = null;
    private File compressedImage;
    private String imageFilePath;
    private Uri custom, imageUri;
    private CompressImageTask compressImageTask;
    private CreateNewUserTask createNewUserTask;
    private FusedLocationProviderClient locationProviderClient;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private GoogleApiClient googleApiClient;
    final static int REQUEST_LOCATION = 199;


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
        mAuth = FirebaseAuth.getInstance();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        phone = (TextView) findViewById(R.id.phoneNumberID);
        profilePic = (CardView) findViewById(R.id.profilePicID);
        getStarted = (CardView) findViewById(R.id.startedID);
        profileImage = (ImageView) findViewById(R.id.profileImageView);
        googleSync = (CardView) findViewById(R.id.googleLoginID);
        facebookSync = (CardView) findViewById(R.id.facebookLoginID);
        name = (TextInputEditText) findViewById(R.id.nameEditTextID);
        email = (TextInputEditText) findViewById(R.id.emailEditTextID);
        password = (TextInputEditText) findViewById(R.id.passwordEditTextID);
        storageReference = FirebaseStorage.getInstance().getReference("Users");
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        database = FirebaseDatabase.getInstance();
        compressImageTask = new CompressImageTask();
        createNewUserTask = new CreateNewUserTask();
        progressDialog = new Dialog(this);
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.setCancelable(false);
        preferences = PreferenceManager.getDefaultSharedPreferences(CompleteAccount.this);
        editor = preferences.edit();


        if (bundle != null) {

            phoneNumber = bundle.getString("phone");
            phone.setText(phoneNumber);
        }



        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfilePopup();
                Log.d(TAG,"Profile");
            }
        });



        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(email.getText()) || TextUtils.isEmpty(password.getText())) {

                    Snackbar.make(findViewById(android.R.id.content), "Please fill all details!", Snackbar.LENGTH_SHORT).show();
                    if (TextUtils.isEmpty(name.getText())) {
                        name.setError("Name cannot be empty");
                    }
                    if (TextUtils.isEmpty(email.getText())) {
                        email.setError("Email cannot be empty");
                    }
                    if (TextUtils.isEmpty(password.getText())) {
                        password.setError("Password cannot be empty");
                    }

                } else {

                    if (!isEmailValid(email.getText())) {
                        Snackbar.make(findViewById(android.R.id.content), "Please enter correct email address!", Snackbar.LENGTH_SHORT).show();
                        email.setError("Email not valid");
                        return;
                    }
                    if (password.getText().length() < 5) {
                        Snackbar.make(findViewById(android.R.id.content), "Password must contain more than 5 characters!", Snackbar.LENGTH_SHORT).show();
                        password.setError("Password not valid");
                        return;
                    }
                    if (compressImageTask.getStatus() == AsyncTask.Status.FINISHED) {
                        Log.d(TAG, "All Data OK");

                        if (!isLocationEnabled()){

                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            Log.d(TAG, "Location Services : DISABLED");


                        }else {


                            if (checkLocationPermission()){
                                LocationServices.getFusedLocationProviderClient(CompleteAccount.this).getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        //TODO: UI updates.
                                        if (location!=null){
                                            longitude = location.getLongitude();
                                            latitude = location.getLatitude();
                                        }

                                        progressDialog.show();
                                        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                                    @Override
                                                    public void onSuccess(AuthResult authResult) {
                                                        createNewUserTask.execute();
                                                    }
                                                });

                                    }
                                });
                            }
                        }


                    }else {
                        Snackbar.make(findViewById(android.R.id.content), "Please set your profile picture!", Snackbar.LENGTH_SHORT).show();
                    }

                }

            }
        });





        EnableLocationServices();

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


    public boolean isLocationEnabled()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            // This is new method provided in API 28
            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {
            // This is Deprecated in API 28
            int mode = Settings.Secure.getInt(getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

        }
    }



    private boolean checkCameraPermission(){

        String permission = Manifest.permission.CAMERA;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    private boolean checkStoragePermissions(){

        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private boolean checkLocationPermission()
    {
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    private void EnableLocationServices() {

        if (googleApiClient == null) {

            //Initializing GoogleApiClient
            googleApiClient = new GoogleApiClient.Builder(CompleteAccount.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            googleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error","Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            googleApiClient.connect();



            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(CompleteAccount.this, REQUEST_LOCATION);


                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                    }
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.d(TAG,"RequestCode:" + requestCode);
        Log.d(TAG,"ResultCode:" + resultCode);

        if (requestCode == PICK_IMAGE_REQUEST){

            if (data == null) {
                Snackbar.make(findViewById(android.R.id.content), "Failed to load picture!", Snackbar.LENGTH_SHORT).show();
                Log.d("Upload","Failed to load picture!");
                //finish();
                return;
            }

            try {

                profilePic.setOnClickListener(null);
                actualImage = FileUtil.from(this, data.getData());
                compressImageTask.execute(actualImage);
                //customCompressImage();

                Glide.with(this)
                        .load(actualImage.getAbsolutePath())
                        .transition(withCrossFade())
                        .apply(new RequestOptions()
                                .centerCrop())
                        .into(profileImage);




            } catch (IOException e) {
                Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }else if (requestCode == CAPTURE_IMAGE_REQUEST){

            if (resultCode == RESULT_OK) {
                profilePic.setOnClickListener(null);
                Glide.with(this)
                        .load(imageFilePath)
                        .transition(withCrossFade())
                        .apply(new RequestOptions()
                                .centerCrop())
                        .into(profileImage);
                compressImageTask.execute(actualImage);
            }

        }else if (requestCode == REQUEST_LOCATION) {
            // Check for the integer request code originally supplied to startResolutionForResult()

            if (resultCode == RESULT_OK){

                Log.d(TAG,"Location Services:ENABLED");

            }else {

                Log.d(TAG,"Location Services:DISABLED");
                finish();

            }


        }

    }


    public class  CompressImageTask extends AsyncTask<File,String,Uri>{


        @Override
        protected Uri doInBackground(File... files) {
            if (actualImage == null) {
                Toast.makeText(CompleteAccount.this,"Please choose an image!",Toast.LENGTH_LONG).show();
            } else {
                // Compress image in main thread using custom Compressor
                new Compressor(CompleteAccount.this)
                        .compressToFileAsFlowable(actualImage)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<File>() {
                            @Override
                            public void accept(File file) {
                                compressedImage = file;
//                            setCompressedImage();
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();
                                Toast.makeText(CompleteAccount.this,throwable.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });
                try {


                    compressedImage = new Compressor(CompleteAccount.this)
                            .setMaxWidth(1024)
                            .setMaxHeight(768)
                            .setQuality(60)
                            .setCompressFormat(Bitmap.CompressFormat.WEBP)
                            .setDestinationDirectoryPath(Environment.getExternalStorageDirectory().getAbsolutePath())
                            .compressToFile(actualImage);

                    custom = getImageContentUri(CompleteAccount.this,compressedImage);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CompleteAccount.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

            return custom;
        }

        @Override
        protected void onPostExecute(Uri uri) {
            super.onPostExecute(uri);

            Log.d(TAG,String.valueOf(uri));
        }
    }

    public class CreateNewUserTask extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {

            if (custom!=null){

                final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + ".JPG");
                final String uploadId = databaseReference.push().getKey();

                fileReference.putFile(custom)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        databaseReference.child(uploadId).child("name").setValue(name.getText().toString());
                                        databaseReference.child(uploadId).child("email").setValue(email.getText().toString());
                                        databaseReference.child(uploadId).child("phoneNumber").setValue(phoneNumber);
                                        databaseReference.child(uploadId).child("profilePic").setValue(uri.toString());
                                        databaseReference.child(uploadId).child("latitude").setValue(String.valueOf(latitude));
                                        databaseReference.child(uploadId).child("longitude").setValue(String.valueOf(longitude));
                                        databaseReference.child(uploadId).child("password").setValue(password.getText().toString());


                                        editor.putInt(PROGRESS,1);
                                        editor.apply();

                                    }
                                });



                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                Log.d(TAG,"FailedUpload:" + e.getLocalizedMessage());

                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                double progress = (100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                //progressBar.setProgress((int) progress);

                            }
                        });

            }
            else if (custom == null){
                Log.d(TAG,"Image Url is null");
            } else {
                Log.d(TAG,"No File Selected");
            }


            return COMPLETED;
        }


        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            progressDialog.dismiss();

            Intent intent = new Intent(CompleteAccount.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    public void ProfilePopup(){

        TextView camera,gallery;

        final Dialog playlistDialog = new Dialog(CompleteAccount.this);
        playlistDialog.setContentView(R.layout.profile_popup);
        Objects.requireNonNull(playlistDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        playlistDialog.setCancelable(true);

        camera = (TextView) playlistDialog.findViewById(R.id.cameraID);
        gallery = (TextView) playlistDialog.findViewById(R.id.galleryID);


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkCameraPermission()){
                    openCameraIntent();
                }
                playlistDialog.dismiss();

            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkStoragePermissions()){
                    pickImage();
                }
                playlistDialog.dismiss();

            }
        });



        playlistDialog.show();

    }


    private void pickImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }


    private void openCameraIntent(){
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(getPackageManager()) != null){
            //Create a file to store the image

            try {
                actualImage = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
                return;
            }
            if (actualImage != null) {
                imageUri = FileProvider.getUriForFile(this,getPackageName()+ ".provider", actualImage);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(pictureIntent, CAPTURE_IMAGE_REQUEST);
            }
        }
    }


    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        imageFilePath = image.getAbsolutePath();
        return image;
    }


    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
