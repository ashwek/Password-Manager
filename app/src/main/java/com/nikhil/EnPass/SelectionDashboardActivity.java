package com.nikhil.EnPass;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nikhil.ActivationKeyModule.ActivationDashActivity;
import com.nikhil.CreditCardModule.CreditDashActivity;
import com.nikhil.EnPass.enums.ImagePickerEnum;
import com.nikhil.EnPass.utils.FileUtils;
import com.nikhil.EnPass.utils.UiHelper;
import com.nikhil.PasswordStoreModule.PasswordDashActivity;
import com.nikhil.PersonalDetailModule.PersonDashActivity;
import com.nikhil.SecureNotesModule.NotesDashActivity;
import com.nikhil.SettingModules.AboutusActivity;
import com.nikhil.EnPass.R;
import com.nikhil.EnPass.listeners.IImagePickerLister;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.Bitmap.CompressFormat.PNG;


public class SelectionDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IImagePickerLister{
    private ActionBarDrawerToggle mToggle;
    TextView navUsername;
    ImageView userimg;
    FirebaseAuth auth;
    FirebaseUser user;
    private StorageReference storageReference;
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;
    private UiHelper uiHelper = new UiHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_dashboard);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        userimg = headerView.findViewById(R.id.userimg);
        //navigation header image retrieve
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imgreference = storageRef.child("Main Users").child(user.getUid());
        imgreference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.get().load(uri).into(userimg);
        });

        //navigation header image onclick function
        userimg.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(this))
                    uiHelper.showImagePickerDialog(this, this);
        });



      //navigation header name retrieve
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Detail").child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String fname = dataSnapshot.child("fname").getValue().toString();
                String lname = dataSnapshot.child("lname").getValue().toString();
                String name = fname +" "+ lname;
                navUsername.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //navigation bar code
        DrawerLayout mDrawerlayout = findViewById(R.id.nav_bar_selectionscreen);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout,R.string.open,R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        navUsername = headerView.findViewById(R.id.username);
        navigationView.setNavigationItemSelectedListener(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, "ImageCropper needs Storage access in order to store your profile picture.");
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uiHelper.toast(this, "ImageCropper needs Camera access in order to take profile picture.");
                finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(this, "ImageCropper needs Camera and Storage access in order to take profile picture.");
                finish();
            }
        } else if (requestCode == ONLY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, "ImageCropper needs Camera access in order to take profile picture.");
                finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(this, this);
            else {
                uiHelper.toast(this, "ImageCropper needs Storage access in order to store your profile picture.");
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Log.e("Bitmap:", "======="+bitmap);
                //create a file to write bitmap data
                String filename = "_selectedImgcamera.jpg";
                File fcam = new File(getCacheDir(), filename);
                fcam.createNewFile();
                //addimg.setImageBitmap(bitmap);
                //Convert bitmap to byte array
                Bitmap bitmap1cam = bitmap;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(PNG, 0 , bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(fcam);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                String fname = "_Croppedimgcam.jpg";
                File file1cam = new File(getCacheDir(), fname);
                file1cam.createNewFile();
                openCropActivity(Uri.fromFile(fcam), Uri.fromFile(file1cam));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            if (data != null) {
                Uri uri = UCrop.getOutput(data);
                showImage(uri);
            }
        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {

                Uri sourceUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(SelectionDashboardActivity.this.getContentResolver(), sourceUri);
                //create a file to write bitmap data
                String filename = "_selectedImg.jpg";
                File f = new File(getCacheDir(), filename);
                f.createNewFile();

                //Convert bitmap to byte array
                Bitmap bitmap1 = bitmap;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(PNG, 0 , bos);
                byte[] bitmapdata = bos.toByteArray();

                //write the bytes in file
                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                String fname = "_Croppedimg.jpg";
                File file1 = new File(getCacheDir(), fname);
                file1.createNewFile();
                openCropActivity(Uri.fromFile(f), Uri.fromFile(file1));
            } catch (Exception e) {
                uiHelper.toast(this, "Please select another image");
            }
        }
    }

    private void openImagesDocument() {
        Intent pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pictureIntent.setType("image/*");
        pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = new String[]{"image/jpeg", "image/png"};
        pictureIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), PICK_IMAGE_GALLERY_REQUEST_CODE);
    }

    private void showImage(Uri imageUri) {
        try {
            File file;
            file = FileUtils.getFile(this, imageUri);
            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            userimg.setImageBitmap(bitmap);
            upload(imageUri);
        } catch (Exception e) {
            uiHelper.toast(this, "Please select different profile picture.");
        }
    }

    private void upload(Uri imageUri) {
        Log.d("upload fun filepath:", String.valueOf(imageUri));
        if(imageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            StorageReference reference = storageReference.child("Main Users").child(user.getUid());
            reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                Toast.makeText(SelectionDashboardActivity.this,"Image uploaded",Toast.LENGTH_LONG).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            });
        }
    }

    private void openCamera() {
        Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(this, R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(100, 100).withMaxResultSize(1000, 1000)
                .start(this);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {
        if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
            openCamera();
        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
            openImagesDocument();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
         if(id == R.id.credit){
             Intent i = new Intent(this,AppcreditActivity.class);
             startActivity(i);

         }
         if(id == R.id.setting){
             Intent i = new Intent(this, SettingsActivity.class);
             startActivity(i);
         }
         if(id == R.id.aboutus){
                Intent i = new Intent(SelectionDashboardActivity.this, AboutusActivity.class);
                startActivity(i);
         }
         if (id == R.id.shareapp){
             try {
                 Intent shareIntent = new Intent(Intent.ACTION_SEND);
                 shareIntent.setType("text/plain");
                 String shareMessage= "Hey there, check out this awesome app to secure your private data\n"+ "https://drive.google.com/open?id=18McQL7_thLPxwXrBw714VgyEJIs2B4AD";
                 shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                 startActivity(Intent.createChooser(shareIntent, "choose one"));
             } catch(Exception ignored) {
             }
         }
         if(id == R.id.rateus){
             Intent i = new Intent(SelectionDashboardActivity.this,RateUsActivity.class);
             startActivity(i);
         }
         if(id == R.id.logout){
             SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
             SharedPreferences.Editor editor = prefs.edit();
             editor.putBoolean("firstStart", true);
             editor.apply();
             FirebaseAuth.getInstance().signOut();
             Intent intToMain = new Intent(this, UserWelcomeSelectionActivity.class);
             startActivity(intToMain);
             finish();
         }
        return false;
    }

    public void Person(View view) {
        Intent det = new Intent(SelectionDashboardActivity.this, PersonDashActivity.class);
        startActivity(det);
    }

    public void Notes(View view) {
        Intent note = new Intent(SelectionDashboardActivity.this, NotesDashActivity.class);
        startActivity(note);
    }

    public void Credit(View view) {
        Intent cc = new Intent(SelectionDashboardActivity.this, CreditDashActivity.class);
        startActivity(cc);
    }

    public void Password(View view) {
        Intent pass = new Intent(SelectionDashboardActivity.this, PasswordDashActivity.class);
        startActivity(pass);
    }

    public void Activation(View view) {
        Intent act = new Intent(SelectionDashboardActivity.this, ActivationDashActivity.class);
        startActivity(act);
    }
}
