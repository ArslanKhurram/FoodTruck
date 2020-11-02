package com.example.foodtruck.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.foodtruck.Activities.SignUpActivity;
import com.example.foodtruck.DataBase.AdminContract;
import com.example.foodtruck.DataBase.CustomersContract;
import com.example.foodtruck.DataBase.FoodTrucksContract;
import com.example.foodtruck.DataBase.VendorsContract;
import com.example.foodtruck.Fragments.Admin.AdminMainFragment;
import com.example.foodtruck.Fragments.Customer.CustomerMainFragment;
import com.example.foodtruck.Fragments.Customer.SignUpFragment;
import com.example.foodtruck.Fragments.Vendor.VendorMainFragment;
import com.example.foodtruck.Fragments.Vendor.VendorSignUpFragment;
import com.example.foodtruck.Models.Vendor;
import com.example.foodtruck.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;


public class LoginFragment extends Fragment implements View.OnClickListener {

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 0;
    Spinner spinner;
    EditText email, password;


    SignUpActivity SignUpA;


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
        FoodTrucksContract contract = new FoodTrucksContract(getActivity());
        if(contract.CountContracts() <= 3)
            GenerateTestData(contract);
        AdminContract ac = new AdminContract(getContext());
        ac.addAdmin("1", "1");

        email = v.findViewById(R.id.etEmail);
        password = v.findViewById(R.id.etPassword);

        spinner = v.findViewById(R.id.spnLoginType); //add reference to spinner so it can be used in the onclick method
        v.findViewById(R.id.btnSignUp).setOnClickListener(this); //set onclick listener to sign up button
        v.findViewById(R.id.btnLogin).setOnClickListener(this); //set onclick listener to login button
        SignInButton googleSignInBtn = v.findViewById(R.id.sign_in_button);
        googleSignInBtn.setOnClickListener(this); //set onclick listener to google sign in button
        return v;
    }

    //on click implemented method
    @Override
    public void onClick(View v) {
        switch (v.getId()) { //switch case to handle passed in on click objects
            case R.id.sign_in_button: //if google sign in is pressed the execute signIn()
                signIn();
                break;
            case R.id.btnSignUp: //if local signUp button is pressed then check for login type
                if (spinner.getSelectedItem().toString().equals("Customer")) { //launch customer sign up
                    saveKeyData(spinner);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SignUpFragment()).commit();
                } else if (spinner.getSelectedItem().toString().equals("Vendor")) { //launch vendor sign up
                    saveKeyData(spinner);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VendorSignUpFragment()).commit();
                }
                break;
            case R.id.btnLogin:
                validateLogIn(spinner.getSelectedItem().toString()); //validate the log in
                break;
        }
    }

    int num1 = 9;

    //process the login validation
    private void validateLogIn(String loginType) {
        switch (loginType) {
            case "Customer":
                if (checkForExistingCustomer()) { //login if the user exists in the database
                    saveKeyData(spinner, email);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomerMainFragment()).commit();
                } else
                    Toast.makeText(getContext(), "Login Incorrect", Toast.LENGTH_SHORT).show(); // notify customer if the login was incorrect
                break;
            case "Vendor":
                if (checkForExistingVendor()) { //login if the user exists in the database
                    saveKeyData(spinner, email);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new VendorMainFragment()).commit();
                } else
                    Toast.makeText(getContext(), "Login Incorrect", Toast.LENGTH_SHORT).show(); // notify customer if the login was incorrect
                break;
            case "Admin":
                if (checkForExistingAdmin()) {//login if admin exist in the database
                    saveKeyData(spinner, email);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminMainFragment()).commit();
                } else
                    Toast.makeText(getContext(), "Login Incorrect", Toast.LENGTH_SHORT).show();//notify admin if they key did not match

                break;
        }
    }

    public boolean checkForExistingCustomer() {
        CustomersContract cc = new CustomersContract(getContext());
        if (cc.checkForEmptyTable()) { //check is the table is empty
            return false;
        } else if (cc.validateCustomer(email.getText().toString(), password.getText().toString())) {
            return true;
        } else
            return false;
    }

    public boolean checkForExistingVendor() {
        VendorsContract vc = new VendorsContract(getContext());
        if (vc.checkForEmptyTable()) { //check is the table is empty
            return false;
        } else if (vc.validateVendor(email.getText().toString(), password.getText().toString())) {
            return true;
        } else
            return false;
    }

    // Placeholder food truck data
    private void GenerateTestData(FoodTrucksContract tc) {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.test, null);
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        VendorsContract vc = new VendorsContract(getActivity());
        vc.addVendor("Yvonne", "Stone", "ystone@foodsinternational.com", "A$df1234", "6317584559", "E Clinton Ave", "24", "11575", "Roosevelt", "New York");
        tc.createFoodTruck("Meridian Waffle", "Breakfast", bitMapData, 40.683632, -73.592079, 1);
        tc.createFoodTruck("Presto Pretzel", "Snack", bitMapData, 40.740791, -73.638207, 1);
        tc.createFoodTruck("HalaHolic", "Halal", bitMapData, 40.669564, -73.715821, 1);
        tc.createFoodTruck("Wok and Roll on Wheels", "Chinese", bitMapData, 40.738306, -73.733669, 1);
        tc.createFoodTruck("Ricky's Famoso", "Italian", bitMapData, 40.828284, -73.164124, 1);
        tc.createFoodTruck("Cheesella Finest Grilled", "Grilled Cheese", bitMapData, 40.758890, -73.366484, 1);
        tc.createFoodTruck("Funkin' Foshonuts", "Snack", bitMapData, 40.813417, -73.002045, 1);
    }

    public boolean checkForExistingAdmin() {
        AdminContract ac = new AdminContract(getContext());
        if (ac.checkForEmptyTable()) {//check if table is empty
            return false;
        } else if (ac.validateAdmin(email.getText().toString(), password.getText().toString())) {
            return true;
        } else
            return false;
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

    public void saveKeyData(Spinner spinner, EditText email) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UserType", spinner.getSelectedItem().toString());
        editor.putString("Email", email.getText().toString());
        editor.apply();
    }

    public void saveKeyData(Spinner spinner) {
        SharedPreferences sharedPref = getActivity().getSharedPreferences("KeyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("UserType", spinner.getSelectedItem().toString());
        editor.apply();
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

            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CustomerMainFragment()).commit();
        }
    }


}