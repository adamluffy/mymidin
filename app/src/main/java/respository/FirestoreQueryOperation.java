package respository;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public interface FirestoreQueryOperation<T> {

    Task<DocumentReference> add(T object);
    Task<Void> remove(String id);
    DocumentReference get(String id);
    FirestoreRecyclerOptions<T> getOptions(Query query);
    Task<QuerySnapshot> list();
    Task<Void> update(String id, Map<String,Object> updates);
}
