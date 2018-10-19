package mymidin.com.mymidin.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import mymidin.com.mymidin.R;
import utilities.FragmentUtility;
import utilities.ValidationUtility;

public class SignUpFragment extends Fragment implements OnCompleteListener<AuthResult>{

    private TextInputLayout nameLayout, emailLayout, passwordLayout, confirmPwdLayout;
    private TextInputEditText name, email, pwd, confirmPwd;

    private FirebaseAuth mAuth;

    private Button signUpBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_up_form, container, false);

        mAuth = FirebaseAuth.getInstance();

        nameLayout = view.findViewById(R.id.name_input_layout);
        name = view.findViewById(R.id.name_input_edit_text);

        emailLayout = view.findViewById(R.id.email_signup_layout);
        email = view.findViewById(R.id.email_input_edit_text);

        passwordLayout = view.findViewById(R.id.password_signup_layout);
        pwd = view.findViewById(R.id.password_input_edit_text);

        confirmPwdLayout = view.findViewById(R.id.confirm_password_layout);
        confirmPwd = view.findViewById(R.id.confirm_password_edit_text);

        signUpBtn = view.findViewById(R.id.signup_btn);
        signUpBtn.setOnClickListener(v -> {

            if(validateInput()){

                signUpBtn.setEnabled(false);

                Toast.makeText(getActivity(),"Successfully SignUp",Toast.LENGTH_SHORT).show();
                String n = email.getText().toString().trim();
                String p = pwd.getText().toString().trim();

                createUserWithEmailAndPassword(n,p);
                //TODO: 1. give verification to user's email
                //TODO: 2. create User
            }
        });

        return view;
    }

    private boolean validateInput(){

        boolean isValid = true;

        if(!ValidationUtility.validateString(nameLayout,name.getText().toString())){
            isValid = false;
        }

        if(!ValidationUtility.validateEmail(emailLayout,email.getText().toString())){
            isValid = false;
        }

        if(!ValidationUtility.validateStrongPassword(passwordLayout,confirmPwdLayout,pwd.getText().toString(),confirmPwd.getText().toString())){

            isValid = false;
        }

        return isValid;
    }

    private void createUserWithEmailAndPassword(String email, String password){

        //TODO: show progress circle

        //TODO: 1. create user to authentication
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this)
                .addOnFailureListener(e->
                    Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show()
                );

        //send Email verification
    }

    //after completing create user
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {

        if(task.isSuccessful()){

            Toast.makeText(getActivity(),"Successfully add a email user",Toast.LENGTH_SHORT).show(); //testing
            FirebaseUser user = mAuth.getCurrentUser();
            if(user!=null && getActivity() instanceof SignInActivity) {

                //TODO: 2. verify user email
                user.sendEmailVerification()
                        .addOnCompleteListener(t->{
                            if(t.isSuccessful()){
                                Toast.makeText(getActivity(),"Verification Email has been send to "+user.getEmail(),Toast.LENGTH_SHORT).show();
                                FragmentUtility.setFragment((AppCompatActivity) getActivity(),R.id.auth_fragment,new SignInFragment());
                            }else{
                                Toast.makeText(getActivity(),"Failed to send verification email to "+user.getEmail(),Toast.LENGTH_SHORT).show();
                            }

                        });
            }

        }else{
            //FALSE

            Toast.makeText(getActivity(),"failed to create user",Toast.LENGTH_SHORT).show();
        }

    }
}
