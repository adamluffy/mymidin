package dataadapter;

import android.support.v7.view.ActionMode;
import android.view.View;

public interface RecyclerViewItemClickListener {
    void onItemClickListener(View v,int position);
    void onLongItemClickListener(View v, int position, ActionMode.Callback callback);
}
