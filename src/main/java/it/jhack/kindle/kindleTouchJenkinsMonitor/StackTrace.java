package it.jhack.kindle.kindleTouchJenkinsMonitor;

import java.awt.Container;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class StackTrace {
	
	public static String get(final Throwable t) {
		final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		final PrintWriter w = new PrintWriter(bytes);
		t.printStackTrace(w);
		w.close();
		return new String(bytes.toByteArray());
	}
	
	public static void showStacktrace(final Container c, final Throwable e) {
		final String error = StackTrace.get(e);
		c.add(new JScrollPane(new JTextArea(error)));
	}
}
