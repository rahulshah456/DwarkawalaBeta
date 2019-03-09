package Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.edward.navigation01.R;

import java.util.ArrayList;
import java.util.List;

import Adapters.ConnectionDetector;
import Adapters.PostListAdapter;
import Models.Response;
import Models.Thumbnail;
import Retrofit.DwarkawalaApi;
import Retrofit.ImageDataApi;
import retrofit2.Call;
import retrofit2.Callback;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FeedsFragment extends Fragment {

    private static final String TAG = FeedsFragment.class.getSimpleName();
    public static List<Response> postList;
    public static List<Thumbnail> thumbnailList;
    Context mContext;
    RecyclerView imageRecyclerView;
    ImageView noConnectionGif;
    RelativeLayout loadingLayout,connectionLost;
    PostListAdapter postListAdapter;
    boolean isLoading = false;
    static int page = 1;
    RecyclerView.LayoutManager mLayoutManager;
    private Parcelable mListState;
    Animation slide_down,slide_up;
    ConnectionDetector networkState;
    SwipeRefreshLayout swipeLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feeds,container,false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getActivity();
        imageRecyclerView = (RecyclerView) view.findViewById(R.id.images_recycler_view);
        connectionLost = (RelativeLayout) view.findViewById(R.id.noConnectionLayoutID);
        loadingLayout = (RelativeLayout) view.findViewById(R.id.loadingRecyclerID);
        noConnectionGif = (ImageView) view.findViewById(R.id.no_connectionImageID);
        networkState = new ConnectionDetector(mContext);
        swipeLayout = view.findViewById(R.id.swipeRefreshID);




        generateRecyclerView();




        // Adding Listener
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh layouts
                //Toast.makeText(mContext, "Works!", Toast.LENGTH_LONG).show();
                connectionLost.setVisibility(View.INVISIBLE);
                postListAdapter.clearList();
                imageRecyclerView.setVisibility(View.VISIBLE);
                DownloadPosts();
                generateRecyclerView();
                // To keep animation for 4 seconds
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        // Stop animation (This will be after 3 seconds)
                        swipeLayout.setRefreshing(false);
                    }
                }, 4000); // Delay in millis
            }
        });

        // Scheme colors for animation
        swipeLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light)
        );

        //Load animation
        slide_down = AnimationUtils.loadAnimation(mContext, R.anim.slide_down);
        slide_up = AnimationUtils.loadAnimation(mContext, R.anim.slide_up);




        // Load images on app run
        DownloadPosts();



        // Load more images onScroll end
        imageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if end of page has been reached
                if( !isLoading && ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition() == postListAdapter.getItemCount()-1 ){
                    isLoading = true;
                    Log.d(TAG , "End has reached, loading more images!");
                    loadingLayout.startAnimation(slide_up);
                    loadingLayout.setVisibility(View.VISIBLE);
                    page++;
                    DownloadPosts();
                }
            }
        });


    }


    public void generateRecyclerView(){
        // Set up RecyclerView
        postList = new ArrayList<Models.Response>();
        mLayoutManager = new GridLayoutManager(mContext, 1);
        postListAdapter = new PostListAdapter(mContext);
        imageRecyclerView.setLayoutManager(mLayoutManager);
        imageRecyclerView.setItemAnimator(new DefaultItemAnimator());
        imageRecyclerView.setAdapter(postListAdapter);
    }



    public void DownloadPosts() {

        DwarkawalaApi.Factory.getInstance().getPosts("post",page).enqueue(new Callback<List<Response>>() {
            @Override
            public void onResponse(Call<List<Response>> call, retrofit2.Response<List<Response>> response) {

                List<Response> newResponse = response.body();
                postListAdapter.addPosts(newResponse);
                isLoading = false;
                loadingLayout.setVisibility(View.INVISIBLE);
                loadingLayout.startAnimation(slide_down);

            }

            @Override
            public void onFailure(Call<List<Response>> call, Throwable t) {

                Log.e(TAG, "Failed " + t.getMessage());
                isLoading = false;
                // reduce page by 1 as page failed to load
                page--;
                imageRecyclerView.setVisibility(View.GONE);

                Glide.with(mContext)
                        .asGif()
                        .load(R.drawable.no_connection)
                        .into(noConnectionGif);
                connectionLost.setVisibility(View.VISIBLE);

            }
        });







    }



}
