package com.example.sendit_chatapp.packages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.view.View;

import com.example.sendit_chatapp.R;
import com.example.sendit_chatapp.databinding.ActivityChatDetailBinding;
import com.example.sendit_chatapp.packages.Adapters.ChatAdapter;
import com.example.sendit_chatapp.packages.Models.MessageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class ChatDetailActivity extends AppCompatActivity {
   ActivityChatDetailBinding binding;
   FirebaseDatabase database;
   FirebaseAuth auth;
//   private byte encryptionKey[] = {9,11,13,15,16,-21,54,76,39};
//   private Cipher cipher, decipher;
//   private SecretKeySpec secretKeySpec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

//        try {
//            cipher = Cipher.getInstance("AES");
//            decipher = Cipher.getInstance("AES");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        }

//        secretKeySpec = new SecretKeySpec(encryptionKey, "AES");


        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderId = auth.getUid();
        String receiverId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");
        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(binding.profileImage2);

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatDetailActivity.this, MainActivity.class));
            }
        });

         ArrayList<MessageModel> messageModels = new ArrayList<>();
         ChatAdapter chatAdapter = new ChatAdapter(messageModels, this, receiverId);
        binding.chatsRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatsRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId+receiverId;
        final String receiverRoom = receiverId+senderId;

        database.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                          messageModels.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()) {
                             MessageModel model = snapshot1.getValue(MessageModel.class);
                             model.setMessageId(snapshot1.getKey());

                             messageModels.add(model);

                         }

                        ChatAdapter chatAdapter = new ChatAdapter(messageModels, getApplicationContext());
                        binding.chatsRecyclerView.setAdapter(chatAdapter);

                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        binding.sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = (binding.etMessage.getText().toString());
                if (message.isEmpty()) {
                    binding.etMessage.setText("");
                } else if(message.trim().length() == 0){
                    binding.etMessage.setText("");
                }
                else {
                    MessageModel model = new MessageModel(senderId, message);
                    model.setTimestamp(new Date().getTime());
                    binding.etMessage.setText("");
                    database.getReference().child("chats")
                            .child(senderRoom)
                            .push()
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            database.getReference().child("chats")
                                    .child(receiverRoom)
                                    .push()
                                    .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                }
                            });
                        }
                    });
                }
            }
        });
    }

//    private String AESencryptionMethod(String string) {
//       byte[] stringByte = string.getBytes();
//       byte[] encryptedByte = new byte[stringByte.length];
//
//        try {
//            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
//            encryptedByte = cipher.doFinal(stringByte);
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//        String returnString = null;
//        try {
//            returnString = new String(encryptedByte, "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        return returnString;
//    }

//    private String AESdecryptionMethod(String string) throws UnsupportedEncodingException {
//        byte[] EncryptedByte = string.getBytes("ISO-8859-1");
//        String decryptedString = string;
//        byte[] decryption;
//
//        try {
//            decipher.init(cipher.DECRYPT_MODE, secretKeySpec);
//            decryption = decipher.doFinal(EncryptedByte);
//            decryptedString = new String(decryption);
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        }
//       return decryptedString;
//    }
}