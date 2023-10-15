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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpFragment extends Fragment {

    private TextView alreadyHaveAnAccount;
    private FrameLayout frameLayout;
    private Drawable errorIcon;
    private EditText userName;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button signUpButton;

    private FirebaseAuth mAuth;
    private ProgressBar signUpProgress;

    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        alreadyHaveAnAccount = view.findViewById(R.id.already_have_account);
        frameLayout = getActivity().findViewById(R.id.register_frame_layout);
        errorIcon = getResources().getDrawable(R.drawable.ic_error);
        userName = view.findViewById(R.id.userName);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);
        confirmPassword = view.findViewById(R.id.confirmPassword);

        signUpButton = view.findViewById(R.id.signUpButton);

        mAuth = FirebaseAuth.getInstance();

        signUpProgress = view.findViewById(R.id.signUpProgress);

        db = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        errorIcon.setBounds(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight());

        alreadyHaveAnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        userName.addTextChangedListener(new TextWatcher() {
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

        password.addTextChangedListener(new TextWatcher() {
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

        confirmPassword.addTextChangedListener(new TextWatcher() {
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

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpButton.setEnabled(false);
                signUpButton.setTextColor(getResources().getColor(R.color.transWhite));
                signUpWithFirebase();
            }
        });
    }

    private void signUpWithFirebase() {
        if (email.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
            if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                signUpProgress.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                signUpProgress.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "User registered successfully. Please verify your Email.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("userName", userName.getText().toString());
                                    user.put("emailId", email.getText().toString());
                                    db.collection("users")
                                            .document(task.getResult().getUser().getUid())
                                            .set(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    setFragment(new SignInFragment());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    signUpButton.setEnabled(true);
                                                    signUpButton.setTextColor(getResources().getColor(R.color.white));
                                                }
                                            });
                                } else {
                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    signUpButton.setEnabled(true);
                                    signUpButton.setTextColor(getResources().getColor(R.color.white));
                                }
                            }
                        });

            } else {
                confirmPassword.setError("Password Mismatch!", errorIcon);
                signUpButton.setEnabled(true);
                signUpButton.setTextColor(getResources().getColor(R.color.white));
            }
        } else {
            email.setError("Invalid Email Pattern!", errorIcon);
            signUpButton.setEnabled(true);
            signUpButton.setTextColor(getResources().getColor(R.color.white));
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.from_left, R.anim.out_from_right);
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!userName.getText().toString().isEmpty()) {
            if (!email.getText().toString().isEmpty()) {
                if (!password.getText().toString().isEmpty() && password.getText().toString().length() >= 6) {
                    if (!confirmPassword.getText().toString().isEmpty()) {
                        signUpButton.setEnabled(true);
                        signUpButton.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        signUpButton.setEnabled(false);
                        signUpButton.setTextColor(getResources().getColor(R.color.transWhite));
                    }
                } else {
                    signUpButton.setEnabled(false);
                    signUpButton.setTextColor(getResources().getColor(R.color.transWhite));
                }
            } else {
                signUpButton.setEnabled(false);
                signUpButton.setTextColor(getResources().getColor(R.color.transWhite));
            }
        } else {
            signUpButton.setEnabled(false);
            signUpButton.setTextColor(getResources().getColor(R.color.transWhite));
        }
    }
}