package com.example.foodtruck;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.foodtruck.DataBase.CustomersContract;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class SignUpActivity extends AppCompatActivity {
    CustomersContract cc = new CustomersContract(this);
    int RC_SIGN_IN = 0;
    Button googleSignIn = findViewById(R.id.googleButton);
    Button signUpButton = (Button) findViewById(R.id.signUpButton);
    EditText txtFName = findViewById(R.id.editTextFirstName);
    EditText txtLName = findViewById(R.id.editTextLastName);
    EditText txtEmail= findViewById(R.id.editTextEmail);
    EditText txtPhone = findViewById(R.id.editTextPhoneNumber);
    EditText txtStreet = findViewById(R.id.editTextStreet);
    EditText txtHNum = findViewById(R.id.editTextNumber);
    EditText txtZip = findViewById(R.id.editTextZipCode);
    EditText txtCity = findViewById(R.id.editTextCity);
    EditText txtState = findViewById(R.id.editTextState);
    GoogleSignInClient mGoogleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        Fragment f = new SignUpFragment();
        ft.add(R.id.fragment_sign_up_container, f);

        signUpButton.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v) {
                                                cc.addCustomer(txtFName.getText().toString(),
                                                        txtLName.getText().toString(),
                                                        txtEmail.getText().toString(),
                                                        txtPhone.getText().toString(),
                                                        txtStreet.getText().toString(),
                                                        txtHNum.getText().toString(),
                                                        txtZip.getText().toString(),
                                                        txtCity.getText().toString(),
                                                        txtState.getText().toString());
                                            }
                                        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signIn();
            }
        });
        ft.commit();
    }
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign in Error","signInResult:failed code=" + e.getStatusCode());
        }
    }
}