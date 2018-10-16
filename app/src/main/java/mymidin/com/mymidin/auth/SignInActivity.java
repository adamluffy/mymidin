package mymidin.com.mymidin.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mymidin.com.mymidin.home.HomeActivity;
import mymidin.com.mymidin.R;
import utilities.FragmentUtility;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "tag";


    private FirebaseAuth auth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FragmentUtility.setFragment(this,R.id.auth_fragment,new SignInFragment());
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = auth.getCurrentUser();

        if(user!=null){
            startActivity(new Intent(this,HomeActivity.class));

        }
    }

}
