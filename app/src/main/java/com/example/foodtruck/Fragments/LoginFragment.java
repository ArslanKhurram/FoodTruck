package com.example.foodtruck.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.R;
import com.example.foodtruck.SignUpFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;


public class LoginFragment extends Fragment implements View.OnClickListener {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        Button signUpBtn = v.findViewById(R.id.btnSignUp);
        final Spinner spnChoice = v.findViewById(R.id.spnLoginType);
        //switch statement for spnChoice for onclick btn
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override


            public void onClick(View v) {
                //switch statement for spnChoice for onclick btn
                switch (spnChoice.getSelectedItem().toString()){
                    case "Customer":
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SignUpFragment()).commit();
                        break;
                    case "Vendor":
                        //  getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.test,new SignUpFragment()).commit();      Enter vendor fragment here
                        break;
                    default:
                        break;

                }

            }
        });
        SignInButton googleSignInBtn = v.findViewById(R.id.sign_in_button);
        googleSignInBtn.setOnClickListener(this); //set onclick listener to google sign in button
        return v;
    }

    //on click implemented method
    @Override
    public void onClick(View v) {
        switch (v.getId()) { //switch case to handle passed in on click objects
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    //method to execute google sign in process
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //method to sign user out
    public void signOut() {
        mGoogleSignInClient.signOut();
    }

    //handel the result after a user signs in with google
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //will process the transaction for new fragment
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateFragment(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Error", "signInResult:failed code=" + e.getStatusCode());
            updateFragment(null);
        }
    }

    //method to switch fragment after google login
    private void updateFragment(GoogleSignInAccount account) {
        if (account != null) {
            //BELOW UNCOMMENT AND REPLACE **new SignUpNameFragment()** with any fragment to switch to after successful google sign in

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
        }
    }


}
