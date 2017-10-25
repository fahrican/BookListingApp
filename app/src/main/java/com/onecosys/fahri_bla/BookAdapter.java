package com.onecosys.fahri_bla;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by User on 26.10.2017.
 */

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(Activity context, List<Book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = getItem(position);

        //Find the ImageView with view ID
        ImageView bookImage = (ImageView) listItemView.findViewById(R.id.book_image);
        //display the book picture of the current book in the ImageView
        Picasso.with(getContext()).load(currentBook.getBookImage()).into(bookImage);

        TextView bookTitle = (TextView) listItemView.findViewById(R.id.book_title);
        bookTitle.setText(currentBook.getBookTitle());

        TextView publisher = (TextView) listItemView.findViewById(R.id.publisher);
        publisher.setText(currentBook.getPublisher());

        TextView author = (TextView) listItemView.findViewById(R.id.author);
        author.setText(currentBook.getAuthor());

        RatingBar bookRating = (RatingBar) listItemView.findViewById(R.id.book_rating);
        bookRating.setRating((float) currentBook.getRating());

        return listItemView;
    }
}
