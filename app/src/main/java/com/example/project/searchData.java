package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class searchData extends AppCompatActivity {
    private Button lastWeekButton;
    private Button lastMonthButton;
    private Button searchButton;
    private EditText timestampEditText;
    private RecyclerView recyclerView;
    private myAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);

        // Initialize views
        lastWeekButton = findViewById(R.id.last_week_button);
        lastMonthButton = findViewById(R.id.last_month_button);
        searchButton = findViewById(R.id.search_button);
        timestampEditText = findViewById(R.id.timestamp_edit_text);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new myAdapter();
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("my_data");


        lastWeekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -7);
                long timestamp = calendar.getTimeInMillis();

                Query query = databaseReference.orderByChild("sensor").startAt(timestamp);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<HelperClass2> dataList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HelperClass2 data = dataSnapshot.getValue(HelperClass2.class);
                            dataList.add(data);
                        }
                        adapter.setDataList(dataList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MyActivity", "Failed to retrieve data", error.toException());
                    }
                });
            }
        });


        lastMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.MONTH, -1);
                long timestamp = calendar.getTimeInMillis();

                Query query = databaseReference.orderByChild("timestamp").startAt(timestamp);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<HelperClass2> dataList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HelperClass2 data = dataSnapshot.getValue(HelperClass2.class);
                            dataList.add(data);
                        }
                        adapter.setDataList(dataList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MyActivity", "Failed to retrieve data", error.toException());
                    }
                });
            }
        });


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userInput = timestampEditText.getText().toString();
                if (userInput.isEmpty()) {
                    Toast.makeText(searchData.this, "Please enter a timestamp", Toast.LENGTH_SHORT).show();
                    return;
                }

                long timestamp = Long.parseLong(userInput);

                Query query = databaseReference.orderByChild("timestamp").equalTo(timestamp);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<HelperClass2> dataList = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            HelperClass2 data = dataSnapshot.getValue(HelperClass2.class);
                            dataList.add(data);
                        }
                        adapter.setDataList(dataList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("MyActivity", "Failed to retrieve data", error.toException());
                    }
                });
            }
        });
    }
}
