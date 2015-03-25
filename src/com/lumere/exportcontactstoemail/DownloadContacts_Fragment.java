package com.lumere.exportcontactstoemail;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DownloadContacts_Fragment extends Fragment {
	DownloadContacts_Task dc_task;
	private Activity activity;
	private ContentResolver contacts_content_resolver;

	public DownloadContacts_Fragment(ContentResolver cr) {
		this.contacts_content_resolver = cr;
	}

	public void startTask(Context context) {
		this.dc_task = new DownloadContacts_Task(activity,
				this.contacts_content_resolver, context);
		this.dc_task.execute();

	}

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;

		if (this.dc_task != null) {
			this.dc_task.onAttach(activity);
		}

		Log.d("DownloadContacts_Fragment", "onAttach()");
	}

	public void onDetach() {
		super.onDetach();
		if (this.dc_task != null) {
			this.dc_task.onDetach();
		}
		Log.d("DownloadContacts_Fragment", "onDettach()");
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("DownloadContacts_Fragment", "onCreate()");

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return null;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		Log.d("DownloadContacts_Fragment", "onActivityCreated()");
	}

	public void onStart() {
		super.onStart();
		Log.d("DownloadContacts_Fragment", "onStart()");
	}

	public void onResume() {
		super.onResume();
		Log.d("DownloadContacts_Fragment", "onResume()");
	}

	public void onPause() {
		super.onPause();
		Log.d("DownloadContacts_Fragment", "onPause()");
	}

	public void onStop() {
		super.onStop();
		Log.d("DownloadContacts_Fragment", "onStop()");
	}

	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("DownloadContacts_Fragment", "onSaveInstanceState()");
	}

	public void onViewStateRestored(Bundle savedInstanceState) {
		super.onViewStateRestored(savedInstanceState);
		Log.d("DownloadContacts_Fragment", "onViewStateRestored()");
	}

	public void onDestroy() {
		super.onDestroy();
		Log.d("DownloadContacts_Fragment", "onDestroy()");
	}

}
