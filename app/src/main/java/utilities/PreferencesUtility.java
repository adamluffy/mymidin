package utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtility {

    public static void savePref(Context context, String id){

        SharedPreferences.Editor editor =
                context.getSharedPreferences("pref",Context.MODE_PRIVATE)
                .edit();

        editor.putString("id",id);
        editor.apply();

    }

    public static String loadPref(Context context){

        SharedPreferences pref =
                context.getSharedPreferences("pref",Context.MODE_PRIVATE);

        return pref.getString("id",null);
    }
}
