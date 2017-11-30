package com.nguyenvukhanhuygmail.shoppingonline.adapter;

import android.content.Context;
import android.icu.text.DecimalFormat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.model.Product;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by toannq on 11/9/2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>  {

    Context context;
    ArrayList<Product> arr_Product;
    int code;

    public ProductAdapter(Context context, ArrayList<Product> arr_Product, int code) {
        this.context = context;
        this.arr_Product = arr_Product;
        this.code = code;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_line, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Product product = arr_Product.get(position);

        holder.txt_ProductName.setMaxLines(1);
        holder.txt_ProductName.setEllipsize(TextUtils.TruncateAt.END);
        holder.txt_ProductName.setText(product.getProduct_name());
        DecimalFormat format = new DecimalFormat("###,###,###");
        holder.txt_ProductPrice.setText("Giá: " + format.format(product.getProduct_price()) + "đ");

        if (code == 1) { //khi một sản phẩm được giảm giá
            holder.sale_icon.setVisibility(View.VISIBLE);
            holder.new_icon.setVisibility(View.INVISIBLE);
        } else if (code == 2) { // khi một sản phẩm mới ra
            holder.new_icon.setVisibility(View.VISIBLE);
            holder.sale_icon.setVisibility(View.INVISIBLE);
        } else { //khi sản phẩm đó không mới và ko đc giảm giá
            holder.new_icon.setVisibility(View.INVISIBLE);
            holder.sale_icon.setVisibility(View.INVISIBLE);
        }

        Picasso.with(context).load(product.getProduct_image())
                .placeholder(R.drawable.wait)
                .error(R.drawable.error)
                .into(holder.img_Product);

    }


    @Override
    public int getItemCount() {
        return arr_Product.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {

        public TextView txt_ProductPrice, txt_ProductName;
        public ImageView img_Product, new_icon, sale_icon;

        public ViewHolder(View itemView) {
            super(itemView);

            txt_ProductName = (TextView) itemView.findViewById(R.id.txt_ProductName);
            txt_ProductPrice = (TextView) itemView.findViewById(R.id.txt_ProductPrice);
            img_Product = (ImageView) itemView.findViewById(R.id.img_NewProduct);
            new_icon = (ImageView) itemView.findViewById(R.id.new_icon);
            sale_icon = (ImageView) itemView.findViewById(R.id.sale_icon);

        }


    }

}
