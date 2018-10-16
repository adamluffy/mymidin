package dataadapter;

import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Product;
import mymidin.com.mymidin.R;

public class ProductViewHolder extends RecyclerView.ViewHolder{

    CircleImageView productImage;
    TextView productName, productBalance, productPrice;
    protected int position;
    private RecyclerViewItemClickListener mListener;
    ActionMode.Callback cb;


    ProductViewHolder(@NonNull View itemView, RecyclerViewItemClickListener mListener) {
        super(itemView);
        productImage = itemView.findViewById(R.id.product_image);
        productName = itemView.findViewById(R.id.product_name_display);
        productBalance = itemView.findViewById(R.id.product_qty_display);
        productPrice = itemView.findViewById(R.id.price_display);
        this.mListener = mListener;
        bindListener();
    }


    private void bindListener(){

        itemView.setOnClickListener(v -> mListener.onItemClickListener(v,getAdapterPosition()) );

        itemView.setOnLongClickListener(v -> {
            mListener.onLongItemClickListener(v,getAdapterPosition(),cb);
            return true;
        });
    }


}
