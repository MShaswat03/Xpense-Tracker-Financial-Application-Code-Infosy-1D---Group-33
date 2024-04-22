package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.databinding.ActivityDashBoardBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashBoardActivity extends AppCompatActivity {

    ActivityDashBoardBinding binding;
    SettingsFragment settingsFragment;

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    int sumExpense = 0, sumIncome = 0;

    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        transactionModelArrayList = new ArrayList<>();
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setHasFixedSize(true);

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(DashBoardActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignOutDialog();
            }
        });
        binding.addFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashBoardActivity.this, AddTransactionActivity.class);
                try{
                    startActivity(intent);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(DashBoardActivity.this, DashBoardActivity.class));
                    finish();
                }
                catch (Exception e){

                }
            }
        });


        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                showClickedMsg(item.getTitle() + " clicked");
                if (id == R.id.expense_analysis_menu) {
                    createExpenseAnalysisActivity();
                    return true;
                }
                if (id == R.id.monthly_expenses_menu) {
                    return true;
                }

                return false;
            }
/*
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
//
//                }
                return true;
            }
*/
        });


        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        loadData();
    }

    private void createSignOutDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardActivity.this);
        builder.setTitle("Logout").setMessage("Are you sure you want to Log Out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }
    private void loadData(){
        firebaseFirestore.collection("Expenses").document(firebaseAuth.getUid()).collection("Notes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                SimpleDateFormat  sourceDateformat = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();

                for (DocumentSnapshot ds: task.getResult()) {
                    TransactionModel model = new TransactionModel(
                            ds.getString("id"),
                            ds.getString("note"),
                            ds.getString("amount"),
                            ds.getString("type"),
                            ds.getString("date"),
                            ds.getString("category"));
                    int amount = Integer.parseInt(ds.getString("amount"));
                    String dateS = ds.getString("date");
                    try {
                        if(ds.getString("type").equals("Expense")){
                            sumExpense= sumExpense+amount;
                        }
                        else{
                            sumIncome = sumIncome+amount;
                        }
                        transactionModelArrayList.add(model);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                binding.totalIncome.setText(String.valueOf(sumIncome));
                binding.totalExpense.setText(String.valueOf(sumExpense));
                binding.balance.setText(String.valueOf(sumIncome-sumExpense));
                transactionAdapter = new TransactionAdapter(DashBoardActivity.this, transactionModelArrayList);
                binding.historyRecyclerView.setAdapter(transactionAdapter);
                }
        });
    }
    private void showClickedMsg(String message)
    {
        Toast msgtoast = Toast.makeText(this.getBaseContext(), message,
                Toast.LENGTH_LONG);
        msgtoast.show();
    }

    private void createExpenseAnalysisActivity(){
        Intent intent = new Intent(DashBoardActivity.this, ExpenseAnalysis.class);
        try{
            startActivity(intent);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void createPieChartActivity(){
        Intent intent = new Intent(DashBoardActivity.this, PieChartActivity.class);
        try{
            startActivity(intent);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}