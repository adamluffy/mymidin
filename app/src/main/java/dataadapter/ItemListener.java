package dataadapter;

import android.content.Context;
import android.view.View;

public interface ItemListener {
    void selectedItem(int position, View view);
    void viewItem(int position, Context context);
    void removeItem(int position);
}
