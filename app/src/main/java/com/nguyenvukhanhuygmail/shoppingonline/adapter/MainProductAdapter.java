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
import com.nguyenvukhanhuygmail.shoppingonline.model.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by toannq on 11/10/2017.
 */

public class MainProductAdapter extends BaseAdapter {

    Context context;
    ArrayList<Product> arr_product;

    public MainProductAdapter(Context context, ArrayList<Product> arr_product) {
        this.context = context;
        this.arr_product = arr_product;
    }

    @Override
    public int getCount() {
        return arr_product.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_product.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        public TextView txt_mainProductName, txt_mainProductPrice, txt_mainProductDes;
        public ImageView img_mainProduct;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        //khởi tạo
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.main_line_product, null);
            viewHolder.img_mainProduct = (ImageView) view.findViewById(R.id.img_main_product);
            viewHolder.txt_mainProductName = (TextView) view.findViewById(R.id.txt_main_product);
            viewHolder.txt_mainProductPrice = (TextView) view.findViewById(R.id.txt_main_price);
            viewHolder.txt_mainProductDes = (TextView) view.findViewById(R.id.txt_mainProductDes);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        //gán giá trị
        Product product = (Product) getItem(i);
        viewHolder.txt_mainProductName.setText(product.getProduct_name());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txt_mainProductPrice.setText("Giá: " + decimalFormat.format(product.getProduct_price()) + "đ");
        Picasso.with(context).load(product.getProduct_image())
                .placeholder(R.drawable.wait)
                .error(R.drawable.error)
                .into(viewHolder.img_mainProduct);
        viewHolder.txt_mainProductDes.setMaxLines(2);
        viewHolder.txt_mainProductDes.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txt_mainProductDes.setText(product.getProduct_description());

        return view;
    }
}
