package pl.trou.shoplist.db;

import android.database.sqlite.SQLiteDatabase;

public class ShopListTable {

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static final String TABLE_SHOPLIST = "productlist";

	public static final String COLUMN_ID = "_id";

	public static final String COLUMN_STATUS = "status";

	public static final String COLUMN_PRODUCT = "product";

	private static final String DATABASE_CREATE = "create table " + TABLE_SHOPLIST + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_STATUS + " text not null, " + COLUMN_PRODUCT + " text not null" + ");";
}
