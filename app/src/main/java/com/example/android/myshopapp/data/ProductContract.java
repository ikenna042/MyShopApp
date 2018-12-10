package com.example.android.myshopapp.data;

import android.content.ContentResolver;
import android.media.Image;
import android.net.Uri;
import android.provider.BaseColumns;

public final class ProductContract {

    public static abstract class ProductEntry implements BaseColumns {

        public static final String TABLE_NAME = "products";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PRODUCT_NAME = "name";
        public static final String COLUMN_PRODUCT_BRAND = "brand";
        public static final String COLUMN_PRODUCT_SUPPLIER = "supplier";
        public static final String COLUMN_PRODUCT_QUANTITY = "quantity";
        public static final String COLUMN_PRODUCT_PRICE = "price";
        public static final String COLUMN_PRODUCT_PHOTO = "photo";

        public static final String CONTENT_AUTHORITY = "com.example.android.myshopapp";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+ CONTENT_AUTHORITY);
        public static final String PATHS_PRODUCTS = "products";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,
                PATHS_PRODUCTS);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of products.
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATHS_PRODUCTS;

        /**
         *  The MIME type of the {@link #CONTENT_URI} for a single product.
         */
       public static final String CONTENT_LIST_ITEM = ContentResolver.CURSOR_DIR_BASE_TYPE +
                "/" + CONTENT_AUTHORITY + "/" + PATHS_PRODUCTS;


    }

}
