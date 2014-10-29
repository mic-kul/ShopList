package pl.trou.shoplist;

import pl.trou.shoplist.contentprovider.ProductListContentProvider;
import pl.trou.shoplist.db.ShopListTable;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProductEditActivity extends Activity {

	private EditText mTitleText;

	private Uri contentProviderUri;

	private void fillData(Uri uri) {
		String[] projection = { ShopListTable.COLUMN_PRODUCT, ShopListTable.COLUMN_STATUS };
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			mTitleText.setText(cursor.getString(cursor.getColumnIndexOrThrow(ShopListTable.COLUMN_PRODUCT)));
			cursor.close();
		}
	}

	private void makeToast() {
		Toast.makeText(ProductEditActivity.this, "Please provide product name", Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.product_edit);

		mTitleText = (EditText) findViewById(R.id.product_edit_name);
		Button commitButton = (Button) findViewById(R.id.product_commit_button);

		Bundle extras = getIntent().getExtras();

		contentProviderUri = (bundle == null) ? null : (Uri) bundle.getParcelable(ProductListContentProvider.CONTENT_ITEM_TYPE);

		if (extras != null) {
			contentProviderUri = extras.getParcelable(ProductListContentProvider.CONTENT_ITEM_TYPE);

			fillData(contentProviderUri);
		}

		commitButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				if (TextUtils.isEmpty(mTitleText.getText().toString())) {
					makeToast();
				} else {
					setResult(RESULT_OK);
					finish();
				}
			}

		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveState();
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		saveState();
		outState.putParcelable(ProductListContentProvider.CONTENT_ITEM_TYPE, contentProviderUri);
	}

	private void saveState() {
		String productName = mTitleText.getText().toString();

		if (productName.length() == 0) {
			return;
		}

		ContentValues values = new ContentValues();
		values.put(ShopListTable.COLUMN_STATUS, "0");
		values.put(ShopListTable.COLUMN_PRODUCT, productName);

		if (contentProviderUri == null) {
			contentProviderUri = getContentResolver().insert(ProductListContentProvider.CONTENT_URI, values);
		} else {
			getContentResolver().update(contentProviderUri, values, null, null);
		}
	}
}
