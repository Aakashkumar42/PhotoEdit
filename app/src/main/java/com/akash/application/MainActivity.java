package com.akash.application;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.theartofdev.edmodo.cropper.CropImage.activity;

public class MainActivity extends AppCompatActivity  {

    int TAKE_PHOTO=0;
    public static int count=0;
    FloatingActionButton fab_gallery,fab_take_photo,fab_save;
    ActivityResultLauncher<String> mContract;
    ImageView mImageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_gallery = findViewById(R.id.gallery_photo);
        fab_take_photo = findViewById(R.id.take_selfie);
        fab_save = findViewById(R.id.fab_save);
        mImageView = findViewById(R.id.imagepreview);


        fab_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(MainActivity.this)
                        .cameraOnly()
                        .cropSquare()
                        .start();
            }
        });
        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImagePicker.with(MainActivity.this)
                        .galleryOnly()
                        .cropSquare()
                        .start();
            }
        });
 fab_save.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View view) {
         savefile();
     }
 });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      Uri uri=data.getData();
      mImageView.setImageURI(uri);



    }

    private void savefile(){

        FileOutputStream fileOutputStream=null;
        File file=getdisc();
        if (!file.exists() && !file.mkdirs())
        {
            Toast.makeText(getApplicationContext(),"sorry can not make dir",Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyymmsshhmmss");
        String date=simpleDateFormat.format(new Date());
        String name="img"+date+".jpeg";
        String file_name=file.getAbsolutePath()+"/"+name; File new_file=new File(file_name);
        try {
            fileOutputStream =new FileOutputStream(new_file);
            Bitmap bitmap=viewToBitmap(mImageView,mImageView.getWidth(),mImageView.getHeight());
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            Toast.makeText(getApplicationContext(),"sucses", Toast.LENGTH_LONG).show();
            fileOutputStream.flush();
            fileOutputStream.close();
        }
        catch
        (FileNotFoundException e) {

        } catch (IOException e) {

        } refreshGallary(file);
    }
    private void refreshGallary(File file) {
        Intent i=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        i.setData(Uri.fromFile(file)); sendBroadcast(i);
    }

    private File getdisc(){
        File file= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(file,"Image");
    }

private static Bitmap viewToBitmap(View view, int widh, int hight)
        {
        Bitmap bitmap=Bitmap.createBitmap(widh,hight, Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap); view.draw(canvas);
        return bitmap;
        }
    }





