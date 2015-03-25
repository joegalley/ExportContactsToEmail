package com.lumere.exportcontactstoemail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

public class DownloadContacts_Task extends AsyncTask<String, Float, File[]> {
	private ContentResolver content_resolver;
	private Activity activity;
	private File contacts_f;
	private int num_contacts_read;
	private Context context;

	public DownloadContacts_Task(Activity activity, ContentResolver cr,
			Context context) {
		this.onAttach(activity);
		this.content_resolver = cr;
		this.context = context;
	}

	public void onAttach(Activity activity) {
		this.activity = activity;
	}

	public void onDetach() {
		// nullify activity reference
		this.activity = null;
	}

	protected void onPreExecute() {
		if (this.activity != null) {
			((MainActivity) activity).showProgressBar();
		}
	}

	@Override
	protected File[] doInBackground(String... params) {

		FileOutputStream fos_txt = null;
		try {
			fos_txt = this.context.openFileOutput("contacts.txt",
					Context.MODE_PRIVATE);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Cursor phone_cursor = null, email_cursor = null;

		Cursor contacts_cursor = this.content_resolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		String phoneNo = null, email = null, email_type = null;

		float total_contacts = contacts_cursor.getCount();
		float num_read = 0;
		if (total_contacts > 0) {
			String id, name;

			// for each contact
			while (contacts_cursor.moveToNext()) {
				id = contacts_cursor.getString(contacts_cursor
						.getColumnIndex(ContactsContract.Contacts._ID));

				Log.d("Contact " + num_read, " id: " + id);
				name = contacts_cursor
						.getString(contacts_cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				Log.d("Contact " + num_read, " name: " + name);

				phone_cursor = this.content_resolver.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = ?", new String[] { id }, null);

				email_cursor = this.content_resolver.query(
						ContactsContract.CommonDataKinds.Email.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Email.CONTACT_ID
								+ " = ?", new String[] { id }, null);

				// all phone numbers for the contact
				while (phone_cursor.moveToNext()) {
					phoneNo = phone_cursor
							.getString(phone_cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					Log.d("Contact " + num_read, " phoneNo: " + phoneNo);

				}

				// all email addresses for the contact
				while (email_cursor.moveToNext()) {

					email = email_cursor
							.getString(email_cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
					email_type = email_cursor
							.getString(email_cursor
									.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

					Log.d("Contact " + num_read, " email: " + email
							+ " email type: " + email_type);

				}

				try {
					fos_txt.write(new String(name + ',' + phoneNo + ',' + email)
							.getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}

				num_read++;

				this.publishProgress((float) (num_read / total_contacts));

			}
			phone_cursor.close();
			email_cursor.close();

		}

		try {
			fos_txt.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void onProgressUpdate(Float... progress) {
		if (this.activity != null) {
			((MainActivity) activity).updateProgress(progress[0]);
		}
	}

	@Override
	protected void onPostExecute(File[] f) {
		if (this.activity != null) {
			((MainActivity) activity).hideProgressBar();

		}

		((MainActivity) activity).sendEmail();

	}
}
