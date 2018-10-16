package viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import model.Product;

public class ProductViewModel extends ViewModel {

    private LiveData<Product> productLiveData;

    public void init(String id){

    }


    public LiveData<Product> getProductLiveData() {
        return productLiveData;
    }

}
