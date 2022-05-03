package com.example.gongguham_;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class viewMoreFragment extends Fragment {
    private static final String TAG = "fragmentviewMore";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String phonenumber;
    private TextView phoneNumberTextE;

    public viewMoreFragment() {
        // Required empty public constructor
    }



    public static viewMoreFragment newInstance(String param1, String param2) {
        viewMoreFragment fragment = new viewMoreFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewmore, container, false);

        view.findViewById(R.id.myInfoButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.myDeliveryButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.changesButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.logoutButton).setOnClickListener(onClickListener);
        view.findViewById(R.id.userDeleteButton).setOnClickListener(onClickListener);
        final TextView nameTextView = view.findViewById(R.id.nameText);

        phoneNumberTextE = view.findViewById(R.id.phoneNumberText);



        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.e(TAG, "DocumentSnapshot data: " + document.getData());
                            nameTextView.setText(document.getData().get("name").toString());
                        } else {
                            Log.e(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return view;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.myInfoButton:
                    FirebaseAuth.getInstance().signOut();
                    startMyInfoActivity();
                    break;

                case R.id.logoutButton:
                    FirebaseAuth.getInstance().signOut();
                    startMainActivity();
                    break;


                case R.id.userDeleteButton:
                    FirebaseAuth.getInstance().signOut();
                    FirebaseFirestore.getInstance().collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully deleted!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error deleting document", e);
                            }
                        });
                    FirebaseAuth.getInstance().getCurrentUser().delete();
                    FirebaseAuth.getInstance().signOut();

                    startMainActivity();
                    break;
            }
        }
    };


    private  void startMainActivity(){
        Intent intent=new Intent(getContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    private  void startMyInfoActivity(){
        Intent intent=new Intent(getContext(),MyInfoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
      //  intent.putExtra("phonenumber",phoneNumberTextE.getText().toString());
        startActivity(intent);
    }


}