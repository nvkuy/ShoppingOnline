package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;

public class LoginAndSignUp extends AppCompatActivity {

    ImageView img_logo;
    CheckBox cbox_remember;
    EditText edt_username, edt_password;
    Button btn_login, btn_signup;
    LinearLayout box, layout;

    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_sign_up);

        start();

        if (CheckConnection.haveNetworkConnection(getApplication())) {
            OnClick();
        } else {
            CheckConnection.notification(getApplication(), "Vui lòng kiểm tra lại kết nối!");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            // User is signed in
        } else {
            // No user is signed in
        }

    }

    private void OnClick() {

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), SignUp.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignIn(edt_username.getText().toString(), edt_password.getText().toString());
            }
        });

    }

    private void SignIn(final String email, final String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            startActivity(new Intent(getApplication(), MainActivity.class));
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            finish();

                            Toast.makeText(getApplication(), "Đăng nhập thành công!",
                                    Toast.LENGTH_SHORT).show();

                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if (cbox_remember.isChecked()) {

                                editor.putString("email", email);
                                editor.putString("password", password);
                                editor.putBoolean("isChecked", true);
                                editor.commit();

                            } else {

                                editor.remove("email");
                                editor.remove("password");
                                editor.remove("isChecked");
                                editor.commit();

                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplication(), "Đăng nhập thất bại!\n" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void start() {

        mAuth = FirebaseAuth.getInstance();

        img_logo = (ImageView) findViewById(R.id.logo_app);
        box = (LinearLayout) findViewById(R.id.box);
        box.setVisibility(View.INVISIBLE);
        layout = (LinearLayout) findViewById(R.id.layout);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        cbox_remember = (CheckBox) findViewById(R.id.cbox_remember);

        sharedPreferences = getSharedPreferences("SavedUser", MODE_PRIVATE);
        //gán username + pass cho edt nếu có lưu mk
        edt_username.setText(sharedPreferences.getString("email", null));
        edt_password.setText(sharedPreferences.getString("password", null));
        cbox_remember.setChecked(sharedPreferences.getBoolean("isChecked", false));

        Animation alpha_anim = AnimationUtils.loadAnimation(this, R.anim.welcome_alpha);
        final Animation box_anim = AnimationUtils.loadAnimation(this, R.anim.welcome_box);
        Animation icon_anim = AnimationUtils.loadAnimation(this, R.anim.welcome_icon);

        layout.setAnimation(alpha_anim);
        img_logo.setAnimation(icon_anim);
//        box.setAnimation(box_anim);
        icon_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        box.setVisibility(View.VISIBLE);
                        box.setAnimation(box_anim);
                    }
                }, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
