package dataadapter;

import android.widget.Filter;

import com.firebase.ui.firestore.FirestoreArray;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import model.Product;

public class ProductFilter extends Filter {

    ProductDataAdapter dataAdapter;
    ObservableSnapshotArray<Product> products;

    ProductFilter(ProductDataAdapter dataAdapter, ObservableSnapshotArray<Product> products) {
        this.dataAdapter = dataAdapter;
        this.products = products;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();


        if(constraint!=null && constraint.length()>0 ){

            constraint = constraint.toString();
            Query query = FirebaseFirestore.getInstance().collection("product")
                    .whereEqualTo("sellerId", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .whereEqualTo("name",constraint);


            FirestoreArray<Product> productFirestoreArray = new FirestoreArray<>(query, snapshot -> {
                Product product = snapshot.toObject(Product.class);

                if (product != null) {
                    return product;
                } else {
                    return new Product();
                }
            });

            results.count = productFirestoreArray.size();
            results.values = productFirestoreArray;

        }else{
            results.count = products.size();
            results.values = products;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        dataAdapter.products = (ObservableSnapshotArray<Product>)results.values;
        dataAdapter.notifyDataSetChanged();
    }
}
