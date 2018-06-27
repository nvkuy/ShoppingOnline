package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.CategoryAdapter;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.Tabbar_Adapter;
import com.nguyenvukhanhuygmail.shoppingonline.fragment.ProfileFragment;
import com.nguyenvukhanhuygmail.shoppingonline.fragment.SearchProductFragment;
import com.nguyenvukhanhuygmail.shoppingonline.fragment.ShipperFragment;
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

public class MainActivity extends AppCompatActivity implements ShoppingFragment.OnFragmentInteractionListener, ProfileFragment.OnFragmentInteractionListener,
        SearchProductFragment.OnFragmentInteractionListener, ShipperFragment.OnFragmentInteractionListener {

    final int slide_in = R.anim.slide_in;
    final int slide_out = R.anim.slide_out;

//    android.support.v7.widget.SearchView sview_main;
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
    MaterialSearchView searchView;

    int id = 0;
    String Category_name = "";
    String Category_image = "";

    String[] tab_name = {"Trang chính", "Hồ sơ", "Ship deal"};
    int[] tab_icon = {
            R.drawable.ic_home_white_24dp,
            R.drawable.ic_person_outline_white_24dp,
            R.drawable.ic_local_shipping_white_24dp
    };

    final String tag = "SearchProductFragment";

    Tabbar_Adapter tabbar_adapter;
    FrameLayout container;

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

    private void showDialogRestartApp(String charge) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đã có sự thay đổi về " + charge + ".");
        builder.setMessage("Khởi động lại và cập nhật?");
        builder.setCancelable(false);
        builder.setPositiveButton("Khởi động ngay!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = getBaseContext().getPackageManager()
                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                if (intent != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Để sau!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void display_TabBar() {

        tabLayout.addTab(tabLayout.newTab().setText(tab_name[0]).setIcon(tab_icon[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tab_name[1]).setIcon(tab_icon[1]));
//        tabLayout.addTab(tabLayout.newTab().setText(tab_name[2]).setIcon(tab_icon[2]));


        mData.child("Users").child(user.getUid()).child("shipper_mode").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (Boolean.parseBoolean(dataSnapshot.getValue().toString())) {
                    tabLayout.addTab(tabLayout.newTab().setText(tab_name[2]).setIcon(tab_icon[2]));
                    tabbar_adapter.notifyDataSetChanged();
                } else {
                    try {
                        tabLayout.removeTabAt(2);
                        tabbar_adapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        //khi tab 3 ko tồn tại
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                    overridePendingTransition(slide_in, slide_out);
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
        overridePendingTransition(slide_in, slide_out);
        FirebaseAuth.getInstance().signOut();
        finish();
    }

    public void ToActivity(Class activity, int pos) {
        Intent i = new Intent(getApplication(), activity);
        i.putExtra("title", arr_category.get(pos).getCategoty_name());
        i.putExtra("product_id", arr_category.get(pos).getId());
        startActivity(i);
//        overridePendingTransition(slide_in, slide_out);
    }

    private void onNavItemClick() {

        gv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == (arr_category.size() - 1)) {
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

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                container.setVisibility(View.VISIBLE);
                progressFragment(new SearchProductFragment(), true, tag);

            }

            @Override
            public void onSearchViewClosed() {
                SearchProductFragment fragment = (SearchProductFragment) getSupportFragmentManager().findFragmentByTag(tag);
                progressFragment(fragment, false, tag);
                container.setVisibility(View.GONE);
            }
        });

//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//
//                if (query != null) {
//                    SearchProductFragment fragment = (SearchProductFragment) getSupportFragmentManager().findFragmentByTag(tag);
//                    ArrayList<Product> arrProduct = fragment.onQuery(query);
//                    fragment.DisplayListView(arrProduct);
//                }
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                return false;
//            }
//        });

//        sview_main = (android.support.v7.widget.SearchView) item.getActionView();
//        sview_main.setIconifiedByDefault(true);
//
//        sview_main.setOnCloseListener(new SearchView.OnCloseListener() {
//            @Override
//            public boolean onClose() {
//
//                SearchProductFragment fragment = (SearchProductFragment) getSupportFragmentManager().findFragmentByTag(tag);
//                progressFragment(fragment, false, tag);
//                container.setVisibility(View.GONE);
//
//                return false;
//            }
//        });


        // Associate searchable configuration with the SearchView
//        SearchManager searchManager =
//                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//        SearchView searchView =
//                (SearchView) menu.findItem(R.id.sview_main).getActionView();
//        searchView.setSearchableInfo(
//                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_cart:
                startActivity(new Intent(getApplication(), CartActivity.class));
                overridePendingTransition(slide_in, slide_out);
                break;
        }

//        switch (id) {
//            case R.id.sview_main:
//                container.setVisibility(View.VISIBLE);
//                progressFragment(new SearchProductFragment(), true, tag);
//
////                getSupportFragmentManager()
////                        .beginTransaction()
////                        .setCustomAnimations(R.anim.slide_in,
////                                R.anim.slide_out)
////                        .replace(R.id.paper, new SearchProductFragment())
////                        .commit();
//
//                break;
//        }

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

    private void progressFragment(Fragment fragment, boolean isAdd, String tag) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .setCustomAnimations(R.anim.slide_in,
//                        R.anim.slide_out)
//                .replace(R.id.container, fragment)
//                .addToBackStack("ShoppingFragment")
//                .commit();
        if (isAdd) {
            //add fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.container, fragment, tag)
                    .commit();
        } else {
            //remove fragment
            getSupportFragmentManager()
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
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
        container = (FrameLayout) findViewById(R.id.container);

        nv_home = (NavigationView) findViewById(R.id.nav_view);
//        lv_home = (ListView) findViewById(R.id.lv_home);
        gv_category = (GridView) findViewById(R.id.gv_category);
        drawerLayout = (DrawerLayout) findViewById(R.id.DrawerLayout);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_username = (TextView) findViewById(R.id.tv_username);
        user_icon = (ImageView) findViewById(R.id.user_icon);
        user_wall = (ImageView) findViewById(R.id.user_wall);
        searchView = (MaterialSearchView) findViewById(R.id.sview_main);

        arr_category = new ArrayList<>();

        if (arr_cart == null) {
            arr_cart = new ArrayList<>();
        }

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
