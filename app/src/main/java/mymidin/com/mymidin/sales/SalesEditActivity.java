package mymidin.com.mymidin.sales;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Pattern;

import dataadapter.CustomerArrayAdapter;
import dataadapter.ProductSoldDataAdapter;
import model.Customer;
import model.ProductSold;
import model.Sales;
import mymidin.com.mymidin.R;
import mymidin.com.mymidin.product.ProductSoldDialogFragment;
import respository.CustomerDatabase;
import respository.SalesDatabase;

public class SalesEditActivity extends AppCompatActivity implements View.OnClickListener, ProductSoldDialogFragment.ProductSoldListener {

    private TextInputLayout salesDateLayout, salesCustLayout;
    private TextInputEditText salesDate;
    private AutoCompleteTextView salesCust;
    private RecyclerView soldRecyclerView;
    private TextView totalAmount, addSoldBtn;
    private Button addSalesBtn;
    
    private Customer customer;

    private ArrayList<ProductSold> productSolds;
    private ArrayList<Customer> customers;
    private ProductSoldDataAdapter dataAdapter;

    private double total = 0; //total amount

    private ListenerRegistration salesListener;

    SimpleDateFormat dateFormat;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_edit);

        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        Toolbar toolbar = findViewById(R.id.sales_toolbar_edit);
        toolbar.setTitle("Edit sales");
        setSupportActionBar(toolbar);

        productSolds = new ArrayList<>();
        customers = new ArrayList<>();

        salesDateLayout = findViewById(R.id.sales_input_date_layout);
        salesDate = findViewById(R.id.sales_input_date);

        salesCustLayout = findViewById(R.id.sales_cust_input_layout);
        salesCust = findViewById(R.id.sales_cust_input);

        soldRecyclerView = findViewById(R.id.sold_list);

        addSalesBtn = findViewById(R.id.add_sales_btn);
        addSoldBtn = findViewById(R.id.add_sold_btn);

        totalAmount = findViewById(R.id.total_amount_sales);


    }

    private void initializeUI(Sales s){

        salesDate.setText(dateFormat.format(s.getDate()));

        customer = s.getCustomer();
        salesCust.setText(s.getCustomer().getCustName());

        if(user!=null){
            CustomerDatabase.getCustomers()
                    .addOnSuccessListener(this, queryDocumentSnapshots -> {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                            customers.add(doc.toObject(Customer.class));
                        }


                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Failed: "+e.getMessage(), Toast.LENGTH_SHORT).show());
        }

        CustomerArrayAdapter arrayAdapter = new CustomerArrayAdapter(SalesEditActivity.this,customers);
        salesCust.setAdapter(arrayAdapter);
        salesCust.setThreshold(1);
        salesCust.setOnItemClickListener((parent, view, position, id) -> customer = customers.get(position) );

        total = s.getTotalAmount();
        totalAmount.setText(String.format(Locale.ENGLISH,"%.2f",s.getTotalAmount()));

        productSolds = s.getProducts();
        dataAdapter = new ProductSoldDataAdapter(productSolds);

        soldRecyclerView.setAdapter(dataAdapter);
        soldRecyclerView.setHasFixedSize(true);
        soldRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initItemTouchHelper();

        addSalesBtn.setOnClickListener(this);
        addSoldBtn.setOnClickListener(this);


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
                dataAdapter.removeItem(position);
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(cb);
        helper.attachToRecyclerView(soldRecyclerView);
    }


    private boolean validateDate(){

        String date = salesDate.getText().toString().trim();

        if(date.isEmpty()){
            salesDateLayout.setError("Please Input Date");
            return false;
        }else if(!Pattern.matches("\\d{2}/\\d{2}/\\d{4}",date)){
            salesDateLayout.setError("Please Input a valid date");
            return false;
        }

        return true;
    }

    private boolean validateCustomer(){

        String custName = salesCust.getText().toString().trim();

        if(custName.isEmpty() || customer==null){
            salesCustLayout.setError("Please select customer or customer is not available");
            return false;
        }

        return true;
    }

    private boolean validateItemSold(){

        return total>0 && !productSolds.isEmpty();
    }

    private boolean validateInput(){

        boolean isValid = true;

        if(!validateDate()){
            isValid = false;
        }

        if(!validateCustomer()){
            isValid = false;
        }

        if(!validateItemSold()){
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected void onStart() {
        super.onStart();
        salesListener = SalesDatabase.getSale(getIntent().getStringExtra("id"))
                .addSnapshotListener((documentSnapshot, e) -> {

                    if(e!=null){
                        Toast.makeText(this, "Failed : "+e.getCode(), Toast.LENGTH_SHORT).show();
                    }

                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        Sales sales = documentSnapshot.toObject(Sales.class);
                        if(sales!=null) initializeUI(sales);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        salesListener.remove();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.add_sales_btn:

                if(validateInput()){

                }

                break;

            case R.id.add_sold_btn:

                ProductSoldDialogFragment dialogFragment = new ProductSoldDialogFragment();
                dialogFragment.show(getSupportFragmentManager(),"select Product");

                break;

            case R.id.sales_cust_input:
                break;
        }
    }


    @Override
    public void onProductSoldListener(ProductSold sold) {

        //if product is exist inside the array, update the value of quantity
    }
}
