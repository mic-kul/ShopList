package pl.trou.shoplist;

import java.util.List;

import pl.trou.shoplist.contentprovider.ProductListContentProvider;
import pl.trou.shoplist.db.ShopListTable;
import pl.trou.shoplist.model.Product;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class InteractiveArrayAdapter extends ArrayAdapter<Product> {

	static class ViewHolder {

		protected TextView text;

		protected CheckBox checkbox;
	}

	private final List<Product> list;

	private final Activity context;

	private SharedPreferences sharedPrefs;

	public InteractiveArrayAdapter(Activity context, List<Product> list) {
		super(context, R.layout.product_row, list);
		this.context = context;
		this.list = list;
		this.sharedPrefs = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.product_row, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.label);
			String color = sharedPrefs.getString(MainActivity.PREF_X, "");

			if (color.equals("1")) {
				viewHolder.text.setTextColor(Color.GREEN);
			} else if (color.equals("2")) {
				viewHolder.text.setTextColor(Color.RED);
			} else {
				viewHolder.text.setTextColor(Color.BLACK);
			}

			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Product product = (Product) viewHolder.checkbox.getTag();
					product.setSelected(buttonView.isChecked());

					Uri uri = Uri.parse(ProductListContentProvider.CONTENT_URI + "/" + product.getId());

					ContentValues newValues = new ContentValues();
					newValues.put(ShopListTable.COLUMN_STATUS, product.isSelected() ? "1" : "0");

					context.getContentResolver().update(uri, newValues, null, null);
				}
			});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(list.get(position));
			viewHolder.text.setTag(list.get(position));

		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
			((ViewHolder) view.getTag()).text.setTag(list.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(list.get(position).getName());
		holder.checkbox.setChecked(list.get(position).isSelected());
		return view;
	}
}
