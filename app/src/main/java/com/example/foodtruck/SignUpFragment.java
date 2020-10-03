package com.example.foodtruck;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SignUpFragment extends Fragment {

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_signup, container,  false);
        Button nextBtn = v.findViewById(R.id.btnNext);
        Button backBtn = v.findViewById(R.id.btnBack);

        //Moves From signUpFragment to signUPAddFragment
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new SignUpAddFragment()).commit();

            } });

        //Moves From signUPAddFragment to signUpFragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new LoginFragment()).commit();
            }
        });




        return v;
    }
}