package mymidin.com.mymidin.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Pattern;

import model.Customer;
import model.ProductSold;
import mymidin.com.mymidin.R;

public class SalesEditActivity extends AppCompatActivity {

    private TextInputLayout salesDateLayout, salesCustLayout;
    private TextInputEditText salesDate, salesCust;
    private RecyclerView soldRecyclerView;
    private TextView totalAmount;
    private Button addSalesBtn, addSoldBtn;
    
    private Customer customer;

    private ArrayList<ProductSold> productSolds;
    private double total = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_edit);

        Toolbar toolbar = findViewById(R.id.sales_toolbar_edit);
        toolbar.setTitle("Edit sales");
        setSupportActionBar(toolbar);

        Intent intent = getIntent(); //get data from the previous activity
        

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

}
