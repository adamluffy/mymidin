package mymidin.com.mymidin.auth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import mymidin.com.mymidin.home.HomeActivity;
import mymidin.com.mymidin.R;
import utilities.FragmentUtility;

public class SignInFragment extends Fragment{

    private static final int RC_SIGN_IN = 1002;
    private FirebaseAuth auth;

    CallbackManager cbManager;
    GoogleSignInClient client;

    GoogleSignInButton googleSignInButton;

    private static final String TAG = "tag";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.sign_in_fragment, container, false);

        cbManager = CallbackManager.Factory.create();

        auth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build(); //google sign in options

        if(getActivity() instanceof SignInActivity){
            client = GoogleSignIn.getClient(this.getActivity(),gso);
        }

        Button signUpBtn = view.findViewById(R.id.signup_btn);

        //add email sign up button
        if(getActivity() instanceof SignInActivity){
            signUpBtn.setOnClickListener(view13 -> FragmentUtility.setFragment((AppCompatActivity) getActivity(),R.id.auth_fragment,new SignUpFragment()));
        }

        Button loginBtn = view.findViewById(R.id.login_btn);

        //add email login button
        if(getActivity() instanceof SignInActivity){
            loginBtn.setOnClickListener(view12 ->  FragmentUtility.setFragment((AppCompatActivity) getActivity(),R.id.auth_fragment,new LoginFragment()));
        }

        //add facebook login
        LoginButton fbLogin = view.findViewById(R.id.fb_login_btn);
        fbLogin.setReadPermissions("public_profile", "email");
        fbLogin.setFragment(this);
        fbLogin.registerCallback(cbManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {

                signInWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity(),"cancel by user",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getActivity(),error.getMessage(),Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        });

        googleSignInButton = view.findViewById(R.id.google_sign_in_btn);

        googleSignInButton.setOnClickListener(view1 -> {

            Intent intent = client.getSignInIntent();
            startActivityForResult(intent,RC_SIGN_IN);
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void signInWithFacebook(AccessToken token){

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        signInWithCredential(credential);
    }

    public void signInWithGoogle(GoogleSignInAccount acc){


        AuthCredential credential = GoogleAuthProvider.getCredential(acc.getIdToken(),null);

        signInWithCredential(credential);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount acc = result.getSignInAccount();
                signInWithGoogle(acc);
            }else{
                Log.d(TAG,String.valueOf(result.getStatus()));
            }
        }

        cbManager.onActivityResult(requestCode,resultCode,data);

    }

    private void toHomePage(FirebaseUser user){
        startActivity(new Intent(this.getActivity(),HomeActivity.class));
    }

    private void signInWithCredential(AuthCredential credential){

        if(getActivity() instanceof SignInActivity){
            auth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){

                            Toast.makeText(getActivity(),"Successfully Login",Toast.LENGTH_SHORT).show();
                            FirebaseUser user = auth.getCurrentUser();
                            if(user!=null){
                                toHomePage(user);
                            }
                        }else{
                            Log.d(TAG,"failed");
                        }
                    });
        }
    }
}
