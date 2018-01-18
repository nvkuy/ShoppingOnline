package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.CategoryAdapter;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.Tabbar_Adapter;
import com.nguyenvukhanhuygmail.shoppingonline.fragment.ProfileFragment;
import com.nguyenvukhanhuygmail.shoppingonline.fragment.ShoppingFragment;
import com.nguyenvukhanhuygmail.shoppingonline.model.Cart;
import com.nguyenvukhanhuygmail.shoppingonline.model.Category;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.PicassoCircleTransformation;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ShoppingFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener {

//    final int Slide_in = R.anim.slide_in;
//    final int Slide_out = R.anim.slide_out;

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager pager;
    NavigationView nv_home;
    //    ListView lv_home;
    GridView gv_category;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle Toggle;
    ArrayList<Category> arr_category;
    CategoryAdapter categoryAdapter;

    DatabaseReference mData;
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageRef;

    Button btn_logout;
    TextView tv_username, tv_email;
    ImageView user_icon, user_wall;

    int id = 0;
    String Category_name = "";
    String Category_image = "";

    String[] tab_name = {"Cửa hàng", "Người dùng"};
    int[] tab_icon = {R.drawable.ic_shopping_cart_white_24dp, R.drawable.ic_person_outline_white_24dp};

    Tabbar_Adapter tabbar_adapter;

    public static ArrayList<Cart> arr_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (CheckConnection.haveNetworkConnection(getApplication())) {

            //nếu có kết nối
            start();

            display_TabBar();
            display_NavHeader();
            showListView();
            ActionBar();

            onNavItemClick();

        } else {
            CheckConnection.notification(getApplication(), "Please check your connection!");
            finish();
        }

    }

    private void display_TabBar() {

        tabLayout.addTab(tabLayout.newTab().setText(tab_name[0]).setIcon(tab_icon[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tab_name[1]).setIcon(tab_icon[1]));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        tabbar_adapter = new Tabbar_Adapter(getSupportFragmentManager(), tabLayout.getTabCount());
        pager.setAdapter(tabbar_adapter);
        pager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void display_NavHeader() {

        // display user name
        mData.child("Users").child(user.getUid()).child("user_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    tv_username.setText(dataSnapshot.getValue().toString());
                } catch (NullPointerException e) {

                    startActivity(new Intent(getApplication(), UserProfile.class));
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // display uEmail
        tv_email.setText(user.getEmail());

        // display uIcon
        storageRef.child("uIcon" + user.getUid() + ".png")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplication())
                                .load(uri)
                                .placeholder(R.drawable.wait)
                                .error(R.drawable.default_user)
                                .transform(new PicassoCircleTransformation())
                                .into(user_icon);
                    }
                });

        //display uWall
        storageRef.child("uWall" + user.getUid() + ".png")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getApplication())
                                .load(uri)
                                .placeholder(R.drawable.wait)
                                .error(R.drawable.profile_gradient)
                                .into(user_wall);
                    }
                });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogoutAndBack();
            }
        });

    }

    private void LogoutAndBack() {
        startActivity(new Intent(getApplication(), LoginAndSignUp.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void ToActivity(Class activity, int pos) {
        Intent i = new Intent(getApplication(), activity);
        i.putExtra("title", arr_category.get(pos).getCategoty_name());
        i.putExtra("product_id", arr_category.get(pos).getId());
        startActivity(i);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void onNavItemClick() {

        gv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    //click vào trang chính
                } else if (i == (arr_category.size() - 1)) {
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

    private void ActionBar() {

        setSupportActionBar(toolbar);
        Toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(Toggle);
        Toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_apps_white_24dp);

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
        gv_category.setAdapter(categoryAdapter);

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

        mData = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReferenceFromUrl("gs://shopping-online-6c182.appspot.com");
//        StorageReference uIconRef = storageRef.child("uIcon" + user.getUid() + ".png");
//        StorageReference uWallRef = storageRef.child("uWall" + user.getUid() + ".png");

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        pager = (ViewPager) findViewById(R.id.paper);

        nv_home = (NavigationView) findViewById(R.id.nav_view);
//        lv_home = (ListView) findViewById(R.id.lv_home);
        gv_category = (GridView) findViewById(R.id.gv_category);
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_username = (TextView) findViewById(R.id.tv_username);
        user_icon = (ImageView) findViewById(R.id.user_icon);
        user_wall = (ImageView) findViewById(R.id.user_wall);

        arr_category = new ArrayList<>();

        if (arr_cart == null) {
            arr_cart = new ArrayList<>();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
