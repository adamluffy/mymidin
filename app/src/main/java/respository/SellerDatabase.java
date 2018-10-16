package respository;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

import model.Seller;

public class SellerDatabase implements FirestoreQueryOperation<Seller> {

    private static CollectionReference ref;

    private SellerDatabase(){
        ref = FirebaseFirestore.getInstance().collection("seller");
    }

    public static SellerDatabase getInstance(){
        return new SellerDatabase();
    }


    @Override
    public Task<DocumentReference> add(Seller object) {
        return ref.add(object);
    }

    @Override
    public Task<Void> remove(String id) {
        return ref.document(id).delete();
    }

    @Override
    public DocumentReference get(String id) {
        return ref.document(id);
    }

    @Override
    public FirestoreRecyclerOptions<Seller> getOptions(Query query) {
        return new FirestoreRecyclerOptions.Builder<Seller>()
                .setQuery(query,Seller.class)
                .build();
    }

    @Override
    public Task<QuerySnapshot> list() {
        return ref.whereEqualTo("sellerId",FirebaseAuth.getInstance().getCurrentUser().getUid()).get();
    }

    @Override
    public Task<Void> update(String id, Map<String,Object> objectMap) {
        return ref.document(id).update(objectMap);
    }
}
