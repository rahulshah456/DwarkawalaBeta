package Adapters;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.edward.navigation01.R;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Fragments.FeedsFragment;
import Models.Response;

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

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.postTitleID);
        }

        @Override
        public void onClick(View v) {
//            Toast.makeText(ctx, "onClick works!", Toast.LENGTH_SHORT).show();
            Log.d("ImageListAdapter", "onClick works! Position: " + this.getLayoutPosition() + " clicked!");
            //itemListener.recyclerViewListClicked(v, this.getLayoutPosition());
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

        String postTitle = String.valueOf(post.getTitle());

        holder.title.setText(postTitle);


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
    }

    public List<Response> getItemList(){
        return FeedsFragment.postList;
    }


}
