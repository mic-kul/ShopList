package pl.trou.shoplist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	public static final String PREF_NAME = "SHOPLIST_PREF";

	public static final String PREF_X = "";

	public static final String DEFAULT_X = "";

	public static final String PREF_Y = "deletable";

	public static final boolean DEFAULT_Y = false;

	private SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPrefs = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
		String preference1 = sharedPrefs.getString(PREF_X, DEFAULT_X);

		setContentView(R.layout.activity_main);
	}

	public void openOptions(View view) {
		Intent intent = new Intent(this, OptionsActivity.class);
		startActivity(intent);
	}

	public void openShopList(View view) {
		Intent intent = new Intent(this, ProductListActivity.class);
		startActivity(intent);
	}
}
