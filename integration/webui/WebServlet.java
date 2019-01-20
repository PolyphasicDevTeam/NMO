package nmo.integration.webui;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import com.google.gson.annotations.Expose;
import freemarker.core.ParseException;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.TemplateException;
import freemarker.template.TemplateNotFoundException;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.FishEyeGimpyRenderer;
import nl.captcha.servlet.CaptchaServletUtil;
import nmo.Action;
import nmo.ActivitySource;
import nmo.CommonUtils;
import nmo.Integration;
import nmo.Main;
import nmo.MainDialog;
import nmo.PlatformData;
import nmo.SleepEntry;
import nmo.config.NMOConfiguration;
import nmo.config.NMOStatistics;
import nmo.config.NMOUsers;
import nmo.integration.noise.IntegrationNoise;
import nmo.integration.philipshue.IntegrationPhilipsHue;
import nmo.integration.tplink.IntegrationTPLink;
import nmo.integration.tplink.TPLinkDeviceEntry;
import nmo.integration.wemo.IntegrationWemo;
import nmo.integration.wemo.WemoDeviceEntry;
import nmo.utils.FormattingHelper;

public class WebServlet extends HttpServlet
{
	public static class JsonData
	{
		@Expose
		public String update;

		@Expose
		public String active_timer;

		@Expose
		public String activity;

		@Expose
		public String schedule_name;

		@Expose
		public String schedule;

		@Expose
		public String pause_state;

		@Expose
		public String conn_count;

		@Expose
		public String noise_state;

		@Expose
		public String ha_state;

		@Expose
		public String zombie_state;
	}

	private static final long serialVersionUID = 6713485873809808119L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		this.loadUserIDFromCookies(request, response);

		String PATH = request.getPathInfo();
		if (PATH.equals("/favicon.ico"))
		{
			this.sendFavicon(response);
		}
		else if (PATH.equals("/ui/log"))
		{
			this.sendLog(response);
		}
		else if (PATH.equals("/ui/json"))
		{
			this.sendJsonState(response);
		}
		else if (PATH.equals("/ui/"))
		{
			this.sendMainPage(response);
		}
		else if (PATH.equals("/validate-captcha"))
		{
			this.sendCaptchaValidation(request, response);
		}
		else if (PATH.equals("/captcha.png"))
		{
			this.getCaptchaImage(request, response);
		}
		else if (PATH.equals("/"))
		{
			response.sendRedirect("/ui/");
		}
		else
		{
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void getCaptchaImage(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String seed = request.getParameter("seed");
		if (seed == null || seed.isEmpty())
		{
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		int seed_;
		try
		{
			seed_ = Integer.parseInt(seed);
		}
		catch (NumberFormatException i)
		{
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		PredictableCaptchaTextProducer producer = new PredictableCaptchaTextProducer(seed_);
		final Captcha captcha = new Captcha.Builder(160, 50).addText(producer).addBackground(new GradiatedBackgroundProducer()).addNoise().gimp(new FishEyeGimpyRenderer()).addBorder().build();
		CaptchaServletUtil.writeImage(response, captcha.getImage());
	}

	private void sendCaptchaValidation(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String seed = request.getParameter("seed");
		String captcha = request.getParameter("captcha");
		if (seed == null || captcha == null || seed.isEmpty() || captcha.isEmpty())
		{
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		int seed_;
		try
		{
			seed_ = Integer.parseInt(seed);
		}
		catch (NumberFormatException i)
		{
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
		PredictableCaptchaTextProducer producer = new PredictableCaptchaTextProducer(seed_);
		String expectedCaptcha = producer.getText();
		if (expectedCaptcha.equals(captcha))
		{
			response.sendError(HttpServletResponse.SC_OK);
			return;
		}
		else
		{
			response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
			return;
		}
	}

	private void sendFavicon(HttpServletResponse response) throws IOException
	{
		InputStream fis = null;
		OutputStream out = null;
		try
		{
			response.setContentType("image/x-icon");
			fis = WebServlet.class.getResourceAsStream("/resources/icon.ico");
			out = response.getOutputStream();
			IOUtils.copy(fis, out);
		}
		finally
		{
			IOUtils.closeQuietly(out);
			IOUtils.closeQuietly(fis);
		}
		return;
	}

	/**
	 * Sends the log in a text-based format.
	 * @param response
	 * @throws IOException
	 */
	private void sendLog(HttpServletResponse response) throws IOException
	{
		int x = 0;
		StringWriter writer = new StringWriter();
		writer.write("log updated " + CommonUtils.convertTimestamp(System.currentTimeMillis()) + "\n\n");
		ListIterator<String> logged_events = MainDialog.events.listIterator(MainDialog.events.size());
		while (logged_events.hasPrevious())
		{
			++x;
			String s = logged_events.previous();
			writer.write(s + "\n");
			if (x == 20)
				break;
		}
		response.getWriter().append(writer.toString());
	}

	/**
	 * Sends the main HTML page of the WebUI.
	 * 
	 * @param response
	 * @throws TemplateNotFoundException
	 * @throws MalformedTemplateNameException
	 * @throws ParseException
	 * @throws IOException
	 * @throws ServletException
	 */
	private void sendMainPage(HttpServletResponse response) throws TemplateNotFoundException, MalformedTemplateNameException, ParseException, IOException, ServletException
	{
		HashMap<String, Object> model = new HashMap<>();
		model.put("version", Main.VERSION);
		model.put("system", PlatformData.computerName);
		model.put("actionButtons", this.determineWebUIButtons());
		model.put("webcamKey", NMOConfiguration.INSTANCE.integrations.webUI.webcamSecurityKey);
		model.put("camTotal", WebcamCapture.webcams.length);
		model.put("message", NMOConfiguration.INSTANCE.integrations.webUI.message);
		model.put("username", NMOConfiguration.INSTANCE.integrations.webUI.username);
		if (!NMOConfiguration.INSTANCE.integrations.webUI.cameraProxyAddress.isEmpty())
		{
			model.put("webcamURL", NMOConfiguration.INSTANCE.integrations.webUI.cameraProxyAddress);
		}
		else
		{
			model.put("webcamURL", "\" + socket_security + \"://\" + window.location.hostname + \":\" + window.location.port + \"/swc");
		}
		String[] cc = new String[WebcamCapture.webcams.length];
		for (int i = 0; i < WebcamCapture.webcams.length; i++)
		{
			cc[i] = WebcamCapture.webcams[i].cc;
		}
		model.put("webcams", cc);
		for (Integration integration : Main.integrations)
		{
			model.put("integration_" + integration.id, integration.isEnabled());
		}
		try
		{
			WebTemplate.renderTemplate("nmo.ftl", response, model);
		}
		catch (TemplateException e)
		{
			throw new ServletException(e);
		}
	}

	/**
	 * Creates the HTML representing the list of buttons that users of the WebUI can interact with
	 * to perform actions in NMO.
	 * 
	 * @return A snippet of HTML representing the list of buttons.
	 */
	private String determineWebUIButtons()
	{
		String actionButtons = "";
		String[] colours = { "danger", "nmo-orange", "nmo-gold", "success", "info", "primary", "nmo-purp" };
		int colour = -1;
		for (Integration integration : Main.integrations)
		{
			LinkedHashMap<String, Action> actions = integration.getActions();
			// secret action fix
			int actionsNotSecret = 0;
			for (Action action : actions.values())
			{
				if (!action.isHiddenFromWebUI())
				{
					actionsNotSecret++;
				}
			}
			if (actionsNotSecret > 0)
			{
				++colour;
				if (colour >= colours.length)
					colour = 0;
			}
			for (String action_key : actions.keySet())
			{
				Action action = actions.get(action_key);
				if (!action.isHiddenFromWebUI())
				{
					actionButtons += this.buttonFormHTML(colours[colour], action_key, action.getName(), action.getDescription());
				}
			}
		}
		return actionButtons;
	}

	/**
	 * Sends a short JSON-response containing high-level status information about how NMO is running.
	 * @param response
	 * @throws IOException
	 */
	private void sendJsonState(HttpServletResponse response) throws IOException
	{
		JsonData data = new JsonData();
		long now = System.currentTimeMillis();
		boolean isPaused = MainDialog.isCurrentlyPaused.get();
		data.update = CommonUtils.convertTimestamp(now);
		ActivitySource lastSource = MainDialog.lastActivitySourceObject;
		data.activity = isPaused ? "Disabled while paused" : CommonUtils.convertTimestamp(lastSource.time) + " (" + String.format("%.3f", (now - lastSource.time) / 1000.0) + "s ago from " + lastSource.type + ")";
		data.active_timer = MainDialog.timer == null ? "null" : MainDialog.timer.name + " (" + MainDialog.timer.secondsForFirstWarning + "s/" + MainDialog.timer.secondsForSubsequentWarnings + "s)";
		data.active_timer += "<br/>with oversleep warning on warning #" + NMOConfiguration.INSTANCE.oversleepWarningThreshold;
		if (isPaused)
		{
			data.pause_state = "PAUSED for \"" + MainDialog.pauseReason + "\" until " + CommonUtils.dateFormatter.get().format(MainDialog.pausedUntil);
		}
		else
		{
			data.pause_state = "RUNNING";
		}
		data.conn_count = "";
		for (WebcamData wd : WebcamCapture.webcams)
		{
			data.conn_count += (data.conn_count.isEmpty() ? "" : "<br/>") + "<b>" + wd.cc + "</b>: " + wd.count();
		}
		data.noise_state = IntegrationNoise.INSTANCE.getNoiseList();
		String state = "";
		if (IntegrationPhilipsHue.INSTANCE.isEnabled())
		{
			for (String key : IntegrationPhilipsHue.INSTANCE.lightStates.keySet())
			{
				state += (!state.isEmpty() ? "<br/>" : "");
				int val = IntegrationPhilipsHue.INSTANCE.lightStates.get(key);
				state += "<b>" + key + "</b>:  " + (val == -2 ? "DISCONNECTED" : val == -1 ? "OFF" : "ON (" + String.format("%,.0f", val / 2.54f) + "%)");
			}
		}
		if (IntegrationTPLink.INSTANCE.isEnabled())
		{
			for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.tplink.devices.length; i++)
			{
				TPLinkDeviceEntry tpde = NMOConfiguration.INSTANCE.integrations.tplink.devices[i];
				state += (!state.isEmpty() ? "<br/>" : "");
				state += "<b>" + tpde.name + "</b>:  " + (tpde.isSwitchedOn ? "ON" : "OFF");
			}
		}
		if (IntegrationWemo.INSTANCE.isEnabled())
		{
			for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.wemo.devices.length; i++)
			{
				WemoDeviceEntry wemodevice = NMOConfiguration.INSTANCE.integrations.wemo.devices[i];
				state += (!state.isEmpty() ? "<br/>" : "");
				state += "<b>" + wemodevice.name + "</b>:  " + (wemodevice.isSwitchedOn ? "ON" : "OFF");
			}
		}
		data.ha_state = state;
		String sn = NMOConfiguration.INSTANCE.scheduleName;
		if (sn == null || sn.isEmpty())
		{
			sn = "UNKNOWN SCHEDULE";
		}
		data.schedule_name = "<b>" + sn + "</b>";
		if (!NMOConfiguration.INSTANCE.schedule.isEmpty())
		{
			data.schedule_name += "<ul>";
		}
		for (SleepEntry se : NMOConfiguration.INSTANCE.schedule)
		{
			data.schedule_name += "<li>" + se.describeTime() + " -- " + se.name + "</li>";
		}
		if (!NMOConfiguration.INSTANCE.schedule.isEmpty())
		{
			data.schedule_name += "</ul>";
		}
		if (NMOStatistics.INSTANCE.scheduleStartedOn > 0)
		{
			if (NMOConfiguration.INSTANCE.schedule.isEmpty())
			{
				data.schedule_name += "<br/>";
			}
			data.schedule_name += "Started on " + CommonUtils.dateFormatter.get().format(NMOStatistics.INSTANCE.scheduleStartedOn) + " &nbsp; (" + FormattingHelper.formatTimeElapsedWithDays(now, NMOStatistics.INSTANCE.scheduleStartedOn) + " ago)";

			if (NMOStatistics.INSTANCE.scheduleLastOversleep != NMOStatistics.INSTANCE.scheduleStartedOn)
			{
				data.schedule_name += "<br/>Last overslept on " + CommonUtils.dateFormatter.get().format(NMOStatistics.INSTANCE.scheduleLastOversleep) + " &nbsp; (" + FormattingHelper.formatTimeElapsedWithDays(now, NMOStatistics.INSTANCE.scheduleLastOversleep) + " ago)";
			}
		}
		data.schedule = MainDialog.scheduleStatus;
		if (MainDialog.timer.zombiePenaltyLimit == 0)
		{
			data.zombie_state = "<b>DISABLED</b>";
		}
		else
		{
			data.zombie_state = "<b>ENABLED</b> - " + MainDialog.timer.zombiePenaltyForFirstWarning + "s for first warning, " + MainDialog.timer.zombiePenaltyForOversleepWarning + "s for oversleep warning, " + MainDialog.timer.zombiePenaltyForOtherWarnings + "s for other warnings<br/>Penalty limit: " + MainDialog.timer.zombiePenaltyLimit + "s<br/>Current penalty: " + String.format("%.3f", MainDialog.zombieDetectionPenalty / 1000.0f) + "s<br/>Penalty reduction starts in " + String.format("%.3f", MainDialog.zombieDetectionPenaltyWait / 1000.0f) + "s";
		}
		response.getWriter().append(CommonUtils.GSON.toJson(data));
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String uid = this.loadUserIDFromCookies(request, response);
		String uname = uid == null ? "UNKNOWN USER" : NMOUsers.INSTANCE.users.get(uid);
		if (uname == null)
		{
			uname = uid;
		}
		try
		{
			String PATH = request.getPathInfo();
			if (PATH.startsWith("/ui/"))
			{
				for (Integration integrations : Main.integrations)
				{
					Action button = integrations.getActions().get(PATH.substring(3));
					if (button != null && !button.isBlockedFromWebUI())
					{
						String requestIP = request.getRemoteAddr();
						if (NMOConfiguration.INSTANCE.integrations.webUI.readProxyForwardingHeaders)
						{
							String xff = request.getHeader("X-Forwarded-For");
							if (xff != null && !xff.isEmpty())
							{
								requestIP = xff.split("\\Q, \\E")[0];
							}
						}
						if (MainDialog.isCurrentlyPaused.get() && MainDialog.pauseIsScheduleRelated)
						{
							MainDialog.triggerEvent("<" + button.getName() + "> from " + uname + " @ /" + requestIP + " was cancelled because a sleep block is currently scheduled", null, null);
						}
						else
						{
							button.onAction(request.getParameterMap());
							MainDialog.triggerEvent("<" + button.getName() + "> from " + uname + " @ /" + requestIP, null, null);
						}

						// When calling through AJAX, no response HTML necessary
						if (request.getParameter("ajax_form") != null)
						{
							response.setStatus(HttpServletResponse.SC_OK);
						}
						else
						{
							response.sendRedirect("/");
						}
						return;
					}
				}
			}
			response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
	}

	private String buttonFormHTML(String colour_name, String action_key, String name, String description)
	{
		String encoded_description = description.replace("&", "&amp;").replace("'", "&apos;");
		return "<form method='POST' data-js-ajax-form='true' action='/ui" + action_key + "'>" + "<button type='submit' class='btn btn-" + colour_name + " nmo-action-button' title='" + encoded_description + "'>" + name + "</button></form>";
	}

	private String loadUserIDFromCookies(HttpServletRequest request, HttpServletResponse response)
	{
		HashMap<String, String> cookieMap = new HashMap<>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				// we only load the first instance of each cookie. this *should* be the latest one that was set.
				// therefore, if there are duplicate cookies for some reason, we should always get the newest one.
				cookieMap.putIfAbsent(cookie.getName(), cookie.getValue());
			}
		}
		String userID = cookieMap.get("_NMO_user_id");
		if (userID == null || userID.isEmpty() || this.isInvalidUUID(userID))
		{
			// set a new user ID
			userID = UUID.randomUUID().toString();
			Cookie cookie = new Cookie("_NMO_user_id", userID);
			System.out.println(request.getRequestURL().toString());
			if (request.getRequestURL().toString().contains(".nmo.polyphasic.net"))
			{
				cookie.setDomain(".nmo.polyphasic.net"); // apply the same UUID to all NMO domains
			}
			cookie.setPath("/");
			cookie.setMaxAge(315360000);
			response.addCookie(cookie);
		}
		return userID;
	}

	private boolean isInvalidUUID(String userID)
	{
		try
		{
			return UUID.fromString(userID) == null;
		}
		catch (IllegalArgumentException e)
		{
			return true;
		}
	}
}
