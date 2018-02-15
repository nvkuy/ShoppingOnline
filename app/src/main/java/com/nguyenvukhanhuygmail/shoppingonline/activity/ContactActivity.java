package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.fragment.ContactMapFragment;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CustomEditText;

public class ContactActivity extends AppCompatActivity implements ContactMapFragment.OnFragmentInteractionListener, OnMapReadyCallback {

    Toolbar tb_contact;
    CustomEditText contactLocation, contactPhone, contactEmail;

    private DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        if (CheckConnection.haveNetworkConnection(getApplication())) {
            start();
            ActionBar();
            showMapFragment();
            setInfo();
        } else {
            CheckConnection.notification(getApplicationContext(), "Vui lòng kiểm tra kết nối!");
            finish();
        }


    }

    private void setInfo() {

        mData.child("Contact").child("Company").child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactLocation.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mData.child("Contact").child("Company").child("phone").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactPhone.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mData.child("Contact").child("Company").child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactEmail.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void showMapFragment() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.container_map, new ContactMapFragment()).commit();

    }

    private void start() {

        mData = FirebaseDatabase.getInstance().getReference();

        tb_contact = (Toolbar) findViewById(R.id.tb_contact);

        contactLocation = (CustomEditText) findViewById(R.id.contactLocation);
        contactEmail = (CustomEditText) findViewById(R.id.contactEmail);
        contactPhone = (CustomEditText) findViewById(R.id.contactPhone);
    }

    private void ActionBar() {
        setSupportActionBar(tb_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        tb_contact.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}
