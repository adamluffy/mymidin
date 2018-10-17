package mymidin.com.mymidin.product;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.regex.Pattern;

import dataadapter.ProductArrayAdapter;
import model.Product;
import model.ProductSold;
import mymidin.com.mymidin.R;

public class ProductSoldDialogFragment extends DialogFragment {


    private TextInputLayout prodLayout, qtyLayout;
    private AutoCompleteTextView prodEditText;
    private TextInputEditText qtyEditText;

    private ArrayList<Product> products;
    Product selectedProduct;

    public interface ProductSoldListener{
        void onProductSoldListener(ProductSold sold);
    }

    ProductSoldListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.product_sold_item_dialog,null,false);

        prodLayout = v.findViewById(R.id.product_sold_layout);
        prodEditText = v.findViewById(R.id.product_sold_input);

        Query query = FirebaseFirestore.getInstance()
                .collection("product")
                .whereEqualTo("sellerId", FirebaseAuth.getInstance().getCurrentUser().getUid());

        products = new ArrayList<>();

        query.get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots){
                products.add(doc.toObject(Product.class));
            }
        });

        ProductArrayAdapter productArrayAdapter = new ProductArrayAdapter(this.getActivity(), products);
        prodEditText.setAdapter(productArrayAdapter);
        prodEditText.setOnItemClickListener((parent, view, position, id) -> {
            selectedProduct = (Product) parent.getItemAtPosition(position);
            Toast.makeText(getActivity(),selectedProduct.getName(),Toast.LENGTH_SHORT).show();
        });
        prodEditText.setThreshold(1);

        qtyLayout = v.findViewById(R.id.qty_sold_layout);
        qtyEditText = v.findViewById(R.id.qty_sold_input);

        dialog.setView(v)
                .setPositiveButton("OK", (dialog12, which) -> {
                    if(validateInput()){

                        ProductSold sold = new ProductSold(
                                selectedProduct.getName(),
                                selectedProduct.getPrice(),
                                Integer.parseInt(qtyEditText.getText().toString()),
                                selectedProduct.getType());

                        Toast.makeText(getActivity(),sold.getProductName(),Toast.LENGTH_SHORT).show();
                        mListener.onProductSoldListener(sold);
                    }else{
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("CANCEL",((dialog1, which) -> {
                    //dismiss
                    dismiss();
                }))
                .setTitle("Select Product");

        return dialog.create();
    }

    private boolean validateProduct(){


        if(selectedProduct==null){
            prodLayout.setError("Please select product");
            return false;
        }

        //if product is not in database, return false

        return true;
    }

    private boolean validateQty(){

        String qty = qtyEditText.getText().toString().trim();

        if(qty.isEmpty()){
            qtyLayout.setError("Please input the desire quantity");
            Toast.makeText(getActivity(),"Please input the desire quantity",Toast.LENGTH_SHORT).show();
            return false;
        }else if(!Pattern.matches("\\d",qty)){
            qtyLayout.setError("Please input a numeric input");
            Toast.makeText(getActivity(),"Please input a numeric input",Toast.LENGTH_SHORT).show();
            return false;
        }else if(selectedProduct!=null && Integer.parseInt(qty)>selectedProduct.getQuantity()){
            qtyLayout.setError("unsufficient balance");
            Toast.makeText(getActivity(),"unsufficient balance",Toast.LENGTH_SHORT).show();
            return false;
        }else{
            qtyLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateInput(){

        boolean isValid = true;

        if(!validateQty()){
            isValid = false;
        }

        if(!validateProduct()){
            isValid = false;
        }

        return isValid;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (ProductSoldListener) context;
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
