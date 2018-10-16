package storage;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProductImageRespository implements FirebaseStorageOperation {

    private StorageReference storage;
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static final String IMAGE_PATH = "product_image/"+user.getDisplayName()+"/";

    private ProductImageRespository() {
        this.storage = FirebaseStorage.getInstance().getReference();
    }

    public static ProductImageRespository getInstance(){
        return new ProductImageRespository();
    }

    @Override
    public UploadTask uploadImage(String fileName, Uri filepath) {
        return storage.child(IMAGE_PATH+fileName).putFile(filepath);
    }

    @Override
    public Task<Uri> getImage(String imageUrl) {
        return storage.child(imageUrl).getDownloadUrl();
    }

    @Override
    public Task<Void> deleteImage(String imageUrl) {
        return storage.child(imageUrl).delete();
    }
}
