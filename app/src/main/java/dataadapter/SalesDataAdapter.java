package dataadapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.DateFormat;
import java.util.Locale;

import model.Sales;
import mymidin.com.mymidin.R;
import mymidin.com.mymidin.sales.SalesViewActivity;

public class SalesDataAdapter extends FirestoreRecyclerAdapter<Sales,SalesViewHolder> implements ItemListener{

    protected boolean multiSelect = false;

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

        RecyclerViewItemClickListener listener = new RecyclerViewItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

            }

            @Override
            public void onLongItemClickListener(View v, int position, ActionMode.Callback callback) {

            }
        };

        return new SalesViewHolder(v,listener);
    }

    @Override
    public void selectedItem(int position, View view) {

    }

    @Override
    public void viewItem(int position, Context context) {

        Intent intent = new Intent(context,SalesViewActivity.class);
        intent.putExtra("id",getSnapshots().getSnapshot(position).getId());

        context.startActivity(intent);

    }

    @Override
    public void removeItem(int position) {
        getSnapshots().remove(position);
        notifyItemRangeRemoved(position,getSnapshots().size());
    }
}
