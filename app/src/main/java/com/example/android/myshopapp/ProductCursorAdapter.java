package com.example.android.myshopapp;

import android.content.Context;
import android.database.Cursor;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myshopapp.data.ProductContract;

public class ProductCursorAdapter extends CursorAdapter{


    public ProductCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup,
                false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameTextView = view.findViewById(R.id.product_name_text_view);
        TextView priceTextView = view.findViewById(R.id.price_text_view);
        TextView quantityTextView = view.findViewById(R.id.quantity_text_view);
        //ImageView imageView = view.findViewById(R.id.image_view);

        int nameColumnIndex = cursor.getColumnIndex(
                ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        int priceColumnIndex = cursor.getColumnIndex(
                ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(
                ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        //int imageColumnIndex = cursor.getColumnIndex(
               // ProductContract.ProductEntry.COLUMN_PRODUCT_PHOTO);

        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);
       // byte[] productImage = cursor.getBlob(imageColumnIndex);

        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);
        //imageView.setImageResource(R.drawable.bag);

    }
}
