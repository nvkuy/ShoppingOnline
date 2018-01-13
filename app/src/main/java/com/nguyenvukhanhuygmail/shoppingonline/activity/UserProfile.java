package com.nguyenvukhanhuygmail.shoppingonline.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CustomEditText;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.DrawableClickListener;

import java.util.HashMap;
import java.util.Map;

public class UserProfile extends AppCompatActivity {

    ImageView user_icon, user_wall, btn_back;
    CustomEditText edt_uName, edt_uPass, edt_uPhoneNum, edt_uLocation;
    Button btn_commit;
    FloatingActionButton fab_wall, fabTakePhoto, fabPickPhoto;
    ProgressDialog progressDialog;

    final String[] arr_action = {"Chụp ảnh cho:", "Chọn ảnh cho:"};
    String uName, uPhone, uLocation, uID, uNewPass, uOldPass, uEmail;
    Boolean isNeed, isShow = false;

    int request_code_image1 = 1;
    int request_code_image2 = 2;
    int request_code_image3 = 3;
    int request_code_image4 = 4;

    int request_code_map = 5;

    FirebaseUser user;
    DatabaseReference mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        if (CheckConnection.haveNetworkConnection(getApplication())) {

            start();
            btnClick();
            drawableEdtClick();

        } else {
            CheckConnection.notification(getApplication(), "Vui lòng kiểm tra kết nối internet!");
            finish();
        }

    }

    private void takePhoto(int code) {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, code);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == request_code_image1 && resultCode == RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            user_icon.setImageBitmap(bitmap);

        } else if (requestCode == request_code_image2 && resultCode == RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            user_wall.setImageBitmap(bitmap);

        } else if (requestCode == request_code_image3 && resultCode == RESULT_OK && data != null) {

            Uri selectedImgUri = data.getData();
            user_icon.setImageURI(selectedImgUri);

        } else if (requestCode == request_code_image4 && resultCode == RESULT_OK && data != null) {

            Uri selectedImgUri = data.getData();
            user_wall.setImageURI(selectedImgUri);

        } else if (requestCode == request_code_map && resultCode == RESULT_OK && data != null) {

            Place place = PlacePicker.getPlace(data, this);
            edt_uLocation.setText(place.getAddress());

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void showMapPicker() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(UserProfile.this), request_code_map);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    private void choosePhoto(int code) {

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.setType("image/*");
        startActivityForResult(i, code);

    }

    private void drawableEdtClick() {

        edt_uName.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        edt_uName.setEnabled(true);
                        edt_uPass.setEnabled(false);
                        edt_uLocation.setEnabled(false);
                        edt_uPhoneNum.setEnabled(false);
                        break;
                }
            }
        });

        edt_uPhoneNum.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        edt_uPhoneNum.setEnabled(true);
                        edt_uPass.setEnabled(false);
                        edt_uLocation.setEnabled(false);
                        edt_uName.setEnabled(false);
                        break;
                }
            }
        });

        edt_uLocation.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        edt_uLocation.setEnabled(true);
                        edt_uPass.setEnabled(false);
                        edt_uName.setEnabled(false);
                        edt_uPhoneNum.setEnabled(false);
                        showMapPicker();
                        break;
                }
            }
        });

        edt_uPass.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        edt_uPass.setEnabled(true);
                        edt_uName.setEnabled(false);
                        edt_uLocation.setEnabled(false);
                        edt_uPhoneNum.setEnabled(false);
                        break;
                }
            }
        });

    }

    private void showFab(FloatingActionButton fabTakePhoto, FloatingActionButton fabPickPhoto) {

        fabTakePhoto.show();
        fabPickPhoto.show();

    }

    private void hideFab(FloatingActionButton fabTakePhoto, FloatingActionButton fabPickPhoto) {

        fabTakePhoto.hide();
        fabPickPhoto.hide();

    }

    private void btnClick() {

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNeed) {
                    //sau khi tạo tài khoản
                    LogoutAndBack();
                } else {
                    finish();
                }

            }
        });

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(UserProfile.this);
                progressDialog.setMessage("Đang xử lí dữ liệu..");
                progressDialog.show();

                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    uID = user.getUid();
                    uEmail = user.getEmail();
                    mData = FirebaseDatabase.getInstance().getReference();


                    uName = edt_uName.getText().toString();
                    uPhone = edt_uPhoneNum.getText().toString();
                    uLocation = edt_uLocation.getText().toString().trim();
                    uNewPass = edt_uPass.getText().toString();

                    if (uName.isEmpty() || uPhone.isEmpty() || uLocation.isEmpty()) {

                        Toast.makeText(getApplication(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_LONG).show();

                    } else if (uNewPass.length() > 0 && uNewPass.length() < 6) {

                        Toast.makeText(getApplication(), "Mật khẩu mới phải dài tối thiểu 6 kí tự!", Toast.LENGTH_LONG).show();

                    } else {

                        if (uNewPass.length() >= 6) {
                            showDialogUpdatePass(uNewPass);
                        } else {
                            post_user();
                        }

                    }
                }

            }
        });

        fab_wall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isShow) {
                    hideFab(fabTakePhoto, fabPickPhoto);
                    isShow = false;
                } else {
                    showFab(fabTakePhoto, fabPickPhoto);
                    isShow = true;
                }
            }
        });

        fabTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPhoto(arr_action[0], true);
            }
        });

        fabPickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogPhoto(arr_action[1], false);
            }
        });

    }

    private void showDialogPhoto(String title, final boolean isTakePhoto) {

        final Dialog IconOrWallDialog = new Dialog(this);
        IconOrWallDialog.setContentView(R.layout.icon_or_wall);

        CustomEditText edt_title = (CustomEditText) IconOrWallDialog.findViewById(R.id.edtIconOrWall);
        final RadioButton radiobtnIcon = (RadioButton) IconOrWallDialog.findViewById(R.id.radiobtn_icon);
        RadioButton radiobtnWall = (RadioButton) IconOrWallDialog.findViewById(R.id.radiobtn_wall);
        Button btn_Ok = (Button) IconOrWallDialog.findViewById(R.id.Ok);

        //display title dialog
        edt_title.setText(title);

        edt_title.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case LEFT:
                        IconOrWallDialog.dismiss();
                        break;
                }
            }
        });

        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (radiobtnIcon.isChecked()) {

                    if (isTakePhoto) {
                        takePhoto(request_code_image1);
                    } else {
                        choosePhoto(request_code_image3);
                    }

                } else {

                    if (isTakePhoto) {
                        takePhoto(request_code_image2);
                    } else {
                        choosePhoto(request_code_image4);
                    }

                }

                IconOrWallDialog.dismiss();
            }
        });

        IconOrWallDialog.show();

    }

    private void update_pass(AuthCredential credential, final String uNewPass, final FirebaseUser user) {

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            user.updatePassword(uNewPass)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplication(), "Cập nhật mật khẩu mới thành công!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getApplication(), "Cập nhật mật khẩu mới thất bại!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(getApplication(), "Cập nhật mật khẩu mới thất bại!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    private void showDialogUpdatePass(final String uNewPass) {

        final Dialog UserUpdateDialog = new Dialog(this);
        UserUpdateDialog.setContentView(R.layout.update_pass_dialog);

        Button btn_cancel = (Button) UserUpdateDialog.findViewById(R.id.cancel);
        Button btn_next = (Button) UserUpdateDialog.findViewById(R.id.contin);
        final EditText edt_email = (EditText) UserUpdateDialog.findViewById(R.id.email);
        final EditText edt_pass = (EditText) UserUpdateDialog.findViewById(R.id.pass);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUpdateDialog.dismiss();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uOldPass = edt_pass.getText().toString();
                uEmail = edt_email.getText().toString();

                if (!uEmail.isEmpty() && !uOldPass.isEmpty()) {

                    AuthCredential credential = EmailAuthProvider
                            .getCredential(uEmail, uOldPass);

                    update_pass(credential, uNewPass, user);
                    post_user();

                }

                UserUpdateDialog.dismiss();

            }
        });

        UserUpdateDialog.show();

    }

    private void post_user() {

        Map profile_post = new HashMap();
        profile_post.put("user_name", uName);
        profile_post.put("phone_number", uPhone);
        profile_post.put("address", uLocation);

        mData.child("Users").child(uID).setValue(profile_post, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                if (databaseError == null) {
                    Toast.makeText(getApplication(), "Cập nhật hồ sơ người dùng thành công!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplication(), MainActivity.class));
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                    finish();
                } else {
                    Toast.makeText(getApplication(), "Cập nhật hồ sơ người dùng thất bại!", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();

            }
        });
    }

    private void LogoutAndBack() {

        startActivity(new Intent(getApplication(), LoginAndSignUp.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        FirebaseAuth.getInstance().signOut();
        finish();

    }

    private void start() {

        user_icon = (ImageView) findViewById(R.id.user_img);
        user_wall = (ImageView) findViewById(R.id.user_wall);

        btn_back = (ImageView) findViewById(R.id.btnBack);
        btn_commit = (Button) findViewById(R.id.btn_commit);

        fab_wall = (FloatingActionButton) findViewById(R.id.wall_fab);
        fabTakePhoto = (FloatingActionButton) findViewById(R.id.fabTakePhoto1);
        fabPickPhoto = (FloatingActionButton) findViewById(R.id.fabPickPhoto1);

        edt_uName = (CustomEditText) findViewById(R.id.edt_uName);
        edt_uPass = (CustomEditText) findViewById(R.id.edt_uPass);
        edt_uPhoneNum = (CustomEditText) findViewById(R.id.uPhoneNum);
        edt_uLocation = (CustomEditText) findViewById(R.id.uLocation);

        isNeed = getIntent().getBooleanExtra("isNeedBack", false);
    }
}
