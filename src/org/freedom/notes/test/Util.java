package org.freedom.notes.test;

import org.freedom.notes.model.Note;
import org.freedom.notes.model.NotesManagerSingleton;

public class Util {
	public static void createNotes(final int n) {
		for (int i = 0; i < n; i++) {
			NotesManagerSingleton.instance().addNote(
					new Note(String.valueOf(i) + "-title", String.valueOf(i)
							+ "-note"));
		}
	}
}
