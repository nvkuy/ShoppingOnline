package com.nguyenvukhanhuygmail.shoppingonline.fragment;

import android.app.ActivityOptions;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.activity.AboutProduct;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.ProductAdapter;
import com.nguyenvukhanhuygmail.shoppingonline.model.Product;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.RecyclerItemClickListener;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.Server;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    final int Slide_in = R.anim.slide_in;
    final int Slide_out = R.anim.slide_out;

    ProgressDialog progressDialog;

    ViewFlipper viewFlipper;
    RecyclerView rv_popular, rv_news, rv_rate;

    String link;
    boolean isFirst = true;
    //String[] adv_links = new String[5];

    int product_id = 0;
    String product_name = "";
    int product_price = 0;
    String product_image = "";
    String product_description = "";
    int orders = 0;
    double rate_point = 0;
    int product_left = 0;
    int category_id = 0;

    ArrayList<Product> arr_NewProduct, arr_PopularProduct, arr_RateProduct;
    ProductAdapter NewProductAdapter, PopularProductAdapter, RateProductAdapter;

    private OnFragmentInteractionListener mListener;

    public ShoppingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingFragment newInstance(String param1, String param2) {
        ShoppingFragment fragment = new ShoppingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            if (isFirst) {
//                isFirst = false;
//            } else {
//                FragmentTransaction ft = getFragmentManager().beginTransaction();
//                ft.detach(ShoppingFragment.this).attach(ShoppingFragment.this).commit();
////                getActivity().recreate();
//            }
//        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        new ProgressFrag().execute();
        onRV_ItemClick();

    }

    private class ProgressFrag extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Đang xử lí dữ liệu..");
            progressDialog.show();
//            Log.d("isShow", String.valueOf(progressDialog.isShowing()));

        }

        @Override
        protected Void doInBackground(Void... voids) {

            showAdvs();

            //show new product
            showProduct(Server.new_product_path, arr_NewProduct);
            //show popular product
            showProduct(Server.popular_product_path, arr_PopularProduct);
            //show rate product
            showProduct(Server.rate_product_path, arr_RateProduct);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            displayUI(arr_NewProduct, arr_PopularProduct, arr_RateProduct);
            progressDialog.dismiss();

//            if (rv_news.getChildCount() > 0 || rv_popular.getChildCount() > 0 || rv_rate.getChildCount() > 0) {
//                progressDialog.dismiss();
//            }

            //            Log.d("isShow", String.valueOf(progressDialog.isShowing()));

        }

    }

    public void displayUI(ArrayList<Product> arr_NewProduct, ArrayList<Product> arr_PopularProduct, ArrayList<Product> arr_RateProduct) {
        /*
                * code = 1: new_product
                * code = 2: rate_product
                * else: popular_product
                * */

        NewProductAdapter = new ProductAdapter(getActivity().getApplicationContext(), arr_NewProduct, 1);
        PopularProductAdapter = new ProductAdapter(getActivity().getApplicationContext(), arr_PopularProduct, -1);
        RateProductAdapter = new ProductAdapter(getActivity().getApplicationContext(), arr_RateProduct, 2);

//        NewProductAdapter.notifyDataSetChanged();
//        PopularProductAdapter.notifyDataSetChanged();
//        RateProductAdapter.notifyDataSetChanged();

        rv_news.setAdapter(NewProductAdapter);
        rv_popular.setAdapter(PopularProductAdapter);
        rv_rate.setAdapter(RateProductAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // new ProgressFrag().execute();

    }

    public void GoToAboutProduct(Context context, Product product, View view) {

        Intent intent = new Intent(context, AboutProduct.class);
        intent.putExtra("product", product);

        Bundle scaleBundle = ActivityOptions.makeScaleUpAnimation(
                view, 0, 0, view.getWidth(), view.getHeight()
        ).toBundle();

        startActivity(intent, scaleBundle);
//        getActivity().overridePendingTransition(Slide_in, Slide_out);
    }

    private void showProduct(final String url, final ArrayList<Product> arrProduct) {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                            rate_point = jsonObject.getDouble("rate_point");
                            product_left = jsonObject.getInt("product_left");
                            category_id = jsonObject.getInt("category_id");

                            arrProduct.add(i, new Product(product_id, product_name, product_price, product_image, product_description, orders, rate_point, product_left, category_id));

//                            if (code == 1) {
//
//                                //get sản phẩm được đánh giá cao và add vào mảng
//                                arr_RateProduct.add(i, new Product(product_id, product_name, product_price, product_image, product_description, orders, rate_point, product_left, category_id));
//                                RateProductAdapter.notifyDataSetChanged();
//                            } else if (code == 2) {
//
//                                //get sản phẩm mới và add vào mảng
//                                arr_NewProduct.add(i, new Product(product_id, product_name, product_price, product_image, product_description, orders, rate_point, product_left, category_id));
//                                NewProductAdapter.notifyDataSetChanged();
//                            } else {
//
//                                //get sản phẩm thông dụng và add vào mảng
//                                arr_PopularProduct.add(i, new Product(product_id, product_name, product_price, product_image, product_description, orders, rate_point, product_left, category_id));
//                                PopularProductAdapter.notifyDataSetChanged();
//                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

//                    Log.d("sizetest", String.valueOf(arrProduct.size()) + "\n" + response.length() + "\n" + url);

                } else {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplication(), String.valueOf(error), Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(jsonArrayRequest);

    }

    private void showAdvs() {
        RequestQueue requesQuese = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsArrRequest = new JsonArrayRequest(Server.adv_path, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response != null) {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            link = jsonObject.getString("link_advs");
                            ImageView imgView = new ImageView(getActivity().getApplicationContext());
                            Picasso.with(getActivity().getApplicationContext()).load(link).into(imgView);
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
                Toast.makeText(getActivity().getApplication(), String.valueOf(error), Toast.LENGTH_LONG).show();
            }
        });

        requesQuese.add(jsArrRequest);

    }

    private void start(View view) {

        rv_popular = (RecyclerView) view.findViewById(R.id.rv_popular);
        rv_news = (RecyclerView) view.findViewById(R.id.rv_news);
        rv_rate = (RecyclerView) view.findViewById(R.id.rv_rate);
        viewFlipper = (ViewFlipper) view.findViewById(R.id.ViewFlipper);


        arr_NewProduct = new ArrayList<>();
        arr_PopularProduct = new ArrayList<>();
        arr_RateProduct = new ArrayList<>();

        rv_news.setHasFixedSize(true);
        rv_popular.setHasFixedSize(true);
        rv_rate.setHasFixedSize(true);

//        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
//        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);

//        rv_news.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        rv_popular.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
//        rv_sale.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));

//        rv_sale.setLayoutManager(manager);
//        rv_popular.setLayoutManager(manager);
//        rv_news.setLayoutManager(manager);

        rv_news.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_popular.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rv_rate.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));

//        rv_news.setNestedScrollingEnabled(false);
//        rv_popular.setNestedScrollingEnabled(false);
//        rv_rate.setNestedScrollingEnabled(false);

        viewFlipper.setFlipInterval(7500);
        viewFlipper.setAutoStart(true);

        Animation anim_in = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), Slide_in);
        Animation anim_out = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), Slide_out);

        viewFlipper.setInAnimation(anim_in);
        viewFlipper.setOutAnimation(anim_out);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_shopping, container, false);

        start(view);

        // Inflate the layout for this fragment
        return view;
    }

    private void onRV_ItemClick() {

        rv_rate.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplication(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product sale_product = arr_RateProduct.get(position);
                GoToAboutProduct(getActivity().getApplication(), sale_product, view);
            }
        }));

        rv_popular.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplication(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product popular_product = arr_PopularProduct.get(position);
                GoToAboutProduct(getActivity().getApplication(), popular_product, view);
            }
        }));

        rv_news.addOnItemTouchListener(new RecyclerItemClickListener(getActivity().getApplication(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Product new_product = arr_NewProduct.get(position);
                GoToAboutProduct(getActivity().getApplication(), new_product, view);
            }
        }));
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
