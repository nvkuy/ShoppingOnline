package com.nguyenvukhanhuygmail.shoppingonline.ultil;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenvukhanhuygmail.shoppingonline.model.Product;

/**
 * Created by Uy Nguyen on 6/10/2018.
 */

public class ProductManager {

    private DatabaseReference mDatabase;

    public ProductManager() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void AddOrUpdateProduct(Product product) {
        mDatabase.child("Products").child(String.valueOf(product.getProduct_id())).setValue(product);
    }

    public void DeleteProduct(int product_id) {
        mDatabase.child("Products").child(String.valueOf(product_id)).removeValue();
    }

}
