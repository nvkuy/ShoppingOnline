package com.nguyenvukhanhuygmail.shoppingonline.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.model.Cart;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by toannq on 1/21/2018.
 */

public class CartGridAdapter extends BaseAdapter {

    Context context;
    ArrayList<Cart> arrCart;

    public CartGridAdapter(Context context, ArrayList<Cart> arrCart) {
        this.context = context;
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
        public TextView tvCartName, tvCartNum, tvCartPrice;

    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        if (view == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.card_grid, null);

            viewHolder.imgCart = (ImageView) view.findViewById(R.id.CartProductImage);
            viewHolder.tvCartName = (TextView) view.findViewById(R.id.CartProductName);
            viewHolder.tvCartNum = (TextView) view.findViewById(R.id.CartProductNum);
            viewHolder.tvCartPrice = (TextView) view.findViewById(R.id.CartProductPrice);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Cart cart_item = (Cart) getItem(i);

        Picasso.with(context)
                .load(cart_item.getProduct_image())
                .placeholder(R.drawable.wait)
                .error(R.drawable.error)
                .into(viewHolder.imgCart);

        DecimalFormat formater = new DecimalFormat("###,###,###");
        viewHolder.tvCartPrice.setText(formater.format(cart_item.getProduct_price()) + "đ");

        viewHolder.tvCartName.setMaxLines(3);
        viewHolder.tvCartName.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.tvCartName.setText(cart_item.getProduct_name());

        viewHolder.tvCartNum.setText(String.valueOf("Số lượng: " + cart_item.getProduct_number()));

        return view;
    }

}
