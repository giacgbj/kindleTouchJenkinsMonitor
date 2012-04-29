package it.jhack.kindle.kindleTouchJenkinsMonitor;

import java.awt.Container;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.amazon.kindle.kindlet.AbstractKindlet;
import com.amazon.kindle.kindlet.KindletContext;

public class Main extends AbstractKindlet {
	private static boolean stopped = false;
	private boolean isFirstStart = true;
	// private static long startTime = System.currentTimeMillis();
	private static Logger L = Logger.getLogger(Main.class);
	
	private static String JENKINS_CC_XML_URL = "http://codebuilder.unimatica.lan:8080/cc.xml";
	
	private KindletContext ctx;
	String xmlFallback = "<Projects>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jAlmawelcome/\" name=\"jAlmawelcome\" lastBuildLabel=\"27\" lastBuildTime=\"2012-04-29T05:01:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jFat/\" name=\"jFat\" lastBuildLabel=\"127\" lastBuildTime=\"2012-04-29T14:16:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jOrdcons/\" name=\"jOrdcons\" lastBuildLabel=\"61\" lastBuildTime=\"2012-04-29T14:01:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jOrdscuole/\" name=\"jOrdscuole\" lastBuildLabel=\"71\" lastBuildTime=\"2012-04-29T14:16:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jPagamenti/\" name=\"jPagamenti\" lastBuildLabel=\"120\" lastBuildTime=\"2012-04-29T14:16:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jProva/\" name=\"jProva\" lastBuildLabel=\"8\" lastBuildTime=\"2012-04-29T05:01:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniboard/\" name=\"jUniboard\" lastBuildLabel=\"126\" lastBuildTime=\"2012-04-29T14:21:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniboard-JBond/\" name=\"jUniboard-JBond\" lastBuildLabel=\"126\" lastBuildTime=\"2012-04-29T14:23:10Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnilet/\" name=\"jUnilet\" lastBuildLabel=\"91\" lastBuildTime=\"2012-04-29T14:23:58Z\" lastBuildStatus=\"Failure\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnimCA/\" name=\"jUnimCA\" lastBuildLabel=\"55\" lastBuildTime=\"2012-04-29T14:24:50Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnimoney/\" name=\"jUnimoney\" lastBuildLabel=\"121\" lastBuildTime=\"2012-04-29T14:25:09Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnimoney-Branch/\" name=\"jUnimoney-Branch\" lastBuildLabel=\"47\" lastBuildTime=\"2012-04-29T14:27:57Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnirepo/\" name=\"jUnirepo\" lastBuildLabel=\"117\" lastBuildTime=\"2012-04-29T14:30:45Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnirepo-Matrix/\" name=\"jUnirepo-Matrix\" lastBuildLabel=\"38\" lastBuildTime=\"2012-04-29T12:16:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniserv/\" name=\"jUniserv\" lastBuildLabel=\"125\" lastBuildTime=\"2012-04-29T14:33:23Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "</Projects>";
	
	public static boolean isStopped() {
		return Main.stopped;
	}
	
	public static void setStopped(final boolean isStopped) {
		Main.stopped = isStopped;
	}
	
	public static void timeLog(final String msg) {
		// double timestamp = timestamp = (System.currentTimeMillis() -
		// Main.startTime) / 1000.0;
		// L.info("JJJ [" + timestamp + "]   " + msg);
		// L.info("JJJ " + msg);
	}
	
	Container rootContainer;
	
	@Override
	public void create(final KindletContext context) {
		timeLog("JJJ <CREATE>");
		
		this.ctx = context;
		this.rootContainer = this.ctx.getRootContainer();
		
		timeLog("JJJ </CREATE>");
	}
	
	@Override
	public void start() {
		timeLog("JJJ <START>");
		
		synchronized (this) {
			timeLog("JJJ <START_SYNC>");
			
			Main.setStopped(false);
			super.start();
			
			if (this.isFirstStart) {
				
				timeLog("JJJ <START_INITIAL>");
				
				if (!Main.isStopped()) {
					
					final Runnable runnable = new Runnable() {
						public void run() {
							Main.this.initialStart();
						}
					};
					timeLog("JJJ <START_RUNNABLE>");
					EventQueue.invokeLater(runnable);
				}
			} else {
				// Ad esempio, dopo screensaver
				timeLog("JJJ <START_AFTER_PAUSE>");
				
				// Se cose fatte nello stop fanno danni
				
			}
			
		}
		
		timeLog("JJJ </START*>");
	}
	
	private void initialStart() {
		this.ctx.setSubTitle("Sponsored by Jhack");
		
		createMenu();
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		
		Reader reader;
		try {
			final URL url = new URL("http://code.praqma.net/ci/cc.xml2");
			
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			
		} catch (final Exception e) {
			reader = new StringReader(this.xmlFallback);
		}
		
		try {
			db = factory.newDocumentBuilder();
			final InputSource inStream = new InputSource();
			inStream.setCharacterStream(reader);
			final Document doc = db.parse(inStream);
			final NodeList projects = doc.getElementsByTagName("Project");
			processProjects(projects);
		} catch (final Exception e) {
			e.printStackTrace();
			final String log = "Message " + e.getMessage() + ", cause " + e.getCause();
			this.ctx.getRootContainer().add(new JTextArea(log));
		}
		
		this.isFirstStart = false;
		
	}
	
	@Override
	public void stop() {
		timeLog("JJJ <STOP>");
		
		Main.setStopped(true);
		
		synchronized (this) {
			timeLog("JJJ <STOP_SYNC>");
			
			// ...
		}
		
		timeLog("JJJ </STOP>");
	}
	
	@Override
	public void destroy() {
		timeLog("JJJ </DESTROY>");
		
		this.rootContainer.setLayout(null);
		this.rootContainer.removeAll();
		System.gc();
		
		timeLog("JJJ </DESTROY>");
	}
	
	private void createMenu() {
		// final KMenu kMenu = new KMenu();
		// kMenu.add("kMenuString");
		//
		// new K
		// this.ctx.setMenu(kMenu);
	}
	
	// <Project
	// webUrl="http://codebuilder.unimatica.lan:8080/job/Unilet/"
	// name="Unilet"
	// lastBuildLabel="91"
	// lastBuildTime="2012-04-29T14:23:58Z"
	// lastBuildStatus="Failure"
	// activity="Sleeping"/>"
	private void processProjects(final NodeList projects) {
		
		final Object[][] rowDataArr = new Object[projects.getLength()][];
		for (int i = 0; i < projects.getLength(); i++) {
			final Object[] colsArr = new Object[5]; // ATTENZIONE
			final Node project = projects.item(i);
			final NamedNodeMap attrsMap = project.getAttributes();
			final Node webUrl = attrsMap.getNamedItem("webUrl");
			final Node name = attrsMap.getNamedItem("name");
			final Node lastBuildLabel = attrsMap.getNamedItem("lastBuildLabel");
			final Node lastBuildTime = attrsMap.getNamedItem("lastBuildTime");
			final Node lastBuildStatus = attrsMap.getNamedItem("lastBuildStatus");
			final Node activity = attrsMap.getNamedItem("activity");
			
			final String webUrlStr = (webUrl != null) ? webUrl.getTextContent() : "?";
			final String nameStr = (name != null) ? name.getTextContent() : "?";
			final String lastBuildLabelStr = (lastBuildLabel != null) ? lastBuildLabel.getTextContent() : "?";
			final String lastBuildTimeStr = (lastBuildTime != null) ? lastBuildTime.getTextContent() : "?";
			final String lastBuildStatusStr = (lastBuildStatus != null) ? lastBuildStatus.getTextContent() : "?";
			final String activityStr = (activity != null) ? activity.getTextContent() : "?";
			colsArr[0] = nameStr;
			colsArr[1] = lastBuildLabelStr;
			colsArr[2] = lastBuildTimeStr;
			colsArr[3] = lastBuildStatusStr;
			colsArr[4] = activityStr;
			rowDataArr[i] = colsArr;
		}
		
		final String[] columnNamesArr = { "Name", "Last Build Label", "Last Build Time", "Last Build Status",
				"Activity" };
		final JTable table = new JTable(rowDataArr, columnNamesArr);
		
		// this.ctx.getRootContainer().add(comp)
		
		// this.rootContainer.setLayout(new BorderLayout());
		this.rootContainer.add(table);
		
		// this.ctx.getRootContainer().add(
		// new JTextArea(name + " " + lastBuildLabel + " " + lastBuildTime + " "
		// + lastBuildStatus + " "
		// + activity));
		
	}
	
}
