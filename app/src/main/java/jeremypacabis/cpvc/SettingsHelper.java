package jeremypacabis.cpvc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsHelper {

	SharedPreferences mSharedPrefs;
	SharedPreferences.Editor mEditor;
	Context mContext;

	public SettingsHelper(Context c) {
		mContext = c;
	}

	public void saveStringSettings(String key, String value) {
		initializemSharedPrefs();
		mEditor = mSharedPrefs.edit();
		mEditor.putString(key, value);
		mEditor.commit();
	}

	public void saveBooleanSettings(String key, Boolean value) {
		initializemSharedPrefs();
		mEditor = mSharedPrefs.edit();
		mEditor.putBoolean(key, value);
		mEditor.commit();
	}

	public void saveIntSettings(String key, int value) {
		initializemSharedPrefs();
		mEditor = mSharedPrefs.edit();
		mEditor.putInt(key, value);
		mEditor.commit();
	}

	public String retrieveStringSettings(String key) {
		initializemSharedPrefs();
		return mSharedPrefs.getString(key, "0");
	}

	public boolean retrieveBooleanSettings(String key) {
		initializemSharedPrefs();
		return mSharedPrefs.getBoolean(key, true);
	}

	public int retrieveIntSettings(String key) {
		initializemSharedPrefs();
		return mSharedPrefs.getInt(key, 0);
	}

	private void initializemSharedPrefs() {
		// TODO Auto-generated method stub
		mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
	}
}
