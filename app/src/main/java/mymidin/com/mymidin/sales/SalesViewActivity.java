package mymidin.com.mymidin.sales;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.ListenerRegistration;

import java.text.DateFormat;
import java.util.Locale;

import dataadapter.ProductSoldDataAdapter;
import model.Sales;
import mymidin.com.mymidin.R;
import respository.SalesDatabase;

public class SalesViewActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private FrameLayout contentFragment;
    private TextView salesNumber, salesDate, custName, totalAmount;

    private RecyclerView salesList;

    private ListenerRegistration salesListener;
    private Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_view);

        Toolbar toolbar = findViewById(R.id.sales_toolbar_view);
        toolbar.setTitle("View Product");
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent(); //get intent for the previous activity

        progressBar = findViewById(R.id.sales_view_pb);
        progressBar.setVisibility(View.VISIBLE);

        contentFragment = findViewById(R.id.sales_view_content_fragment);
        contentFragment.setVisibility(View.GONE);

        salesNumber = findViewById(R.id.sales_number_view);
        salesDate = findViewById(R.id.sales_date_view);
        custName = findViewById(R.id.customer_name_view);

        salesList = findViewById(R.id.sales_list_view_view);
        totalAmount = findViewById(R.id.total_amount_view);


    }

    private void initializeUI(Sales sales){

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        salesNumber.setText(sales.getSalesNumber());
        salesDate.setText(format.format(sales.getDate()));
        custName.setText(sales.getCustomer().getCustName());


        totalAmount.setText(String.format(Locale.ENGLISH,"RM %.2f",sales.getTotalAmount()));
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

                Intent i = new Intent(this,SalesEditActivity.class);
                i.putExtra("id",intent.getStringExtra("id"));
                startActivity(i);

                break;

            case R.id.action_delete_item:

                //delete item
                //return to sales list

                break;

                default:
                    return true;
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        salesListener = SalesDatabase.getSale(intent.getStringExtra("id"))
                .addSnapshotListener((documentSnapshot, e) -> {
                    if(e!=null){
                        Toast.makeText(SalesViewActivity.this, "Failed to get sales", Toast.LENGTH_SHORT).show();
                    }

                    if(documentSnapshot!=null && documentSnapshot.exists()){
                        Sales s = documentSnapshot.toObject(Sales.class);
                        if(s!=null) initializeUI(s); //inject the sales object into the view
                        contentFragment.setVisibility(View.VISIBLE);

                    }

                    progressBar.setVisibility(View.GONE);
                }); //start listening any changes made from the firestore database

    }

    @Override
    protected void onStop() {
        super.onStop();
        salesListener.remove(); //stop listening from the firestore
    }
}
