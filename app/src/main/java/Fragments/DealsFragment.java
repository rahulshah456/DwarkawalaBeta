package Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.example.edward.dwarkawala.FullFeed;
import com.example.edward.dwarkawala.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapters.DealsAdapter;
import Models.MerchantData;

public class DealsFragment extends Fragment {
    private static final String FIREBASE_DATABASE_LOCATION = "Merchants";
    private static final String TAG = DealsFragment.class.getSimpleName();
    DatabaseReference recyclerDatabaseReference,likeDatabase;
    DealsAdapter imageListAdaper;
    FirebaseAuth mAuth;
    //    private AdView mAdView;
    FirebaseDatabase database;
    ValueEventListener eventListener;
    Context mContext;
    RecyclerView imageRecyclerView;
    ImageView noConnectionGif;
    RelativeLayout loadingLayout,connectionLost;
    RecyclerView.LayoutManager mLayoutManager;
    public static List<MerchantData> merchantDataList;
    MerchantData merchantData;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deals,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mContext = getActivity();
        imageRecyclerView = (RecyclerView) view.findViewById(R.id.dealsRecyclerID);
        imageRecyclerView.setAdapter(null);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerDatabaseReference = FirebaseDatabase.getInstance().getReference("Merchants");


        // Set up RecyclerView
        mLayoutManager = new GridLayoutManager(mContext,1);
        imageListAdaper = new DealsAdapter(mContext);
        imageRecyclerView.setLayoutManager(mLayoutManager);
        imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imageRecyclerView.setAdapter(imageListAdaper);



        DownloadShops();




    }


    public void DownloadShops(){

        Query query;
        query = FirebaseDatabase.getInstance().getReference()
                .child(FIREBASE_DATABASE_LOCATION)
                .orderByKey();
        query.keepSynced(true);



        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                merchantDataList = new ArrayList<>();
                merchantDataList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    merchantData = postSnapshot.getValue(MerchantData.class);


                    merchantDataList.add(merchantData);
                    //Collections.reverse(wallpaperList);


                }
                imageListAdaper.addAll(merchantDataList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //loadingLayout.setVisibility(View.INVISIBLE);
                //loadingLayout.startAnimation(slide_down);
                Log.d(TAG,databaseError.getDetails());

            }
        });




    }
}