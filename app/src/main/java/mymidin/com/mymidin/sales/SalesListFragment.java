package mymidin.com.mymidin.sales;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import dataadapter.SalesDataAdapter;
import mymidin.com.mymidin.home.HomeActivity;
import mymidin.com.mymidin.R;
import respository.SalesDatabase;

public class SalesListFragment extends Fragment {

    FloatingActionButton addBtn;
    SalesDataAdapter dataAdapter;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.sales_list_fragment,container,false);

        TextView emptyView = v.findViewById(R.id.sales_list_empty_view);
        emptyView.setVisibility(View.GONE);

        ProgressBar progressBar = v.findViewById(R.id.sales_list_progress);
        progressBar.setVisibility(View.VISIBLE);

        RecyclerView salesRecyclerView = v.findViewById(R.id.sales_list);
        salesRecyclerView.setVisibility(View.GONE);

        Query query = FirebaseFirestore.getInstance()
                .collection("sales")
                .whereEqualTo("sellerId",user.getUid());

        query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if(queryDocumentSnapshots!=null && queryDocumentSnapshots.isEmpty()){
                emptyView.setVisibility(View.VISIBLE);
            }else{
                emptyView.setVisibility(View.GONE);
                salesRecyclerView.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
        });

        dataAdapter = new SalesDataAdapter(SalesDatabase.getOptions(query));

        salesRecyclerView.setAdapter(dataAdapter);
        salesRecyclerView.setHasFixedSize(true);
        salesRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));


        initializeAddButton();
        return v;
    }


    public void initializeAddButton(){

        if(getActivity() instanceof HomeActivity){
            addBtn = ((HomeActivity) getActivity()).getFloatingButton();
            addBtn.setOnClickListener(view->{
                Log.d("tag","Sales List: Add Button");
                startActivity(new Intent(getActivity(),SalesInputActivity.class));
            });
        }
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
