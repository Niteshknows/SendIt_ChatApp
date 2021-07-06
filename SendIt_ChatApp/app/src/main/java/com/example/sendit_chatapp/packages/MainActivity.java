package com.example.sendit_chatapp.packages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.sendit_chatapp.R;
import com.example.sendit_chatapp.databinding.ActivityMainBinding;
import com.example.sendit_chatapp.packages.Adapters.UsersAdapters;
import com.example.sendit_chatapp.packages.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        UsersAdapters adapters = new UsersAdapters(list, getApplicationContext());
        binding.chatsRecyclerView.setAdapter(adapters);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        binding.chatsRecyclerView.setLayoutManager(layoutManager);
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                   list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users users = dataSnapshot.getValue(Users.class);
                    users.setUserId(dataSnapshot.getKey());
                    if(dataSnapshot.getKey().equals(auth.getUid())){

                    } else {
                        list.add(users);
                    }
                }
                adapters.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.setting :
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;

            case R.id.logout :
                auth.signOut();
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
                break;
        }

        return true;
    }
}