package mymidin.com.mymidin.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import model.Product;
import model.ProductType;
import mymidin.com.mymidin.R;
import respository.ProductDatabase;
import utilities.ValidationUtility;

public class ProductEditActivity extends AppCompatActivity {

    private TextInputLayout prodNameLayout, prodPriceLayout, prodQtyLayout, prodTypeLayout;
    private TextInputEditText prodName, prodPrice, prodQty;
    private AutoCompleteTextView prodType;
    private Button saveBtn;
    private ImageView prodImage;

    private boolean isImageEdited = false;

    private Uri filePath;

    Product oldProduct;

    public static final int PICK_IMAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);

        Toolbar toolbar = findViewById(R.id.product_edit_toolbar);
        toolbar.setTitle("Edit Product");
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        prodImage = findViewById(R.id.product_image_edit);

        prodNameLayout = findViewById(R.id.product_name_edit_layout);
        prodName = findViewById(R.id.product_name_edit);

        prodPriceLayout = findViewById(R.id.product_price_edit_layout);
        prodPrice = findViewById(R.id.product_price_edit);

        prodQtyLayout = findViewById(R.id.product_qty_edit_layout);
        prodQty = findViewById(R.id.product_quantity_edit);

        prodTypeLayout = findViewById(R.id.product_type_edit_layout);
        prodType = findViewById(R.id.product_type_edit);

        ArrayAdapter<String> typeAdatper = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, ProductType.getTypes());
        prodType.setAdapter(typeAdatper);
        prodType.setThreshold(1);

        saveBtn = findViewById(R.id.save_product_btn);

        //get product from database and insert into the ui
        ProductDatabase.getProduct(intent.getStringExtra("id"))
                .get()
                .addOnSuccessListener(documentSnapshot -> {

                    oldProduct = documentSnapshot.toObject(Product.class);

                    FirebaseStorage.getInstance().getReference().child(oldProduct.getImageUrl()).getDownloadUrl()
                            .addOnSuccessListener(uri ->
                                    Picasso.get()
                                            .load(uri)
                                            .fit()
                                            .placeholder(R.drawable.ic_photo_black_24dp)
                                            .into(prodImage)
                            )
                            .addOnFailureListener(e ->
                                    Log.d("tag",e.getLocalizedMessage())
                            );

                    prodName.setText(oldProduct.getName());
                    prodPrice.setText(String.format(Locale.ENGLISH,"%.2f",oldProduct.getPrice()));
                    prodQty.setText(String.valueOf(oldProduct.getQuantity()));
                    prodType.setText(oldProduct.getType());

                });


        saveBtn.setOnClickListener(v -> {
            //if valid input


            if(validateInput()){

                saveBtn.setEnabled(false);

                //update the product
                ProductDatabase.updateProduct(intent.getStringExtra("id"),updateProduct())
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(ProductEditActivity.this,"Successfully update product",Toast.LENGTH_SHORT).show();

                            Intent data = new Intent();
                            data.putExtra("id",intent.getStringExtra("id"));
                            ProductEditActivity.this.setResult(113,data);
                            ProductEditActivity.this.finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(ProductEditActivity.this,"Failed to update product",Toast.LENGTH_SHORT).show();
                            saveBtn.setEnabled(true);
                        });

            }
        });

    }

    private boolean validateInput() {

        boolean isValid = true;

        if(!ValidationUtility.validateString(prodNameLayout,prodName.getText().toString())){
            isValid = false;
        }

        if (!ValidationUtility.validateQuantity(prodQtyLayout,prodQty.getText().toString())){
            isValid = false;
        }

        if(!ValidationUtility.validatePrice(prodPriceLayout,prodPrice.getText().toString())){
            isValid = false;
        }

        if(!ValidationUtility.validateString(prodTypeLayout,prodType.getText().toString())){
            isValid = false;
        }

        return isValid;

    }

    private Map<String,Object> updateProduct(){

        /*
        by comparing the new product and old product,
        if any changes has made, it will update the necessary field only
         */

        Map<String,Object> updates = new HashMap<>();

        if(isImageEdited){
            String s = "product_image/"+ FirebaseAuth.getInstance().getCurrentUser().getDisplayName() +"/"+ prodName.getText().toString();

            StorageReference reference = FirebaseStorage.getInstance().getReference().child(oldProduct.getImageUrl());
            reference.delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(ProductEditActivity.this, "Successfully delete old image", Toast.LENGTH_SHORT).show();

                        FirebaseStorage.getInstance().getReference().child(s)
                                .putFile(filePath)
                                .addOnSuccessListener(taskSnapshot -> {
                                    Toast.makeText(ProductEditActivity.this, "Successfully upload image", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ProductEditActivity.this, "Failed delete old image", Toast.LENGTH_SHORT).show();
                                });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(ProductEditActivity.this, "Failed delete old image", Toast.LENGTH_SHORT).show();
                    });
            updates.put("imageUrl",s);
        }

        if(!oldProduct.getName().equals(prodName.getText().toString())){
            updates.put("name",prodName.getText().toString());
        }

        if(oldProduct.getPrice() != Double.parseDouble(prodPrice.getText().toString())){
            updates.put("price",Double.parseDouble(prodPrice.getText().toString()));
        }

        if(oldProduct.getQuantity() != Integer.parseInt(prodQty.getText().toString())){
            updates.put("quantity",Integer.parseInt(prodQty.getText().toString()));
        }

        if(!oldProduct.getType().equals(prodType.getText().toString())){
            updates.put("type",prodType.getText().toString());
        }

        updates.put("updateAt",FieldValue.serverTimestamp());

        return updates;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            filePath = data.getData();
            isImageEdited = true;
            Picasso.get()
                    .load(data.getData())
                    .fit()
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .into(prodImage);
        }else if(requestCode == PICK_IMAGE && resultCode == RESULT_CANCELED){

            isImageEdited = false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== android.R.id.home){
            Intent i = new Intent(this,ProductViewActivity.class);
            i.putExtra("id",getIntent().getStringExtra("id"));
            startActivity(i);
        }

        return true;
    }
}
