package dataadapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import model.Customer;
import mymidin.com.mymidin.R;

public class CustomerDataAdapter extends FirestoreRecyclerAdapter<Customer,CustomerViewHolder>{

    /*
    An Custom RecyclerView DataAdapter for customer using firestore
     */

    private Query query = FirebaseFirestore.getInstance()
            .collection("customer")
            .whereEqualTo("sellerId", FirebaseAuth.getInstance().getCurrentUser().getUid()); //get all customer for seller

    public CustomerDataAdapter(@NonNull FirestoreRecyclerOptions<Customer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomerViewHolder holder, int position, @NonNull Customer model) {

        //inject the model image into the image view
        FirebaseStorage.getInstance().getReference().child(model.getcustImageUrl()).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Picasso.get()
                            .load(uri)
                            .fit()
                            .placeholder(R.drawable.ic_account_circle_black_24dp)
                            .into(holder.custImage);
                });
        holder.custName.setText(model.getCustName()); //inject the customer name into the textview
        holder.custPhone.setText(model.getPhoneNumber()); //inject the customer ic into the textview
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_item_layout,viewGroup,false);
        return new CustomerViewHolder(v);
    }

    @NonNull
    @Override
    public ObservableSnapshotArray<Customer> getSnapshots() {
        return super.getSnapshots();
    }


}
