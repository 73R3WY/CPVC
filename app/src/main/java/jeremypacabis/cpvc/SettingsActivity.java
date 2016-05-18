package jeremypacabis.cpvc;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class SettingsActivity extends PreferenceActivity {

	int orientation;
	SettingsHelper sh;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);
		initialize();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setOrientation();
	}

	private void initialize() {
		// TODO Auto-generated method stub
		sh = new SettingsHelper(getBaseContext());
	}

	private void setOrientation() {
		// TODO Auto-generated method stub
		orientation = Integer.parseInt(sh
				.retrieveStringSettings(PublicConstants._ORIENTATION));
		setRequestedOrientation(orientation);
	}
}
