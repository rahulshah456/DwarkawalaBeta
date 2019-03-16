package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.edward.dwarkawala.FullFeed;
import com.example.edward.dwarkawala.R;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Fragments.FeedsFragment;
import Models.Response;
import Models.Thumbnail;
import Retrofit.ImageDataApi;
import retrofit2.Call;
import retrofit2.Callback;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.MyViewHolder> {



    private static final String TAG = PostListAdapter.class.getSimpleName();
    private static RecyclerViewClickListener itemListener;
    private static SwipeRefreshLayout swipeRefreshLayout;
    Context mContext;

    public PostListAdapter(Context context) {
        mContext = context;
        //this.itemListener = itemListener;
    }

    public interface RecyclerViewClickListener
    {
        public void recyclerViewListClicked(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title;
        public RelativeTimeTextView time;
        public ImageView thumbnail;
        public ImageButton share,bookmark;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            title = (TextView) itemView.findViewById(R.id.postTitleID);
            time = (RelativeTimeTextView) itemView.findViewById(R.id.timeTextID);
            thumbnail = (ImageView) itemView.findViewById(R.id.imageThumbnailID);
            share = (ImageButton) itemView.findViewById(R.id.shareButtonID);
            bookmark = (ImageButton) itemView.findViewById(R.id.bookmarkButtonID);



        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(ctx, "onClick works!", Toast.LENGTH_SHORT).show();
            Log.d("ImageListAdapter", "onClick works! Position: " + this.getLayoutPosition() + " clicked!");
            //itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
                Intent intent  = new Intent(mContext, FullFeed.class);
                intent.putExtra("item_position", getLayoutPosition());
                mContext.startActivity(intent);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.feed_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Response post = FeedsFragment.postList.get(position);

        final String postTitle = String.valueOf(post.getTitle().getRendered());
        holder.title.setText(postTitle);


        Date date = new Date();
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = inputFormat.parse(post.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.time.setReferenceTime(date.getTime());


<<<<<<< HEAD
//        int number = post.getFeatured_media();
//        ImageDataApi.ImageFactory.getInstance().getImageData(number).enqueue(new Callback<Thumbnail>() {
//            @Override
//            public void onResponse(Call<Thumbnail> call, retrofit2.Response<Thumbnail> response) {
//
//                Log.d(TAG,String.valueOf(response.body()));
//                Thumbnail newThumbnail = response.body();
//
//
//                if (newThumbnail!=null){
//                    Glide.with(mContext).load(newThumbnail.getSourceUrl())
//                            .thumbnail(0.5f)
//                            .transition(withCrossFade())
//                            .apply(new RequestOptions()
//                                    .centerCrop()
//                                    .diskCacheStrategy(DiskCacheStrategy.ALL))
//                            .into(holder.thumbnail);
//                }
//
//
//
//            }
//
//            @Override
//            public void onFailure(Call<Thumbnail> call, Throwable t) {
//
//            }
//        });
=======

        int number = post.getFeatured_media();
        ImageDataApi.ImageFactory.getInstance().getImageData(number).enqueue(new Callback<Thumbnail>() {
            @Override
            public void onResponse(Call<Thumbnail> call, retrofit2.Response<Thumbnail> response) {

                Log.d(TAG,String.valueOf(response.body()));
                Thumbnail newThumbnail = response.body();


                if (newThumbnail!=null){
                    Glide.with(mContext).load(newThumbnail.getSourceUrl())
                            .thumbnail(0.5f)
                            .transition(withCrossFade())
                            .apply(new RequestOptions()
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL))
                            .into(holder.thumbnail);
                }



            }

            @Override
            public void onFailure(Call<Thumbnail> call, Throwable t) {

            }
        });
>>>>>>> 049e53a1f3181edfdc54fe2f631b76ab1fba58d1







    }


    @Override
    public int getItemCount() {
        return FeedsFragment.postList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addPosts(List<Response> list){
        FeedsFragment.postList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearList(){
        FeedsFragment.postList.clear();
        notifyDataSetChanged();
    }

    public List<Response> getItemList(){
        return FeedsFragment.postList;
    }


}
