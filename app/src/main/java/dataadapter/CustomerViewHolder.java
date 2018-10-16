package dataadapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import mymidin.com.mymidin.R;

public class CustomerViewHolder extends RecyclerView.ViewHolder{

    protected CircleImageView custImage;
    protected TextView custName, custIC;

    CustomerViewHolder(@NonNull View itemView) {
        super(itemView);
        custImage = itemView.findViewById(R.id.customer_profile_image);
        custName = itemView.findViewById(R.id.cust_name_display);
        custIC = itemView.findViewById(R.id.cust_ic_display);
    }

    public CircleImageView getCustImage() {
        return custImage;
    }

    public void setCustImage(CircleImageView custImage) {
        this.custImage = custImage;
    }

    public TextView getCustName() {
        return custName;
    }

    public void setCustName(TextView custName) {
        this.custName = custName;
    }

}
