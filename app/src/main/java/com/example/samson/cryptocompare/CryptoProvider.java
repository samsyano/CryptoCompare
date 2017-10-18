package com.example.samson.cryptocompare;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by SAMSON on 10/13/2017.
 */

public class CryptoProvider extends ContentProvider {

    public static final int CRYPTO = 100;
    public static final int CRYPTO_WITH_ID = 101;


    public static UriMatcher uriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(CryptoContract.AUTHORITY, CryptoContract.PATH, CRYPTO);

        matcher.addURI(CryptoContract.AUTHORITY, CryptoContract.PATH + "/#", CRYPTO_WITH_ID);

        return matcher;
    }


    CryptoDbHelper dbHelper;


    @Override
    public boolean onCreate() {

        dbHelper = new CryptoDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {

            case CRYPTO:
                cursor = db.query(CryptoContract.CryptoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);

                break;
            case CRYPTO_WITH_ID:
                selection = "_id=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = db.query(CryptoContract.CryptoEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Not supported");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match){
            case CRYPTO:
                return CryptoContract.CONTENT_LIST_TYPE;
            case CRYPTO_WITH_ID:
                return CryptoContract.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unable to the get the type");
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);

        Uri uris;
        Long id;
        switch (match) {
            case CRYPTO:
                id = db.insert(CryptoContract.CryptoEntry.TABLE_NAME, null, values);

                if (id > 0) {
                    uris = ContentUris.withAppendedId(uri, id);
                } else {
                    throw new SQLException("Unable to insert into the database at " + id);
                }
                break;
            default:
                throw new IllegalArgumentException("Insertion is not supported");
        }

        getContext().getContentResolver().notifyChange(uris, null);
        return uris;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);
        int delete_id = 0;
        switch (match) {
            case CRYPTO:
                delete_id = db.delete(CryptoContract.CryptoEntry.TABLE_NAME, selection, selectionArgs);

                break;
            case CRYPTO_WITH_ID:
                selection = "_id=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                delete_id = db.delete(CryptoContract.CryptoEntry.TABLE_NAME, selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Not supported...");
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return delete_id;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int update_id;
        switch (match) {
            case CRYPTO:

                update_id = db.delete(CryptoContract.CryptoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CRYPTO_WITH_ID:
                selection = "_id=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
//                selectionArgs = new String[]{uri.getPathSegments().get(1)};
                update_id = db.delete(CryptoContract.CryptoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
        if (update_id != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return update_id;
    }

}
