package com.diretta.livescore;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.diretta.livescore.common.BaseActivity;
import com.diretta.livescore.common.Common;
import com.diretta.livescore.net.NetworkManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by suc on 4/25/2018.
 */

public class SettingActivity extends Activity {
    LinearLayout lnChangePassword;
    Button btnGoPremium;
    ImageView imgBack;
    RelativeLayout mainLayout;
    LinearLayout lnSignOut, lnSaveChanges, lnEditBio;
    ImageView imgProfile;
    EditText edtFullName, edtEmail, edtPhone, edtBio;
    LinearLayout lnFaceBook, lnContact;
    ProgressDialog mProgDlg;
    private static final int REQ_CODE_PICK_IMAGE = 3;
    private Uri mImageCaptureUri = null;
    private String strFileName = "";
    private String mSave_path = "";
    android.content.SharedPreferences.Editor ed;
    SharedPreferences sp;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sp = getSharedPreferences("userinfo", MODE_PRIVATE);
        ed = sp.edit();
        mProgDlg = new ProgressDialog(SettingActivity.this);
        mProgDlg.setMessage("Wait a little!");
        mProgDlg.setCancelable(false);
        lnSaveChanges = (LinearLayout)findViewById(R.id.lnSaveChanges);
        lnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtEmail.getText().toString().equals("")){
                    Toast.makeText(SettingActivity.this, "Please input the email", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    if(edtEmail.getText().toString().contains("@")){
                        mProgDlg.show();
                        new Thread(mLoadRunnable_upload).start();
                    }else{
                        Toast.makeText(SettingActivity.this, "Please input the correct email", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            }
        });
        imgProfile = (ImageView)findViewById(R.id.imgProfile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mImageCaptureUri = createSaveCropFile();
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                startActivityForResult(Intent.createChooser(intent, "Your title"), REQ_CODE_PICK_IMAGE);
                */
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, REQ_CODE_PICK_IMAGE);

                //startActivityForResult(intent, REQ_CODE_PICK_IMAGE);
            }
        });
        edtFullName = (EditText)findViewById(R.id.edtFullName);
        edtEmail = (EditText)findViewById(R.id.edtEmail);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtBio = (EditText)findViewById(R.id.edtBio);
        lnEditBio = (LinearLayout)findViewById(R.id.lnEditBio);
        lnEditBio.setVisibility(View.GONE);
        lnFaceBook = (LinearLayout)findViewById(R.id.lnFaceBook);
        lnFaceBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/tipya"));
                startActivity(intent);
            }
        });
        lnFaceBook.setVisibility(View.GONE);
        lnContact = (LinearLayout)findViewById(R.id.lnContact);
        lnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String to = "hello@tipyasportsapp.com";
                String subject = "Feedback";
                String message = "";

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
        lnContact.setVisibility(View.GONE);
        lnSignOut = (LinearLayout)findViewById(R.id.lnSignOut);
        lnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingActivity.this);

                alertDialog.setTitle("Confirm");
                alertDialog.setMessage("Do you want to sign out now?");
                alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog,int which) {
                        if(Common.getInstance().social.equals("1")){
                            LoginManager.getInstance().logOut();
                        }else{

                        }
                        ed.putBoolean("remember", false);
                        ed.commit();
                        Intent intent_home = new Intent(SettingActivity.this, MainActivity.class);
                        intent_home.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent_home);
                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        dialog.cancel();
                    }
                });

                alertDialog.show();
            }
        });

        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mainLayout.requestLayout();
        imgBack = (ImageView)findViewById(R.id.imgBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnGoPremium = (Button)findViewById(R.id.btnGoPremium);
        btnGoPremium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, PremiumSubscriptionActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        btnGoPremium.setVisibility(View.GONE);
        if(Common.getInstance().free == false){
            btnGoPremium.setText("Premium");
        }else{
            btnGoPremium.setText(getResources().getString(R.string.upgrade_to_premium));
        }

        lnChangePassword = (LinearLayout)findViewById(R.id.lnChangePassword);
        lnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        mProgDlg.show();
        loadData();
    }
    private void loadData(){
        mProgDlg.show();
        new Thread(mLoadRunnable).start();
    }
    private Runnable mLoadRunnable = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().profileSetting(Common.getInstance().user_id);
            mLoadHandler.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                try{
                    JSONObject userDic = Common.getInstance().settingProfileJson.getJSONObject("profile");
                    edtFullName.setText(userDic.getString("full_name"));
                    edtEmail.setText(userDic.getString("email"));
                    edtPhone.setText(userDic.getString("phone"));
                    edtBio.setText(userDic.getString("bio"));
                    String strPath = "https://tipyaapp.com/" + userDic.getString("profile_img");
                    ImageLoader.getInstance().displayImage(strPath, imgProfile, Common.getInstance().goodsDisplayImageOptions);

                }catch (JSONException e){

                }
            }else if(msg.what == 0){
                Toast.makeText(SettingActivity.this, "No data", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SettingActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Uri createSaveCropFile() {
        Uri uri;
        strFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "0.jpg";
        File file = new File(Environment.getExternalStorageDirectory() + "/livescore");
        if (!file.exists()) {
            file.mkdirs();
        }
        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/livescore", strFileName));
        return uri;
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    private Bitmap rotate(Bitmap bitmap, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode != REQ_CODE_PICK_IMAGE) {
            return;
        }
        if (requestCode == REQ_CODE_PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath, options);

            imgProfile.setImageBitmap(bitmap);
            strFileName = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "0.jpg";

            mSave_path = Environment.getExternalStorageDirectory() + "/livescore/" + strFileName;

            try {
                FileOutputStream fos = new FileOutputStream(mSave_path);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("AbaTask", "Failed to scale image!");
            }
        }
        mProgDlg.show();
        new Thread(mLoadRunnable_uploadProfile).start();
    }
    private Runnable mLoadRunnable_uploadProfile = new Runnable() {
        @Override
        public void run() {
            String path = "uploads/" + NetworkManager.getManager().upload(mSave_path);
            int ret = NetworkManager.getManager().placeProfileSettingImage(Common.getInstance().user_id, path);
            mLoadHandler_uploadProfile.sendEmptyMessage(ret);
        }
    };
    private Runnable mLoadRunnable_upload = new Runnable() {
        @Override
        public void run() {
            int ret = NetworkManager.getManager().placeProfileSetting(Common.getInstance().user_id, edtFullName.getText().toString(), edtEmail.getText().toString(), edtPhone.getText().toString(), edtBio.getText().toString());
            mLoadHandler_upload.sendEmptyMessage(ret);
        }
    };
    private Handler mLoadHandler_upload = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(SettingActivity.this, "Profile update Successfully", Toast.LENGTH_SHORT).show();
            }else if(msg.what == 0){
                Toast.makeText(SettingActivity.this, "You have to put other email!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SettingActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private Handler mLoadHandler_uploadProfile = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mProgDlg.hide();
            if (msg.what == 1) {
                Toast.makeText(SettingActivity.this, "Profile Image update Successfully", Toast.LENGTH_SHORT).show();
            }else if(msg.what == 0){
                Toast.makeText(SettingActivity.this, "You have to put other email!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SettingActivity.this, "Network Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
