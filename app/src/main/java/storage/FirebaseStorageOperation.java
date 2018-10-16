package storage;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

public interface FirebaseStorageOperation {

    UploadTask uploadImage(String fileName,Uri filepath);
    Task<Uri> getImage(String imageUrl);
    Task<Void> deleteImage(String imageUrl);

}
