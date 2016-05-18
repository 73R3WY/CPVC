package jeremypacabis.cpvc;

import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLDatabaseHelper {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_CFNAME = "c_fname";
	public static final String KEY_CLNAME = "c_lname";
	public static final String KEY_CADDRESS = "c_address";
	public static final String KEY_CCONTACT = "c_contact";
	public static final String KEY_COCCUPATION = "c_job";
	public static final String KEY_PNAME = "p_name";
	public static final String KEY_PBREED = "p_breed";
	public static final String KEY_PCOLOR = "p_color";
	public static final String KEY_PBIRTHDAY = "p_birthday";

	private static final String DATABASE_NAME = "ClientInfodb";
	private static final String DATABASE_TABLE = "clientTable";
	private static final int DATABASE_VERSION = 1;

	private DBHelper mHelper;
	private final Context mContext;
	private SQLiteDatabase mDatabase;
	private Cursor cur;

	private ArrayList<String> return_list = new ArrayList<String>();
	private ArrayList<String> return_idlist = new ArrayList<String>();

	String CFNAME, CLNAME, CADDRESS, CCONTACT, COCCUPATION, PNAME, PBREED,
			PCOLOR, PBIRTHDAY, ID, ORDER, SQLQUERY, ORDERBY;
	ContentValues cv;

	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE + " (" + KEY_ROWID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_CFNAME
					+ " TEXT NOT NULL, " + KEY_CLNAME + " TEXT NOT NULL, "
					+ KEY_CADDRESS + " TEXT NOT NULL, " + KEY_CCONTACT
					+ " TEXT NOT NULL, " + KEY_COCCUPATION + " TEXT NOT NULL, "
					+ KEY_PNAME + " TEXT NOT NULL, " + KEY_PBREED
					+ " TEXT NOT NULL, " + KEY_PCOLOR + " TEXT NOT NULL, "
					+ KEY_PBIRTHDAY + " TEXT NOT NULL);"

			);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}

	}

	public SQLDatabaseHelper(Context c) {
		mContext = c;
	}

	public SQLDatabaseHelper open() throws SQLException {
		mHelper = new DBHelper(mContext);
		if (mDatabase == null || !mDatabase.isOpen()) {
			mDatabase = mHelper.getWritableDatabase();
		}
		return this;
	}

	public void close() {
		mDatabase.close();
		mHelper.close();
	}

	public long enterData(String cFNAME, String cLNAME, String cADDRESS,
			String cCONTACT, String cOCCUPATION, String pNAME, String pBREED,
			String pCOLOR, String pBIRTHDAY) {
		// TODO Auto-generated method stub
		cv = new ContentValues();
		cv.put(KEY_CFNAME, cFNAME);
		cv.put(KEY_CLNAME, cLNAME);
		cv.put(KEY_CADDRESS, cADDRESS);
		cv.put(KEY_CCONTACT, cCONTACT);
		cv.put(KEY_COCCUPATION, cOCCUPATION);
		cv.put(KEY_PNAME, pNAME);
		cv.put(KEY_PBREED, pBREED);
		cv.put(KEY_PCOLOR, pCOLOR);
		cv.put(KEY_PBIRTHDAY, pBIRTHDAY);
		return mDatabase.insert(DATABASE_TABLE, null, cv);
	}

	public long updateData(String SELECTED_ROW_ID, String CFNAME,
			String CLNAME, String CADDRESS, String CCONTACT,
			String COCCUPATION, String PNAME, String PBREED, String PCOLOR,
			String PBIRTHDAY) {
		// TODO Auto-generated method stub
		cv = new ContentValues();
		cv.put(KEY_CFNAME, CFNAME);
		cv.put(KEY_CLNAME, CLNAME);
		cv.put(KEY_CADDRESS, CADDRESS);
		cv.put(KEY_CCONTACT, CCONTACT);
		cv.put(KEY_COCCUPATION, COCCUPATION);
		cv.put(KEY_PNAME, PNAME);
		cv.put(KEY_PBREED, PBREED);
		cv.put(KEY_PCOLOR, PCOLOR);
		cv.put(KEY_PBIRTHDAY, PBIRTHDAY);
		return mDatabase.update(DATABASE_TABLE, cv, KEY_ROWID + " = "
				+ SELECTED_ROW_ID, null);
	}

	public void deleteData(String sELECTED_ROW_ID) {
		// TODO Auto-generated method stub
		mDatabase.delete(DATABASE_TABLE, KEY_ROWID + " = " + sELECTED_ROW_ID,
				null);
	}

	public ArrayList<String> getListByLastName(int order) {
		// TODO Auto-generated method stub
		ORDER = ascOrDesc(order);
		return_list.clear();
		SQLQUERY = "SELECT " + KEY_CLNAME + ", " + KEY_CFNAME + ", "
				+ KEY_PNAME + " FROM " + DATABASE_TABLE + " ORDER BY "
				+ KEY_CLNAME + " COLLATE NOCASE " + ORDER;
		try {
			open();
			cur = mDatabase.rawQuery(SQLQUERY, null);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						CLNAME = cur.getString(cur.getColumnIndex(KEY_CLNAME));
						CFNAME = cur.getString(cur.getColumnIndex(KEY_CFNAME));
						PNAME = cur.getString(cur.getColumnIndex(KEY_PNAME));
						return_list.add(CLNAME + ", " + CFNAME
								+ " / Pet's name: " + PNAME);
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return return_list;
	}

	public ArrayList<String> getListByFirstName(int order) {
		// TODO Auto-generated method stub
		ORDER = ascOrDesc(order);
		return_list.clear();
		SQLQUERY = "SELECT " + KEY_CLNAME + ", " + KEY_CFNAME + ", "
				+ KEY_PNAME + " FROM " + DATABASE_TABLE + " ORDER BY "
				+ KEY_CFNAME + " COLLATE NOCASE " + ORDER;
		try {
			open();
			cur = mDatabase.rawQuery(SQLQUERY, null);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						CLNAME = cur.getString(cur.getColumnIndex(KEY_CLNAME));
						CFNAME = cur.getString(cur.getColumnIndex(KEY_CFNAME));
						PNAME = cur.getString(cur.getColumnIndex(KEY_PNAME));
						return_list.add(CFNAME + " " + CLNAME
								+ " / Pet's name: " + PNAME);
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return return_list;
	}

	public ArrayList<String> getListByPetName(int order) {
		// TODO Auto-generated method stub
		ORDER = ascOrDesc(order);
		return_list.clear();
		SQLQUERY = "SELECT " + KEY_CLNAME + ", " + KEY_CFNAME + ", "
				+ KEY_PNAME + " FROM " + DATABASE_TABLE + " ORDER BY "
				+ KEY_PNAME + " COLLATE NOCASE " + ORDER;
		try {
			open();
			cur = mDatabase.rawQuery(SQLQUERY, null);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						CLNAME = cur.getString(cur.getColumnIndex(KEY_CLNAME));
						CFNAME = cur.getString(cur.getColumnIndex(KEY_CFNAME));
						PNAME = cur.getString(cur.getColumnIndex(KEY_PNAME));
						return_list.add(PNAME + " / Owner's name: " + CFNAME
								+ " " + CLNAME);
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return return_list;
	}

	public ArrayList<String> getIDList(int IDorder) {
		// TODO Auto-generated method stub
		ORDER = ascOrDesc(IDorder % 2);
		ORDERBY = orderBy(IDorder);
		return_idlist.clear();
		SQLQUERY = "SELECT " + KEY_ROWID + " FROM " + DATABASE_TABLE
				+ " ORDER BY " + ORDERBY + " COLLATE NOCASE " + ORDER;
		try {
			open();
			cur = mDatabase.rawQuery(SQLQUERY, null);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						ID = cur.getString(cur.getColumnIndex(KEY_ROWID));
						return_idlist.add(ID);
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return return_idlist;
	}

	public String getClientFirstName(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						CFNAME = cur.getString(cur.getColumnIndex(KEY_CFNAME));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return CFNAME;
	}

	public String getClientLastName(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						CLNAME = cur.getString(cur.getColumnIndex(KEY_CLNAME));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return CLNAME;
	}

	public String getClientAddress(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						CADDRESS = cur.getString(cur
								.getColumnIndex(KEY_CADDRESS));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return CADDRESS;
	}

	public String getClientContact(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						CCONTACT = cur.getString(cur
								.getColumnIndex(KEY_CCONTACT));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return CCONTACT;
	}

	public String getClientOccupation(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						COCCUPATION = cur.getString(cur
								.getColumnIndex(KEY_COCCUPATION));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return COCCUPATION;
	}

	public String getPetName(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						PNAME = cur.getString(cur.getColumnIndex(KEY_PNAME));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return PNAME;
	}

	public String getPetBreed(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						PBREED = cur.getString(cur.getColumnIndex(KEY_PBREED));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return PBREED;
	}

	public String getPetColor(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						PCOLOR = cur.getString(cur.getColumnIndex(KEY_PCOLOR));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return PCOLOR;
	}

	public String getPetBirthday(String SELECTED_ROW_ID) {
		try {
			open();
			setCursor(SELECTED_ROW_ID);
			if (cur != null) {
				if (cur.moveToFirst()) {
					do {
						PBIRTHDAY = cur.getString(cur
								.getColumnIndex(KEY_PBIRTHDAY));
					} while (cur.moveToNext());
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		} finally {
			closeDatabase(mDatabase);
			closeCursor(cur);
		}
		return PBIRTHDAY;
	}

	public void closeAll() {
		if (cur != null && !cur.isClosed()) {
			cur.close();
		}
		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
		}
		if (mHelper != null) {
			mHelper.close();
		}
	}

	private String ascOrDesc(int order) {
		// TODO Auto-generated method stub
		if (order == 0) {
			ORDER = "ASC";
		} else {
			ORDER = "DESC";
		}
		return ORDER;
	}

	private void setCursor(String sELECTED_ROW_ID) {
		// TODO Auto-generated method stub
		cur = mDatabase.rawQuery("SELECT * FROM clientTable WHERE _id = ?",
				new String[] { sELECTED_ROW_ID });
	}

	private String orderBy(int IDorder) {
		// TODO Auto-generated method stub
		switch (IDorder) {
		case 0:
			ORDERBY = KEY_PNAME;
			break;
		case 1:
			ORDERBY = KEY_PNAME;
			break;
		case 2:
			ORDERBY = KEY_CLNAME;
			break;
		case 3:
			ORDERBY = KEY_CLNAME;
			break;
		case 4:
			ORDERBY = KEY_CFNAME;
			break;
		case 5:
			ORDERBY = KEY_CFNAME;
			break;
		}
		return ORDERBY;
	}

	private void closeCursor(Cursor c) {
		if (c != null && !c.isClosed()) {
			c.close();
		}
	}

	private void closeDatabase(SQLiteDatabase mDatabase) {
		// TODO Auto-generated method stub
		if (mDatabase != null && mDatabase.isOpen()) {
			mDatabase.close();
		}
	}
}
