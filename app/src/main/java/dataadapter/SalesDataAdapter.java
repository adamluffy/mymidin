package dataadapter;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.util.Locale;

import model.Sales;
import mymidin.com.mymidin.R;

public class SalesDataAdapter extends FirestoreRecyclerAdapter<Sales,SalesViewHolder>{


    public SalesDataAdapter(@NonNull FirestoreRecyclerOptions<Sales> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SalesViewHolder holder, int position, @NonNull Sales model) {

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
        holder.salesNumber.setText(model.getSalesNumber());
        holder.salesDate.setText(format.format(model.getDate()));
        holder.totalAmount.setText(String.format(Locale.ENGLISH,"RM %.2f",model.getTotalAmount()));
    }

    @NonNull
    @Override
    public SalesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_item_layout,viewGroup,false);
        return new SalesViewHolder(v);
    }
}
