package jeremypacabis.cpvc;

import android.app.Activity;
import android.os.Bundle;

public class AboutActivity extends Activity {

	int orientation;
	SettingsHelper sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initialize();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setOrientation();
	}

	private void setOrientation() {
		// TODO Auto-generated method stub
		orientation = Integer.parseInt(sh
				.retrieveStringSettings(PublicConstants._ORIENTATION));
		setRequestedOrientation(orientation);
	}

	private void initialize() {
		sh = new SettingsHelper(getBaseContext());
	}
}
