package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder>{
    Context context;
    ArrayList<TransactionModel> transactionModelArrayList;

    public TransactionAdapter(Context context, ArrayList<TransactionModel> transactionModelArrayList) {
        this.context = context;
        this.transactionModelArrayList = transactionModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_recycler_item,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TransactionModel model = transactionModelArrayList.get(position);
        String priority = model.getType();
        if(priority.equals("Expense")){
            holder.priority.setBackgroundResource(R.drawable.red_shape);
        }
        else{
            holder.priority.setBackgroundResource(R.drawable.green_shape);
        }

        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDate());
        holder.note.setText(model.getNote());
        holder.category.setText(model.getCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id",transactionModelArrayList.get(position).getId());
                intent.putExtra("amount",transactionModelArrayList.get(position).getAmount());
                intent.putExtra("note",transactionModelArrayList.get(position).getNote());
                intent.putExtra("type",transactionModelArrayList.get(position).getType());
                intent.putExtra("category",transactionModelArrayList.get(position).getCategory());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return transactionModelArrayList.size();
    } //to show the database data in the UI {recycler view}
    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView note, amount, date, category;
        View priority;
        public MyViewHolder(@NotNull View itemView){
            super(itemView);
            note=itemView.findViewById(R.id.note_one);
            amount=itemView.findViewById(R.id.amount_one);
            date=itemView.findViewById(R.id.date_one);
            priority=itemView.findViewById(R.id.priority_one);
            category= itemView.findViewById(R.id.category_one);
        }
    }
}
