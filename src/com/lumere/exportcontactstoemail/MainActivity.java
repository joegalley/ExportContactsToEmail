package com.lumere.exportcontactstoemail;

import android.content.ContentResolver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

public class MainActivity extends ActionBarActivity {
	DownloadContacts_Fragment dc_fragment;
	ProgressBar dc_progress_bar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		this.dc_progress_bar = (ProgressBar) this
				.findViewById(R.id.contacts_progress);

		// activity starting for first time
		if (savedInstanceState == null) {
			// create fragment to hold progress bar
			ContentResolver cr = getContentResolver();
			this.dc_fragment = new DownloadContacts_Fragment(cr);
			this.getSupportFragmentManager().beginTransaction()
					.add(dc_fragment, "DownloadContactsFragment").commit();

		} else {
			// activity is started, so retrieve fragment
			dc_fragment = (DownloadContacts_Fragment) this
					.getSupportFragmentManager().findFragmentByTag(
							"DownloadContactsFragment");
		}
		if (dc_fragment != null) {
			// download contacts task is currently running
			if (dc_fragment.dc_task != null
					&& dc_fragment.dc_task.getStatus() == AsyncTask.Status.RUNNING) {
				// show progress
				this.dc_progress_bar.setVisibility(View.VISIBLE);
			} else {
				// task not running, so hide progress
				this.dc_progress_bar.setVisibility(View.GONE);
			}
		}
	}

	public void downloadContacts(View view) {
		this.dc_fragment.startTask();
	}

	public void showProgressBar() {
		if (this.dc_fragment.dc_task != null) {
			// show the progress bar if it's not currently visible and the
			// progress isn't 100%
			if (this.dc_progress_bar.getVisibility() != View.VISIBLE
					&& this.dc_progress_bar.getProgress() != this.dc_progress_bar
							.getMax()) {
				this.dc_progress_bar.setVisibility(View.VISIBLE);
			}
		}
	}

	public void hideProgressBar() {
		if (this.dc_progress_bar.getVisibility() == View.VISIBLE) {
			this.dc_progress_bar.setVisibility(View.GONE);
		}
	}

	public void updateProgress(int progress) {
		this.dc_progress_bar.setProgress(progress);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
