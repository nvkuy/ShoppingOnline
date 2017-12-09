package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.model.Cart;
import com.nguyenvukhanhuygmail.shoppingonline.model.Product;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class AboutProduct extends AppCompatActivity {

    Product product;

    TextView fullname_product, AboutProduct_price, AboutProduct_des;
    Button btn_add, btn_buynow, btn_rate;
    ImageView img_AboutProduct;
    Toolbar toolbar;

    boolean isNum, isExits, isFull;
    long total_price = 0;
    int numProduct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_product);

        product = getIntentData("product");
        start();
        ActionBar();
        showProduct(product);
        onButtonClickListener();

    }

    private void rate() {
        final Dialog dialog_rate = new Dialog(this);
        dialog_rate.setContentView(R.layout.rate_dialog);

        Button btn_Send = dialog_rate.findViewById(R.id.btn_Send);
        Button btn_Cancel = dialog_rate.findViewById(R.id.btn_Cancel);
        final EditText edt_commnent = dialog_rate.findViewById(R.id.edit_comment);
        final RatingBar ratingBar = dialog_rate.findViewById(R.id.ratingBar);

        btn_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_rate.dismiss();
            }
        });

        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                float rating_point = ratingBar.getRating();
                String comment = edt_commnent.getText().toString();

                if (comment.equals("") || comment.isEmpty()) {
                    dialog_rate.dismiss();
                } else {

                }

            }
        });

        dialog_rate.show();
    }

    private void add(final int price) {
        final Dialog add_dialog = new Dialog(this);
        add_dialog.setContentView(R.layout.add_dialog);

        final EditText edt_number = add_dialog.findViewById(R.id.edt_NumberProduct);
        final TextView tv_price = add_dialog.findViewById(R.id.tv_price);
        final TextView tv_notify = add_dialog.findViewById(R.id.tv_notify);
        final Button btn_cof = add_dialog.findViewById(R.id.btn_cof);
        Button btn_exit = add_dialog.findViewById(R.id.btn_exit);

        final DecimalFormat formater = new DecimalFormat("###,###,###,###,###");
        tv_price.setText(formater.format(price) + "đ");

        edt_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    numProduct = Integer.parseInt(edt_number.getText().toString().trim());
                    total_price = price * numProduct;
                    isNum = total_price > 0;
                    tv_price.setText(formater.format(total_price) + "đ");
                } catch (Exception e) {
                    //khi người dùng nhập vào 1 chuỗi ko phải số nguyên
                    isNum = false;
                    tv_price.setText(formater.format(price) + "đ");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isNum && !edt_number.getText().toString().isEmpty()) {
                    tv_notify.setVisibility(View.VISIBLE);
                    tv_notify.setText(R.string.notify1);
                    btn_cof.setClickable(false);
                } else if (numProduct > 15) {
                    tv_notify.setVisibility(View.VISIBLE);
                    btn_cof.setClickable(false);
                    tv_notify.setText(R.string.notify2);
                } else {
                    btn_cof.setClickable(true);
                    tv_notify.setVisibility(View.GONE);
                }
            }
        });

        btn_cof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edt_number.getText().toString().isEmpty()) {
                    if (isNum && !edt_number.getText().toString().isEmpty()) {
                        if (MainActivity.arr_cart.size() > 0) {
                            //khi size mảng > 0(mảng đã có phần tử) thì kiểm tra xem người dùng đã add sản phẩm này hay chưa
                            for (int i = 1; i < MainActivity.arr_cart.size(); i++) {
                                if (MainActivity.arr_cart.get(i).getProduct_id() == product.getProduct_id()) {
                                    //khi mảng đã có sản phẩm này rồi thì cập nhật lại
                                    long old_price = MainActivity.arr_cart.get(i).getProduct_price();
                                    long new_price = old_price + total_price;
                                    isExits = true;

                                    long old_num = MainActivity.arr_cart.get(i).getProduct_number();
                                    long new_num = old_num + numProduct;

                                    MainActivity.arr_cart.get(i).setProduct_price(new_price);
                                    if (old_num == 15) {
                                        isFull = true;
                                    } else if (new_num >= 15) {
                                        isFull = true;
                                        MainActivity.arr_cart.get(i).setProduct_number(15);
                                    } else {
                                        isFull = false;
                                        MainActivity.arr_cart.get(i).setProduct_number(new_num);
                                    }

                                }
                            }

                            if (!isExits) {
                                //khi mảng chưa có sản phẩm này thì add vào
                                MainActivity.arr_cart.add(new Cart(
                                        product.getProduct_id(),
                                        product.getProduct_name(),
                                        total_price,
                                        product.getProduct_image(),
                                        numProduct
                                ));
                            }

                        } else {
                            //khi size của mảng = 0(mảng chưa có phần tử nào) thì add sản phẩm vào mảng
                            MainActivity.arr_cart.add(new Cart(
                                    product.getProduct_id(),
                                    product.getProduct_name(),
                                    total_price,
                                    product.getProduct_image(),
                                    numProduct
                            ));
                        }

                    } else {
                        CheckConnection.notification(getApplicationContext(), "Thêm vào giỏ hàng thất bại!");
                    }

                    if (isFull) {
                        tv_notify.setText(R.string.notify3);
                    } else {
                        add_dialog.dismiss();
                    }

                } else {
                    add_dialog.dismiss();
                }

            }
        });

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_dialog.dismiss();
            }
        });

        add_dialog.show();
    }

    private void onButtonClickListener() {

        btn_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rate();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(product.getProduct_price());
            }
        });

        btn_buynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void showProduct(Product product) {

        fullname_product.setText(product.getProduct_name());
        AboutProduct_des.setText("  " + product.getProduct_description());

        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        AboutProduct_price.setText("Giá: " + decimalFormat.format(product.getProduct_price()) + "đ");


        Picasso.with(getApplicationContext()).load(product.getProduct_image())
                .placeholder(R.drawable.wait)
                .error(R.drawable.error)
                .into(img_AboutProduct);

    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void start() {
        fullname_product = (TextView) findViewById(R.id.fullname_product);
        AboutProduct_des = (TextView) findViewById(R.id.AboutProduct_des);
        AboutProduct_price = (TextView) findViewById(R.id.AboutProduct_price);
        img_AboutProduct = (ImageView) findViewById(R.id.img_AboutProduct);
        btn_add = (Button) findViewById(R.id.btn_Add);
        btn_buynow = (Button) findViewById(R.id.btn_BuyNow);
        btn_rate = (Button) findViewById(R.id.btn_Rate);
        toolbar = (Toolbar) findViewById(R.id.toolbar_AboutProduct);
    }

    private Product getIntentData(String key) {

        Intent i = getIntent();
        Product product = (Product) getIntent().getExtras().getSerializable(key);

        return product;
    }

}
