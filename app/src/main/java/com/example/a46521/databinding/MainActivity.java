package com.example.a46521.databinding;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;


//import com.example.a46521.databinding.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.a46521.databinding.R.id.listView;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {


    static public ArrayList<Product> productList;
    static public ArrayList<String> productNameList;
    static public ArrayList<String> brandName;
    static public ArrayList<String> productURL;
    static public ArrayList<String> originalPrice;
    static public ArrayList<String> productID;
    static public ArrayList<String> productName;
    static public ArrayList<String> pictureURL;



    public ArrayAdapter<String> listAdapter;
    public LinearLayout linearLayout;
    private android.widget.SearchView searchView;
    private MenuItem searchItem;
    private SearchManager searchManager;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        // productNameList will mainly display product name on the app
        productNameList = new ArrayList<String>();
        // productList will display the actual product information
        productList = new ArrayList<Product>();

        brandName = new ArrayList<String>();

        productURL = new ArrayList<String>();

        productName = new ArrayList<String>();

        originalPrice = new ArrayList<String>();

        productID = new ArrayList<String>();

        pictureURL = new ArrayList<String>();

        listAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, productName);
        // find the listView
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(listAdapter);
        loadData();

        searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);

        // so that when I tapped a specific product, I can go to see the details of that product
       listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                  Intent i = new Intent(getApplicationContext(),ProductPageActivity.class);
                  i.putExtra("productName",listAdapter.getItem(position));
                  i.putExtra("productURL", productURL.get(position));

                  i.putExtra("productURL", productURL.get(position));


                  startActivity(i);

           }
       });

    }

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (android.widget.SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.requestFocus();

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void loadData(){


        try {
            DownloadTask myTask = new DownloadTask();
            String myString = "";
            myTask.execute("https://api.zappos.com/Search?term=&key=b743e26728e16b81da139182bb2094357c31d331");
            Log.i("result", myString);
        }
        catch(Exception e){
            Log.i("error","error");
        }

    }

    public class DownloadTask extends AsyncTask <String, Void, String>{


        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try{
                url = new URL (urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();
                while (data!=-1){
                    char currentChar = (char)data;
                    result = result + currentChar;
                    data = reader.read();

                }
                return result;
            }
            catch(Exception e){

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{

                String message = "";

                JSONObject jsonObject = new JSONObject(result);

                String productInfo = jsonObject.getString("results");


                JSONArray arr = new JSONArray(productInfo);

                for(int i =0; i<arr.length();i++){
                    JSONObject currentObj = arr.getJSONObject(i);
                    String brand = "";
                    String pictureweb = "";
                    String productname = "";
                    String oriPrice = "";
                    String ID = "";
                    String url = "";
                    String picturelink = "";
                    brand = currentObj.getString("brandName");
                    pictureweb = currentObj.getString("thumbnailImageUrl");
                    productname = currentObj.getString("productName");
                    oriPrice = currentObj.getString("originalPrice");
                    ID = currentObj.getString("productId");
                    url = currentObj.getString("productUrl");
                    picturelink = currentObj.getString("thumbnailImageUrl");



                    Product a = new Product(oriPrice, brand, productname,url, picturelink);

                    if(pictureweb!="") {
                        productURL.add(url);
                        productName.add(productname);
                        originalPrice.add(oriPrice);
                        brandName.add(brand);
                        productID.add(ID);
                        pictureURL.add(picturelink);
                        productList.add(a);

                        listView.setAdapter(listAdapter);
                        listAdapter.notifyDataSetChanged();

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onQueryTextSubmit(String query) {
       listAdapter.getFilter().filter(query);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),ProductPageActivity.class);

                i.putExtra("productName",listAdapter.getItem(position));
                Log.i("position", String.valueOf(position));

                i.putExtra("productName",listAdapter.getItem(position));
                i.putExtra("productURL", productURL.get(position));
                Log.i("productURL", productURL.get(position));
                startActivity(i);

            }
        });
        return false;

    }

    @Override
    public boolean onQueryTextChange(String newText) {
        listAdapter.getFilter().filter(newText);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(),ProductPageActivity.class);



                i.putExtra("productName",listAdapter.getItem(position));
                i.putExtra("productURL", productURL.get(position));



                startActivity(i);

            }
        });
        return false;
    }

    @Override
    public boolean onClose() {
        listAdapter.getFilter().filter("");
        return false;
    }
}


