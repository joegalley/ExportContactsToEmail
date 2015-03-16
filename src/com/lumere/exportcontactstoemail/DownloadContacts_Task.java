package com.lumere.exportcontactstoemail;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class DownloadContacts_Task extends AsyncTask<String, Integer, Integer> {
	private ContentResolver content_resolver;
	private Activity activity;
	private int num_contacts_read;

	public DownloadContacts_Task(Activity activity, ContentResolver cr) {
		this.onAttach(activity);
		this.content_resolver = cr;
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

		Cursor cursor = this.content_resolver.query(
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

		int total_contacts = cursor.getCount();
		int num_read = 0;
		if (total_contacts > 0) {
			String id, name;

			while (cursor.moveToNext()) {
				System.out.println("test");
				id = cursor.getString(cursor
						.getColumnIndex(ContactsContract.Contacts._ID));
				name = cursor
						.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

				if (Integer
						.parseInt(cursor.getString(cursor
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

					Cursor pCur = this.content_resolver.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);
					while (pCur.moveToNext()) {
						String phoneNo = pCur
								.getString(pCur
										.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

					}

					pCur.close();
				}
			}
			num_read++;
			try {
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.publishProgress((int) num_read / total_contacts);
		}
		return null;
	}

	protected void onProgressUpdate(Integer... progress) {
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
