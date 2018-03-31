package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.MainProductAdapter;
import com.nguyenvukhanhuygmail.shoppingonline.model.Product;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductActivity extends AppCompatActivity {

    String title;
    int id_product = 0;
    int page = 1;

    View footer;
    boolean isLoading = false;
    boolean isLimit = false;
    mHandler handler;

    LayoutAnimationController controller;

    ArrayList<Product> arr_product;
    MainProductAdapter mainProductAdapter;
    ListView lv_product;
    Toolbar toolbar_product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        if (CheckConnection.haveNetworkConnection(getApplication())) {

            start();
            get_intent();
            ActionToolbar();
            setTitle(title);
            getProductByCategoryId(page);
            LoadMore();
            OnLVItemClick();

        } else {
            CheckConnection.notification(getApplication(), "Please check your connection!");
            finish();
        }


    }

    private void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void OnLVItemClick() {
        lv_product.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Product product = arr_product.get(i);
                Intent intent = new Intent(getApplication(), AboutProduct.class);
                intent.putExtra("product", product);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });
    }

    private void LoadMore() {

        lv_product.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int FirstItem, int VisibleItem, int TotalItem) {
                if (FirstItem + VisibleItem == TotalItem && TotalItem != 0 && !isLoading && !isLimit) {

                    isLoading = true;
                    mThread thread = new mThread();
                    thread.start();

                }
            }
        });
    }


    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    lv_product.addFooterView(footer);
                    break;
                case 1:
                    page++;
                    getProductByCategoryId(page);
                    isLoading = false;
                    break;
            }

            super.handleMessage(msg);
        }
    }


    private void getProductByCategoryId(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = Server.main_product_path + Page;
//        Log.d("url_product", url);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                lv_product.removeFooterView(footer);

                if (response.length() > 2) {
                    try {

                        JSONArray jsonArray = new JSONArray(response);
//                        Log.d("jsonArray", String.valueOf(jsonArray));

                        for (int i = 0; i < jsonArray.length(); i++) {
                            arr_product.add(new Product(
                                    jsonArray.getJSONObject(i).getInt("product_id"),
                                    jsonArray.getJSONObject(i).getString("product_name"),
                                    jsonArray.getJSONObject(i).getInt("product_price"),
                                    jsonArray.getJSONObject(i).getString("product_image"),
                                    jsonArray.getJSONObject(i).getString("product_description"),
                                    jsonArray.getJSONObject(i).getInt("orders"),
                                    jsonArray.getJSONObject(i).getDouble("rate_point"),
                                    jsonArray.getJSONObject(i).getInt("product_left"),
                                    jsonArray.getJSONObject(i).getInt("category_id")

                            ));

//                            Log.d("json_arr", String.valueOf(arr_product.size()));

                        }

                        mainProductAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    isLimit = true;
                    page--;
                    lv_product.removeFooterView(footer);
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                CheckConnection.notification(getApplication(), "Please check your connection!");
                finish();
            }

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                HashMap<String, String> param = new HashMap<>();
                param.put("category_id", String.valueOf(id_product));
//                Log.d("category_id", String.valueOf(id_product));

                return param;
            }

        };

        requestQueue.add(request);

    }


    private void ActionToolbar() {
        setSupportActionBar(toolbar_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        toolbar_product.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    public void get_intent() {
        Intent i = getIntent();
        id_product = i.getIntExtra("product_id", -1);
        title = i.getStringExtra("title");
    }

    private void start() {

        arr_product = new ArrayList<>();

        toolbar_product = (Toolbar) findViewById(R.id.toolbar_product);
        lv_product = (ListView) findViewById(R.id.lv_product);

        mainProductAdapter = new MainProductAdapter(getApplicationContext(), arr_product);
        lv_product.setAdapter(mainProductAdapter);

        controller = AnimationUtils.loadLayoutAnimation(getApplicationContext(), R.anim.list_item_anim_controller);
        lv_product.setLayoutAnimation(controller);
        lv_product.scheduleLayoutAnimation();


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(R.layout.progressbar, null);
        handler = new mHandler();

    }

    public class mThread extends Thread {
        @Override
        public void run() {

            handler.sendEmptyMessage(0);

            try {
                mThread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message message = handler.obtainMessage(1);
            handler.sendMessage(message);

            super.run();
        }
    }

}
