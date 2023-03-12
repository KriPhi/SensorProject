package com.vu.lorasensorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class adminLogin extends AppCompatActivity {

    Button loginBtn, backBtn;
    EditText userUsername, userPassword;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        userUsername = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        backBtn = findViewById(R.id.backBtn);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {

                } else {
                    checkUser();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminLogin.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


    public Boolean validateUsername() {
        String val = userUsername.getText().toString();
        if (val.isEmpty()) {
            userUsername.setError("username cannot be empty");
            return false;
        } else {
            userUsername.setError(null);
            return true;
        }
    }

    public Boolean validatePassword() {
        String val = userPassword.getText().toString().trim();
        if (val.isEmpty()) {
            userPassword.setError("password cannot be empty");
            return false;
        } else {
            userPassword.setError(null);
            return true;
        }
    }

    public void checkUser() {
        String loginUsername = userUsername.getText().toString().trim();
        String loginPassword = userPassword.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(loginUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    userUsername.setError(null);
                    String passwordFromDB = snapshot.child(loginUsername).child("password").getValue(String.class);

                    if (passwordFromDB.equals(loginPassword)) {
                        userPassword.setError(null);

                        String usernameFromDB = snapshot.child(loginUsername).child("username").getValue(String.class);
                        String roleFromDB = snapshot.child(loginUsername).child("role").getValue(String.class);

                        adminPreference.setIsAdmin(adminLogin.this, true);

                        Intent intent = new Intent(adminLogin.this, adminHome.class);

                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);
                        intent.putExtra("roleFromDB", roleFromDB);


                        startActivity(intent);
                    } else {
                        userPassword.setError("invalid credentials");
                        userPassword.requestFocus();
                        handleFailedLogin(loginUsername);
                    }
                } else {
                    userUsername.setError("user does not exist");
                    userUsername.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void handleFailedLogin(String username) {
        int maxAttempts = 5;
        long lockoutTime = 5 * 60 * 1000; // 5 minutes in milliseconds
        SharedPreferences preferences = getSharedPreferences("loginAttempts", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        int failedAttempts = preferences.getInt(username, 0);
        long lastAttemptTime = preferences.getLong(username + "_time", 0);
        long currentTime = System.currentTimeMillis();

        if (failedAttempts >= maxAttempts && currentTime - lastAttemptTime < lockoutTime) {
            long remainingTime = lockoutTime - (currentTime - lastAttemptTime);
            int minutes = (int) (remainingTime / (60 * 1000));
            int seconds = (int) ((remainingTime / 1000) % 60);
            Toast.makeText(this, "You have exceeded the maximum number of login attempts. Please try again in " + minutes + " minutes and " + seconds + " seconds.", Toast.LENGTH_LONG).show();
        } else {
            editor.putInt(username, failedAttempts + 1);
            editor.putLong(username + "_time", currentTime);
            editor.apply();
            int remainingAttempts = maxAttempts - (failedAttempts + 1);
            Toast.makeText(this, "Invalid login credentials. You have " + remainingAttempts + " attempts remaining.", Toast.LENGTH_SHORT).show();
        }
    }
}