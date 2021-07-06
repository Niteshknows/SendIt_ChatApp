package com.example.sendit_chatapp.packages.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sendit_chatapp.R;
import com.example.sendit_chatapp.packages.ChatDetailActivity;
import com.example.sendit_chatapp.packages.Models.MessageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter {
    ArrayList<MessageModel> messageModels;
    Context context;
    String recId;

    int SENDER_TYPE = 1;
    int RECEIVER_TYPE = 2;


    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context) {
        this.messageModels = messageModels;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModel> messageModels, Context context, String recId) {
        this.messageModels = messageModels;
        this.context = context;
        this.recId = recId;
    }

    @Override
    public int getItemViewType(int position) {
        if(messageModels.get(position).getuId().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_TYPE;
        } else {
            return RECEIVER_TYPE;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender, parent, false);
            return new SenderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
         MessageModel messageModel = messageModels.get(position);

         holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 Log.d("nitesh74", "inside long click");
//                 if(holder.getClass()==SenderViewHolder.class){
                      AlertDialog.Builder builder = new AlertDialog.Builder(context);
                              builder.setTitle("Delete")
                              .setMessage("Are you sure you want to delete the message")
                              .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {

                                      FirebaseDatabase database = FirebaseDatabase.getInstance();
                                      String senderRoom =  FirebaseAuth.getInstance().getUid() + recId;
                                      database.getReference().child("chats").child(senderRoom)
                                              .child(messageModel.getMessageId())
                                              .setValue(null);


                                  }
                              }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                          }
                      }).show();

                 return false;
             }

         });

         if(holder.getClass()==SenderViewHolder.class){
             ((SenderViewHolder)holder).senderMsg.setText(messageModel.getMessage());
             SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
             String dateString = formatter.format(new Date(Long.parseLong(messageModel.getTimestamp().toString())));
             ((SenderViewHolder)holder).senderTime
                     .setText(dateString);

         } else {
             ((ReceiverViewHolder)holder).receiverMsg.setText(messageModel.getMessage());
             SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
             String dateString = formatter.format(new Date(Long.parseLong(messageModel.getTimestamp().toString())));

             ((ReceiverViewHolder)holder).receiverTime.setText(dateString);

         }
    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{

        TextView receiverMsg, receiverTime;

        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.receiverText);
            receiverTime = itemView.findViewById(R.id.receiverTime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg, senderTime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.senderText);
            senderTime = itemView.findViewById(R.id.senderTime);
        }
    }
}
