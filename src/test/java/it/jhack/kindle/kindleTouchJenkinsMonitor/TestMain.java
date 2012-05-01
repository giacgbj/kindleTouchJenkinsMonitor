package it.jhack.kindle.kindleTouchJenkinsMonitor;

import junit.framework.TestCase;

import org.junit.Assert;

public class TestMain extends TestCase {
	
	public void testGetTimeStrFromDateTimeStr() {
		final String dateTimeStr = "2012-03-19T07:36:40Z";
		final String timeStr = Main.getTimeFromDateTime(dateTimeStr);
		System.out.println(timeStr);
		Assert.assertTrue("07:36:40".equals(timeStr));
		
	}
	
	public void downloadCcXML() {
		// Reader reader;
		// // Provo prima url rete interna, poi esterna, poi dummy
		// try {
		// InputStream urlInputStream =
		// Main.getUrlInputStream(Main.JENKINS_CC_XML_URL);
		// reader = new BufferedReader(new InputStreamReader(urlInputStream));
		// System.out.println("AAA");
		// } catch (final Throwable e) {
		// System.out.println("BBB");
		// e.printStackTrace();
		// try {
		// reader = new BufferedReader(new InputStreamReader(
		// Main.getUrlInputStream("http://code.praqma.net/ci/cc.xml")));
		// System.out.println("CCC");
		//
		// } catch (final Exception g) {
		// System.out.println("DDD");
		// e.printStackTrace();
		// reader = new StringReader(Main2.xmlFallback);
		// }
		// }
		//
		// final DocumentBuilderFactory factory =
		// DocumentBuilderFactory.newInstance();
		// DocumentBuilder db;
		//
		// try {
		// db = factory.newDocumentBuilder();
		// final InputSource inStream = new InputSource();
		// inStream.setCharacterStream(reader);
		// final Document doc = db.parse(inStream);
		// reader.close();
		// final NodeList projects = doc.getElementsByTagName("Project");
		// System.out.println(projects.getLength());
		// } catch (final Exception e) {
		// e.printStackTrace();
		// final String log = "Message " + e.getMessage() + ", cause " +
		// e.getCause();
		// System.out.println(log);
		// }
		
	}
}
