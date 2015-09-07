package com.lumere.exportcontactstoemail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import android.app.Activity;
import android.app.AlertDialog;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class MainActivity extends ActionBarActivity {
	DownloadContacts_Fragment dc_fragment;
	ProgressBar dc_progress_bar;
	File contacts_file;
	String usr_email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		/* view setup */

		// about button
		Button btn_about = (Button) this.findViewById(R.id.btn_about);
		btn_about.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, AboutActivity.class);
				MainActivity.this.startActivity(i);
			}
		});

		// settings button
		Button btn_settings = (Button) this.findViewById(R.id.btn_settings);
		btn_settings.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, SettingsActivity.class);
				startActivity(i);
			}
		});

		// help button
		Button btn_help = (Button) this.findViewById(R.id.btn_help);
		btn_help.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, HelpActivity.class);
				MainActivity.this.startActivity(i);
			}
		});

		// .csv selector
		final LinearLayout csv_selector = (LinearLayout) this
				.findViewById(R.id.csv_btn);

		csv_selector.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (csv_selector.isSelected()) {
					csv_selector.setSelected(false);

				} else {
					csv_selector.setSelected(true);
				}
			}
		});

		// .doc selector
		final LinearLayout doc_selector = (LinearLayout) this
				.findViewById(R.id.doc_btn);

		doc_selector.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (doc_selector.isSelected()) {
					doc_selector.setSelected(false);

				} else {
					doc_selector.setSelected(true);
				}
			}
		});

		// .pdf selector

		/*-
		final LinearLayout pdf_selector = (LinearLayout) this
				.findViewById(R.id.pdf_btn);

		pdf_selector.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (pdf_selector.isSelected()) {
					pdf_selector.setSelected(false);

				} else {
					pdf_selector.setSelected(true);
				}
			}
		});
		 */

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
		EditText usr_email = (EditText) this.findViewById(R.id.usr_email);
		String email_addr = usr_email.getText().toString();

		//
		if (email_addr == null || email_addr.equals("")) {
			new AlertDialog.Builder(this).setTitle("Invalid Email Address")
					.setMessage("Please enter your email address")
					.setPositiveButton(android.R.string.ok, null).show();
		} else {
			this.dc_fragment.startTask(this);
		}
	}

	// progress bar for DownloadContacts_Task
	public void showProgressBar() {
		if (this.dc_fragment.dc_task != null) {
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

	public void updateProgress(Float progress) {
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

		this.getApplicationContext().getFilesDir();
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

			}
			fis.close();
			isr.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		} catch (Exception e) {
			e.printStackTrace();
		}

		// send the email
		AsyncEmail email = new AsyncEmail();
		try {
			email.setToAddress("to_addr");
			email.setSubject("Your Contacts");
			email.setBody("Test body");
			// email.addFileAttachment(new File(this.getFilesDir(),
			// "contacts.csv"));
			email.addFileAttachment(new File(this.getFilesDir(), "contacts.txt"));

			email.send();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
