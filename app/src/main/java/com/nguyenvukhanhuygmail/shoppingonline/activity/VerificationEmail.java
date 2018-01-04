package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;

public class VerificationEmail extends AppCompatActivity {

    TextView txt_email, txt_status, txt_hello, txt_link;
    Button btn_done, btn_SendLink;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_email);

        start();

        if (CheckConnection.haveNetworkConnection(getApplication())) {

            onBtnClick();
            setInfo();

        } else {
            CheckConnection.notification(getApplication(), "Vui lòng kiểm tra kết nối internet!");
        }

    }

    private void setInfo() {
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            txt_email.setText(user.getEmail());

            if (user.isEmailVerified()) {
                txt_status.setText("Trạng thái: Đã xác minh");
            } else {
                txt_status.setText("Trạng thái: Chưa xác minh");
            }

        }

    }

    private void onBtnClick() {

        btn_SendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btn_SendLink.setVisibility(View.GONE);
                sendVerification();

            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onDone();

            }
        });

    }

    private void onDone() {

        try {
            user.reload()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            setInfo();

                        }
                    }).wait(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startActivity(new Intent(getApplication(), UserProfile.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }

    private void sendVerification() {
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            txt_email.setVisibility(View.VISIBLE);
                            txt_status.setVisibility(View.VISIBLE);
                            txt_link.setVisibility(View.VISIBLE);
                            btn_done.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(getApplication(), String.valueOf(task.getException()), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void start() {

        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_status = (TextView) findViewById(R.id.txt_status);
        txt_hello = (TextView) findViewById(R.id.hello);
        txt_link = (TextView) findViewById(R.id.txt_link);

        String user_name = getIntent().getStringExtra("user_name");

        if (user_name != null && !user_name.isEmpty()) {
            txt_hello.setText("Chào " + getIntent().getStringExtra("user_name") + "! Vui lòng xác minh địa chỉ email của bạn!");
        } else {
            txt_hello.setText(R.string.verification_email);
        }

        btn_done = (Button) findViewById(R.id.btn_done);
        btn_SendLink = (Button) findViewById(R.id.btn_send_link);


    }
}
