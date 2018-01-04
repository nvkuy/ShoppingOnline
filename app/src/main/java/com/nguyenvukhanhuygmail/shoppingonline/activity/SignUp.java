package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckEmailType;

public class SignUp extends AppCompatActivity {

    EditText edt_username, edt_email, edt_pass, edt_repass;
    Button btn_finish;
    ImageView back;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        start();

        if (CheckConnection.haveNetworkConnection(getApplication())) {
            onClick();
        } else {
            CheckConnection.notification(getApplicationContext(), "Vui lòng kiểm tra kết nối!");
            finish();
        }

    }

    private void signup(final String email, final String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(getApplication(), "Đăng kí thành công!",
                                    Toast.LENGTH_SHORT).show();

                            mAuth.signInWithEmailAndPassword(email, password);
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                if (user.isEmailVerified()) {

                                    //khi đã xác minh email
                                    startActivity(new Intent(getApplication(), MainActivity.class));

                                } else {

                                    //khi chưa xác minh email
                                    startActivity(new Intent(getApplication(), VerificationEmail.class).putExtra("user_name", edt_username.getText().toString()));
                                }
                            }

                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplication(), "Đăng kí thất bại!\n" + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    private void onClick() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = edt_username.getText().toString();
                String email = edt_email.getText().toString();
                String pass = edt_pass.getText().toString();
                String repass = edt_repass.getText().toString();

                if (username.isEmpty() || email.isEmpty() || pass.isEmpty() || repass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_LONG).show();
                } else if (!pass.equals(repass)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng kiểm tra lại việc xác nhận mật khẩu!", Toast.LENGTH_LONG).show();
                } else if (!CheckEmailType.isEmail(email)) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập đúng định dạng email!", Toast.LENGTH_LONG).show();
                } else if (pass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu phải dài tối thiểu 6 kí tự!", Toast.LENGTH_LONG).show();
                } else {
                    signup(email, pass);
                }

            }
        });

    }

    private void start() {

        mAuth = FirebaseAuth.getInstance();

        edt_email = (EditText) findViewById(R.id.edit_useremail);
        edt_username = (EditText) findViewById(R.id.edit_username);
        edt_pass = (EditText) findViewById(R.id.edit_pass);
        edt_repass = (EditText) findViewById(R.id.edit_repass);
        back = (ImageView) findViewById(R.id.back);
        btn_finish = (Button) findViewById(R.id.btn_finish);

    }

}
