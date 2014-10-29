package pl.trou.shoplist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "shoplist.db";

	private static final int DATABASE_VERSION = 2;

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		ShopListTable.onCreate(database);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS " + ShopListTable.TABLE_SHOPLIST);
		ShopListTable.onCreate(db);
	}
}
