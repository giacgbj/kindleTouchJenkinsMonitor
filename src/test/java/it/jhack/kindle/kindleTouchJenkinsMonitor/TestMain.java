package it.jhack.kindle.kindleTouchJenkinsMonitor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TestMain {
	
	@Test
	public void downloadCcXML() {
		Reader reader;
		// Provo prima url rete interna, poi esterna, poi dummy
		try {
			InputStream urlInputStream = Main.getUrlInputStream(Main.JENKINS_CC_XML_URL);
			reader = new BufferedReader(new InputStreamReader(urlInputStream));
			System.out.println("AAA");
		} catch (final Throwable e) {
			System.out.println("BBB");
			e.printStackTrace();
			try {
				reader = new BufferedReader(new InputStreamReader(
						Main.getUrlInputStream("http://code.praqma.net/ci/cc.xml")));
				System.out.println("CCC");
				
			} catch (final Exception g) {
				System.out.println("DDD");
				e.printStackTrace();
				reader = new StringReader(Main2.xmlFallback);
			}
		}
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		
		try {
			db = factory.newDocumentBuilder();
			final InputSource inStream = new InputSource();
			inStream.setCharacterStream(reader);
			final Document doc = db.parse(inStream);
			reader.close();
			final NodeList projects = doc.getElementsByTagName("Project");
			System.out.println(projects.getLength());
		} catch (final Exception e) {
			e.printStackTrace();
			final String log = "Message " + e.getMessage() + ", cause " + e.getCause();
			System.out.println(log);
		}
		
	}
}
