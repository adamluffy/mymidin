package mymidin.com.mymidin.customer;

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

import dataadapter.CustomerDataAdapter;
import mymidin.com.mymidin.home.HomeActivity;
import mymidin.com.mymidin.R;
import respository.CustomerDatabase;

public class CustomerListFragment extends Fragment {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CustomerDataAdapter dataAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.customer_list_fragment,container,false);

        TextView emptyView = v.findViewById(R.id.customer_list_empty_view);
        emptyView.setVisibility(View.GONE);

        ProgressBar progressBar = v.findViewById(R.id.customer_list_progress);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView customerList = v.findViewById(R.id.customer_list);
        customerList.setVisibility(View.GONE);
        customerList.addItemDecoration(new DividerItemDecoration(getActivity(),DividerItemDecoration.VERTICAL));

        Query query = FirebaseFirestore.getInstance()
                .collection("customer")
                .whereEqualTo("sellerId",user.getUid());

        query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if(queryDocumentSnapshots!=null && queryDocumentSnapshots.isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
            }else{
                customerList.setVisibility(View.VISIBLE);
            }

            progressBar.setVisibility(View.GONE);
        });

       dataAdapter = new CustomerDataAdapter(CustomerDatabase.getOptions(query));

        customerList.setHasFixedSize(true);
        customerList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        customerList.setAdapter(dataAdapter);

        initializeFloatingBtn();

        return v;
    }

    private void initializeFloatingBtn(){

        if(getActivity() instanceof HomeActivity){
            FloatingActionButton addBtn = getActivity().findViewById(R.id.add_btn);
            addBtn.setOnClickListener(view->
                    startActivity(new Intent(getActivity(),CustomerInputActivity.class))
            );
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
}
