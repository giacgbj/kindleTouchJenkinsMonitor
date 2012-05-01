package it.jhack.kindle.kindleTouchJenkinsMonitor;

import ixtab.jailbreak.Jailbreak;
import ixtab.jailbreak.SuicidalKindlet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.amazon.kindle.kindlet.KindletContext;
import com.amazon.kindle.kindlet.net.Connectivity;
import com.amazon.kindle.kindlet.net.ConnectivityHandler;
import com.amazon.kindle.kindlet.net.NetworkDisabledDetails;

public class Main3 extends SuicidalKindlet {
	private static boolean stopped = false;
	private boolean isFirstStart = true;
	// private static long startTime = System.currentTimeMillis();
	private static Logger L = Logger.getLogger(Main3.class);
	
	static String JENKINS_CC_XML_URL = "http://codebuilder.unimatica.lan:8080/cc.xml";
	private KindletContext ctx;
	
	static String xmlFallback = "<Projects>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jAlmawelcome/\" name=\"jAlmawelcome\" lastBuildLabel=\"27\" lastBuildTime=\"2012-04-29T05:01:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jFat/\" name=\"jFat\" lastBuildLabel=\"127\" lastBuildTime=\"2012-04-29T14:16:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jOrdcons/\" name=\"jOrdcons\" lastBuildLabel=\"61\" lastBuildTime=\"2012-04-29T14:01:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jPagamenti/\" name=\"jPagamenti\" lastBuildLabel=\"120\" lastBuildTime=\"2012-04-29T14:16:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniboard/\" name=\"jUniboard\" lastBuildLabel=\"126\" lastBuildTime=\"2012-04-29T14:21:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnilet/\" name=\"jUnilet\" lastBuildLabel=\"91\" lastBuildTime=\"2012-04-29T14:23:58Z\" lastBuildStatus=\"Failure\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnimCA/\" name=\"jUnimCA\" lastBuildLabel=\"55\" lastBuildTime=\"2012-04-29T14:24:50Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnimoney/\" name=\"jUnimoney\" lastBuildLabel=\"121\" lastBuildTime=\"2012-04-29T14:25:09Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnirepo/\" name=\"jUnirepo\" lastBuildLabel=\"117\" lastBuildTime=\"2012-04-29T14:30:45Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUnirepo-Matrix/\" name=\"jUnirepo-Matrix\" lastBuildLabel=\"38\" lastBuildTime=\"2012-04-29T12:16:12Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniserv/\" name=\"jUniserv\" lastBuildLabel=\"125\" lastBuildTime=\"2012-04-29T14:33:23Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			+ "<Project webUrl=\"http://codebuilder.unimatica.lan:8080/job/jUniwex/\" name=\"jUniwex\" lastBuildLabel=\"72\" lastBuildTime=\"2012-04-29T14:36:25Z\" lastBuildStatus=\"Success\" activity=\"Sleeping\"/>"
			
			+ "</Projects>";
	
	public static boolean isStopped() {
		return Main3.stopped;
	}
	
	public static void setStopped(final boolean isStopped) {
		Main3.stopped = isStopped;
	}
	
	public static void timeLog(final String msg) {
		// double timestamp = timestamp = (System.currentTimeMillis() -
		// Main.startTime) / 1000.0;
		// L.info("JJJ [" + timestamp + "]   " + msg);
		// L.info("JJJ " + msg);
	}
	
	static Container rootContainer;
	final static JTextArea jTA = new JTextArea();
	
	protected Jailbreak instantiateJailbreak() {
		return new MainHackJailbreak();
	}
	
	public void onCreate(final KindletContext context) {
		timeLog("JJJ <CREATE>");
		
		// this.ctx = context;
		this.rootContainer = this.ctx.getRootContainer();
		this.rootContainer.add(new JScrollPane(this.jTA));
		
		if (Main3.this.jailbreak.isAvailable()) {
			if (((MainHackJailbreak) Main3.this.jailbreak).requestPermissions()) {
				Main3.jTA.append("JAILBREAK OK");
			} else {
				Main3.jTA.append("JAILBREAK YEAH");
				
			}
		} else {
			Main3.jTA.append("JAILBREAK KO");
		}
		timeLog("JJJ </CREATE>");
	}
	
	public void onStart() {
		timeLog("JJJ <START>");
		
		synchronized (this) {
			timeLog("JJJ <START_SYNC>");
			
			Main3.setStopped(false);
			super.start();
			
			if (this.isFirstStart) {
				
				timeLog("JJJ <START_INITIAL>");
				
				if (!Main3.isStopped()) {
					final Runnable runnable = new Runnable() {
						public void run() {
							Main3.this.initialStart();
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
	
	BufferedReader getBufferedReader(final String urlStr) {
		
		BufferedReader rd = null;
		final HttpClient client = new HttpClient();
		final GetMethod get = new GetMethod(urlStr);
		
		get.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, "10000");
		client.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
		this.jTA.append("\nCONNESSIONE A [" + urlStr + "]");
		
		try {
			this.jTA.append("\nPRIMA ANCORA");
			
			final int statusCode = client.executeMethod(get);
			
			if (statusCode != HttpStatus.SC_OK) {
				this.jTA.append("Method failed: " + get.getStatusLine());
			}
			this.jTA.append("\nPRIMA");
			
			final InputStream responseIS = get.getResponseBodyAsStream();
			
			this.jTA.append("\nRESPONSE: ");
			
			rd = null;
			// final InputStream responseIS = get.getResponseBodyAsStream();
			//
			// rd = new BufferedReader(new InputStreamReader(responseIS));
			
		} catch (final HttpException e) {
			this.jTA.append("HttpException: " + StackTrace.get(e));
		} catch (final IOException e) {
			this.jTA.append("IOException: " + StackTrace.get(e));
		} catch (final Throwable e) {
			this.jTA.append("Throwable: " + StackTrace.get(e));
		} finally {
			this.jTA.append("Finally");
			get.releaseConnection();
		}
		
		return rd;
		
	}
	
	Reader reader;
	
	private void initialStart() {
		this.ctx.setSubTitle("Sponsored by Jhack");
		
		createMenu();
		
		this.rootContainer.setLayout(new BorderLayout());
		this.rootContainer.requestFocus();
		this.rootContainer.add(new JScrollPane(this.jTA));
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		final DocumentBuilder db;
		
		try {
			
			final Connectivity connectivity = this.ctx.getConnectivity();
			// connectivity.requestConnectivity(false);
			
			this.jTA.append("Rete: " + (connectivity.isConnected() ? "SU\n" : "GIU'\n"));
			connectivity.submitSingleAttemptConnectivityRequest(new ConnectivityHandler() {
				
				public void disabled(final NetworkDisabledDetails nDD) throws InterruptedException {
					Main3.this.jTA.append("Network not working: " + nDD.getReason());
					
				}
				
				public void connected() throws InterruptedException {
					if (true) {
						try {
							Main3.this.reader = getBufferedReader("http://code.praqma.net/ci/cc.xml");
						} catch (final Throwable e) {
							Main3.this.jTA.append("exception: " + StackTrace.get(e));
							try {
								Main3.this.reader = getBufferedReader("http://code.praqma.net/ci/cc.xml");
							} catch (final Throwable f) {
								Main3.this.jTA.append("exception: " + StackTrace.get(e));
								Main3.this.reader = new StringReader(Main3.xmlFallback);
							}
						}
					} else {
						Main3.this.reader = new StringReader(Main3.xmlFallback);
						
					}
				}
			}, false);
			
		} catch (final Throwable e) {
			Main3.this.reader = new StringReader(Main3.xmlFallback);
		}
		
		if (false) {
			try {
				db = factory.newDocumentBuilder();
				final InputSource inStream = new InputSource();
				inStream.setCharacterStream(this.reader);
				final Document doc = db.parse(inStream);
				this.reader.close();
				final NodeList projects = doc.getElementsByTagName("Project");
				processProjects(projects);
			} catch (final Exception e) {
				StackTrace.showStacktrace(this.rootContainer, e);
			}
		} else {
			Main3.this.jTA.append("\nRiattiva parsing");
		}
		this.isFirstStart = false;
	}
	
	public void onStop() {
		timeLog("JJJ <STOP>");
		this.jTA.append("STOP");
		
		Main3.setStopped(true);
		
		synchronized (this) {
			timeLog("JJJ <STOP_SYNC>");
			
			// ...
		}
		
		timeLog("JJJ </STOP>");
	}
	
	public void onDestroy() {
		timeLog("JJJ </DESTROY>");
		this.jTA.append("DESTROY");
		
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
		
		final String[] columnNamesArr = { "Name", "Build #", "Build Time", "Build Status", "Activity" };
		final JTable table = new JTable(rowDataArr, columnNamesArr);
		// ColumnsAutoSizer.sizeColumnsToFit(table);
		// table.getModel().addTableModelListener(new TableModelListener() {
		//
		// public void tableChanged(final TableModelEvent e) {
		//
		// ColumnsAutoSizer.sizeColumnsToFit(table);
		//
		// }
		//
		// });
		
		final JScrollPane scrollPane = new JScrollPane(table);
		this.rootContainer.add(scrollPane);
	}
	
}
