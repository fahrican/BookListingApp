package com.onecosys.fahri_bla;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();

    public String userBookSearch;

    private EditText userInput;

    private ArrayList<Book> tempBookArrayList = null;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!CheckNetwork.isInternetAvailable(MainActivity.this)) {

            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_LONG).show();
        }
        else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            progressBar = (ProgressBar) findViewById(R.id.loading_spinner);
            progressBar.setVisibility(View.GONE);


            ListView listView = (ListView) findViewById(R.id.list);

            userInput = (EditText) findViewById(R.id.user_input);
            Button searchButton = (Button) findViewById(R.id.search_button);

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //storing the text in a string called userBookSearch/ .replaceAll,
                    //added to be able to search for multiple words
                    userBookSearch = userInput.getText().toString().replaceAll(" ", "+");

                    //Logging the search term the user entered
                    Log.v(LOG_TAG, userBookSearch);
                    //if user doesn't enter a search term a toast will show,
                    //and then it will be logged
                    if (userBookSearch.trim().length() <= 0 || userBookSearch.length() <= 0) {
                        Toast.makeText(getApplicationContext(), "No Search Entered", Toast.LENGTH_LONG).show();
                        Log.e(LOG_TAG, "Error Response code: No Search Given");
                        //if a search term is entered continue with task and log search term
                    } else {
                        Log.v(LOG_TAG, userBookSearch);
                        BookAsyncTask task = new BookAsyncTask();
                        task.execute();
                    }
                }
            });

            if (tempBookArrayList != null) {
                BookAdapter adapter = new BookAdapter(this, tempBookArrayList);
                listView.setAdapter(adapter);
            }
        }
    }//end of onCreate

        private void updateUi(ArrayList<Book> books) {

            tempBookArrayList = books;

            if (books != null && !books.isEmpty()) {
                ListView bookListView = (ListView) findViewById(R.id.list);

                BookAdapter adapter = new BookAdapter(this, books);

                bookListView.setAdapter(adapter);
            } else {
                Toast.makeText(getApplicationContext(), "No book found!", Toast.LENGTH_LONG).show();
                Log.e(LOG_TAG, "Still suffering from random void errors and no results with a correct string");
            }
        }

    private class BookAsyncTask extends AsyncTask<URL, Void, ArrayList<Book>> {

        @Override
        protected ArrayList<Book> doInBackground(URL... urls) {

            URL url = QueryUtils.createURL(userBookSearch.trim());
            Log.e(LOG_TAG, "in DoInBackground OverdideMethod..." + url);
            String jsonResponce = "";
            try {
                jsonResponce = QueryUtils.makeHttpRequest(url);
            } catch (IOException e) {
                Log.e(LOG_TAG, "" + e);
            }

            ArrayList<Book> books = QueryUtils.extractBookFromJson(jsonResponce);

            userBookSearch = "";
            return books;
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {

            // super.onPostExecute(books);
            if (books == null) {
                return;
            }
            updateUi(books);
        }

    }
}
