package mymidin.com.mymidin.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import mymidin.com.mymidin.home.HomeActivity;
import mymidin.com.mymidin.R;
import utilities.ValidationUtility;

public class LoginFragment extends Fragment {

    private TextInputLayout emailLayout, pwdLayout;
    private TextInputEditText email, pwd;

    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);
        mAuth = FirebaseAuth.getInstance(); //get firebase auth instance

        emailLayout = view.findViewById(R.id.login_email_layout);
        email = view.findViewById(R.id.login_email_input);

        pwdLayout = view.findViewById(R.id.login_pwd_layout);
        pwd = view.findViewById(R.id.login_pwd_input);

        Button loginBtn = view.findViewById(R.id.email_login_btn);
        loginBtn.setOnClickListener(v->{
            if(validateInput()){

                //TODO: Login process
                signInWithEmailAndPassword(
                        email.getText().toString().trim(),
                        pwd.getText().toString().trim()
                );
            }
        });

        TextView forgotPasswordBtn = view.findViewById(R.id.forgot_pwd_link);
        forgotPasswordBtn.setOnClickListener(v2->{

            //TODO: Go to Forgot Password Page
        });

        return view;
    }

    private boolean validateInput() {

        boolean isValid = true;

        if(!ValidationUtility.validateEmail(emailLayout,email.getText().toString())){
            isValid = false;
        }

        if(!ValidationUtility.validateString(pwdLayout,pwd.getText().toString())){
            isValid = false;
        }

        return isValid;
    }

    private void signInWithEmailAndPassword(String email,String password){


        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){

                       FirebaseUser user = mAuth.getCurrentUser();


                       if(user!=null){

                           Toast.makeText(getActivity(),String.valueOf(user.isEmailVerified()),Toast.LENGTH_SHORT).show(); //testing if email is verify
                           toHomePage(); //toHomePage
                       }

                   }else{

                       Toast.makeText(getActivity(),"Failed to login"+task.getResult(),Toast.LENGTH_SHORT).show();
                   }
                });
    }

    private void toHomePage(){
        startActivity(new Intent(this.getActivity(),HomeActivity.class));
    }
}
