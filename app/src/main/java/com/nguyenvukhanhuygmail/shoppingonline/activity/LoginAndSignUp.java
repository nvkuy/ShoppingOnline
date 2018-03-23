package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CustomEditText;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.DrawableClickListener;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginAndSignUp extends AppCompatActivity {

    TextView tvForgotPass, tv_status;
    ImageView img_logo;
    CheckBox cbox_remember;
    EditText edt_username, edt_password, edt_email;
    Button btn_login, btn_signup;
    CircularProgressButton btn_continue;
    LinearLayout box, layout;
    Dialog resetPassDialog;

    SharedPreferences sharedPreferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_sign_up);

        if (CheckConnection.haveNetworkConnection(getApplication())) {
            start();
            OnClick();
        } else {
            CheckConnection.notification(getApplication(), "Vui lòng kiểm tra lại kết nối!");
            finish();
        }

    }

    public void progress_user(FirebaseUser user) {

        if (user != null) {
            if (user.isEmailVerified()) {

                //khi đã xác minh email

                String uID = user.getUid();

                DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
                mData.child("Users").child(uID).child("user_name").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        try {
                            dataSnapshot.getValue().toString();
                            //khi đã điền đầy thông tin profile
                            startActivity(new Intent(getApplication(), MainActivity.class));
                        } catch (NullPointerException e) {
                            //khi chưa điền thông tin profile
                            startActivity(new Intent(getApplication(), UserProfile.class));
                        }

                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        finish();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            } else {

                //khi chưa xác minh email
                startActivity(new Intent(getApplication(), VerificationEmail.class).putExtra("user_name", edt_username.getText().toString()));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        progress_user(currentUser);

    }

    private void OnClick() {

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(), SignUp.class));
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignIn(edt_username.getText().toString(), edt_password.getText().toString());
            }
        });

        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogResetPass();
            }
        });

    }

    private void showDialogResetPass() {

        resetPassDialog = new Dialog(this);
        resetPassDialog.setContentView(R.layout.reset_pass_dialog);

        btn_continue = (CircularProgressButton) resetPassDialog.findViewById(R.id.btn_continue);
        edt_email = (EditText) resetPassDialog.findViewById(R.id.edt_email);
        tv_status = (TextView) resetPassDialog.findViewById(R.id.status);
        CustomEditText edt_title = (CustomEditText) resetPassDialog.findViewById(R.id.edt_title);

        //display title
        edt_title.setText(R.string.resetPass1);

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_continue.startAnimation();
                String email = edt_email.getText().toString();
                if (email.isEmpty()) {

                    tv_status.setVisibility(View.VISIBLE);
                    tv_status.setText(R.string.resetPass2);
                    btn_continue.doneLoadingAnimation(R.color.white, BitmapFactory.decodeResource(getResources(), R.drawable.ic_close_white_48dp));
                    btn_continue.revertAnimation();

                } else {

                    resetPass(email);

                }

            }
        });

        edt_title.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        resetPassDialog.dismiss();
                        break;
                }
            }
        });

        resetPassDialog.show();

    }

    private void resetPass(final String email) {

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            tv_status.setVisibility(View.VISIBLE);
                            tv_status.setText(getString(R.string.resetPass3) + email);
                            edt_email.setVisibility(View.GONE);
                            btn_continue.doneLoadingAnimation(R.color.white, BitmapFactory.decodeResource(getResources(), R.drawable.ic_done_white_48dp));
                        } else {

                            tv_status.setText(String.valueOf(task.getException()));
                            btn_continue.doneLoadingAnimation(R.color.white, BitmapFactory.decodeResource(getResources(), R.drawable.ic_close_white_48dp));

                        }

//                        btn_continue.revertAnimation();
                        btn_continue.setVisibility(View.GONE);

                    }
                });

    }

    private void SignIn(final String email, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Đang đăng nhập..");
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            progress_user(user);

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

                            progressDialog.dismiss();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplication(), "Đăng nhập thất bại!\n" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }

                    }
                });
    }

    private void start() {

        mAuth = FirebaseAuth.getInstance();

        tvForgotPass = (TextView) findViewById(R.id.tv_ForgotPass);
        img_logo = (ImageView) findViewById(R.id.logo_app);
        box = (LinearLayout) findViewById(R.id.box);
        box.setVisibility(View.GONE);
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
                }, 1500);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

}
