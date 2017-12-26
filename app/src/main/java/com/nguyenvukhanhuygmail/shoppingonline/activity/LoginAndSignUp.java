package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nguyenvukhanhuygmail.shoppingonline.R;

public class LoginAndSignUp extends AppCompatActivity {

    ImageView img_logo;
    EditText edt_username, edt_password;
    LinearLayout box, layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_sign_up);

        start();

    }

    private void start() {

        img_logo = (ImageView) findViewById(R.id.logo_app);
        box = (LinearLayout) findViewById(R.id.box);
        box.setVisibility(View.INVISIBLE);
        layout = (LinearLayout) findViewById(R.id.layout);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);

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
