package pl.trou.shoplist;

import java.util.ArrayList;
import java.util.List;

import pl.trou.shoplist.contentprovider.ProductListContentProvider;
import pl.trou.shoplist.db.ShopListTable;
import pl.trou.shoplist.model.Product;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ProductListActivity extends ListActivity {

	private static final int REQUEST_CODE = 1;

	private static final int DELETE_ID = Menu.FIRST + 1;

	private ArrayAdapter<Product> adapter;

	private void addItem() {
		Intent i = new Intent(this, ProductEditActivity.class);
		startActivityForResult(i, REQUEST_CODE);
	}

	private void fillData() {
		List<Product> productList = new ArrayList<Product>();

		Uri uri = ProductListContentProvider.CONTENT_URI;
		String[] projection = { ShopListTable.COLUMN_ID, ShopListTable.COLUMN_PRODUCT, ShopListTable.COLUMN_STATUS };
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

		if (null != cursor) {
			while (cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndexOrThrow(ShopListTable.COLUMN_PRODUCT));
				String status = cursor.getString(cursor.getColumnIndexOrThrow(ShopListTable.COLUMN_STATUS));
				Long id = cursor.getLong(cursor.getColumnIndexOrThrow(ShopListTable.COLUMN_ID));
				Product p = new Product(id, name, status);
				productList.add(p);
			}

			cursor.close();
		}
		adapter = new InteractiveArrayAdapter(this, productList);
		setListAdapter(adapter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			fillData();
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case DELETE_ID:
				SharedPreferences sharedPrefs = getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
				boolean isDeletable = sharedPrefs.getBoolean(MainActivity.PREF_Y, false);
				if (!isDeletable) {
					Toast.makeText(this, "Enable product deletion in options", Toast.LENGTH_SHORT).show();
					return true;
				}
				AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				Product product = (Product) getListAdapter().getItem(info.position);
				if (null != product && null != product.getId()) {
					Uri uri = Uri.parse(ProductListContentProvider.CONTENT_URI + "/" + product.getId());
					getContentResolver().delete(uri, null, null);
					fillData();
				}
				return true;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list);
		this.getListView().setDividerHeight(2);
		fillData();
		registerForContextMenu(getListView());
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.listmenu, menu);
		return true;
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Product product = (Product) l.getItemAtPosition(position);
		if (null != product && null != product.getId()) {
			Intent i = new Intent(this, ProductEditActivity.class);
			Uri uri = Uri.parse(ProductListContentProvider.CONTENT_URI + "/" + product.getId());
			i.putExtra(ProductListContentProvider.CONTENT_ITEM_TYPE, uri);
			startActivityForResult(i, REQUEST_CODE);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.insert:
				addItem();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
