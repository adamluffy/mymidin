package mymidin.com.mymidin.product;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import model.Product;
import mymidin.com.mymidin.R;
import respository.ProductDatabase;
import utilities.PreferencesUtility;

public class ProductViewActivity extends AppCompatActivity {

    ImageView productImage;
    TextView productName, productType, productQuantity, productPrice;

    Product product;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);
        intent = getIntent();

        Toolbar toolbar = findViewById(R.id.product_view_app_bar);
        toolbar.setTitle("View Product");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImage = findViewById(R.id.product_image_view);
        productName = findViewById(R.id.product_name_view);
        productType = findViewById(R.id.product_type_view);
        productQuantity = findViewById(R.id.product_qty_view);
        productPrice = findViewById(R.id.product_price_view);


        if(intent!=null && intent.getStringExtra("id")!=null){
            initializeProduct(intent.getStringExtra("id"));
        }


    }

    public void initializeProduct(String id){

        ProductDatabase.getProduct(id)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    product = documentSnapshot.toObject(Product.class);

                    FirebaseStorage.getInstance().getReference().child(product.getImageUrl()).getDownloadUrl()
                            .addOnSuccessListener(uri ->
                                    Picasso.get()
                                            .load(uri)
                                            .fit()
                                            .placeholder(R.drawable.ic_photo_black_24dp)
                                            .into(productImage)
                            )
                            .addOnFailureListener(e ->
                                    Log.d("tag",e.getLocalizedMessage())
                            );
                    
                    productName.setText(product.getName());
                    productType.setText(product.getType());
                    productQuantity.setText(String.valueOf(product.getQuantity()));
                    productPrice.setText(String.format(Locale.ENGLISH,"RM %.2f",product.getPrice()));
                });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_action_edit,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.action_edit:
                //go to edit mode
                Intent i = new Intent(this,ProductEditActivity.class);
                i.putExtra("id",intent.getStringExtra("id"));
                startActivity(i);

                return true;

            case R.id.action_delete_item:
                //delete item
                //finish()

                //1. show dialog to conform delete the item
                //2. if yes, delete the item
                //3. if no, abort the operation

                return true;

            case android.R.id.home:

                Intent in = new Intent(this,ProductEditActivity.class);
                in.putExtra("id",intent.getStringExtra("id"));
                startActivity(in);
                return true;

                default:
                    return true;

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 113){
            initializeProduct(data.getStringExtra("id"));
        }
    }
}
