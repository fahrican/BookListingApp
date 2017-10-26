package com.onecosys.fahri_bla;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

/**
 * Created by User on 26.10.2017.
 */

public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils () {

    }

    /*
    * @param the website address aka the website URL as String
    * if the website address is initialized, make a URL object of it and return it
    * */
    public static URL createURL (String searchItem1) {

        String baseUrl = "https://www.googleapis.com/books/v1/volumes?q=";
        String completeURL = baseUrl + searchItem1.replace(" ", "20%");
        URL url = null;
        try {
            url = new URL(completeURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error Creating URl", e);
        }
        Log.e(LOG_TAG, "Compelelte URL = " + completeURL);
        return url;
    }

    /*
    * @param is a InputStream variable (so it only contains bytes)
     * check if the InputStream variable is not empty and make a InputStreamReader variable of it.
     * InputStreamReader contains characters than make a BufferedReader of it
     * the BufferedReader allows to read the complete file in one stream
    * */
    public static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();

            while (line != null) {

                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /*
    * @param website address as URL variable
     * than create a HttpURLConnection and InputStream Data object
     * the HttpURLConnection object is necessary to open the connection, how to get the data (GET-Request)
     * and to establish the connection
     * for reading the data you need the InputStream object and for saving you need a String object
     * @return the HTTP Request as a JSON Response String
    * */
    public static String makeHttpRequest(URL websiteURL) throws IOException {

        String jsonResponse = "";

        if (websiteURL == null) {
            return jsonResponse;
        }

        HttpURLConnection httpUrlConnection = null;
        InputStream inputStream = null;
        try {
            httpUrlConnection = (HttpURLConnection) websiteURL.openConnection();
            httpUrlConnection.setReadTimeout(10000 );// milliseconds
            httpUrlConnection.setConnectTimeout(15000);// milliseconds
            httpUrlConnection.setRequestMethod("GET");
            httpUrlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (httpUrlConnection.getResponseCode() == 200) {

                inputStream = httpUrlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error response code: " + httpUrlConnection.getResponseCode());
            }
        }
        catch (IOException ioe) {
            assert httpUrlConnection != null;
            if (httpUrlConnection.getResponseCode() != 200) {
                Log.e(LOG_TAG, "In MakeHttpRequest Method", ioe);
            }
        }
        finally {

            if (httpUrlConnection != null) {

                httpUrlConnection.disconnect();
            }

            if (inputStream != null) {

                inputStream.close();
            }

        }

        return jsonResponse;
    }

    public static ArrayList<Book> extractBookFromJson (String bookJson) {

        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }

        ArrayList<Book> books = new ArrayList<Book>();

        try {

            JSONObject jsonObject = new JSONObject(bookJson);
            JSONArray items = jsonObject.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {

                // Temporary variables for storing/augmenting data to push to a book object
                String author = "Author: ";
                String category = "";
                String publisher = "Publisher: ";
                double rating;

                JSONObject firstObject = items.getJSONObject(i);
                JSONObject volumeInfo = firstObject.getJSONObject("volumeInfo");

                JSONObject bookPictures = volumeInfo.getJSONObject("imageLinks");
                String picture = bookPictures.getString("thumbnail");


                String title = volumeInfo.getString("title");
                publisher += volumeInfo.getString("publisher");
                if (volumeInfo.isNull("averageRating")) {
                    // Default unrated value? Hmm...
                    rating = 5;
                } else {
                    rating = volumeInfo.getDouble("averageRating");
                }

                JSONArray authors = volumeInfo.getJSONArray("authors");
                if (authors.length() > 0) {
                    for (int j = 0; j < authors.length(); j++) {
                        author += authors.optString(j) + " ";
                    }
                }

                /*
                 * Loop for the category(ies) array
                 */
                JSONArray categories = volumeInfo.getJSONArray("categories");
                if (categories.length() > 0) {
                    for (int j = 0; j < categories.length(); j++) {
                        category += categories.optString(j) + " ";
                    }
                }

                Log.v(LOG_TAG, title + " " + author + " " + publisher + " " + rating + " " +
                        category + " " + picture);
                books.add(new Book(picture, title, publisher, author, rating, category));
            }
        }catch (JSONException jsone) {
            Log.e(LOG_TAG, "JSONException: ");
            jsone.printStackTrace();
        }

        return books;
    }

    /*
     * Query the dataset and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBookData(String requestUrl) {

        URL url = createURL(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        List<Book> books = extractBookFromJson(jsonResponse);
        Log.v("MainActivity", "fetchBookData: " + LOG_TAG);

        return books;
    }

}//end of class QueryUtils
