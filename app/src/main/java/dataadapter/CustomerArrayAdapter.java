package dataadapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import model.Customer;
import mymidin.com.mymidin.R;

public class CustomerArrayAdapter extends ArrayAdapter<Customer> implements Filterable{

    /*
    Custom array adapter for searching the customer
     */

    private ArrayList<Customer> customers; //list of customers in database
    private Context context; //activity class

    //constructor
    public CustomerArrayAdapter(@NonNull Context context, @NonNull ArrayList<Customer> objects) {
        super(context, 0, objects);
        this.customers = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listView = convertView;

        if(listView==null){
            listView = LayoutInflater.from(this.context).inflate(R.layout.customer_item_layout,parent,false);
        }

        //get customer for each position
        Customer customer = customers.get(position);


        CircleImageView custImage = listView.findViewById(R.id.customer_profile_image);  //customer profile image view

        //download the customer image into the ImageView
        FirebaseStorage.getInstance().getReference().child(customer.getcustImageUrl()).getDownloadUrl()
                .addOnSuccessListener(uri -> {
                    Picasso.get()
                            .load(uri)
                            .fit()
                            .placeholder(R.drawable.ic_account_circle_black_24dp)
                            .into(custImage);
                });

        TextView custName = listView.findViewById(R.id.cust_name_display);  //customer name view
        custName.setText(customer.getCustName()); //inject the customer name into the text view

        TextView custIc = listView.findViewById(R.id.cust_ic_display); //customer identity card view
        custIc.setText(customer.getIcNo()); //inject the ic number into the textview

        return listView;

    }

    //to filter the customer
    private Filter customerFilter = new Filter() {


        //convert the search result into string
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Customer)resultValue).getCustName();
        }

        //process of filtering customer
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults filterResults = new FilterResults(); //filter result object
            ArrayList<Customer> filteredCustomerList = new ArrayList<>(); //result array

            if(constraint==null || constraint.length()==0){

                //if the input text is empty, show all customer in the search view
                filteredCustomerList.addAll(customers);
            }else{

                String filter = constraint.toString().toLowerCase().trim(); //get the input text

                for(Customer customer: customers){

                    //add all customer into the result array if the customer name match the input text
                    if(customer.getCustName().toLowerCase().contains(filter)){
                        filteredCustomerList.add(customer);
                    }
                }
            }

            filterResults.values = filteredCustomerList;  //assign the result array into the filter result value
            filterResults.count = filteredCustomerList.size(); //assign the size of the search into the result count

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            //if there is a number of the result
            if(results.count>0){
                customers.clear(); //remove all customer
                customers.addAll((ArrayList<Customer>)results.values); //insert the result customers into the array
                notifyDataSetChanged(); //notify the array has changed
            }
        }
    };

    @NonNull
    @Override
    public Filter getFilter() {
        return customerFilter;
    }

    @Override
    public int getCount() {
        return customers.size();
    }


}
