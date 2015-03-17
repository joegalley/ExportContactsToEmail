package com.lumere.exportcontactstoemail;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

public class DownloadContacts_Task extends AsyncTask<String, Float, Integer> {
	private ContentResolver content_resolver;
	private Activity activity;
	private File contacts_f;
	private int num_contacts_read;

	public DownloadContacts_Task(Activity activity, ContentResolver cr,
			File cts_file) {
		this.onAttach(activity);
		this.content_resolver = cr;
		this.contacts_f = cts_file;
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
	protected Integer doInBackground(String... params) {

		Cursor phone_cursor = null, email_cursor = null;

		Cursor contacts_cursor = this.content_resolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		String phoneNo, email = null, email_type = null;

		float total_contacts = contacts_cursor.getCount();
		float num_read = 0;
		if (total_contacts > 0) {
			String id, name;

			// for each contact
			while (contacts_cursor.moveToNext()) {
				id = contacts_cursor.getString(contacts_cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				name = contacts_cursor
						.getString(contacts_cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				if (Integer
						.parseInt(contacts_cursor.getString(contacts_cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

					phone_cursor = this.content_resolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);

					email_cursor = this.content_resolver.query(
							ContactsContract.CommonDataKinds.Email.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Email.CONTACT_ID
									+ " = ?", new String[] { id }, null);

					while (phone_cursor.moveToNext()) {
						phoneNo = phone_cursor
								.getString(phone_cursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

					}

					while (email_cursor.moveToNext()) {

						email = email_cursor
								.getString(email_cursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
						email_type = email_cursor
								.getString(email_cursor
										.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

					}
					num_read++;

					this.publishProgress((float) (num_read / total_contacts));

				}
			}
			phone_cursor.close();
			email_cursor.close();

		}
		return null;
	}

	protected void onProgressUpdate(Float... progress) {
		if (this.activity != null) {
			((MainActivity) activity).updateProgress(progress[0]);
		}
	}

	protected void onPostExecute(Integer... num_read) {
		if (this.activity != null) {
			((MainActivity) activity).hideProgressBar();

		}
	}
}
