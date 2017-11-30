package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.CategoryAdapter;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.ProductAdapter;
import com.nguyenvukhanhuygmail.shoppingonline.model.Cart;
import com.nguyenvukhanhuygmail.shoppingonline.model.Category;
import com.nguyenvukhanhuygmail.shoppingonline.model.Product;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.RecyclerItemClickListener;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final int Slide_in = R.anim.slide_in;
    final int Slide_out = R.anim.slide_out;

    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView rv_popular, rv_news, rv_sale;
    NavigationView nv_home;
    ListView lv_home;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    ArrayList<Category> arr_category;
    CategoryAdapter categoryAdapter;

    int id = 0;
    String Category_name = "";
    String Category_image = "";
    String links = "";
    String[] adv_links = new String[5];

    int product_id = 0;
    String product_name = "";
    int product_price = 0;
    String product_image = "";
    String product_description = "";
    int orders = 0;
    int category_id = 0;
    ArrayList<Product> arr_NewProduct, arr_PopularProduct, arr_SaleProduct;
    ProductAdapter NewProductAdapter, PopularProductAdapter, SaleProductAdapter;

    public static ArrayList<Cart> arr_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start();

        if (CheckConnection.haveNetworkConnection(getApplication())) {

            //nếu có kết nối
            showListView();
            ActionBar();
            showAdvs();
            //show new product
            showProduct(Server.new_product_path, 2);
            //show popular product
            showProduct(Server.popular_product_path, -1);
            //show sale product
            showProduct(Server.sale_product_path, 1);

            NewProductAdapter = new ProductAdapter(getApplicationContext(), arr_NewProduct, 2);
            PopularProductAdapter = new ProductAdapter(getApplicationContext(), arr_PopularProduct, -1);
            SaleProductAdapter = new ProductAdapter(getApplicationContext(), arr_SaleProduct, 1);

            rv_news.setAdapter(NewProductAdapter);
            rv_popular.setAdapter(PopularProductAdapter);
            rv_sale.setAdapter(SaleProductAdapter);

            onNavItemClick();
            onRV_ItemClick();

        } else {
            CheckConnection.notification(getApplication(), "Please check your connection!");
            finish();
        }

    }

    private void onRV_ItemClick() {

        rv_sale.addOnItemTouchListener(new RecyclerItemClickListener(getApplication(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product sale_product = arr_SaleProduct.get(position);
                GoToAboutProduct(getApplication(), sale_product);
            }
        }));

        rv_popular.addOnItemTouchListener(new RecyclerItemClickListener(getApplication(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product popular_product = arr_PopularProduct.get(position);
                GoToAboutProduct(getApplication(), popular_product);
            }
        }));

        rv_news.addOnItemTouchListener(new RecyclerItemClickListener(getApplication(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product new_product = arr_NewProduct.get(position);
                GoToAboutProduct(getApplication(), new_product);
            }
        }));
    }

    public void GoToAboutProduct(Context context, Product product) {
        Intent i = new Intent(context, AboutProduct.class);
        i.putExtra("product", product);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void ToActivity(Class activity, int pos) {
        Intent i = new Intent(getApplication(), activity);
        i.putExtra("title", arr_category.get(pos).getCategoty_name());
        i.putExtra("product_id", arr_category.get(pos).getId());
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void onNavItemClick() {

        lv_home.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    //click vào trang chính
                } else if (i == 6) {
                    //click vào liên hệ
                    ToActivity(ContactActivity.class, i);
                } else {
                    //click vào một loại sản phẩm
                    ToActivity(ProductActivity.class, i);
                }

                drawerLayout.closeDrawer(GravityCompat.START);


            }
        });

    }

    private void showProduct(String url, final int code) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {

                            //get sản phẩm
                            JSONObject jsonObject = response.getJSONObject(i);
                            product_id = jsonObject.getInt("product_id");
                            product_name = jsonObject.getString("product_name");
                            product_price = jsonObject.getInt("product_price");
                            product_image = jsonObject.getString("product_image");
                            product_description = jsonObject.getString("product_description");
                            orders = jsonObject.getInt("orders");
                            category_id = jsonObject.getInt("category_id");

                            if (code == 1) {
                                //get sản phẩm giảm giá và add vào mảng
                                arr_SaleProduct.add(i, new Product(product_id, product_name, product_price, product_image, product_description, orders, category_id));
                                SaleProductAdapter.notifyDataSetChanged();
                            } else if (code == 2) {

                                //get sản phẩm mới và add vào mảng
                                arr_NewProduct.add(i, new Product(product_id, product_name, product_price, product_image, product_description, orders, category_id));
                                NewProductAdapter.notifyDataSetChanged();
                            } else {

                                //get sản phẩm thông dụng và add vào mảng
                                arr_PopularProduct.add(i, new Product(product_id, product_name, product_price, product_image, product_description, orders, category_id));
                                PopularProductAdapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    private void ActionBar() {

        setSupportActionBar(toolbar);
        Toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void showAdvs() {
        RequestQueue requesQuese = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsArrRequest = new JsonArrayRequest(Server.adv_path, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    CheckConnection.notification(getApplication(), "Error!");
                } else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            links = jsonObject.getString("link_advs");
                            adv_links[i] = links;
                            ImageView imgView = new ImageView(getApplicationContext());
                            Picasso.with(getApplicationContext()).load(adv_links[i]).into(imgView);
                            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                            viewFlipper.addView(imgView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });

        requesQuese.add(jsArrRequest);

        viewFlipper.setFlipInterval(7500);
        viewFlipper.setAutoStart(true);

        Animation anim_in = AnimationUtils.loadAnimation(getApplicationContext(), Slide_in);
        Animation anim_out = AnimationUtils.loadAnimation(getApplicationContext(), Slide_out);

        viewFlipper.setInAnimation(anim_in);
        viewFlipper.setOutAnimation(anim_out);

    }


    //getData from sever
    private void showListView() {
        RequestQueue requesQuese = Volley.newRequestQueue(getApplicationContext());
        JsonArrayRequest jsonArrRequest = new JsonArrayRequest(Server.category_path, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    CheckConnection.notification(getApplication(), "Error!");
                } else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            id = jsonObject.getInt("id");
                            Category_name = jsonObject.getString("product_name");
                            Category_image = jsonObject.getString("product_image");
                            arr_category.add(i, new Category(id, Category_name, Category_image));
                            categoryAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.toString());
            }
        });

        requesQuese.add(jsonArrRequest);
        categoryAdapter = new CategoryAdapter(arr_category, getApplicationContext());
        lv_home.setAdapter(categoryAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (Toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void start() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        viewFlipper = (ViewFlipper) findViewById(R.id.ViewFlipper);

        rv_popular = (RecyclerView) findViewById(R.id.rv_popular);
        rv_news = (RecyclerView) findViewById(R.id.rv_news);
        rv_sale = (RecyclerView) findViewById(R.id.rv_sale);

        nv_home = (NavigationView) findViewById(R.id.nav_view);
        lv_home = (ListView) findViewById(R.id.lv_home);
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        arr_category = new ArrayList<>();
        arr_NewProduct = new ArrayList<>();
        arr_PopularProduct = new ArrayList<>();
        arr_SaleProduct = new ArrayList<>();

        rv_news.setHasFixedSize(true);
        rv_popular.setHasFixedSize(true);
        rv_sale.setHasFixedSize(true);

//        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
//        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

//        rv_news.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        rv_popular.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        rv_sale.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

//        rv_sale.setLayoutManager(manager);
//        rv_popular.setLayoutManager(manager);
//        rv_news.setLayoutManager(manager);

        rv_news.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_popular.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_sale.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

        if (arr_cart == null) {
            arr_cart = new ArrayList<>();
        }

    }

}
