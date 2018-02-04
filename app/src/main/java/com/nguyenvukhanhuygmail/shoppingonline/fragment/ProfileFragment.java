package com.nguyenvukhanhuygmail.shoppingonline.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nguyenvukhanhuygmail.shoppingonline.R;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CheckConnection;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.CustomEditText;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.DrawableClickListener;
import com.nguyenvukhanhuygmail.shoppingonline.ultil.PicassoCircleTransformation;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ImageView user_icon, user_wall;
    CustomEditText edt_uName, edt_uPass, edt_uPhoneNum, edt_uLocation, edt_uMoney;
    Button btn_commit;
    FloatingActionButton fab_wall, fabTakePhoto, fabPickPhoto;
    ProgressDialog progressDialog;

    final String[] arr_action = {"Chụp ảnh cho:", "Chọn ảnh cho:"};
    String uName, uPhone, uLocation, uID, uNewPass, uOldPass, uEmail;
    Boolean isShow = false;

    int request_code_image1 = 1;
    int request_code_image2 = 2;
    int request_code_image3 = 3;
    int request_code_image4 = 4;

    int request_code_map = 5;

    FirebaseUser user;
    DatabaseReference mData;
    FirebaseStorage storage;
    StorageReference storageRef;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
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

        if (CheckConnection.haveNetworkConnection(getActivity().getApplication())) {

            start();
            setInfo();
            btnClick();
            drawableEdtClick();

        } else {
            CheckConnection.notification(getActivity().getApplication(), "Vui lòng kiểm tra kết nối internet!");
            getActivity().finish();
        }

    }

    private void setInfo() {

        //display user name
        mData.child("Users").child(uID).child("user_name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    edt_uName.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //display phone number
        mData.child("Users").child(uID).child("phone_number").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    edt_uPhoneNum.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //display address
        mData.child("Users").child(uID).child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    edt_uLocation.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // display money
        mData.child("Users").child(uID).child("money").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    DecimalFormat formater = new DecimalFormat("###,###,###");
                    edt_uMoney.setText("Tiền trong tài khoản: " + formater.format(dataSnapshot.getValue()) + "đ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //display uIcon
        storageRef.child("uIcon" + uID + ".png")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getActivity().getApplication())
                                .load(uri)
                                .placeholder(R.drawable.wait)
                                .error(R.drawable.default_user)
                                .transform(new PicassoCircleTransformation())
                                .into(user_icon);
                    }
                });

        //display uWall
        storageRef.child("uWall" + uID + ".png")
                .getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(getActivity().getApplication())
                                .load(uri)
                                .placeholder(R.drawable.wait)
                                .error(R.drawable.profile_gradient)
                                .into(user_wall);
                    }
                });

    }

    private void takePhoto(int code) {

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, code);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == request_code_image1 && resultCode == getActivity().RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            user_icon.setImageBitmap(bitmap);
            btn_commit.setVisibility(View.VISIBLE);

        } else if (requestCode == request_code_image2 && resultCode == getActivity().RESULT_OK && data != null) {

            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            user_wall.setImageBitmap(bitmap);
            btn_commit.setVisibility(View.VISIBLE);

        } else if (requestCode == request_code_image3 && resultCode == getActivity().RESULT_OK && data != null) {

            Uri selectedImgUri = data.getData();
            user_icon.setImageURI(selectedImgUri);
            btn_commit.setVisibility(View.VISIBLE);

        } else if (requestCode == request_code_image4 && resultCode == getActivity().RESULT_OK && data != null) {

            Uri selectedImgUri = data.getData();
            user_wall.setImageURI(selectedImgUri);
            btn_commit.setVisibility(View.VISIBLE);

        } else if (requestCode == request_code_map && resultCode == getActivity().RESULT_OK && data != null) {

            Place place = PlacePicker.getPlace(data, getActivity());
            edt_uLocation.setText(place.getAddress());
            btn_commit.setVisibility(View.VISIBLE);

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void showMapPicker() {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(getActivity()), request_code_map);
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
                        btn_commit.setVisibility(View.VISIBLE);
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
                        btn_commit.setVisibility(View.VISIBLE);
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
                        btn_commit.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        edt_uMoney.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(DrawablePosition target) {
                switch (target) {
                    case RIGHT:
                        edt_uPass.setEnabled(false);
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

    private void uploadImg(ImageView imageView, StorageReference storageRef) {
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(getActivity().getApplication(), String.valueOf(exception), Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    private void btnClick() {

        btn_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Đang xử lí dữ liệu..");
                progressDialog.show();

                StorageReference uIconRef = storageRef.child("uIcon" + uID + ".png");
                StorageReference uWallRef = storageRef.child("uWall" + uID + ".png");

                uploadImg(user_icon, uIconRef);
                uploadImg(user_wall, uWallRef);

                if (user != null) {

                    uName = edt_uName.getText().toString();
                    uPhone = edt_uPhoneNum.getText().toString().trim();
                    uLocation = edt_uLocation.getText().toString().trim();
                    uNewPass = edt_uPass.getText().toString().trim();

                    if (uName.isEmpty() || uPhone.isEmpty() || uLocation.isEmpty()) {

                        Toast.makeText(getActivity().getApplication(), "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_LONG).show();

                    } else if (uNewPass.length() > 0 && uNewPass.length() < 6) {

                        Toast.makeText(getActivity().getApplication(), "Mật khẩu mới phải dài tối thiểu 6 kí tự!", Toast.LENGTH_LONG).show();

                    } else {

                        btn_commit.setVisibility(View.GONE);
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

        final Dialog IconOrWallDialog = new Dialog(getActivity());
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
                                                Toast.makeText(getActivity().getApplication(), "Cập nhật mật khẩu mới thành công!", Toast.LENGTH_LONG).show();
                                            } else {
                                                Toast.makeText(getActivity().getApplication(), "Cập nhật mật khẩu mới thất bại!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                        } else {
                            Toast.makeText(getActivity().getApplication(), "Cập nhật mật khẩu mới thất bại!", Toast.LENGTH_LONG).show();
                        }

                    }
                });

    }

    private void showDialogUpdatePass(final String uNewPass) {

        final Dialog UserUpdateDialog = new Dialog(getActivity());
        UserUpdateDialog.setContentView(R.layout.update_pass_dialog);

        Button btn_cancel = (Button) UserUpdateDialog.findViewById(R.id.cancel);
        Button btn_next = (Button) UserUpdateDialog.findViewById(R.id.contin);
        final EditText edt_email = (EditText) UserUpdateDialog.findViewById(R.id.email);
        final EditText edt_pass = (EditText) UserUpdateDialog.findViewById(R.id.pass);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserUpdateDialog.dismiss();
                post_user();
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

                }

                post_user();
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
                    Toast.makeText(getActivity().getApplication(), "Cập nhật hồ sơ người dùng thành công!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity().getApplication(), "Cập nhật hồ sơ người dùng thất bại!", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();

            }
        });
    }

    private void start() {

        storage = FirebaseStorage.getInstance();
        // Create a storage reference from our app
        storageRef = storage.getReferenceFromUrl("gs://shopping-online-6c182.appspot.com");

        mData = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            uID = user.getUid();
            uEmail = user.getEmail();
        }

        user_icon = (ImageView) getActivity().findViewById(R.id.user_img);
        user_wall = (ImageView) getActivity().findViewById(R.id.user_wall);

        btn_commit = (Button) getActivity().findViewById(R.id.btn_commit);

        fab_wall = (FloatingActionButton) getActivity().findViewById(R.id.wall_fab);
        fabTakePhoto = (FloatingActionButton) getActivity().findViewById(R.id.fabTakePhoto1);
        fabPickPhoto = (FloatingActionButton) getActivity().findViewById(R.id.fabPickPhoto1);

        edt_uName = (CustomEditText) getActivity().findViewById(R.id.edt_uName);
        edt_uPass = (CustomEditText) getActivity().findViewById(R.id.edt_uPass);
        edt_uMoney = (CustomEditText) getActivity().findViewById(R.id.uMoney);
        edt_uPhoneNum = (CustomEditText) getActivity().findViewById(R.id.uPhoneNum);
        edt_uLocation = (CustomEditText) getActivity().findViewById(R.id.uLocation);
        edt_uLocation.setSingleLine();

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
