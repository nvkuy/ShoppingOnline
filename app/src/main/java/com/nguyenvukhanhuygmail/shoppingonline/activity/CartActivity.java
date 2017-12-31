package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;

import java.text.DecimalFormat;

public class CartActivity extends AppCompatActivity {

    Toolbar tb_cart;
    ImageView show_grid, show_list;
    TextView btn_buyall, total_money;

    long money = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (CheckConnection.haveNetworkConnection(getApplication())) {
            start();
            ActionBar();
            onClick();
            show_cart();
        }

    }

    private void onClick() {

        show_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        show_grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        btn_buyall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void show_cart() {

    }

    private void ActionBar() {
        setSupportActionBar(tb_cart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tb_cart.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void start() {
        tb_cart = (Toolbar) findViewById(R.id.tb_cart);
        show_grid = (ImageView) findViewById(R.id.show_grid);
        show_list = (ImageView) findViewById(R.id.show_list);
        btn_buyall = (TextView) findViewById(R.id.buy_all);
        total_money = (TextView) findViewById(R.id.total_money);

        for (int i = 0; i < MainActivity.arr_cart.size(); i++) {
            money += MainActivity.arr_cart.get(i).getProduct_price();
        }

        DecimalFormat formater = new DecimalFormat("###,###,###");
        total_money.setText("Tổng số tiền: " + formater.format(money) + "đ");

    }
}
