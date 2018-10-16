package mymidin.com.mymidin.product;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.regex.Pattern;

import model.Product;
import mymidin.com.mymidin.R;
import respository.ProductDatabase;

public class ProductInputActivity extends AppCompatActivity{

    private ImageView productImage;
    private TextInputEditText productName, productPrice, productQuantity;
    private TextInputLayout productNameLayout, productPriceLayout, productQuantityLayout;
    private AutoCompleteTextView productType;

    private Uri filePath;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    public static final int PICK_IMAGE = 112;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_input_fragment);

        productName = findViewById(R.id.product_name_input);
        productNameLayout = findViewById(R.id.product_name_input_layout);

        productPrice = findViewById(R.id.product_price_input);
        productPriceLayout = findViewById(R.id.product_price_input_layout);

        productQuantity = findViewById(R.id.product_quantity_input);
        productQuantityLayout = findViewById(R.id.product_qty_input_layout);

        productType = findViewById(R.id.type_input);

        productImage = findViewById(R.id.product_image_input);
        productImage.setOnClickListener(view->{

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
        });

        Button saveBtn = findViewById(R.id.add_product_btn);
        saveBtn.setOnClickListener(view-> {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //if all the input is valid and the image is not empty
            if (validateInput() && filePath != null && user!=null) {

                saveBtn.setEnabled(false); //disable the button

                String s = "product_image/"+ user.getDisplayName()+"/"+ productName.getText().toString(); //set the image url

                StorageReference ref = storage.getReference().child(s);

                //upload the image into the firebase storage
                ref.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(ProductInputActivity.this,"Successfully upload",Toast.LENGTH_SHORT).show();

                           Product product = new Product(
                                   s,
                                   productName.getText().toString(),
                                   productType.getText().toString(),
                                   Integer.parseInt(productQuantity.getText().toString()),
                                   Double.parseDouble(productPrice.getText().toString()),
                                   user.getUid()
                           );


                            ProductDatabase.addProduct(product)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(ProductInputActivity.this,"Successfully add to database",Toast.LENGTH_SHORT).show();
                                        ProductInputActivity.this.finish();
                                    })
                                    .addOnFailureListener(e->
                                        Log.d("tag",e.getLocalizedMessage())
                                    );



                        })
                        .addOnFailureListener(e->
                            Toast.makeText(ProductInputActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show()
                        );
            }

        });

    }

    private boolean validateName(){

        String name = productName.getText().toString();

        if(name.isEmpty()){
            productNameLayout.setError("Please input product name");
            return false;
        }else{
            productNameLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePrice(){

        String price = productPrice.getText().toString();

        if(price.isEmpty()){
            productPriceLayout.setError("Please input product price");
            return false;
        }else if(!Pattern.matches("\\d+",price)){
            productPriceLayout.setError("Please input a numeric price");{
                productPriceLayout.setErrorEnabled(false);
            }

            return false;
        }else
        return true;
    }

    private boolean validateQuantity(){

        String qty = productQuantity.getText().toString();

        if(qty.isEmpty()){
            productQuantityLayout.setError("Please input product quantity");
            return false;
        }else if(!Pattern.matches("\\d+",qty)){
            productQuantityLayout.setError("Please input a numeric quantity");
            return false;
        }else{
            productQuantityLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateInput() {

        boolean isValid = true;

        if(!validateName()){
            isValid = false;
        }

        if (!validateQuantity()){
            isValid = false;
        }

        if(!validatePrice()){
            isValid = false;
        }

        return isValid;

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            filePath = data.getData();

            Picasso.get()
                    .load(data.getData())
                    .fit()
                    .placeholder(R.drawable.ic_photo_black_24dp)
                    .into(productImage);
        }
    }
}
