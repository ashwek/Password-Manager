package com.nikhil.EnPass.initialsetupfrag;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.nikhil.EnPass.enums.ImagePickerEnum;
import com.nikhil.EnPass.utils.FileUtils;
import com.nikhil.EnPass.utils.UiHelper;
import com.nikhil.EnPass.R;
import com.nikhil.EnPass.listeners.IImagePickerLister;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.graphics.Bitmap.CompressFormat.PNG;


public class UserImgFragment extends Fragment implements IImagePickerLister {
    private static final int RESULT_OK = -1 ;
    FloatingActionButton submit;
    ImageView addimg;
    Button skip;
    private StorageReference storageReference;
    private static final int CAMERA_ACTION_PICK_REQUEST_CODE = 610;
    private static final int PICK_IMAGE_GALLERY_REQUEST_CODE = 609;
    public static final int CAMERA_STORAGE_REQUEST_CODE = 611;
    public static final int ONLY_CAMERA_REQUEST_CODE = 612;
    public static final int ONLY_STORAGE_REQUEST_CODE = 613;
    private String currentPhotoPath = "";
    private UiHelper uiHelper = new UiHelper();


    public UserImgFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_img, container, false);
        addimg = view.findViewById(R.id.userimg);
        submit = view.findViewById(R.id.submit);
        skip = view.findViewById(R.id.skip);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        addimg.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (uiHelper.checkSelfPermissions(getActivity()))
                    uiHelper.showImagePickerDialog(getContext(), this);
        });

        skip.setOnClickListener(v -> nextpage());
        submit.setOnClickListener(v -> nextpage());
        return view;
    }

    private void nextpage() {
        FullNameFragment fragment = new FullNameFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(getContext(), this);
            else if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(getContext(), "ImageCropper needs Storage access in order to store your profile picture.");
                getActivity().finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uiHelper.toast(getContext(), "ImageCropper needs Camera access in order to take profile picture.");
                getActivity().finish();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                uiHelper.toast(getContext(), "ImageCropper needs Camera and Storage access in order to take profile picture.");
                getActivity().finish();
            }
        } else if (requestCode == ONLY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(getContext(), this);
            else {
                uiHelper.toast(getContext(), "ImageCropper needs Camera access in order to take profile picture.");
                getActivity().finish();
            }
        } else if (requestCode == ONLY_STORAGE_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                uiHelper.showImagePickerDialog(getContext(), this);
            else {
                uiHelper.toast(getContext(), "ImageCropper needs Storage access in order to store your profile picture.");
                getActivity().finish();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_ACTION_PICK_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            try {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                Log.e("Bitmap:", "======="+bitmap);
                //create a file to write bitmap data
                String filename = "_selectedImgcamera.jpg";
                File fcam = new File(getContext().getCacheDir(), filename);
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
                File file1cam = new File(getContext().getCacheDir(), fname);
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
        } else if (requestCode == PICK_IMAGE_GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            try {
                Uri sourceUri = data.getData();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getActivity().getContentResolver(), sourceUri);
                //create a file to write bitmap data
                String filename = "_selectedImg.jpg";
                File f = new File(getContext().getCacheDir(), filename);
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
                File file1 = new File(getContext().getCacheDir(), fname);
                file1.createNewFile();
                openCropActivity(Uri.fromFile(f), Uri.fromFile(file1));
            } catch (Exception e) {
                uiHelper.toast(getContext(), "Please select another image");
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
            file = FileUtils.getFile(getContext(), imageUri);
            InputStream inputStream = new FileInputStream(file);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            addimg.setImageBitmap(bitmap);
            upload(imageUri);
        } catch (Exception e) {
            uiHelper.toast(getContext(), "Please select different profile picture.");
        }
    }

    private void upload(Uri imageUri) {
        Log.d("upload fun filepath:", String.valueOf(imageUri));
        if(imageUri != null){
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
            StorageReference reference = storageReference.child("Main Users").child(user.getUid());
            reference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                progressDialog.dismiss();
                Toast.makeText(getContext(),"Image uploaded",Toast.LENGTH_LONG).show();
            }).addOnProgressListener(taskSnapshot -> {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded "+(int)progress+"%");
            });
            skip.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
        }
    }


    private void openCamera() {
        Intent pictureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(pictureIntent, CAMERA_ACTION_PICK_REQUEST_CODE);
    }

    private void openCropActivity(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(true);
        options.setCropFrameColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        UCrop.of(sourceUri, destinationUri)
                .withMaxResultSize(100, 100).withMaxResultSize(1000, 1000)
                .start(getActivity(),UserImgFragment.this);
    }

    @Override
    public void onOptionSelected(ImagePickerEnum imagePickerEnum) {
        if (imagePickerEnum == ImagePickerEnum.FROM_CAMERA)
            openCamera();
        else if (imagePickerEnum == ImagePickerEnum.FROM_GALLERY)
            openImagesDocument();
    }

}
