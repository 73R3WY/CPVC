package jeremypacabis.cpvc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewARecordActivity extends Activity implements OnClickListener {

	Intent mIntent;
	String SELECTED_ROW_ID;
	SQLDatabaseHelper dbHelper;
	public static final String EDIT = "jeremypacabis.cpvc.EDIT";

	public String CFNAME, CLNAME, CADDRESS, CCONTACT, COCCUPATION, PNAME,
			PBREED, PCOLOR, PBIRTHDAY;
	TextView cfullname, caddress, ccontact, coccupation, pname, pbreed, pcolor,
			pbirthday;
	Button btnEDIT;
	int orientation;
	SettingsHelper sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_a_record_activity);
		initialize();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getDataFromDB();
		setFieldsData();
		setOrientation();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dbHelper.closeAll();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbHelper.closeAll();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_edit:
			mIntent = new Intent(this, AddEditActivity.class);
			mIntent.putExtra(EDIT, true);
			mIntent.putExtra(RecordsActivity.SELECTED_ITEM_ID, SELECTED_ROW_ID);
			startActivity(mIntent);
			finish();
			break;
		}
	}

	private void setFieldsData() {
		// TODO Auto-generated method stub
		cfullname.setText(CFNAME + " " + CLNAME);
		caddress.setText(CADDRESS);
		ccontact.setText(CCONTACT);
		coccupation.setText(COCCUPATION);
		pname.setText(PNAME);
		pbreed.setText(PBREED);
		pcolor.setText(PCOLOR);
		pbirthday.setText(PBIRTHDAY);
	}

	private void setOrientation() {
		// TODO Auto-generated method stub
		orientation = Integer.parseInt(sh
				.retrieveStringSettings(PublicConstants._ORIENTATION));
		setRequestedOrientation(orientation);
	}

	private void getDataFromDB() {
		// TODO Auto-generated method stub
		CFNAME = dbHelper.getClientFirstName(SELECTED_ROW_ID);
		CLNAME = dbHelper.getClientLastName(SELECTED_ROW_ID);
		CADDRESS = dbHelper.getClientAddress(SELECTED_ROW_ID);
		CCONTACT = dbHelper.getClientContact(SELECTED_ROW_ID);
		COCCUPATION = dbHelper.getClientOccupation(SELECTED_ROW_ID);
		PNAME = dbHelper.getPetName(SELECTED_ROW_ID);
		PBREED = dbHelper.getPetBreed(SELECTED_ROW_ID);
		PCOLOR = dbHelper.getPetColor(SELECTED_ROW_ID);
		PBIRTHDAY = dbHelper.getPetBirthday(SELECTED_ROW_ID);
	}

	private void initialize() {
		// TODO Auto-generated method stub
		sh = new SettingsHelper(getBaseContext());
		dbHelper = new SQLDatabaseHelper(ViewARecordActivity.this);
		mIntent = getIntent();
		SELECTED_ROW_ID = mIntent
				.getStringExtra(RecordsActivity.SELECTED_ITEM_ID);
		cfullname = (TextView) findViewById(R.id.var_fullname);
		caddress = (TextView) findViewById(R.id.var_address);
		ccontact = (TextView) findViewById(R.id.var_contact);
		coccupation = (TextView) findViewById(R.id.var_occupation);
		pname = (TextView) findViewById(R.id.var_pname);
		pbreed = (TextView) findViewById(R.id.var_pbreed);
		pcolor = (TextView) findViewById(R.id.var_pcolor);
		pbirthday = (TextView) findViewById(R.id.var_pbirthday);
		btnEDIT = (Button) findViewById(R.id.btn_edit);
		btnEDIT.setOnClickListener(this);
	}
}
