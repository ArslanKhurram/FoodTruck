package com.example.foodtruck;

        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.Spinner;


public class LoginFragment extends Fragment {




    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,  @Nullable Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_login, container,  false);

        Button signUpBtn = v.findViewById(R.id.btnSignUp);
        final Spinner spnChoice = v.findViewById(R.id.spnLoginType);


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

        return v;
    }
}