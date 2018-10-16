package dataadapter;

import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

import java.util.ArrayList;

import mymidin.com.mymidin.R;

public class ListCallback<T> implements ActionMode.Callback {

    private boolean multiSelect;
    private ArrayList<T> selectedItemList;
    private FirestoreRecyclerAdapter<T,RecyclerView.ViewHolder> dataAdapter;

    public ListCallback(boolean multiSelect, ArrayList<T> selectedItemList, FirestoreRecyclerAdapter<T, RecyclerView.ViewHolder> dataAdapter) {
        this.multiSelect = multiSelect;
        this.selectedItemList = selectedItemList;
        this.dataAdapter = dataAdapter;
    }


    public boolean isMultiSelect() {
        return multiSelect;
    }

    public ArrayList<T> getSelectedItemList() {
        return selectedItemList;
    }

    public FirestoreRecyclerAdapter<T, RecyclerView.ViewHolder> getDataAdapter() {
        return dataAdapter;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        //multiselect = true
        multiSelect = true;
        mode.getMenuInflater().inflate(R.menu.menu_action_bar,menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {


        if(item.getItemId() == R.id.action_delete){
            //delete
            for (T object:selectedItemList) {
                dataAdapter.getSnapshots().remove(object);
            }

        }

        mode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {

        //multiselect = false
        multiSelect = false;
        dataAdapter.notifyDataSetChanged();
        //notifydatasetchanged
    }
}
