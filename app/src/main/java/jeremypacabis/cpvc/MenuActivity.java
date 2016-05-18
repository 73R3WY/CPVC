package jeremypacabis.cpvc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MenuActivity extends Activity implements OnClickListener {

	Button btnAdd, btnRecords, btnSettings, btnAbout, btnExit;

	String activityToStart;
	Class<?> mClass;
	int orientation;
	SettingsHelper sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_activity);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_add:
			activityToStart = "AddEditActivity";
			break;
		case R.id.btn_records:
			activityToStart = "RecordsActivity";
			break;
		case R.id.btn_settings:
			activityToStart = "SettingsActivity";
			break;
		case R.id.btn_about:
			activityToStart = "AboutActivity";
			break;
		case R.id.btn_exit:
			activityToStart = "finish";
			break;
		}
		startActivity(activityToStart);
	}

	private void initialize() {
		// TODO Auto-generated method stub
		sh = new SettingsHelper(getBaseContext());
		btnAdd = (Button) findViewById(R.id.btn_add);
		btnRecords = (Button) findViewById(R.id.btn_records);
		btnSettings = (Button) findViewById(R.id.btn_settings);
		btnAbout = (Button) findViewById(R.id.btn_about);
		btnExit = (Button) findViewById(R.id.btn_exit);
		btnAdd.setOnClickListener(this);
		btnRecords.setOnClickListener(this);
		btnSettings.setOnClickListener(this);
		btnAbout.setOnClickListener(this);
		btnExit.setOnClickListener(this);
	}

	private void startActivity(String a) {
		// TODO Auto-generated method stub
		if (a.contentEquals("finish")) {
			finish();
		} else {
			try {
				mClass = Class.forName("jeremypacabis.cpvc." + a);
				startActivity(new Intent(MenuActivity.this, mClass));
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
}
