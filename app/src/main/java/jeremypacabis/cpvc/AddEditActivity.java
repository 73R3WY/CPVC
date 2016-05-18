package jeremypacabis.cpvc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AddEditActivity extends Activity implements OnClickListener {

	EditText cfName, clName, cAddress, cContact, cOccupation, pName, pBreed,
			pColor;
	DatePicker pBirthday;
	ImageView pPicture;
	ImageButton btnCancel, btnReset, btnRecords, btnSave;
	Intent mIntent;
	Dialog mDlg;

	Calendar mCal = Calendar.getInstance();
	SimpleDateFormat mSDF;
	int month, day, year, bMonth, bDay, bYear, DLGSTATUS, orientation;
	String CFNAME, CLNAME, CADDRESS, CCONTACT, COCCUPATION, PNAME, PBREED,
			PCOLOR, PBIRTHDAY, CDATE, SELECTED_ROW_ID, DLGTITLE, DLGYES, DLGNO;
	StringBuilder warning;
	boolean DATA_INSERTED, EDIT, PROCEEDADD;
	SQLDatabaseHelper dbHelper;
	SettingsHelper sh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_edit_activity);
		initialize();
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setOrientation();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (EDIT) {
			switch (v.getId()) {
			// Add new data to the database
			case R.id.btn_cancel:
				PROCEEDADD = true;
				getFieldData();
				if (checkGatheredData()) {
					DLGSTATUS = 4;
					setDialogContentsAndShowDialog(DLGSTATUS);
				}
				break;
			// Delete the current data from the database
			case R.id.btn_reset:
				DLGSTATUS = 2;
				setDialogContentsAndShowDialog(DLGSTATUS);
				break;
			// Commits the changes of current data into database
			case R.id.btn_save:
				getFieldData();
				if (checkGatheredData()) {
					DLGSTATUS = 4;
					setDialogContentsAndShowDialog(DLGSTATUS);
				}
				break;
			case R.id.img_animal:

				break;
			}
		} else {
			switch (v.getId()) {
			// Cancels the current data without saving into database
			case R.id.btn_cancel:
				DLGSTATUS = 0;
				setDialogContentsAndShowDialog(DLGSTATUS);
				break;
			// Resets the fields into blank to fill up again
			case R.id.btn_reset:
				DLGSTATUS = 1;
				setDialogContentsAndShowDialog(DLGSTATUS);
				break;
			// Proceeds to the records list
			case R.id.btn_view_records:
				startActivity(new Intent(this, RecordsActivity.class));
				break;
			// Commits the changes of current data into database
			case R.id.btn_save:
				getFieldData();
				if (checkGatheredData()) {
					DLGSTATUS = 3;
					setDialogContentsAndShowDialog(DLGSTATUS);
				}
				break;
			case R.id.img_animal:

				break;
			}
		}
	}

	private void saveDataToDB() {
		// TODO Auto-generated method stub
		DATA_INSERTED = true;
		mCal.set(bYear, bMonth, bDay);
		PBIRTHDAY = mCal.getDisplayName(Calendar.MONTH, Calendar.LONG,
				Locale.getDefault())
				+ " " + String.valueOf(bDay) + ", " + String.valueOf(bYear);
		try {
			dbHelper.open();
			dbHelper.enterData(CFNAME, CLNAME, CADDRESS, CCONTACT, COCCUPATION,
					PNAME, PBREED, PCOLOR, PBIRTHDAY);
		} catch (SQLiteException e) {
			// TODO: handle exception
			DATA_INSERTED = false;
			Dialog dlg = new Dialog(this);
			dlg.setTitle("Data insertion failure!");
			TextView tv = new TextView(this);
			tv.setTextColor(Color.RED);
			tv.setText("Failed!\n" + e.toString());
			dlg.setContentView(tv);
			dlg.show();
		} finally {
			if (DATA_INSERTED) {
				Toast.makeText(AddEditActivity.this,
						"New data has been added to database.",
						Toast.LENGTH_SHORT).show();
			}
			dbHelper.close();
		}
	}

	private void updateDataToDB() {
		// TODO Auto-generated method stub
		DATA_INSERTED = true;
		mCal.set(bYear, bMonth, bDay);
		PBIRTHDAY = mCal.getDisplayName(Calendar.MONTH, Calendar.LONG,
				Locale.getDefault())
				+ " " + String.valueOf(bDay) + ", " + String.valueOf(bYear);
		try {
			dbHelper.open();
			dbHelper.updateData(SELECTED_ROW_ID, CFNAME, CLNAME, CADDRESS,
					CCONTACT, COCCUPATION, PNAME, PBREED, PCOLOR, PBIRTHDAY);
		} catch (SQLiteException e) {
			// TODO: handle exception
			e.printStackTrace();
			DATA_INSERTED = false;
		} finally {
			if (DATA_INSERTED) {
				Toast.makeText(AddEditActivity.this,
						"Data has been updated successfully!",
						Toast.LENGTH_SHORT).show();
			}
			dbHelper.close();
		}
	}

	private void deleteDataFromDB() {
		// TODO Auto-generated method stub
		DATA_INSERTED = true;
		try {
			dbHelper.open();
			dbHelper.deleteData(SELECTED_ROW_ID);
		} catch (SQLiteException e) {
			// TODO: handle exception
			e.printStackTrace();
			DATA_INSERTED = false;
		} finally {
			if (DATA_INSERTED) {
				Toast.makeText(AddEditActivity.this,
						"Data has been deleted successfully!",
						Toast.LENGTH_SHORT).show();
			}
			dbHelper.close();
		}
	}

	private boolean checkGatheredData() {
		// TODO Auto-generated method stub
		warning.append("Make sure that you enter data to all text fields.\nType n/a if field is not applicable\nPlease check the following fields:\n\n");
		if (CFNAME.isEmpty()) {
			warning.append("Client's First Name\n");
		}
		if (CLNAME.isEmpty()) {
			warning.append("Client's Last Name\n");
		}
		if (CADDRESS.isEmpty()) {
			warning.append("Client's Address\n");
		}
		if (CCONTACT.isEmpty()) {
			warning.append("Client's Contact Number\n");
		}
		if (COCCUPATION.isEmpty()) {
			warning.append("Client's Occupation\n");
		}
		if (PNAME.isEmpty()) {
			warning.append("Pet's Name\n");
		}
		if (PBREED.isEmpty()) {
			warning.append("Pet's Breed\n");
		}
		if (PCOLOR.isEmpty()) {
			warning.append("Pet's Color/Marking\n");
		}
		if (PBIRTHDAY.contentEquals(CDATE)) {
			warning.append("I know that your pet is not born today!");
		}
		if (warning.toString().length() > 130) {
			Toast.makeText(AddEditActivity.this, warning.toString(),
					Toast.LENGTH_SHORT).show();
			warning.delete(0, warning.length());
			return false;
		}
		warning.delete(0, warning.length());
		return true;
	}

	private void getFieldData() {
		// TODO Auto-generated method stub
		CFNAME = cfName.getText().toString();
		CLNAME = clName.getText().toString();
		CADDRESS = cAddress.getText().toString();
		CCONTACT = cContact.getText().toString();
		COCCUPATION = cOccupation.getText().toString();
		PNAME = pName.getText().toString();
		PBREED = pBreed.getText().toString();
		PCOLOR = pColor.getText().toString();
		bMonth = pBirthday.getMonth();
		bDay = pBirthday.getDayOfMonth();
		bYear = pBirthday.getYear();
		PBIRTHDAY = bMonth + bDay + bYear + "";
	}

	private void initialize() {
		// TODO Auto-generated method stub
		dbHelper = new SQLDatabaseHelper(AddEditActivity.this);
		sh = new SettingsHelper(getBaseContext());
		EDIT = false;
		PROCEEDADD = false;
		mIntent = getIntent();
		if (mIntent.hasExtra(RecordsActivity.SELECTED_ITEM_ID)
				&& mIntent.hasExtra(ViewARecordActivity.EDIT)) {
			SELECTED_ROW_ID = mIntent
					.getStringExtra(RecordsActivity.SELECTED_ITEM_ID);
			EDIT = mIntent.getBooleanExtra(ViewARecordActivity.EDIT, false);
		}
		cfName = (EditText) findViewById(R.id.etxt_fname);
		clName = (EditText) findViewById(R.id.etxt_lname);
		cAddress = (EditText) findViewById(R.id.etxt_address);
		cContact = (EditText) findViewById(R.id.etxt_contact);
		cOccupation = (EditText) findViewById(R.id.etxt_occupation);
		pName = (EditText) findViewById(R.id.etxt_petname);
		pBreed = (EditText) findViewById(R.id.etxt_petbreed);
		pColor = (EditText) findViewById(R.id.etxt_petcolor);
		pPicture = (ImageView) findViewById(R.id.img_animal);
		pBirthday = (DatePicker) findViewById(R.id.dp_petbday);
		btnCancel = (ImageButton) findViewById(R.id.btn_cancel);
		btnReset = (ImageButton) findViewById(R.id.btn_reset);
		btnRecords = (ImageButton) findViewById(R.id.btn_view_records);
		btnSave = (ImageButton) findViewById(R.id.btn_save);
		btnCancel.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		btnRecords.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		pPicture.setOnClickListener(this);
		month = mCal.get(Calendar.MONTH);
		day = mCal.get(Calendar.DAY_OF_MONTH);
		year = mCal.get(Calendar.YEAR);
		CDATE = month + day + year + "";
		pBirthday.init(year, month, day, null);
		warning = new StringBuilder();
		if (EDIT) {
			edit();
		}
	}

	private void edit() {
		// TODO Auto-generated method stub
		dbHelper.closeAll();
		btnCancel.setImageResource(android.R.drawable.ic_menu_add);
		btnReset.setImageResource(android.R.drawable.ic_menu_delete);
		btnRecords.setVisibility(View.GONE);
		try {
			dbHelper.open();
			CFNAME = dbHelper.getClientFirstName(SELECTED_ROW_ID);
			CLNAME = dbHelper.getClientLastName(SELECTED_ROW_ID);
			CADDRESS = dbHelper.getClientAddress(SELECTED_ROW_ID);
			CCONTACT = dbHelper.getClientContact(SELECTED_ROW_ID);
			COCCUPATION = dbHelper.getClientOccupation(SELECTED_ROW_ID);
			PNAME = dbHelper.getPetName(SELECTED_ROW_ID);
			PBREED = dbHelper.getPetBreed(SELECTED_ROW_ID);
			PCOLOR = dbHelper.getPetColor(SELECTED_ROW_ID);
			PBIRTHDAY = dbHelper.getPetBirthday(SELECTED_ROW_ID);
			dbHelper.close();
		} catch (SQLiteException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			try {
				mSDF = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault());
				mCal.setTime(mSDF.parse(PBIRTHDAY));
				pBirthday.init(mCal.get(Calendar.YEAR),
						mCal.get(Calendar.MONTH),
						mCal.get(Calendar.DAY_OF_MONTH), null);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cfName.setText(CFNAME);
			clName.setText(CLNAME);
			cAddress.setText(CADDRESS);
			cContact.setText(CCONTACT);
			cOccupation.setText(COCCUPATION);
			pName.setText(PNAME);
			pBreed.setText(PBREED);
			pColor.setText(PCOLOR);
		}
	}

	private void clearEntryFields() {
		cfName.setText(null);
		clName.setText(null);
		cAddress.setText(null);
		cContact.setText(null);
		cOccupation.setText(null);
		pName.setText(null);
		pBreed.setText(null);
		pColor.setText(null);
		mCal = Calendar.getInstance();
		pBirthday.init(mCal.get(Calendar.YEAR), mCal.get(Calendar.MONTH),
				mCal.get(Calendar.DAY_OF_MONTH), null);
	}

	private void setDialogContentsAndShowDialog(int DLGSTATUS) {
		DLGTITLE = getDialogTitle(DLGSTATUS);
		DLGYES = getDialogPositiveButton(DLGSTATUS);
		DLGNO = getDialogNegativeButton(DLGSTATUS);
		showDialog(DLGTITLE, DLGYES, DLGNO, DLGSTATUS);
	}

	private String getDialogTitle(int DLGSTATUS) {
		switch (DLGSTATUS) {
		// Cancel
		case 0:
			DLGTITLE = PublicConstants._CANCEL_BTN;
			break;
		// Clear fields
		case 1:
			DLGTITLE = PublicConstants._CLEAR_BTN;
			break;
		// Delete
		case 2:
			DLGTITLE = PublicConstants._DELETE_BTN;
			break;
		// Confirm save on leave
		case 3:
			DLGTITLE = PublicConstants._CONFIRM_SAVE_BTN;
			break;
		// Confirm update on entry
		case 4:
			DLGTITLE = PublicConstants._CONFIRM_UPDATE_BTN;
			break;
		}
		return DLGTITLE;
	}

	private String getDialogPositiveButton(int DLGSTATUS) {
		switch (DLGSTATUS) {
		// Cancel
		case 0:
			DLGYES = PublicConstants._CANCEL_BTN_YES;
			break;
		// Clear fields
		case 1:
			DLGYES = PublicConstants._CLEAR_BTN_YES;
			break;
		// Delete
		case 2:
			DLGYES = PublicConstants._DELETE_BTN_YES;
			break;
		// Confirm save on leave
		case 3:
			DLGYES = PublicConstants._CONFIRM_SAVE_BTN_YES;
			break;
		// Confirm update on entry
		case 4:
			DLGYES = PublicConstants._CONFIRM_UPDATE_BTN_YES;
			break;
		}
		return DLGYES;
	}

	private String getDialogNegativeButton(int DLGSTATUS) {
		switch (DLGSTATUS) {
		// Cancel
		case 0:
			DLGNO = PublicConstants._CANCEL_BTN_NO;
			break;
		// Clear fields
		case 1:
			DLGNO = PublicConstants._CLEAR_BTN_NO;
			break;
		// Delete
		case 2:
			DLGNO = PublicConstants._DELETE_BTN_NO;
			break;
		// Confirm save on leave
		case 3:
			DLGNO = PublicConstants._CONFIRM_SAVE_BTN_NO;
			break;
		// Confirm update on entry
		case 4:
			DLGNO = PublicConstants._CONFIRM_UPDATE_BTN_NO;
			break;
		}
		return DLGNO;
	}

	private void showDialog(String DLGTITLE, String DLGYES, String DLGNO,
			final int DLGSTATUS) {
		mDlg = new AlertDialog.Builder(AddEditActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(DLGTITLE)
				.setPositiveButton(DLGYES,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								ifDialogPositivePressed(DLGSTATUS);
							}
						})
				.setNegativeButton(DLGNO,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								ifDialogNegativePressed(DLGSTATUS);
							}
						}).show();
	}

	private void setOrientation() {
		// TODO Auto-generated method stub
		orientation = Integer.parseInt(sh
				.retrieveStringSettings(PublicConstants._ORIENTATION));
		setRequestedOrientation(orientation);
	}

	protected void ifDialogPositivePressed(int DLGSTATUS) {
		// TODO Auto-generated method stub
		switch (DLGSTATUS) {
		case 0:
			finish();
			break;
		case 1:
			clearEntryFields();
			break;
		case 2:
			deleteDataFromDB();
			clearEntryFields();
			finish();
			break;
		case 3:
			saveDataToDB();
			clearEntryFields();
			break;
		case 4:
			updateDataToDB();
			if (PROCEEDADD) {
				startActivity(new Intent(this, AddEditActivity.class));
			}
			finish();
			break;
		}
	}

	protected void ifDialogNegativePressed(int DLGSTATUS) {
		// TODO Auto-generated method stub
		switch (DLGSTATUS) {
		case 0:
			getFieldData();
			if (checkGatheredData()) {
				saveDataToDB();
			}
			break;
		case 1:
			getFieldData();
			if (checkGatheredData()) {
				saveDataToDB();
			}
			break;
		default:
			if (PROCEEDADD) {
				startActivity(new Intent(this, AddEditActivity.class));
				finish();
			}
			break;
		}
	}
}
