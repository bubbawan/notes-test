package org.freedom.notes.test;

import org.freedom.notes.NoteActivity;
import org.freedom.notes.R;
import org.freedom.notes.StartActivity;
import org.freedom.notes.model.NotesManagerSingleton;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.widget.ListView;

public class StartActivityTest extends ActivityUnitTestCase<StartActivity> {

	private Intent mLaunchIntent;

	public StartActivityTest() {
		super(StartActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		NotesManagerSingleton.init(getInstrumentation().getTargetContext());
		NotesManagerSingleton.instance().deleteAll();

		mLaunchIntent = new Intent(getInstrumentation().getTargetContext(),
				StartActivity.class);
		startActivity(mLaunchIntent, null, null);
	}

	public void testNotesList() {
		refreshActivity();

		ListView listView = (ListView) getActivity().findViewById(
				R.id.list_notes);
		View noItems = getActivity().findViewById(R.id.list_no_items);

		assertNotNull(listView);
		assertNotNull(noItems);

		assertTrue(listView.getCount() == 0);
		assertTrue(listView.getVisibility() == View.INVISIBLE);
		assertTrue(noItems.getVisibility() == View.VISIBLE);

		Util.createNotes(5);
		refreshActivity();

		assertTrue(listView.getVisibility() == View.VISIBLE);
		assertTrue(noItems.getVisibility() == View.INVISIBLE);

		assertTrue(listView.getCount() == 5);
	}

	public void testOnClickListItem() {
		Util.createNotes(5);
		refreshActivity();

		final ListView listView = (ListView) getActivity().findViewById(
				R.id.list_notes);

		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				listView.performItemClick(null, 0, listView.getAdapter()
						.getItemId(0));
			}
		});

		getInstrumentation().waitForIdleSync();
		Intent intent = getStartedActivityIntent();
		assertEquals(intent.getExtras().get(NoteActivity.ACTION_KEY),
				NoteActivity.ACTION_EDIT);
		assertEquals(intent.getExtras().get(NoteActivity.ACTION_EDIT_ID),
				String.valueOf(1));

	}

	private void refreshActivity() {
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				getActivity().refresh();
			}
		});

		getInstrumentation().waitForIdleSync();
	}

}
