package mymidin.com.mymidin.sales;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

import dataadapter.CustomerArrayAdapter;
import dataadapter.ProductSoldDataAdapter;
import model.Customer;
import model.ProductSold;
import model.Sales;
import mymidin.com.mymidin.R;
import mymidin.com.mymidin.product.ProductSoldDialogFragment;
import respository.SalesDatabase;

public class SalesInputActivity extends AppCompatActivity implements View.OnClickListener, ProductSoldDialogFragment.ProductSoldListener{

    private TextInputLayout dateLayout, customerLayout;
    private TextInputEditText dateInput;

    private ArrayList<ProductSold> productSolds;
    private DatePickerDialog datePickerDialog;

    ProductSoldDataAdapter productSoldDataAdapter;

    private ArrayList<Customer> customers;

    private TextView totalAmount;
    private double total;
    AutoCompleteTextView customerInput;

    Calendar dateSales;
    Customer customer;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ListenerRegistration fireListener;

    RecyclerView soldList;

    String salesNumber;

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
        customerInput.setOnItemClickListener((parent, view, position, id) -> {
            customer = customers.get(position);
        });

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
                        productSolds,
                        total,
                        user.getUid()
                );

                SalesDatabase.addSales(sales)
                        .addOnSuccessListener(documentReference -> {
                           Toast.makeText(SalesInputActivity.this,"Successfully added to database",Toast.LENGTH_SHORT).show();
                           SalesInputActivity.this.finish();
                        })
                        .addOnFailureListener(e -> {
                           Toast.makeText(SalesInputActivity.this,"Failed: "+e.getMessage(),Toast.LENGTH_SHORT).show();
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
                productSoldDataAdapter.removeItem(position);
            }
        };

        ItemTouchHelper helper = new ItemTouchHelper(cb);
        helper.attachToRecyclerView(soldList);
    }

    private boolean validateDate(){

        String date = dateInput.getText().toString().trim();

        if(date.isEmpty()){
            dateLayout.setError("Please Input Date");
            return false;
        }else if(!Pattern.matches("\\d{2}/\\d{2}/\\d{4}",date)){
            dateLayout.setError("Please Input a valid date");
            return false;
        }

        return true;
    }

    private boolean validateCustomer(){

        String custName = customerInput.getText().toString().trim();

        if(custName.isEmpty() || customer==null){
            customerLayout.setError("Please select customer or customer is not available");
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
    public void onClick(View v) {
        if(v.getId()==R.id.sales_input_date){
            datePickerDialog.show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        fireListener = SalesDatabase.getSellerSales()
                .orderBy("salesNumber",Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if(e!=null){
                        e.printStackTrace();
                    }

                    salesNumber = "IV00000001";

                    if(queryDocumentSnapshots!=null && !queryDocumentSnapshots.isEmpty()){
                        Sales s = queryDocumentSnapshots.getDocuments().get(0).toObject(Sales.class);
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
        fireListener.remove();
    }

    @Override
    public void onProductSoldListener(ProductSold sold) {
        productSolds.add(sold);

        total+=sold.getProductPrice()*sold.getProductQty();
        totalAmount.setText(String.format(Locale.ENGLISH,"RM %.2f",total));

        productSoldDataAdapter.notifyItemInserted(productSolds.size()-1);
    }


}
