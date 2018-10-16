package mymidin.com.mymidin.product;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import dataadapter.ProductDataAdapter;
import mymidin.com.mymidin.home.HomeActivity;
import mymidin.com.mymidin.R;
import respository.ProductDatabase;
import viewmodel.ProductViewModel;

public class ProductListFragment extends Fragment {

    private final int ADD_PRODUCT = 202;
    private ProductDataAdapter dataAdapter;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Query query;

    private ProductViewModel viewModel; //TODO: for future view model architecture



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.product_list_fragment,container,false);

        TextView emptyView = v.findViewById(R.id.product_list_empty_view);
        emptyView.setVisibility(View.GONE);

        ProgressBar mProgress = v.findViewById(R.id.product_list_progress);
        mProgress.setVisibility(View.VISIBLE);

        RecyclerView productList = v.findViewById(R.id.product_list);
        productList.setVisibility(View.GONE);
        productList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        if(user!=null){
            query = FirebaseFirestore.getInstance()
                    .collection("product")
                    .whereEqualTo("sellerId", user.getUid());
        }


        query.addSnapshotListener((queryDocumentSnapshots, e) -> {

            if(queryDocumentSnapshots!=null && queryDocumentSnapshots.isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
            }else{
                emptyView.setVisibility(View.GONE);
                productList.setVisibility(View.VISIBLE);
            }

            mProgress.setVisibility(View.GONE);
        });


        dataAdapter = new ProductDataAdapter(ProductDatabase.getOptions(query));


        productList.setAdapter(dataAdapter);
        productList.setHasFixedSize(true);
        productList.setLayoutManager(new LinearLayoutManager(this.getContext()));

        initializeFloatingBtn();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        dataAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        dataAdapter.stopListening();
    }

    private void initializeFloatingBtn(){

        if(getActivity() instanceof HomeActivity){
            FloatingActionButton addBtn = getActivity().findViewById(R.id.add_btn);
            addBtn.setOnClickListener(view->{
                startActivityForResult(new Intent(getContext(),ProductInputActivity.class),ADD_PRODUCT);
            });
        }
    }


}
