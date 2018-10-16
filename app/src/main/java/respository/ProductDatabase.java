package respository;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Map;

import model.Product;

public class ProductDatabase{

    private static CollectionReference productRef = FirebaseFirestore.getInstance().collection("product");
    private final String TAG = "tag"; //for debug purpose

    public static DocumentReference getProduct(String id) {
        return productRef.document(id);
    }


    public static Task<DocumentReference> addProduct(Product product){

        return productRef.add(product);
    }

    public static Task<Void> removeProduct(String id){

        return productRef.document(id).delete();
    }

    public static FirestoreRecyclerOptions<Product> getOptions(Query query){

        return new FirestoreRecyclerOptions.Builder<Product>()
                .setQuery(query,Product.class)
                .build();
    }

    public static Task<Void> updateProduct(String id, Map<String,Object> updates){
        return productRef.document(id).update(updates);
    }
}
