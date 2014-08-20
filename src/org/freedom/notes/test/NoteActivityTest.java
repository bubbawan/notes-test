package org.freedom.notes.test;

import org.freedom.notes.NoteActivity;
import org.freedom.notes.R;
import org.freedom.notes.model.NotesManagerSingleton;

import android.test.ActivityUnitTestCase;
import android.widget.EditText;

public class NoteActivityTest extends ActivityUnitTestCase<NoteActivity> {

	public NoteActivityTest() {
		super(NoteActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		NotesManagerSingleton.init(getInstrumentation().getTargetContext());
		NotesManagerSingleton.instance().deleteAll();
	}

	public void testNoteForm() {
		Util.createNotes(5);

		startActivity(NoteActivity.INTENT_EDIT(getInstrumentation()
				.getTargetContext(), 2), null, null);
		getInstrumentation().waitForIdleSync();

		EditText title = (EditText) getActivity().findViewById(
				R.id.note_txt_title);
		EditText note = (EditText) getActivity().findViewById(
				R.id.note_txt_note);

		assertNotNull(title);
		assertNotNull(note);
		assertEquals("1-title", title.getText().toString());
		assertEquals("1-note", note.getText().toString());

	}

}
