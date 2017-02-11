package com.example.a46521.databinding;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.a46521.databinding.databinding.ActivityProductPageBinding;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


public class ProductPageActivity extends AppCompatActivity {

    Button buyButton;
    RelativeLayout relativeLayout;
    ImageView productView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        buyButton = new Button(getApplicationContext());
        buyButton.setText("buy");
        relativeLayout.addView(buyButton);



        ActivityProductPageBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_product_page);

        // when user taps the a specific product, the detail description of that product will be displayed
        Intent i = getIntent();
        Intent curr = getIntent();
        String currentStirng = i.getStringExtra("productName");
        String productlink = i.getStringExtra("productURL");


        productView = (ImageView) findViewById(R.id.productView);
        Bitmap myImage;
        ImageDownloader task = new ImageDownloader();

        // we find some result if currentPosition is not -1

            for(Product currProduct : MainActivity.productList){
                if(currProduct.getProductName().equals(currentStirng)&& currProduct.getProductURL().equals(productlink)){
                    binding.setProduct(currProduct);
                    try{
                        myImage = task.execute(currProduct.getPictureURL()).get();
                        productView.setImageBitmap(myImage);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }
        }

    public void display(View view){

        ImageView product_added = (ImageView) findViewById(R.id.product_added);
        product_added.animate().alpha(1f).setDuration(100);
    }

    // create ImageDownloader so that we can download the images from the url
    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... urls) {

            try {

                URL url = new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.connect();

                InputStream inputStream = connection.getInputStream();

                Bitmap myBitmap = BitmapFactory.decodeStream(inputStream);

                return myBitmap;


            } catch (MalformedURLException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return null;

        }


    }


    }


