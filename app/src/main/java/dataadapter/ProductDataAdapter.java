package dataadapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import model.Product;
import mymidin.com.mymidin.product.ProductViewActivity;
import mymidin.com.mymidin.R;

public class ProductDataAdapter extends FirestoreRecyclerAdapter<Product,ProductViewHolder> implements Filterable,ItemListener{

    private ProductFilter filter;
    private boolean multiSelect = false;
    private ArrayList<Product> selectedProduct;

    protected ObservableSnapshotArray<Product> products;

    public ProductDataAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
        this.products = getSnapshots();
        selectedProduct = new ArrayList<>();
    }


    protected  ActionMode.Callback callback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            multiSelect = true;
            mode.getMenuInflater().inflate(R.menu.menu_action_bar,menu);
            return true;

        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            if(item.getItemId() == R.id.action_delete){
                //delete item
                mode.finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

            multiSelect = false;
            selectedProduct.clear();
            notifyDataSetChanged();
        }
    };



    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Product model) {

        /*
        initialize each product into the view holder
        */

        FirebaseStorage.getInstance().getReference().child(model.getImageUrl()).getDownloadUrl()
                .addOnSuccessListener(uri ->
                    Picasso.get()
                            .load(uri)
                            .fit()
                            .placeholder(R.drawable.ic_photo_black_24dp)
                            .into(holder.productImage)
                )
                .addOnFailureListener(e ->
                    Log.d("tag",e.getLocalizedMessage())
                );
        holder.productName.setText(model.getName());
        holder.productBalance.setText(String.valueOf(model.getQuantity()));
        holder.productPrice.setText(String.format(Locale.ENGLISH,"RM %.2f",model.getPrice()));

        holder.cb = callback;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item_layout,viewGroup,false);


        RecyclerViewItemClickListener listener = new RecyclerViewItemClickListener() {

            @Override
            public void onItemClickListener(View v, int position) {

                //if the item is click
                if(multiSelect){
                    selectedItem(position,v); //select the item and add into the array
                }else{
                    viewItem(position,viewGroup.getContext()); //view the product in next activity
                }
            }

            @Override
            public void onLongItemClickListener(View v, int position, ActionMode.Callback callback) {
                ((AppCompatActivity)viewGroup.getContext()).startSupportActionMode(callback); //activate action mode
                selectedItem(position,v);
            }
        };

        return new ProductViewHolder(v,listener);
    }


    @Override
    public Filter getFilter() {

        if(filter==null){
            filter = new ProductFilter(this,getSnapshots());
        }
        return filter;
    }


    @Override
    public void selectedItem(int position, View view) {
        if(multiSelect){
            if(selectedProduct.contains(getItem(position))){
                selectedProduct.remove(getItem(position));
                view.setBackgroundColor(Color.WHITE);
            }else{
                selectedProduct.add(getItem(position));
                view.setBackgroundColor(Color.LTGRAY);
            }
        }
    }

    @Override
    public void viewItem(int position, Context context) {


        Intent intent = new Intent(context, ProductViewActivity.class);
        intent.putExtra("id",getSnapshots().getSnapshot(position).getId());

        //PreferencesUtility.savePref(context.getApplicationContext(),getSnapshots().getSnapshot(position).getId());

        context.startActivity(intent);

    }

    @Override
    public void removeItem(int position) {
        getSnapshots().remove(getItem(position)); //remove the product from firebase
        notifyItemRemoved(position);
    }


}