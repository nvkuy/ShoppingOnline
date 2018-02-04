package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.CartGridAdapter;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    Toolbar tb_cart;
    TextView btn_buyall, total_money;
    GridView gv_cart;

    CartGridAdapter cartGridAdapter;

    long money = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (CheckConnection.haveNetworkConnection(getApplication())) {
            start();
            ActionBar();
            showCart();
            onClick();
        } else {
            CheckConnection.notification(getApplicationContext(), "Vui lòng kiểm tra kết nối!");
            finish();
        }

    }

    private void showCart() {

        cartGridAdapter = new CartGridAdapter(getApplicationContext(), MainActivity.arr_cart);
        cartGridAdapter.notifyDataSetChanged();
        gv_cart.setAdapter(cartGridAdapter);

    }

    private void onClick() {

        btn_buyall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void ActionBar() {
        setSupportActionBar(tb_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        tb_cart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void start() {

        gv_cart = (GridView) findViewById(R.id.gv_cart);
        tb_cart = (Toolbar) findViewById(R.id.tb_cart);
        btn_buyall = (TextView) findViewById(R.id.buy_all);
        total_money = (TextView) findViewById(R.id.total_money);

        for (int i = 0; i < MainActivity.arr_cart.size(); i++) {
            money += MainActivity.arr_cart.get(i).getProduct_price();
        }

        DecimalFormat formater = new DecimalFormat("###,###,###");
        total_money.setText("Tổng số tiền: " + formater.format(money) + "đ");

    }
}
