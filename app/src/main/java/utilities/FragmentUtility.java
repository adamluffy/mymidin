package utilities;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import mymidin.com.mymidin.R;

public class FragmentUtility {

    public static void setFragment(@NonNull AppCompatActivity activity,int containerId ,Fragment fragment){

        activity.getSupportFragmentManager().beginTransaction()
                .replace(containerId,fragment)
                .addToBackStack(null)
                .commit();
    }


}
