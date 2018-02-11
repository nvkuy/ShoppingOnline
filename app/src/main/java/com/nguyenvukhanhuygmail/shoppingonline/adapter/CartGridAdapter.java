package com.nguyenvukhanhuygmail.shoppingonline.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.model.Cart;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CustomEditText;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.DrawableClickListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by toannq on 1/21/2018.
 */

public class CartGridAdapter extends BaseAdapter {

    Context context;
    Activity CurrentAct;
    ArrayList<Cart> arrCart;
    private ViewHolder viewHolder;

    public CartGridAdapter(Context context, Activity currentAct, ArrayList<Cart> arrCart) {
        this.context = context;
        CurrentAct = currentAct;
        this.arrCart = arrCart;
    }

    @Override
    public int getCount() {
        return arrCart.size();
    }

    @Override
    public Object getItem(int i) {
        return arrCart.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {

        public ImageView imgCart;
        public TextView tvCartName, tvCartPrice;
        public CustomEditText tvCartNum;

    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        viewHolder = null;
        if (view == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.cart_grid, null);

            viewHolder.imgCart = (ImageView) view.findViewById(R.id.CartProductImage);
            viewHolder.tvCartName = (TextView) view.findViewById(R.id.CartProductName);
            viewHolder.tvCartNum = (CustomEditText) view.findViewById(R.id.CartProductNum);
            viewHolder.tvCartPrice = (TextView) view.findViewById(R.id.CartProductPrice);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Cart cart_item = (Cart) getItem(i);

        Picasso.with(context)
                .load(cart_item.getProduct_image())
                .placeholder(R.drawable.wait)
                .error(R.drawable.error)
                .into(viewHolder.imgCart);

        final DecimalFormat formater = new DecimalFormat("###,###,###");
        viewHolder.tvCartPrice.setText(formater.format(cart_item.getProduct_price()) + "đ");

        viewHolder.tvCartName.setMaxLines(2);
        viewHolder.tvCartName.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.tvCartName.setText(cart_item.getProduct_name());

        viewHolder.tvCartNum.setText(String.valueOf(cart_item.getProduct_number()));

        viewHolder.tvCartNum.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {

                long oldNum = cart_item.getProduct_number();
                long oldPrice = cart_item.getProduct_price();

                switch (target) {
                    case RIGHT:

                        //click cộng
                        long newNum1 = oldNum + 1;

                        if (newNum1 > cart_item.getLimit_num()) {
                            Toast.makeText(context, "Hiện tại chỉ còn " + cart_item.getLimit_num() + " sản phẩm!", Toast.LENGTH_LONG).show();
                        } else {
                            cart_item.setProduct_number(newNum1);
                            cart_item.setProduct_price((oldPrice / oldNum) * newNum1);
                            viewHolder.tvCartPrice.setText(formater.format(cart_item.getProduct_price()) + "đ");
                            viewHolder.tvCartNum.setText(String.valueOf(cart_item.getProduct_number()));
                        }

                        break;
                    case LEFT:

                        //click trừ
                        long newNum2 = oldNum - 1;

                        if (newNum2 <= 0) {
                            arrCart.remove(cart_item);
                        } else {
                            cart_item.setProduct_number(newNum2);
                            viewHolder.tvCartNum.setText(String.valueOf(cart_item.getProduct_number()));
                        }

                        cart_item.setProduct_price((oldPrice / oldNum) * newNum2);
                        viewHolder.tvCartPrice.setText(formater.format(cart_item.getProduct_price()) + "đ");

                        break;
                }
                notifyDataSetChanged();
                CurrentAct.recreate();
            }
        });

        return view;
    }

}
