package jeremypacabis.cpvc;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

public class RecordsActivity extends Activity {

	public static final String SELECTED_ITEM_ID = "jeremypacabis.cpvc.SELECTED_ITEM_ID";
	private static final String SortByList[] = {
			PublicConstants._SORT_BY_PNAME_ASC,
			PublicConstants._SORT_BY_PNAME_DESC,
			PublicConstants._SORT_BY_CLIENT_LNAME_ASC,
			PublicConstants._SORT_BY_CLIENT_LNAME_DESC,
			PublicConstants._SORT_BY_CLIENT_FNAME_ASC,
			PublicConstants._SORT_BY_CLIENT_FNAME_DESC };
	int order, IDorder, mSpinnerPos, orientation;
	String dialogTitle;
	private ArrayList<String> results = new ArrayList<String>();
	private ArrayList<String> resultsID = new ArrayList<String>();
	private ArrayAdapter<String> mSpinnerAdapter;
	private ArrayAdapter<String> mAdapter;
	boolean DATA_INSERTED;
	SettingsHelper sh;
	SQLDatabaseHelper dbHelper;
	Intent mIntent;
	Spinner mSortRecordsList;
	ListView mRecordsListView;
	Dialog mLongClickActionsDlg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.records_list_activity);
		initialize();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		restoreSpinnerPos();
		refreshRecordsList(mSpinnerPos);
		setListDataFromDB();
		setOrientation();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dbHelper.closeAll();
		saveSpinnerPos();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		dbHelper.closeAll();
		saveSpinnerPos();
	}

	protected void refreshRecordsList(int mSpinnerPos) {
		// TODO Auto-generated method stub
		switch (mSpinnerPos) {
		case 0:
			order = 0;
			results = dbHelper.getListByPetName(order);
			break;
		case 1:
			order = 1;
			results = dbHelper.getListByPetName(order);
			break;
		case 2:
			order = 0;
			results = dbHelper.getListByLastName(order);
			break;
		case 3:
			order = 1;
			results = dbHelper.getListByLastName(order);
			break;
		case 4:
			order = 0;
			results = dbHelper.getListByFirstName(order);
			break;
		case 5:
			order = 1;
			results = dbHelper.getListByFirstName(order);
			break;
		}
	}

	private void initialize() {
		// TODO Auto-generated method stub
		order = 0;
		mSortRecordsList = (Spinner) findViewById(R.id.spinner_sort_records);
		mRecordsListView = (ListView) findViewById(R.id.lv_records_list);
		mSpinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, SortByList);
		mSpinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSortRecordsList.setAdapter(mSpinnerAdapter);
		sh = new SettingsHelper(getBaseContext());
		dbHelper = new SQLDatabaseHelper(RecordsActivity.this);
		mIntent = new Intent(this, ViewARecordActivity.class);
		setmSortRecordsListListener();
		setmRecordsListViewListener();
		setmRecordsListViewLongClickListener();
	}

	private void saveSpinnerPos() {
		// TODO Auto-generated method stub
		mSpinnerPos = mSortRecordsList.getSelectedItemPosition();
		sh.saveIntSettings(PublicConstants._SPINNER_SORT_BY, mSpinnerPos);
	}

	private void restoreSpinnerPos() {
		mSpinnerPos = sh.retrieveIntSettings(PublicConstants._SPINNER_SORT_BY);
		mSortRecordsList.setSelection(mSpinnerPos);
	}

	private void setListDataFromDB() {
		// TODO Auto-generated method stub
		if (results.isEmpty()) {
			results.add("No data saved yet");
			mRecordsListView.setEnabled(false);
		} else {
			mRecordsListView.setEnabled(true);
		}
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, results);
		displayList();
	}

	private void displayList() {
		// TODO Auto-generated method stub
		mRecordsListView.setAdapter(mAdapter);
	}

	private void setmSortRecordsListListener() {
		// TODO Auto-generated method stub
		mSortRecordsList
				.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						mSpinnerPos = arg2;
						refreshRecordsList(mSpinnerPos);
						setListDataFromDB();
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						setListDataFromDB();
					}
				});
	}

	private void setmRecordsListViewListener() {
		// TODO Auto-generated method stub
		mRecordsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				getRecordsListItemID();
				mIntent.putExtra(SELECTED_ITEM_ID, resultsID.get(arg2));
				startActivity(mIntent);
			}

		});
	}

	private void setmRecordsListViewLongClickListener() {
		// TODO Auto-generated method stub
		mRecordsListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						// TODO Auto-generated method stub
						getRecordsListItemID();
						dialogTitle = results.get(arg2);
						mLongClickActionsDlg = new AlertDialog.Builder(
								RecordsActivity.this)
								.setTitle(dialogTitle)
								.setIcon(android.R.drawable.ic_dialog_info)
								.setItems(R.array.onLongClickARecord,
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												switch (which) {
												// edit
												case 1:
													mIntent = new Intent(
															RecordsActivity.this,
															AddEditActivity.class);
													mIntent.putExtra(
															ViewARecordActivity.EDIT,
															true);
													mIntent.putExtra(
															RecordsActivity.SELECTED_ITEM_ID,
															resultsID.get(arg2));
													startActivity(mIntent);
													break;
												// delete
												case 2:
													mLongClickActionsDlg = new AlertDialog.Builder(
															RecordsActivity.this)
															.setTitle(
																	"Delete data for "
																			+ dialogTitle
																			+ "?")
															.setIcon(
																	android.R.drawable.ic_menu_delete)
															.setPositiveButton(
																	android.R.string.yes,
																	new DialogInterface.OnClickListener() {

																		@Override
																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																			deleteDataFromDB(resultsID
																					.get(arg2));
																			refreshRecordsList(mSpinnerPos);
																			setListDataFromDB();
																		}
																	})
															.setNegativeButton(
																	android.R.string.no,
																	new DialogInterface.OnClickListener() {

																		@Override
																		public void onClick(
																				DialogInterface dialog,
																				int which) {
																			// TODO
																			// Auto-generated
																			// method
																			// stub
																		}
																	}).show();
													break;
												// view
												default:
													mIntent.putExtra(
															SELECTED_ITEM_ID,
															resultsID.get(arg2));
													startActivity(mIntent);
													break;
												}
											}
										}).show();
						return false;
					}
				});
	}

	private void setOrientation() {
		// TODO Auto-generated method stub
		orientation = Integer.parseInt(sh
				.retrieveStringSettings(PublicConstants._ORIENTATION));
		setRequestedOrientation(orientation);
	}

	private void getRecordsListItemID() {
		// TODO Auto-generated method stub
		IDorder = mSortRecordsList.getSelectedItemPosition();
		resultsID = dbHelper.getIDList(IDorder);
	}

	private void deleteDataFromDB(String SELECTED_ROW_ID) {
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
				Toast.makeText(RecordsActivity.this,
						"Data has been deleted successfully!",
						Toast.LENGTH_SHORT).show();
			}
			dbHelper.close();
		}
	}
}
