package pl.trou.shoplist;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class OptionsActivity extends Activity {

	private SharedPreferences sharedPrefs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);
		sharedPrefs = getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);

		RadioGroup rGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		String selectedTheme = sharedPrefs.getString(MainActivity.PREF_X, "Default");
		if (selectedTheme.length() == 0)
			selectedTheme = "Default";

		for (int i = 0; i < rGroup.getChildCount(); i++) {

			RadioButton rButton = (RadioButton) rGroup.getChildAt(i);
			if (rButton.getText().toString().contains(selectedTheme)) {
				rButton.setChecked(true);
				break;
			}
		}

		rGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup rGroup, int checkedId) {

				RadioButton checkedRadioButton = (RadioButton) rGroup.findViewById(checkedId);

				boolean isChecked = checkedRadioButton.isChecked();

				if (isChecked) {
					Editor editor = sharedPrefs.edit();
					if (checkedRadioButton.getText().equals("Theme 1")) {
						editor.putString(MainActivity.PREF_X, "1");
					} else if (checkedRadioButton.getText().equals("Theme 2")) {
						editor.putString(MainActivity.PREF_X, "2");
					} else {
						editor.putString(MainActivity.PREF_X, "");
					}
					editor.commit();
				}
			}
		});

		CheckBox checkboxDeletable = (CheckBox) findViewById(R.id.deletableCheckbox);
		boolean isDeletable = sharedPrefs.getBoolean(MainActivity.PREF_Y, false);
		checkboxDeletable.setChecked(isDeletable);

		checkboxDeletable.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton button, boolean checked) {
				Editor editor = sharedPrefs.edit();
				editor.putBoolean(MainActivity.PREF_Y, checked);
				editor.commit();
			}

		});
	}
}
