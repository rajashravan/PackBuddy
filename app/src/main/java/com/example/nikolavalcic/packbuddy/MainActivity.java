package com.example.nikolavalcic.packbuddy;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;




import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.*;




public class MainActivity extends Activity {

    class Item{
        String item;
        Double weight;
        Double price;
        String currency;
        public Item( String item, Double weight, Double price,String currency) {
            this.item=item;
            this.weight=weight;
            this.price=price;
            this.currency=currency;
        }
    }
    ArrayList<Item> items = new ArrayList<Item>();

        //list.add("Item2");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner dropdown3 = findViewById(R.id.spinner3);
        String[] items = new String[]{"USD", "CAD", "EUR", "JPY", "GBP", "INR"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown3.setAdapter(adapter3);

        Spinner dropdown2 = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown2.setAdapter(adapter2);

        Spinner dropdown1 = findViewById(R.id.spinner1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown1.setAdapter(adapter1);

        Spinner dropdownDest = findViewById(R.id.destCur);
        ArrayAdapter<String> adapterDest = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdownDest.setAdapter(adapterDest);
        //dropdown1.select
        Log.i("Selected", dropdown1.getSelectedItem().toString());
    }

    public void updateTable(){

    }

    public void printItems(){
        for (int i=0; i<items.size(); i++){
            Log.i("list item","Name is "+items.get(i).item+" Weight is "+ items.get(i).weight+" Price is "+ items.get(i).price+ "Curr is "+ items.get(i).currency);
        }
    }

    public void clearItems(){
        for (int i=(items.size()-1); i>=0; i--){
            items.remove(i);
        }
    }


    //get items as written in the table
    public void getItems(){
        clearItems();
        LinearLayout root=(LinearLayout) findViewById(R.id.itemsPanel);

        for (int i=1; i<root.getChildCount()-1; i++) {
            LinearLayout row = (LinearLayout) root.getChildAt(i);
            //LinearLayout row= root.getChildCount();
            //Log.i("childs",""+l.getChildCount());
            //Log.i("before", "before");
            //Log.i("elem",((EditText)row.getChildAt(1)).getText()));
            if ((((EditText) row.getChildAt(0)).getText().toString().equals("")) ||
                    (((EditText) row.getChildAt(1)).getText().toString().equals("")) ||
                    (((EditText) row.getChildAt(2)).getText().toString().equals(""))) {
                continue;
            }

            String item = ((EditText) row.getChildAt(0)).getText().toString();
            Double weight = Double.parseDouble(((EditText) row.getChildAt(1)).getText().toString());
            Double price = Double.parseDouble(((EditText) row.getChildAt(2)).getText().toString());
            String currency = ((Spinner) row.getChildAt(3)).getSelectedItem().toString();
            Item rowItem = new Item(item, weight, price, currency);
            items.add(rowItem);
        }
    }

    //put items found in the array to the table
    public void pushItems(int currIndex){
        LinearLayout root=(LinearLayout) findViewById(R.id.itemsPanel);
        for (int i=0; i<items.size(); i++) {
            LinearLayout row = (LinearLayout) root.getChildAt(i+1);

            ((EditText) row.getChildAt(0)).setText(items.get(i).item, TextView.BufferType.EDITABLE);
            ((EditText) row.getChildAt(1)).setText(""+items.get(i).weight, TextView.BufferType.EDITABLE);
            ((EditText) row.getChildAt(2)).setText(""+items.get(i).price, TextView.BufferType.EDITABLE);
            ((Spinner) row.getChildAt(3)).setSelection(currIndex);
        }

    }

    public void convert(View view){
        getItems();
        for (int i=0; i<items.size(); i++){
            Item newItem= new Item(items.get(i).item, items.get(i).weight, items.get(i).price, items.get(i).currency);

            newItem.price= convert(newItem.price, items.get(i).currency, (String)((Spinner)findViewById(R.id.destCur)).getSelectedItem().toString());
            newItem.currency=((Spinner)findViewById(R.id.destCur)).getSelectedItem().toString();
            items.set(i,newItem);
        }
        pushItems(((Spinner)findViewById(R.id.destCur)).getSelectedItemPosition());

    }


    //Convert Function
    public double convert(double amount, String fromISO, String toISO) {
        //example: amount is $10.30, fromISO is "USD" and toISO is "CAD"

        String url = "https://xecdapi.xe.com/v1/convert_from?from=" + fromISO + "&to=" + toISO;
        Log.i("XE-API-CALL", url);

        OkHttpHandler okHttpHandler = new OkHttpHandler();
        okHttpHandler.execute(url);

        try {
            String s = okHttpHandler.get();
            try {
                JSONObject obj = new JSONObject(s);
                JSONArray arr = obj.getJSONArray("to");
                Double d = arr.getJSONObject(0).getDouble("mid");
                return amount*d;
                //Log.e("string from task", d.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    //HTTP OkHttpHandler
    public class OkHttpHandler extends AsyncTask<String, Integer, String> {

        OkHttpClient client = new OkHttpClient();

        @Override
        protected String doInBackground(String...params) {

            Log.e("PARAM 0", params[0]);

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]); //params is all the arguments i think
            builder.addHeader("Authorization", Credentials.basic("universityofwaterloo738297875", "ri5ump0mb771ukp2luajv50r43"));
            Request request = builder.build();

            try {
                Log.i("DEBUG", "TRY ENTERED");
                Response response = client.newCall(request).execute();
                String s = response.body().string();
                Log.i("side", s);
                return s;
            } catch (Exception e) {
                Log.i("DEBUG", "EXCEPTION CAUGHT");
                e.printStackTrace();
                return null;
            }
            //return null;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            Log.i("DEBUG", "executed post");
            Log.e("DEBUG", " " + s);
            Log.i("DEBUG", s);
            //txtString.setText(s);
            //txtString.setText("sdf");
            //return s;
        }
    }

    public void KnapSackAlgo(View view){

        LinearLayout root=(LinearLayout) findViewById(R.id.itemsPanel);

        for (int i=0; i<items.size();i++){
            LinearLayout row = (LinearLayout) root.getChildAt(i+1);

            row.setBackgroundColor(0);

        }



        int capacity= (int) Double.parseDouble(((EditText)findViewById(R.id.weightLim)).getText().toString());

        Knapsack pack = new Knapsack();

        int[] weights= new int [items.size()+1];
        double[] values= new double [items.size()+1];

        //dummy values
        weights[0]=-1;
        values[0]=-1;
        for (int i=0; i<items.size(); i++){
            weights[i+1]=(int) Double.parseDouble(items.get(i).weight.toString());
            values[i+1]=Double.valueOf((items.get(i).price.toString()));
        }

        int num_elements = items.size();

        int[] selected = pack.solve(weights, values, capacity, num_elements);

        //LinearLayout root=(LinearLayout) findViewById(R.id.itemsPanel);

        for (int i=1; i<selected.length;i++){
            LinearLayout row = (LinearLayout) root.getChildAt(i);
            if (selected[i]==1) {
                row.setBackgroundColor(Color.parseColor("#a6ebae"));
            }
        }

    }



    public class Knapsack {
        public int[] solve(int[] wt, double[] val, int W, int N) {
            int NEGATIVE_INFINITY = Integer.MIN_VALUE;
            double[][] m = new double[N + 1][W + 1];
            int[][] sol = new int[N + 1][W + 1];

            for (int i = 1; i <= N; i++) {
                for (int j = 0; j <= W; j++) {
                    double m1 = m[i - 1][j];
                    double m2 = Double.NEGATIVE_INFINITY;
                    if (j >= wt[i])
                        m2 = m[i - 1][j - wt[i]] + val[i];
                    /** select max of m1, m2 **/
                    m[i][j] = Math.max(m1, m2);
                    sol[i][j] = m2 > m1 ? 1 : 0;
                }
            }
            /** make list of what all items to finally select **/
            int[] selected = new int[N + 1];
            for (int n = N, w = W; n > 0; n--) {
                if (sol[n][w] != 0) {
                    selected[n] = 1;
                    w = w - wt[n];
                } else
                    selected[n] = 0;
            }
            return selected;
//            /** Print finally selected items **/
//            System.out.println("\nItems selected : ");
//            for (int i = 1; i < N + 1; i++)
//                if (selected[i] == 1)
//                    System.out.print(i + " ");
//            System.out.println();
//        }
        }
    }


}
