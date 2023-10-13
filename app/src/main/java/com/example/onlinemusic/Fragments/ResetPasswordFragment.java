package com.example.onlinemusic.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinemusic.Activities.MainActivity;
import com.example.onlinemusic.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordFragment extends Fragment {

    private TextView back;
    private FrameLayout frameLayout;
    private Drawable errorIcon;
    private EditText email;
    private ProgressBar resetPasswordProgress;
    private TextView responseMassage;
    private Button resetPasswordButton;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        back = view.findViewById(R.id.back);
        frameLayout = getActivity().findViewById(R.id.register_frame_layout);

        errorIcon = getResources().getDrawable(R.drawable.ic_error);

        email = view.findViewById(R.id.email);
        resetPasswordProgress = view.findViewById(R.id.resetPasswordProgressBar);
        responseMassage = view.findViewById(R.id.responseMessage);
        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);

        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorIcon.setBounds(0,0,errorIcon.getIntrinsicWidth(),errorIcon.getIntrinsicHeight());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        if (email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            resetPasswordProgress.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        responseMassage.setTextColor(getResources().getColor(R.color.green));
                        responseMassage.setText("Check your Email.");
                    } else {
                        responseMassage.setTextColor(getResources().getColor(R.color.red));
                        responseMassage.setText("There is an issue sending Email.");
                    }
                    resetPasswordProgress.setVisibility(View.INVISIBLE);
                    responseMassage.setVisibility(View.VISIBLE);
                }
            });
        } else {
            email.setError("Invalid Email Pattern!",errorIcon);
            resetPasswordButton.setEnabled(true);
            resetPasswordButton.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void checkInputs() {
        if (!email.getText().toString().isEmpty()) {
            resetPasswordButton.setEnabled(true);
            resetPasswordButton.setTextColor(getResources().getColor(R.color.white));
        } else {
            resetPasswordButton.setEnabled(false);
            resetPasswordButton.setTextColor(getResources().getColor(R.color.transWhite));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}