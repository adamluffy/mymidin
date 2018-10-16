package mymidin.com.mymidin.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mymidin.com.mymidin.customer.CustomerListFragment;
import mymidin.com.mymidin.product.ProductListFragment;
import mymidin.com.mymidin.R;
import mymidin.com.mymidin.report.ReportActivity;
import mymidin.com.mymidin.sales.SalesListFragment;

public class HomeFragment extends Fragment implements View.OnClickListener {

    CardView salesCard, reportCard, productCard, customerCard;
    Toolbar toolbar;
    FloatingActionButton addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_fragment, container, false);

        salesCard = v.findViewById(R.id.sales_card);
        salesCard.setOnClickListener(this);

        reportCard = v.findViewById(R.id.report_card);
        reportCard.setOnClickListener(this);

        productCard = v.findViewById(R.id.product_card);
        productCard.setOnClickListener(this);

        customerCard = v.findViewById(R.id.customer_card);
        customerCard.setOnClickListener(this);

        if(getActivity() instanceof HomeActivity){
            toolbar = getActivity().findViewById(R.id.toolbar);
        }

        initializeFloatingBtn();

        return v;
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        Fragment fragment = null;

        switch (id) {

            case R.id.sales_card:
                fragment = new SalesListFragment();
                addBtn.show();
                toolbar.setTitle("Sales");
                break;

            case R.id.report_card:
                if(getActivity() instanceof HomeActivity){
                    startActivity(new Intent(getActivity(),ReportActivity.class));
                }
                break;

            case R.id.customer_card:
                fragment = new CustomerListFragment();
                addBtn.show();
                toolbar.setTitle("Customer");
                break;

            case R.id.product_card:
                fragment = new ProductListFragment();
                addBtn.show();
                toolbar.setTitle("Product");
                break;
        }

        if (fragment != null && getActivity() instanceof HomeActivity) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.midin_fragment, fragment);
            transaction.commit();
        }
    }

    private void initializeFloatingBtn() {
        if (getActivity() instanceof HomeActivity) {
            addBtn = getActivity().findViewById(R.id.add_btn);
        }
    }

}
