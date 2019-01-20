package nmo.integration.webui;

import java.io.File;
import org.eclipse.jetty.http.HttpGenerator;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.SecurityHandler;
import org.eclipse.jetty.security.authentication.BasicAuthenticator;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.StatisticsHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import nmo.Main;
import nmo.PlatformData;
import nmo.config.NMOConfiguration;

public class WebServer
{
	/** Jetty server */
	static Server SERVER;

	public static void initialize() throws Exception
	{
		//=================================
		// Start embedded Jetty server for napcharts
		//=================================
		SERVER = new Server();
		ServerConnector httpConnector = new ServerConnector(SERVER);
		httpConnector.setPort(NMOConfiguration.INSTANCE.integrations.webUI.jettyPort);
		httpConnector.setName("Main");
		SERVER.addConnector(httpConnector);
		HandlerCollection handlerCollection = new HandlerCollection();
		StatisticsHandler statsHandler = new StatisticsHandler();
		statsHandler.setHandler(handlerCollection);
		SERVER.setStopTimeout(5000);
		SERVER.setHandler(statsHandler);
		ServletContextHandler contextHandler = new ServletContextHandler();
		contextHandler.setSecurityHandler(basicAuth("NoMoreOversleeps"));
		contextHandler.setContextPath("/");
		WebSocketServlet websocketServlet = new WebSocketServlet()
		{
			private static final long serialVersionUID = -4394403163936790144L;

			@Override
			public void configure(WebSocketServletFactory factory)
			{
				factory.register(WebcamWebSocketHandler.class);
			}
		};
		ServletHolder webcamServletHolder = new ServletHolder("swc", websocketServlet);
		contextHandler.addServlet(webcamServletHolder, "/swc");
		ServletHolder napchartServlet = new ServletHolder("default", new WebServlet());
		contextHandler.addServlet(napchartServlet, "/*");
		handlerCollection.addHandler(contextHandler);
		NCSARequestLog requestLog = new NCSARequestLog(new File(PlatformData.installationDirectory, "logs/requestlog-yyyy_mm_dd.request.log").getAbsolutePath());
		requestLog.setAppend(true);
		requestLog.setExtended(false);
		requestLog.setLogTimeZone("GMT");
		requestLog.setLogLatency(true);
		requestLog.setRetainDays(90);
		requestLog.setLogServer(true);
		requestLog.setPreferProxiedForAddress(true);
		SERVER.setRequestLog(requestLog);
		SERVER.start();
		HttpGenerator.setJettyVersion("NoMoreOversleeps/" + Main.VERSION);
	}

	private static final SecurityHandler basicAuth(String realm)
	{
		HashLoginService l = new HashLoginService();
		l.setConfig(new File(PlatformData.installationDirectory, "webusers.properties").getAbsolutePath());
		l.setName(realm);
		Constraint constraint = new Constraint();
		constraint.setName(Constraint.__BASIC_AUTH);
		constraint.setRoles(new String[] { "user" });
		constraint.setAuthenticate(true);
		ConstraintMapping cm = new ConstraintMapping();
		cm.setConstraint(constraint);
		cm.setPathSpec("/ui/*");
		ConstraintSecurityHandler csh = new ConstraintSecurityHandler();
		csh.setAuthenticator(new BasicAuthenticator());
		csh.setRealmName("myrealm");
		csh.addConstraintMapping(cm);
		csh.setLoginService(l);
		return csh;
	}

	public static void shutdown() throws Exception
	{
		if (SERVER != null)
		{
			SERVER.stop();
		}
	}
}
