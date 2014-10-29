package pl.trou.shoplist.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import pl.trou.shoplist.db.Database;
import pl.trou.shoplist.db.ShopListTable;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class ProductListContentProvider extends ContentProvider {

	// database
	private Database database;

	// used for the UriMacher
	private static final int PRODUCTS = 10;

	private static final int PRODUCT_ID = 20;

	private static final String AUTHORITY = "pl.trou.shoplist.contentprovider";

	private static final String BASE_PATH = "products";

	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + BASE_PATH;

	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + BASE_PATH;

	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, PRODUCTS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PRODUCT_ID);
	}

	private void checkColumns(String[] projection) {
		String[] available = { ShopListTable.COLUMN_STATUS, ShopListTable.COLUMN_PRODUCT, ShopListTable.COLUMN_ID };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(Arrays.asList(available));

			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException("Unknown columns in projection");
			}
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
			case PRODUCTS:
				rowsDeleted = sqlDB.delete(ShopListTable.TABLE_SHOPLIST, selection, selectionArgs);
				break;
			case PRODUCT_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = sqlDB.delete(ShopListTable.TABLE_SHOPLIST, ShopListTable.COLUMN_ID + "=" + id, null);
				} else {
					rowsDeleted = sqlDB.delete(ShopListTable.TABLE_SHOPLIST, ShopListTable.COLUMN_ID + "=" + id + " and " + selection,
							selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		switch (uriType) {
			case PRODUCTS:
				id = sqlDB.insert(ShopListTable.TABLE_SHOPLIST, null, values);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public boolean onCreate() {
		database = new Database(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		checkColumns(projection);

		queryBuilder.setTables(ShopListTable.TABLE_SHOPLIST);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
			case PRODUCTS:
				break;
			case PRODUCT_ID:
				queryBuilder.appendWhere(ShopListTable.COLUMN_ID + "=" + uri.getLastPathSegment());
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
			case PRODUCTS:
				rowsUpdated = sqlDB.update(ShopListTable.TABLE_SHOPLIST, values, selection, selectionArgs);
				break;
			case PRODUCT_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = sqlDB.update(ShopListTable.TABLE_SHOPLIST, values, ShopListTable.COLUMN_ID + "=" + id, null);
				} else {
					rowsUpdated = sqlDB.update(ShopListTable.TABLE_SHOPLIST, values, ShopListTable.COLUMN_ID + "=" + id + " and "
							+ selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

}
