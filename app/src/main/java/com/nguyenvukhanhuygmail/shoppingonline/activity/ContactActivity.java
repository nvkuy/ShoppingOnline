package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.fragment.ContactMapFragment;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;

public class ContactActivity extends AppCompatActivity implements ContactMapFragment.OnFragmentInteractionListener {

    Toolbar tb_contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        if (CheckConnection.haveNetworkConnection(getApplication())) {
            start();
            ActionBar();
            showMapFragment();
        } else {
            CheckConnection.notification(getApplicationContext(), "Vui lòng kiểm tra kết nối!");
            finish();
        }


    }

    private void showMapFragment() {

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.container_map, new ContactMapFragment()).commit();

    }

    private void start() {
        tb_contact = (Toolbar) findViewById(R.id.tb_contact);
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
}
