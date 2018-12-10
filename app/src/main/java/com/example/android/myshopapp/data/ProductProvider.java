package com.example.android.myshopapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class ProductProvider extends ContentProvider{

    private static final int PRODUCTS = 100;
    private static final int PRODUCTS_ID = 101;

    /** Tag for the Log Messages*/
    public static final String LOG_TAG = ProductProvider.class.getSimpleName();

    /** Database helper object*/
    private ProductDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new ProductDbHelper(getContext());
        return true;
    }

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(ProductContract.ProductEntry.CONTENT_AUTHORITY,
                ProductContract.ProductEntry.PATHS_PRODUCTS, PRODUCTS);
        sUriMatcher.addURI(ProductContract.ProductEntry.CONTENT_AUTHORITY,
                ProductContract.ProductEntry.PATHS_PRODUCTS + "/#", + PRODUCTS_ID);
    }

    /**
     * Perform query for the given URI. Use the given projection, selection, selection arguments,
     * and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                cursor= database.query(ProductContract.ProductEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOder);
                break;
            case PRODUCTS_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                cursor = database.query(ProductContract.ProductEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOder);
                break;
                default:
                    throw new IllegalArgumentException("Cannot query unknown URI" + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return insertProduct(uri, contentValues);
                default:
                    throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertProduct(Uri uri, ContentValues values) {

        // Check that name is not null
        String name = values.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        // Check that brand is not null
        String brand = values.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_BRAND);
        if (brand == null) {
            throw new IllegalArgumentException("Product requires a brand");
        }

        // Check that supplier is not null
        String supplier = values.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER);
        if (supplier == null) {
            throw new IllegalArgumentException("Product requires a supplier");
        }

        // Check that quantity is not less than zero
        Integer quantity = values.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
        if (quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Product requires a valid quantity");
        }

        // Check that price is not null
        Integer price = values.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
        if (price != null && price < 0) {
            throw new IllegalArgumentException("Product requires a valid price");
        }

        /*Byte photo = values.getAsByte(ProductContract.ProductEntry.COLUMN_PRODUCT_PHOTO);
        if (photo == null) {
            throw new IllegalArgumentException("Product requires a photo");
        }
*/
       /* // Check that photo is not null
        String photo = values.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_PHOTO);
        if (photo == null) {
            throw new IllegalArgumentException("Product requires a photo");
        }*/

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new product with the given values
        long id = database.insert(ProductContract.ProductEntry.TABLE_NAME, null, values);
        // If the ID is -1, then the  insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //Notify all listeners that the data has changed for the Pet Content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of  the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }


    /**
     * Updates the data at the given selection and selection arguments.
     */
    @Override
    public  int update(Uri uri, ContentValues contentValues, String selection,
                       String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return updateProduct(uri, contentValues, selection, selectionArgs);
            case PRODUCTS_ID:
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri)) };
                return updateProduct(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // Check that name is not null if it is present
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME)) {
            String name = values.getAsString(ProductContract.ProductEntry.COLUMN_PRODUCT_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        // Check that supplier is not null if it exists
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER)) {
            String supplier = values.getAsString
                    (ProductContract.ProductEntry.COLUMN_PRODUCT_SUPPLIER);
            if (supplier == null) {
                throw new IllegalArgumentException("Product requires a supplier");
            }
        }

        // Check that the Quantity is valid if it exists
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY)) {
            Integer quantity = values.getAsInteger(
                    ProductContract.ProductEntry.COLUMN_PRODUCT_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Product requires a valid number of quantity");
            }
        }

        // Check that price is valid if it exists
        if (values.containsKey(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE)) {
            Integer price = values.getAsInteger(ProductContract.ProductEntry.COLUMN_PRODUCT_PRICE);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Product requires a valid price");
            }
        }

        //No need to check the brand, any value is valid (including null)

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise get writable database and update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Update the database and get the number of rows affected
        int rowsUpdated = database.update(ProductContract.ProductEntry.TABLE_NAME, values,
                selection, selectionArgs);

        // If it is more than 0 then notify all listeners that the data at the given URI changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return number of rows affected by the update statement
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        int rowsDeletd;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                // Delete all rows that match the selection and selectionArgs
                rowsDeletd = database.delete(ProductContract.ProductEntry.TABLE_NAME, selection,
                        selectionArgs);
                return rowsDeletd;
            case PRODUCTS_ID:
                // Delete a single row given by the ID
                selection = ProductContract.ProductEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeletd = database.delete(ProductContract.ProductEntry.TABLE_NAME, selection,
                        selectionArgs);

                //If one or more rows were deleted, then notify all listeners that the data
                // has changed for the Pet Content URI
                if (rowsDeletd > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return rowsDeletd;

            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);

        }
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PRODUCTS:
                return ProductContract.ProductEntry.CONTENT_LIST_ITEM;
            case PRODUCTS_ID:
                return ProductContract.ProductEntry.CONTENT_ITEM_TYPE;

            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

}
