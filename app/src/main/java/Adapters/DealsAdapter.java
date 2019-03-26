package Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.edward.dwarkawala.FullFeed;
import com.example.edward.dwarkawala.ShopActivity;
import com.example.edward.dwarkawala.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.ArrayList;
import java.util.List;
import Models.MerchantData;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.MyViewHolder> {

    private static final String TAG = DealsAdapter.class.getSimpleName();
    public Context mContext;
    public static List<MerchantData> mMerchants;



    public DealsAdapter(Context mContext) {
        this.mContext = mContext;
        mMerchants = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.deals_list,viewGroup,false);
        return new MyViewHolder(view);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView shopName, offers;
        CircularImageView shopImage;

        RelativeLayout parentLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            shopImage  = (CircularImageView) itemView.findViewById(R.id.shopImageID);
            shopName  =itemView.findViewById(R.id.shopNameID);
            offers = itemView.findViewById(R.id.offersID);
            parentLayout = itemView.findViewById(R.id.parentLayout);



        }
    }




    @Override
    public void onBindViewHolder(@NonNull final DealsAdapter.MyViewHolder myViewHolder, final int i) {


        myViewHolder.shopName.setText(mMerchants.get(i).getShopName());

        Glide.with(mContext)
                .load(mMerchants.get(i).getShopPic())
                .apply(new RequestOptions()
                        .centerCrop())
                .into(myViewHolder.shopImage);
        myViewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShopActivity.class);
                intent.putExtra("shop_image", mMerchants.get(i).getShopPic());
                intent.putExtra("shop_name", mMerchants.get(i).getShopName());
                intent.putExtra("shop_address", mMerchants.get(i).getShopAddress());
                mContext.startActivity(intent);
            }
        });



    }


    @Override
    public int getItemCount() {
        return mMerchants.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void addAll(List<MerchantData> newWalls) {
        int initialSize = mMerchants.size();
        mMerchants.addAll(newWalls);
        notifyItemRangeInserted(initialSize, newWalls.size());
    }

    public List<MerchantData> getItemList(){
        return mMerchants;
    }
}