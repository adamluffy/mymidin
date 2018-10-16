package dataadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Locale;

import model.ProductSold;
import mymidin.com.mymidin.R;

public class ProductSoldDataAdapter extends RecyclerView.Adapter<ProductSoldViewHolder> implements ItemListener{

    private ArrayList<ProductSold> productSolds;


    public ProductSoldDataAdapter(ArrayList<ProductSold> productSolds) {
        this.productSolds = productSolds;
    }

    @NonNull
    @Override
    public ProductSoldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sold_item,parent,false);
        return new ProductSoldViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductSoldViewHolder holder, int position) {

        ProductSold productSold = productSolds.get(position);

        holder.productName.setText(productSold.getProductName());
        holder.productPrice.setText(String.format(Locale.ENGLISH,"RM %.2f",productSold.getProductPrice()));
        holder.productQty.setText(String.valueOf(productSold.getProductQty()));

        double totalPrice = productSold.getProductQty()*productSold.getProductPrice();

        holder.totalPrice.setText(String.format(Locale.ENGLISH,"RM %.2f",totalPrice));
    }

    @Override
    public int getItemCount() {
        return productSolds.size();
    }

    @Override
    public void selectedItem(int position, View view) {

    }

    @Override
    public void viewItem(int position, Context context) {

    }

    @Override
    public void removeItem(int position) {
        productSolds.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position,productSolds.size());
    }
}
