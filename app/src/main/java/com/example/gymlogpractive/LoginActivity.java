package com.example.gymlogpractive;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gymlogpractive.database.GymLogRepository;
import com.example.gymlogpractive.database.entities.User;
import com.example.gymlogpractive.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    private GymLogRepository repository;

    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = GymLogRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verifyUser()) {
                    toastMaker("Invalid Username or Password");
                } else {
                    // User is verified, proceed to main activity
                    Intent intent = MainActivity.mainActivityIntentFactory(getApplicationContext(), user.getId());
                    startActivity(intent);
                }

            }
        });
    }

    private boolean verifyUser() {
        String username = binding.userNameLoginEditText.getText().toString();
        if(username.isEmpty()) {
            toastMaker("Username cannot be empty");
            return false;
        }
        user = repository.getUserByUsername(username);
        if (user != null) {
            String password = binding.passwordLoginEditText.getText().toString();
            if(password.equals(user.getPassword())) {
                return true;
            } else {
                toastMaker("Invalid Password");
                return false;
            }
        }
        toastMaker(String.format("User %s not found", username));
        return false;
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LoginActivity.class);
    }
}