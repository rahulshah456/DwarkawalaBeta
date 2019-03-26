package com.example.edward.dwarkawala;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.media.RatingCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ShopActivity extends AppCompatActivity {

    private static final String TAG = "ShopActivity";

    ImageView shopImage;
    TextView shopName;
    TextView shopIntro;
    TextView shopAddress, showShopAddress;
    private ImageButton backButton;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        shopImage = findViewById(R.id.shopImage);
        shopName = findViewById(R.id.shopName);
        shopAddress = findViewById(R.id.shopAddress);
        backButton = findViewById(R.id.backButtonID);
        showShopAddress = findViewById(R.id.shopFullAddress);

        getIncomingIntent();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getIncomingIntent(){

        if(getIntent().hasExtra("shop_image") && getIntent().hasExtra("shop_name")){

            String shopImage = getIntent().getStringExtra("shop_image");
            String shopName = getIntent().getStringExtra("shop_name");
            String shopAddress = getIntent().getStringExtra("shop_address");

            setdata(shopImage, shopName, shopAddress);
        }
    }
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setdata(String shopImage, String shopName, String shopAddress){
        this.shopName.setText(shopName);

        this.showShopAddress.setText(shopAddress);
        this.showShopAddress.setTextColor(R.color.greenPressed);


        Glide.with(this)
                .asBitmap()
                .load(shopImage)
                .into(this.shopImage);


    }
}
