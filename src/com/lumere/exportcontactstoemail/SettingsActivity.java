package com.lumere.exportcontactstoemail;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class SettingsActivity extends Activity {
	protected void onCreate(Bundle savedInstaceState) {
		super.onCreate(savedInstaceState);

		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(android.R.id.content, new SettingsFragment());
		ft.commit();

	}
}
