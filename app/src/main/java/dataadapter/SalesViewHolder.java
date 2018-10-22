package dataadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import mymidin.com.mymidin.R;

class SalesViewHolder extends RecyclerView.ViewHolder {

    protected TextView salesNumber, salesDate, totalAmount;
    private RecyclerViewItemClickListener mListener;

    SalesViewHolder(@NonNull View itemView, RecyclerViewItemClickListener mListener) {
        super(itemView);
        salesNumber = itemView.findViewById(R.id.sales_no_display);
        salesDate = itemView.findViewById(R.id.sales_date_display);
        totalAmount = itemView.findViewById(R.id.total_amount_display);
        this.mListener = mListener;
    }

    private void bindListener(){

        itemView.setOnClickListener(v -> mListener.onItemClickListener(v,getAdapterPosition()));

        itemView.setOnLongClickListener(v -> false);
    }
}
