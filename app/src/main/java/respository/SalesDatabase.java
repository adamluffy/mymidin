package respository;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;

import model.Sales;

public class SalesDatabase {

    private static CollectionReference salesRef = FirebaseFirestore.getInstance().collection("sales");

    public static DocumentReference getSale(String id){
        return salesRef.document(id);
    }

    public static Query getSellerSales(){
        return salesRef.whereEqualTo("sellerId",FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static Task<DocumentReference> addSales(Sales sales){
        return salesRef.add(sales);
    }

    public static Task<Void> removeSales(String id){
        return salesRef.document(id).delete();
    }

    public static Task<Void> updateSales(String id, Map<String,Object> updates){ return salesRef.document(id).update(updates); }

    public static FirestoreRecyclerOptions<Sales> getOptions(Query query){

        return new FirestoreRecyclerOptions.Builder<Sales>()
                .setQuery(query,Sales.class)
                .build();
    }

}
