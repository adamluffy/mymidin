package respository;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import model.Customer;

public class CustomerDatabase{

    private static CollectionReference custRef = FirebaseFirestore.getInstance().collection("customer");
    private final String TAG = "tag";

    public static Task<QuerySnapshot> getCustomers(){
        return custRef.whereEqualTo("sellerId",FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
    }

    public static Task<DocumentSnapshot> getCustomer(String id){
       return custRef.document(id).get();
    }

    public static Task<Void> removeCustomer(String id){
        return custRef.document(id).delete();
    }

    public static Task<DocumentReference> addCustomer(Customer customer){
        return custRef.add(customer);
    }

    public static FirestoreRecyclerOptions<Customer> getOptions(Query query){

        return new FirestoreRecyclerOptions.Builder<Customer>()
                .setQuery(query,Customer.class)
                .build();
    }
}
