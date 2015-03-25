package com.lumere.exportcontactstoemail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import android.content.ContentResolver;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	DownloadContacts_Fragment dc_fragment;
	ProgressBar dc_progress_bar;
	File contacts_file;

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
		this.dc_progress_bar.setIndeterminate(false);

	}

	public void downloadContacts(View view) throws IOException {

		this.dc_fragment.startTask(this);

		Log.e("DONE", "DONE");

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
		Log.e("adf", "adf");
	}

	public void hideProgressBar() {
		if (this.dc_progress_bar.getVisibility() == View.VISIBLE) {
			this.dc_progress_bar.setVisibility(View.GONE);
		}

	}

	public void done() {
		Toast.makeText(getApplicationContext(), "DONE", Toast.LENGTH_SHORT)
				.show();
	}

	public void updateProgress(Float progress) {
		String s = Integer.toString((int) (progress * 100));
		Log.e("sdf", s);
		this.dc_progress_bar.setProgress((int) (progress * 100));
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

	public void sendEmail() {

		FileInputStream fis;
		try {

			fis = this.getApplicationContext().openFileInput("contacts.txt");

			InputStreamReader isr = new InputStreamReader(fis);
			char[] buf = new char[1024];
			String s = "";
			int charRead;
			while ((charRead = isr.read(buf)) > 0) {
				// char to string conversion
				String readstring = String.copyValueOf(buf, 0, charRead);
				s += readstring;
				Log.e("aaa", s);

			}
			fis.close();
			isr.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e1) {

		} catch (Exception e2) {
			Log.e("SendMail", e2.getMessage(), e2);
		}

		SendEmail send_email = new SendEmail();
		try {
			send_email.send("_EMAIL", "hello", "test");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
