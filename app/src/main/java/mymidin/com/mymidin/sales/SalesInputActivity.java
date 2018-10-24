package mymidin.com.mymidin.sales;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import dataadapter.CustomerArrayAdapter;
import dataadapter.ProductSoldDataAdapter;
import model.Customer;
import model.ProductSold;
import model.Sales;
import mymidin.com.mymidin.R;
import mymidin.com.mymidin.product.ProductSoldDialogFragment;
import respository.SalesDatabase;
import utilities.ValidationUtility;

public class SalesInputActivity extends AppCompatActivity implements View.OnClickListener, ProductSoldDialogFragment.ProductSoldListener{

    private TextInputLayout dateLayout, customerLayout;
    private TextInputEditText dateInput;

    private ArrayList<ProductSold> productSolds;
    private DatePickerDialog datePickerDialog;

    ProductSoldDataAdapter productSoldDataAdapter;

    private ArrayList<Customer> customers;

    private TextView totalAmount;
    private double total = 0;
    AutoCompleteTextView customerInput;

    Calendar dateSales;
    Customer customer;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ListenerRegistration mListener;

    RecyclerView soldList;

    String salesNumber = "IV00000001";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sales_input_fragment);
        dateSales = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        dateLayout = findViewById(R.id.sales_input_date_layout);
        dateInput = findViewById(R.id.sales_input_date);

        dateInput.setOnClickListener(this);
        datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            Calendar newDate = Calendar.getInstance();
            newDate.set(year,month,dayOfMonth);
            dateSales.set(year,month,dayOfMonth);
            dateInput.setText(dateFormat.format(newDate.getTime()));
        }, dateSales.get(Calendar.YEAR),dateSales.get(Calendar.MONTH),dateSales.get(Calendar.DAY_OF_MONTH));

        customerLayout = findViewById(R.id.sales_cust_input_layout);

        customerInput = findViewById(R.id.sales_cust_input);

        Query query;
        customers = new ArrayList<>();

        if(user !=null){
            query = FirebaseFirestore.getInstance()
                    .collection("customer")
                    .whereEqualTo("sellerId",user.getUid());

            query.get().addOnSuccessListener(queryDocumentSnapshots -> {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                    customers.add(documentSnapshot.toObject(Customer.class));
                }
            });
        }


        CustomerArrayAdapter customerArrayAdapter = new CustomerArrayAdapter(this, customers);
        customerInput.setAdapter(customerArrayAdapter);
        customerInput.setThreshold(1);
        customerInput.setOnItemClickListener((parent, view, position, id) -> customer = customers.get(position) );

        soldList = findViewById(R.id.sold_list);
        productSolds = new ArrayList<>();

        productSoldDataAdapter = new ProductSoldDataAdapter(productSolds);
        soldList.setHasFixedSize(true);
        soldList.setLayoutManager(new LinearLayoutManager(this));
        soldList.setAdapter(productSoldDataAdapter);
        initItemTouchHelper();

        totalAmount = findViewById(R.id.total_amount_sales);
        total = 0;

        Button saveBtn = findViewById(R.id.add_sales_btn);
        saveBtn.setOnClickListener(c->{

            saveBtn.setEnabled(false);

            if(validateInput()){
                //insert into firebase
                Sales sales = new Sales(
                        salesNumber,
                        dateSales.getTime(),
                        customer,
                        total,
                        user.getUid()
                );

                //stock out data from the products

                SalesDatabase.createSales(sales,productSolds)
                        .addOnSuccessListener(this,aVoid -> {

                            Toast.makeText(this,"Successfully add to database", Toast.LENGTH_SHORT).show();
                            this.finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this,"Fail: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            saveBtn.setEnabled(true);
                        });
            }

        });

        TextView addSoldBtn = findViewById(R.id.add_sold_btn);
        addSoldBtn.setOnClickListener(v->{

            ProductSoldDialogFragment dialogFragment = new ProductSoldDialogFragment();
            dialogFragment.show(getSupportFragmentManager(),"select Product");

        });

    }

    private void initItemTouchHelper(){

        ItemTouchHelper.SimpleCallback cb = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                ProductSold sold = productSolds.get(position);
                subtractTotal(sold.getProductQty()*sold.getProductPrice());
                productSolds.remove(position);
                productSoldDataAdapter.notifyDataSetChanged();
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(cb);
        helper.attachToRecyclerView(soldList);
    }

    private boolean validateItemSold(){

        return total>0 && !productSolds.isEmpty();
    }

    private boolean validateInput(){

        boolean isValid = true;

        if(!ValidationUtility.validateDate(dateLayout,dateInput.getText().toString())){
            isValid = false;
        }

        if(!ValidationUtility.validateString(customerLayout,customerInput.getText().toString()) || customer==null){
            isValid = false;
        }

        if(!validateItemSold()){
            isValid = false;
        }

        return isValid;
    }


    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.sales_input_date){
            datePickerDialog.show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        mListener = SalesDatabase.getSellerSales()
                .addSnapshotListener((queryDocumentSnapshots, e) -> {

                    if(queryDocumentSnapshots!=null &&!queryDocumentSnapshots.isEmpty()){


                        Sales s = queryDocumentSnapshots.getDocuments().get(queryDocumentSnapshots.size()-1).toObject(Sales.class);

                        if(s!=null){
                            String str = s.getSalesNumber();
                            str = str.replaceAll("\\D+","");
                            salesNumber = "IV"+String.format("%0"+str.length()+"d",Integer.parseInt(str)+1);
                        }
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mListener.remove();
    }

    @Override
    public void onProductSoldListener(ProductSold sold) {

        //check if the product is in array
        productSolds.add(sold);
        addToTotal(sold.getProductQty()*sold.getProductPrice());

        productSoldDataAdapter.notifyDataSetChanged();
    }

    private void addToTotal(double price){
        total+=price;
        totalAmount.setText(String.format(Locale.ENGLISH,"RM %.2f",total));
    }

    private void subtractTotal(double price){
        total-=price;
        totalAmount.setText(String.format(Locale.ENGLISH,"RM %.2f",total));
    }

}
