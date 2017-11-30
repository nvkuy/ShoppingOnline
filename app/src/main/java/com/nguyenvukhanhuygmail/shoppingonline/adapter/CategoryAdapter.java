package com.nguyenvukhanhuygmail.shoppingonline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.model.Category;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by toannq on 11/5/2017.
 */

public class CategoryAdapter extends BaseAdapter {

    ArrayList<Category> arr_category;
    Context context;

    public CategoryAdapter(ArrayList<Category> arr_category, Context context) {
        this.arr_category = arr_category;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arr_category.size();
    }

    @Override
    public Object getItem(int i) {
        return arr_category.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder {
        TextView tv_category;
        ImageView category_icon;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        if (view == null) {

            //khi chạy lần đầu
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.category_line, null);

            viewHolder.tv_category = (TextView) view.findViewById(R.id.tv_category);
            viewHolder.category_icon = (ImageView) view.findViewById(R.id.category_icon);
            view.setTag(viewHolder);
        } else {
            //chạy những lần sau
            viewHolder = (ViewHolder) view.getTag();
        }

        //gán giá trị
        Category category = (Category) getItem(i);
        viewHolder.tv_category.setText(category.getCategoty_name());
        Picasso.with(context).load(category.getCategory_image())
                .placeholder(R.drawable.wait)   //img khi đang load
                .error(R.drawable.error)        //img khi load gặp lỗi
                .into(viewHolder.category_icon);//img khi load thành công

        return view;
    }
}
