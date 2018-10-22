package mymidin.com.mymidin.customer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Customer;
import mymidin.com.mymidin.R;
import respository.CustomerDatabase;
import utilities.ValidationUtility;

public class CustomerInputActivity extends AppCompatActivity {

    private final int PICK_IMAGE = 101;

    private CircleImageView custImage;
    private TextInputLayout custNameLayout, custPhoneLayout, custAddressLayout;
    private TextInputEditText custName, custPhone, custAdress;

    private Uri filePath;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_input_fragment);

        custImage = findViewById(R.id.cust_image_input);
        Picasso.get().load(R.drawable.ic_account_circle_black_24dp)
                .placeholder(R.drawable.ic_account_circle_black_24dp)
                .into(custImage);

        //pick an image from either camera or gallery
        custImage.setOnClickListener(v ->{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
        });

        custNameLayout = findViewById(R.id.cust_name_input_layout);
        custName = findViewById(R.id.cust_name_input);

        custPhoneLayout = findViewById(R.id.cust_phone_input_layout);
        custPhone = findViewById(R.id.cust_phone_input);

        custAddressLayout = findViewById(R.id.cust_address_input_layout);
        custAdress = findViewById(R.id.cust_address_input);

        Button saveBtn = findViewById(R.id.add_cust_btn);
        saveBtn.setOnClickListener(view->{

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            //if input is valid
            if(validateInput() && filePath!=null && user!=null){

                saveBtn.setEnabled(false);

                String path = "customer_image/"+user.getDisplayName()+"/"+ Timestamp.now().toString();
                StorageReference ref = storage.getReference().child(path);

                ref.putFile(filePath)
                        .addOnSuccessListener(taskSnapshot -> {
                            Toast.makeText(CustomerInputActivity.this,"Successfully uploaded",Toast.LENGTH_SHORT).show();

                            Customer customer = new Customer(
                                    custName.getText().toString(),
                                    custPhone.getText().toString(),
                                    custAdress.getText().toString(),
                                    path,
                                    user.getUid()
                            );

                            //add customer into the firestore
                            CustomerDatabase.addCustomer(customer)
                                    .addOnSuccessListener(documentReference -> {
                                        Toast.makeText(CustomerInputActivity.this,"Successfully add to database",Toast.LENGTH_SHORT).show();
                                        CustomerInputActivity.this.finish();
                                    })
                                    .addOnFailureListener(e->
                                            Toast.makeText(CustomerInputActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show()
                                    );
                        })
                        .addOnFailureListener(e->
                                Toast.makeText(CustomerInputActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show()
                        );

            }
        });

    }


    private boolean validateInput(){

        boolean isValid = true;

        if(!ValidationUtility.validateString(custNameLayout,custName.getText().toString())){
            isValid = false;
        }

        if(!ValidationUtility.validatePhoneNumber(custPhoneLayout,custPhone.getText().toString())){
            isValid = false;
        }

        if(!ValidationUtility.validateString(custAddressLayout,custAdress.getText().toString())){
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){

            filePath = data.getData();

            //load the image into the image view
            Picasso.get()
                    .load(data.getData())
                    .placeholder(R.drawable.ic_account_circle_black_24dp)
                    .into(custImage);
        }
    }
}
