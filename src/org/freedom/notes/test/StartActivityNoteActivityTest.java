package org.freedom.notes.test;

import org.freedom.notes.NoteActivity;
import org.freedom.notes.R;
import org.freedom.notes.StartActivity;
import org.freedom.notes.model.Note;
import org.freedom.notes.model.NotesManagerSingleton;

import android.app.Instrumentation.ActivityMonitor;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.robotium.solo.Solo;

public class StartActivityNoteActivityTest extends
		ActivityInstrumentationTestCase2<StartActivity> {

	private ListView noteList;
	private Solo solo;

	public StartActivityNoteActivityTest() {
		super(StartActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		NotesManagerSingleton.init(getInstrumentation().getTargetContext());
		NotesManagerSingleton.instance().deleteAll();
		Util.createNotes(5);
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}

	@Override
	protected void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}

	public void testActionModeOnLongClick() {
		noteList = (ListView) getActivity().findViewById(R.id.list_notes);
		solo.clickLongInList(2);
		getInstrumentation().waitForIdleSync();
		final View delete = solo.getView(R.id.context_delete);
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				delete.performClick();

			}
		});
		getInstrumentation().waitForIdleSync();
		assertTrue(noteList.getCount() == 4);
	}

	public void testEditNote() {
		noteList = (ListView) getActivity().findViewById(R.id.list_notes);

		ActivityMonitor monitor = getInstrumentation().addMonitor(
				NoteActivity.class.getName(), null, false);

		solo.clickInList(2);

		NoteActivity noteActivity = (NoteActivity) monitor
				.waitForActivityWithTimeout(2000);
		assertNotNull(noteActivity);

		getInstrumentation().waitForIdleSync();

		Solo soloNote = new Solo(getInstrumentation(), noteActivity);
		EditText title = (EditText) soloNote.getView(R.id.note_txt_title);
		EditText noteTxt = (EditText) soloNote.getView(R.id.note_txt_note);
		soloNote.clearEditText(title);
		soloNote.enterText(title, "FOO");

		soloNote.clearEditText(noteTxt);
		soloNote.enterText(noteTxt, "BAR");

		final View saveAction = soloNote.getView(R.id.note_save);
		noteActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				saveAction.performClick();

			}
		});
		getInstrumentation().waitForIdleSync();

		Note note = (Note) noteList.getItemAtPosition(1);
		assertEquals("FOO", note.getTitle());
		assertEquals("BAR", note.getNote());

	}

	public void testCancelEditNote() {
		noteList = (ListView) getActivity().findViewById(R.id.list_notes);

		ActivityMonitor monitor = getInstrumentation().addMonitor(
				NoteActivity.class.getName(), null, false);

		solo.clickInList(2);

		NoteActivity noteActivity = (NoteActivity) monitor
				.waitForActivityWithTimeout(2000);
		assertNotNull(noteActivity);

		getInstrumentation().waitForIdleSync();

		Solo soloNote = new Solo(getInstrumentation(), noteActivity);
		EditText title = (EditText) soloNote.getView(R.id.note_txt_title);
		EditText noteTxt = (EditText) soloNote.getView(R.id.note_txt_note);
		soloNote.clearEditText(title);
		soloNote.enterText(title, "FOO");

		soloNote.clearEditText(noteTxt);
		soloNote.enterText(noteTxt, "BAR");

		final View cancelAction = soloNote.getView(R.id.note_cancel);
		noteActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				cancelAction.performClick();

			}
		});
		getInstrumentation().waitForIdleSync();

		Note note = (Note) noteList.getItemAtPosition(1);
		assertEquals("1-title", note.getTitle());
		assertEquals("1-note", note.getNote());

	}

}
