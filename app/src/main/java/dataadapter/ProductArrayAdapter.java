package dataadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Product;
import mymidin.com.mymidin.R;

public class ProductArrayAdapter extends ArrayAdapter<Product> implements Filterable {

    private ArrayList<Product> products;
    private Context context;

    public ProductArrayAdapter(@NonNull Context context, ArrayList<Product> products) {
        super(context, 0, products);
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if(v==null){
            v = LayoutInflater.from(this.context).inflate(R.layout.product_item_layout,parent,false);
        }

        Product product = products.get(position);

        initializeUI(product,v);

        return v;
    }

    private void initializeUI(Product product, View v){

        CircleImageView prodImage = v.findViewById(R.id.product_image);

        FirebaseStorage.getInstance().getReference().child(product.getImageUrl()).getDownloadUrl()
                .addOnSuccessListener(uri ->
                        Picasso.get()
                                .load(uri)
                                .fit()
                                .placeholder(R.drawable.ic_photo_black_24dp)
                                .into(prodImage)
                );

        TextView productName = v.findViewById(R.id.product_name_display);
        TextView productPrice = v.findViewById(R.id.price_display);
        TextView productQty = v.findViewById(R.id.product_qty_display);

        productName.setText(product.getName());
        productPrice.setText(String.format(Locale.ENGLISH,"RM %.2f",product.getPrice()));
        productQty.setText(String.valueOf(product.getQuantity()));
    }

    private Filter productFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Product)resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults();

            ArrayList<Product> filteredProduct = new ArrayList<>();

            if(constraint.length()==0 || constraint.toString().isEmpty()){
                filteredProduct.addAll(products);
            }else{
                String filter = constraint.toString().toLowerCase().trim();

                for(Product product:products){
                    if(product.getName().toLowerCase().contains(filter)){
                        filteredProduct.add(product);
                    }
                }

            }

            filterResults.values = filteredProduct;
            filterResults.count = filteredProduct.size();

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            if(results.count>0){
                products.clear();
                products.addAll((ArrayList<Product>)results.values);
                notifyDataSetChanged();
            }
        }
    };

    @NonNull
    @Override
    public Filter getFilter() {
        return productFilter;
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public int getCount() {
        return products.size();
    }
}
