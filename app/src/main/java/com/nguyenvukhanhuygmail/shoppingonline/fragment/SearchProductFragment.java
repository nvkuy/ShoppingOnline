package com.nguyenvukhanhuygmail.shoppingonline.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.activity.AboutProduct;
import com.nguyenvukhanhuygmail.shoppingonline.adapter.MainProductAdapter;
import com.nguyenvukhanhuygmail.shoppingonline.model.Product;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchProductFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchProductFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    int product_id = 0;
    String product_name = "";
    int product_price = 0;
    String product_image = "";
    String product_description = "";
    int orders = 0;
    double rate_point = 0;
    int product_left = 0;
    int category_id = 0;

    final int Slide_in = R.anim.slide_in;
    final int Slide_out = R.anim.slide_out;

    ArrayList<Product> arrAllProduct, arrProductFound;
    MainProductAdapter SearchProductAdapter;

    ListView lvProductFound;
    MaterialSearchView searchView;

    private OnFragmentInteractionListener mListener;

    public SearchProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchProductFragment newInstance(String param1, String param2) {
        SearchProductFragment fragment = new SearchProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_product, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        start();
        getAllProduct();
        DisplayListView(arrAllProduct);
        onQuery();
        onLvItemClick();

    }

    private void onLvItemClick() {
        lvProductFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity().getApplication(), AboutProduct.class);
                intent.putExtra("product", arrProductFound.get(i));
                startActivity(intent);
                getActivity().overridePendingTransition(Slide_in, Slide_out);
            }
        });
    }

    public void onQuery() {

        arrProductFound = new ArrayList<>();

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                SearchProductAdapter.filter(newText, arrProductFound, arrAllProduct);
                DisplayListView(arrProductFound);

                return false;
            }
        });

    }

    public void DisplayListView(ArrayList<Product> arrSearchProduct) {

        SearchProductAdapter = new MainProductAdapter(getActivity().getApplication(), arrSearchProduct);
        SearchProductAdapter.notifyDataSetChanged();
        lvProductFound.setAdapter(SearchProductAdapter);

    }

    private void start() {
        lvProductFound = (ListView) getActivity().findViewById(R.id.lvSearchProduct);
        searchView = (MaterialSearchView) getActivity().findViewById(R.id.sview_main);
        arrAllProduct = new ArrayList<>();
    }

    private void getAllProduct() {

        RequestQueue requesQuese = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsArrRequest = new JsonArrayRequest(Server.all_product_path, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response == null) {
                    CheckConnection.notification(getActivity().getApplication(), "Error!");
                } else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
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

                            arrAllProduct.add(new Product(product_id, product_name, product_price, product_image, product_description, orders, rate_point, product_left, category_id));

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
