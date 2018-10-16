package dataadapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import mymidin.com.mymidin.R;

class ProductSoldViewHolder extends RecyclerView.ViewHolder {

    TextView productName, productPrice, productQty, totalPrice;

    ProductSoldViewHolder(View itemView) {
        super(itemView);
        productName = itemView.findViewById(R.id.product_sold_name);
        productPrice = itemView.findViewById(R.id.product_sold_price);
        productQty = itemView.findViewById(R.id.product_sold_qty);
        totalPrice = itemView.findViewById(R.id.product_sold_total_price);
    }
}
