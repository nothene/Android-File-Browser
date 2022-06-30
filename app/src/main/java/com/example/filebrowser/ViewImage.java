package com.example.filebrowser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewImage extends Activity {
    TextView name;
    ImageView image;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_image);

        Intent intent = getIntent();

        String filename = intent.getStringExtra("filepath");

        name = (TextView) findViewById(R.id.imageName);
        image = (ImageView) findViewById(R.id.imageView);

        name.setText(filename);

        Bitmap bmp = BitmapFactory.decodeFile(filename);

        image.setImageBitmap(bmp);
    }
}
