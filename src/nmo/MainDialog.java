package nmo;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.FishEyeGimpyRenderer;
import nmo.config.NMOConfiguration;
import nmo.config.NMOStatistics;
import nmo.integration.cmd.IntegrationCommandLine;
import nmo.integration.discord.IntegrationDiscord;
import nmo.integration.filewriter.IntegrationFileWriter;
import nmo.integration.input.IntegrationKeyboard;
import nmo.integration.input.IntegrationMidiTransmitter;
import nmo.integration.input.IntegrationMouse;
import nmo.integration.input.IntegrationXboxController;
import nmo.integration.input.XboxControllerConfiguration.PlayerConfiguration;
import nmo.integration.iterator.IntegrationIterator;
import nmo.integration.noise.IntegrationNoise;
import nmo.integration.pavlok.IntegrationPavlok;
import nmo.integration.philipshue.IntegrationPhilipsHue;
import nmo.integration.randomizer.IntegrationRandomizer;
import nmo.integration.tplink.IntegrationTPLink;
import nmo.integration.tplink.TPLinkDeviceEntry;
import nmo.integration.twilio.IntegrationTwilio;
import nmo.integration.webui.IntegrationWebUI;
import nmo.integration.webui.PortForwarding;
import nmo.integration.webui.WebcamCapture;
import nmo.integration.webui.WebcamWebSocketHandler;
import nmo.integration.wemo.IntegrationWemo;
import nmo.integration.wemo.WemoDeviceEntry;
import nmo.utils.DesktopHelper;
import nmo.utils.FormattingHelper;
import nmo.utils.JavaFxHelper;

public class MainDialog extends Application
{
	private static final Logger log = LogWrapper.getLogger();
	public static Scene scene;

	public static final ActivitySource SYSTEM_ACTIVITY_SOURCE = new ActivitySource("system");
	public static final ActivitySource PAUSE_ACTIVITY_SOURCE = new ActivitySource("pause");
	public static final ActivitySource TIMER_ACTIVITY_SOURCE = new ActivitySource("pendingTimer");
	public static volatile ActivityTimer pendingTimer = null;
	public static volatile ActivityTimer timer = null;
	public static volatile String pauseReason = "";
	public static volatile long pausedUntil = 0;
	public static volatile boolean pauseIsScheduleRelated = false;
	public static volatile long nextActivityWarningID;
	public static volatile boolean oversleepWarningTriggered;
	public static volatile ActivitySource lastActivitySourceObject = SYSTEM_ACTIVITY_SOURCE;
	public static volatile SimpleStringProperty loginTokenValidUntilString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty webMonitoringString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty activeTimerString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty lastActivityTimeString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty timeDiffString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty webcamName = new SimpleStringProperty("");
	public static volatile SimpleStringProperty lightingStateString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty tplinkStateString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty wemoStateString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty startedString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty startedString2 = new SimpleStringProperty("");
	public static volatile SimpleStringProperty lastOversleepString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty lastOversleepString2 = new SimpleStringProperty("");
	public static volatile SimpleStringProperty personalBestString = new SimpleStringProperty("");
	public static volatile SimpleBooleanProperty isCurrentlyPaused = new SimpleBooleanProperty(false);
	public static volatile SimpleObjectProperty<Image> lastWebcamImage = new SimpleObjectProperty<>();
	public static volatile SleepEntry lastSleepBlockWarning = null;
	public static volatile ScheduleEntryType lastSleepState = null;
	public static volatile SleepEntry nextSleepBlock = null;
	public static volatile String scheduleStatus = "AWAKE";
	public static volatile String scheduleStatusShort = "AWAKE";
	public static volatile SimpleStringProperty scheduleStatusString = new SimpleStringProperty("AWAKE");
	public static volatile SimpleStringProperty scheduleNextBlockString = new SimpleStringProperty("No sleep blocks configured");
	public static volatile SimpleStringProperty scheduleCountdownString = new SimpleStringProperty("");
	public static volatile Label ultiwakerConnectivityLabel;
	public static volatile SimpleStringProperty ultiwakerConnectivityString = new SimpleStringProperty("");
	public static volatile Label zombiePenaltyEnableLabel;
	public static volatile SimpleStringProperty zombiePenaltyEnableString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty zombiePenaltyAccruedString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty zombiePenaltyAccruedString2 = new SimpleStringProperty("");
	public static volatile SimpleStringProperty zombiePenaltyWaitString = new SimpleStringProperty("");
	public static volatile SimpleStringProperty zombiePenaltyValString1 = new SimpleStringProperty("");
	public static volatile SimpleStringProperty zombiePenaltyValString2 = new SimpleStringProperty("");
	public static volatile SimpleStringProperty zombiePenaltyValString3 = new SimpleStringProperty("");
	public static volatile WritableImage writableImage = null;
	public static ObservableList<String> events = FXCollections.observableArrayList();
	public static ArrayList<CustomEvent> customEvents = new ArrayList<>();
	public static volatile long zombieDetectionPenaltyWait = 0;
	public static volatile long zombieDetectionPenalty = 0;
	public static volatile int tick = 0;
	public static volatile long now = System.currentTimeMillis();

	@Override
	public void start(Stage stage) throws Exception
	{
		log.info("JavaFX application start");
		triggerEvent("Application started", null, null);

		nextActivityWarningID = 0;
		oversleepWarningTriggered = false;

		for (SleepEntry entry : NMOConfiguration.INSTANCE.schedule)
		{
			entry.fixTimes();
		}
		Collections.sort(NMOConfiguration.INSTANCE.schedule);
		for (SleepEntry entry : NMOConfiguration.INSTANCE.schedule)
		{
			triggerEvent("Adding schedule entry: " + entry.type + ": " + entry.describe(), null, null);
			entry.updateNextTriggerTime();
		}
		for (ActivityTimer entry : NMOConfiguration.INSTANCE.timers)
		{
			triggerEvent("Adding activity timer: " + entry.name + " " + entry.secondsForFirstWarning + "s/" + entry.secondsForSubsequentWarnings + "s", null, null);
		}
		int q = 0;
		for (CustomEvent action : NMOConfiguration.INSTANCE.events.custom)
		{
			action.originalOrder = q;
			q++;
			action.updateNextTriggerTime();
			triggerEvent("Adding custom event trigger " + action.name + " triggering " + action.describe() + " and next on " + CommonUtils.convertTimestamp(action.nextTriggerTime), null, null);
			customEvents.add(action);
		}
		Collections.sort(customEvents);

		if (NMOConfiguration.INSTANCE.integrations.pavlok.enabled)
		{
			try
			{
				IntegrationPavlok.INSTANCE.vibration(255, "received a vibration from NMO");
				triggerEvent("<VIBRATE PAVLOK> Connection test", null, null);
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				NMOConfiguration.INSTANCE.integrations.pavlok.auth = null;
			}
		}

		// fix bad "last oversleep" value
		if (NMOStatistics.INSTANCE.scheduleLastOversleep < NMOStatistics.INSTANCE.scheduleStartedOn)
		{
			NMOStatistics.INSTANCE.scheduleLastOversleep = NMOStatistics.INSTANCE.scheduleStartedOn;
		}

		//==================================================================
		// CONFIGURE THE STAGE
		//==================================================================
		stage.setTitle("NoMoreOversleeps Polyphasic Sleeping Alarm v" + Main.VERSION);
		stage.getIcons().add(new Image(JavaFxHelper.buildResourcePath("icon.png")));
		stage.setResizable(true);
		stage.setMinWidth(1210);
		stage.setMaxWidth(1210);

		//==================================================================
		// CONFIGURE ANIMATION TIMER
		//==================================================================
		// this is an absurd workaround
		final AnimationTimer at = new AnimationTimer()
		{
			@Override
			public void handle(long now)
			{
				MainDialog.this.tick();
			}
		};

		//==================================================================
		// CONFIGURE THE SCENE
		//==================================================================
		final ScrollPane outerPane = new ScrollPane();
		outerPane.setId("root");
		outerPane.setFitToHeight(true);
		outerPane.setFitToWidth(true);
		scene = new Scene(outerPane, 1210, 910, Color.WHITE);
		scene.getStylesheets().add(JavaFxHelper.buildResourcePath("application.css"));

		//==================================================================
		// INTERCEPT CLOSING OF WINDOW BEHAVIOUR
		//==================================================================
		Platform.setImplicitExit(false);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>()
		{
			@Override
			public void handle(WindowEvent event)
			{
				event.consume();
				MainDialog.this.openAppCloseDialog();
			}
		});

		//==================================================================
		// BUILD THE PRIMARY PANE
		//==================================================================
		final BorderPane innerPane = new BorderPane();
		innerPane.setStyle("-fx-background-color: #222;");
		this.loadFrames(innerPane);

		//==================================================================
		// PAVLOK CRAP
		//==================================================================
		if (NMOConfiguration.INSTANCE.integrations.pavlok.enabled && NMOConfiguration.INSTANCE.integrations.pavlok.auth == null)
		{
			if (Main.CLIENT_ID == null || Main.CLIENT_SECRET == null || Main.CLIENT_CALLBACK == null)
			{
				log.error("Pavlok API id/secret/callbackURI is required in order to perform login, but this is missing");
			}
			else
			{
				final String url = "https://pavlok-mvp.herokuapp.com/oauth/authorize?client_id=" + Main.CLIENT_ID + "&redirect_uri=" + Main.CLIENT_CALLBACK + "&response_type=code";
				BorderPane authPane = new BorderPane();
				WebView browser = new WebView();
				WebEngine webEngine = browser.getEngine();
				webEngine.load(url);
				authPane.setCenter(browser);
				webEngine.setOnStatusChanged(new EventHandler<WebEvent<String>>()
				{
					boolean handledCallbackYet = false;

					@Override
					public void handle(WebEvent<String> event)
					{
						if (!this.handledCallbackYet && event.getSource() instanceof WebEngine)
						{
							WebEngine we = (WebEngine) event.getSource();
							String location = we.getLocation();
							System.out.println(location);
							if (location.equals("https://pavlok-mvp.herokuapp.com/"))
							{
								we.load(url);
							}
							else if (location.startsWith(Main.CLIENT_CALLBACK) && location.contains("?code"))
							{
								this.handledCallbackYet = true;
								System.out.println("Detected location: " + location);
								try
								{
									String[] params = location.split("\\Q?\\E")[1].split("\\Q&\\E");
									Map<String, String> map = new HashMap<>();
									for (String param : params)
									{
										String name = param.split("=")[0];
										String value = param.split("=")[1];
										map.put(name, value);
									}
									NMOConfiguration.INSTANCE.integrations.pavlok.auth = IntegrationPavlok.postAuthToken(map.get("code"));
									NMOConfiguration.save();
									IntegrationPavlok.INSTANCE.vibration(255, "Connection test");
									triggerEvent("<VIBRATE PAVLOK> Connection test", null, null);
									outerPane.setContent(innerPane);
									outerPane.requestFocus();
									outerPane.requestLayout();
									outerPane.setVvalue(0);
									at.start();
								}
								catch (Exception e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				});
				outerPane.setContent(authPane);
			}
		}
		else
		{
			outerPane.setContent(innerPane);
			at.start();
		}

		//==================================================================
		// SHOW STAGE
		//==================================================================
		stage.setScene(scene);
		stage.show();

		outerPane.requestFocus();
		outerPane.requestLayout();
		outerPane.setVvalue(0);
	}

	private void addIntegrationButtonsToVbox(Integration integration, VBox vbox)
	{
		for (String buttonKey : integration.getActions().keySet())
		{
			System.out.println("*" + buttonKey);
			final Action clickableButton = integration.getActions().get(buttonKey);
			if (clickableButton.isHiddenFromFrontend())
			{
				continue;
			}
			final Button jfxButton = new Button(clickableButton.getName());
			jfxButton.setPadding(new Insets(2, 4, 2, 4));
			jfxButton.setMinWidth(256);
			jfxButton.setMaxWidth(256);
			jfxButton.setAlignment(Pos.BASELINE_LEFT);
			jfxButton.setContentDisplay(ContentDisplay.RIGHT);
			jfxButton.setTooltip(new Tooltip(buttonKey + "\n" + clickableButton.getDescription())); // I tried it, but it looks a bit janky
			jfxButton.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent arg0)
				{
					try
					{
						triggerEvent("<" + clickableButton.getName() + "> from frontend", null, null);
						clickableButton.onAction(null);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			});
			vbox.getChildren().add(jfxButton);
		}
	}

	private void loadFrames(BorderPane innerPane)
	{
		// use a grid pane layout
		GridPane pane = new GridPane();
		pane.setPadding(new Insets(8, 8, 8, 8));
		pane.setHgap(8);
		pane.setVgap(8);
		ColumnConstraints none = new ColumnConstraints();
		none.setHgrow(Priority.ALWAYS);
		ColumnConstraints c300 = new ColumnConstraints();
		c300.setMinWidth(340);
		c300.setMaxWidth(340);
		pane.getColumnConstraints().addAll(none, none, none, c300);
		RowConstraints rcNever = new RowConstraints();
		rcNever.setVgrow(Priority.NEVER);
		RowConstraints rcAlways = new RowConstraints();
		rcAlways.setVgrow(Priority.ALWAYS);
		pane.getRowConstraints().addAll(rcNever, rcNever, rcNever, rcNever, rcNever, rcAlways);
		innerPane.setCenter(pane);

		// schedule
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setSpacing(0);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			String sn = NMOConfiguration.INSTANCE.scheduleName;
			if (sn == null || sn.isEmpty())
			{
				sn = "UNKNOWN SCHEDULE";
			}
			statusBox.getChildren().add(JavaFxHelper.createLabel(sn, Color.WHITE, "-fx-font-weight: bold; -fx-font-size: 14pt;"));

			if (NMOStatistics.INSTANCE.scheduleStartedOn > 0)
			{
				final Label started = JavaFxHelper.createLabel("Started: ", Color.WHITE, "-fx-font-weight: bold;");
				final Label startedG = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: normal;");
				startedG.textProperty().bind(startedString);
				started.setGraphic(startedG);
				started.setContentDisplay(ContentDisplay.RIGHT);
				statusBox.getChildren().add(started);

				final Label started2 = JavaFxHelper.createLabel("", Color.WHITE, "");
				started2.textProperty().bind(startedString2);
				started2.setPadding(new Insets(0, 0, 0, 10));
				statusBox.getChildren().add(started2);

				final Label lastOversleep = JavaFxHelper.createLabel("Last oversleep: ", Color.WHITE, "-fx-font-weight: bold;");
				final Label lastOversleepG = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: normal;");
				lastOversleepG.textProperty().bind(lastOversleepString);
				lastOversleep.setGraphic(lastOversleepG);
				lastOversleep.setContentDisplay(ContentDisplay.RIGHT);
				statusBox.getChildren().add(lastOversleep);

				final Label lastOversleep2 = JavaFxHelper.createLabel("", Color.WHITE, "");
				lastOversleep2.textProperty().bind(lastOversleepString2);
				lastOversleep2.setPadding(new Insets(0, 0, 0, 10));
				statusBox.getChildren().add(lastOversleep2);

				final Label personalBest = JavaFxHelper.createLabel("Personal best: ", Color.WHITE, "-fx-font-weight: bold;");
				final Label personalBestG = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: normal;");
				personalBestG.textProperty().bind(personalBestString);
				personalBest.setGraphic(personalBestG);
				personalBest.setContentDisplay(ContentDisplay.RIGHT);
				statusBox.getChildren().add(personalBest);
				personalBest.setPadding(new Insets(0, 0, 4, 0));

				this.addIntegrationButtonsToVbox(ScheduleFakeIntegration.INSTANCE, statusBox);
			}

			Separator s = new Separator(Orientation.HORIZONTAL);
			s.setPadding(new Insets(6, 0, 2, 0));
			statusBox.getChildren().add(s);

			for (SleepEntry entry : NMOConfiguration.INSTANCE.schedule)
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel(entry.type + " -- " + entry.name, Color.WHITE, "-fx-font-weight: bold;"));
				statusBox.getChildren().add(JavaFxHelper.createLabel(entry.describeTime() + "    (" + entry.approachWarning + "m approach warning)", Color.WHITE, "", new Insets(0, 0, 0, 8)));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #26DE42;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Current Schedule", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #26DE42; -fx-background-color: #333;");
			pane.add(frame, 0, 0, 1, 1);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// monitoring control
		{
			Separator s;

			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setSpacing(2);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			final Label status = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold; -fx-font-size: 14pt;");
			status.textProperty().bind(scheduleStatusString);
			statusBox.getChildren().add(status);

			final Label nextblock = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold; -fx-font-size: 12pt;");
			nextblock.textProperty().bind(scheduleNextBlockString);
			statusBox.getChildren().add(nextblock);

			final Label countdown = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold; -fx-font-size: 16pt");
			countdown.textProperty().bind(scheduleCountdownString);
			statusBox.getChildren().add(countdown);

			statusBox.getChildren().add(s = new Separator(Orientation.HORIZONTAL));
			s.setPadding(new Insets(4, 0, 2, 0));

			final Label keyboard = JavaFxHelper.createLabel("Keyboard: ", Color.WHITE, "-fx-font-weight: bold;");
			if (IntegrationKeyboard.INSTANCE.isEnabled())
			{
				keyboard.setGraphic(JavaFxHelper.createLabel("ENABLED", Color.LIME));
				keyboard.setContentDisplay(ContentDisplay.RIGHT);
			}
			else
			{
				keyboard.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
				keyboard.setContentDisplay(ContentDisplay.RIGHT);
			}
			statusBox.getChildren().add(keyboard);

			final Label mouse = JavaFxHelper.createLabel("Mouse: ", Color.WHITE, "-fx-font-weight: bold;");
			if (IntegrationMouse.INSTANCE.isEnabled())
			{
				mouse.setGraphic(JavaFxHelper.createLabel("ENABLED", Color.LIME));
				mouse.setContentDisplay(ContentDisplay.RIGHT);
			}
			else
			{
				mouse.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
				mouse.setContentDisplay(ContentDisplay.RIGHT);
			}
			statusBox.getChildren().add(mouse);

			final Label xbox = JavaFxHelper.createLabel("Controller: ", Color.WHITE, "-fx-font-weight: bold;");
			if (IntegrationXboxController.INSTANCE.isEnabled())
			{
				String enabledString = "";
				final PlayerConfiguration[] confs = { NMOConfiguration.INSTANCE.integrations.xboxController.P1, NMOConfiguration.INSTANCE.integrations.xboxController.P2, NMOConfiguration.INSTANCE.integrations.xboxController.P3, NMOConfiguration.INSTANCE.integrations.xboxController.P4 };
				for (int i = 0; i < 4; i++)
				{
					if (confs[i].enabled)
					{
						enabledString = enabledString + (enabledString.isEmpty() ? "" : ", ") + "P" + (i + 1);
					}
				}
				xbox.setGraphic(JavaFxHelper.createLabel("ENABLED FOR " + enabledString, Color.LIME));
				xbox.setContentDisplay(ContentDisplay.RIGHT);
			}
			else
			{
				xbox.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
				xbox.setContentDisplay(ContentDisplay.RIGHT);
			}
			statusBox.getChildren().add(xbox);

			final Label midi = JavaFxHelper.createLabel("MIDI: ", Color.WHITE, "-fx-font-weight: bold;");
			if (IntegrationMidiTransmitter.INSTANCE.isEnabled())
			{
				midi.setGraphic(JavaFxHelper.createLabel("ENABLED", Color.LIME));
				midi.setContentDisplay(ContentDisplay.RIGHT);
			}
			else
			{
				midi.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
				midi.setContentDisplay(ContentDisplay.RIGHT);
			}
			statusBox.getChildren().add(midi);
			if (IntegrationMidiTransmitter.INSTANCE.isEnabled())
			{
				for (String transmitter : NMOConfiguration.INSTANCE.integrations.midiTransmitter.transmitters)
				{
					final Label transmitterlabel = JavaFxHelper.createLabel(transmitter, Color.YELLOW, "", new Insets(0, 0, 0, 16));
					statusBox.getChildren().add(transmitterlabel);
				}
			}

			statusBox.getChildren().add(s = new Separator(Orientation.HORIZONTAL));
			s.setPadding(new Insets(4, 0, 2, 0));

			if (IntegrationXboxController.INSTANCE.isEnabled())
			{
				this.addIntegrationButtonsToVbox(IntegrationXboxController.INSTANCE, statusBox);
				statusBox.getChildren().add(s = new Separator(Orientation.HORIZONTAL));
				s.setPadding(new Insets(4, 0, 2, 0));
			}

			final Label lastCursorTime = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
			lastCursorTime.textProperty().bind(lastActivityTimeString);
			statusBox.getChildren().add(lastCursorTime);

			final Label timeDiff = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
			timeDiff.textProperty().bind(timeDiffString);
			statusBox.getChildren().add(timeDiff);

			final Label activeTimerLabel = JavaFxHelper.createLabel("", Color.WHITE);
			activeTimerLabel.textProperty().bind(activeTimerString);
			statusBox.getChildren().add(activeTimerLabel);

			statusBox.getChildren().add(s = new Separator(Orientation.HORIZONTAL));
			s.setPadding(new Insets(4, 0, 2, 0));

			this.addIntegrationButtonsToVbox(ActivityTimerFakeIntegration.INSTANCE, statusBox);

			hbox.getChildren().add(new Separator(Orientation.VERTICAL));

			VBox pauseControlBox = new VBox(6);
			pauseControlBox.setAlignment(Pos.TOP_LEFT);
			final Label label2 = JavaFxHelper.createLabel("Pause/Resume", Color.WHITE);
			pauseControlBox.getChildren().add(label2);
			int[] periods = new int[] { 5, 10, 15, 20, 25, 30, 45, 60, 90, 105, 120, 180, 240, 300, 360, 420, 480, 540, 600, 660, 720 };
			GridPane btnGridPane = null;
			for (int p = 0; p < periods.length; p++)
			{
				if (p % 3 == 0)
				{
					if (btnGridPane != null)
					{
						pauseControlBox.getChildren().add(btnGridPane);
					}
					btnGridPane = new GridPane();
					btnGridPane.setHgap(6);
				}
				final int pp = periods[p];
				int hours = pp / 60;
				int minutes = pp % 60;
				final String hm = (((hours > 0) ? hours + "h" : "") + ((minutes > 0) ? minutes + "m" : ""));
				final String hmLong = (((hours > 0) ? hours + " HOUR" + (hours > 1 ? "S" : "") : "") + ((minutes > 0) ? ((hours > 0) ? " " : "") + minutes + " MINUTES" : ""));
				final Button pauseButton = JavaFxHelper.createButton(hm);
				pauseButton.setMinWidth(64);
				pauseButton.setMaxWidth(64);
				pauseButton.setAlignment(Pos.BASELINE_LEFT);
				pauseButton.setPadding(new Insets(5, 4, 5, 4));
				pauseButton.setOnAction(new EventHandler<ActionEvent>()
				{
					@Override
					public void handle(ActionEvent arg0)
					{
						MainDialog.this.openPauseDialog(hmLong, pp);
					}
				});
				//pauseButton.disableProperty().bind(isCurrentlyPaused);
				btnGridPane.addColumn(p % 3, pauseButton);
			}
			if (btnGridPane != null)
			{
				pauseControlBox.getChildren().add(btnGridPane);
			}
			final Button unpauseButton = JavaFxHelper.createButton("Unpause");
			unpauseButton.setMinWidth(204);
			unpauseButton.setMaxWidth(204);
			unpauseButton.setPadding(new Insets(5, 4, 5, 4));
			unpauseButton.setAlignment(Pos.BASELINE_LEFT);
			unpauseButton.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent arg0)
				{
					pausedUntil = 0;
					isCurrentlyPaused.set(false);
					Map<String, String[]> parameters = new HashMap<>();
					String context = "Unpaused manually";
					parameters.put("context", new String[] { context });
					triggerEvent(context, NMOConfiguration.INSTANCE.events.pauseCancelled, parameters);
				}
			});
			unpauseButton.disableProperty().bind(isCurrentlyPaused.not());
			pauseControlBox.getChildren().add(unpauseButton);
			pauseControlBox.getChildren().add(new Separator(Orientation.HORIZONTAL));

			VBox azhVbox = new VBox();
			azhVbox.setSpacing(1);
			final Label azhLabel1 = JavaFxHelper.createLabel("Zombie Trapper", Color.WHITE, "-fx-font-weight: bold; -fx-font-size: 12pt;");
			azhVbox.getChildren().add(azhLabel1);
			zombiePenaltyEnableLabel = JavaFxHelper.createLabel("", Color.ORANGE, "-fx-font-weight: bold; -fx-font-size: 10pt;", new Insets(0, 0, 0, 0));
			zombiePenaltyEnableLabel.textProperty().bind(zombiePenaltyEnableString);
			azhVbox.getChildren().add(zombiePenaltyEnableLabel);
			final Label azhLabel2 = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
			azhLabel2.textProperty().bind(zombiePenaltyAccruedString);
			azhVbox.getChildren().add(azhLabel2);
			final Label azhLabel2a = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
			azhLabel2a.textProperty().bind(zombiePenaltyAccruedString2);
			azhVbox.getChildren().add(azhLabel2a);
			final Label azhLabel3 = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
			azhLabel3.textProperty().bind(zombiePenaltyWaitString);
			azhVbox.getChildren().add(azhLabel3);
			pauseControlBox.getChildren().add(azhVbox);
			final Label azhLabel4 = JavaFxHelper.createLabel("", Color.WHITE, "");
			azhLabel4.textProperty().bind(zombiePenaltyValString1);
			azhVbox.getChildren().add(azhLabel4);
			final Label azhLabel5 = JavaFxHelper.createLabel("", Color.WHITE, "");
			azhLabel5.textProperty().bind(zombiePenaltyValString2);
			azhVbox.getChildren().add(azhLabel5);
			final Label azhLabel6 = JavaFxHelper.createLabel("", Color.WHITE, "");
			azhLabel6.textProperty().bind(zombiePenaltyValString3);
			azhVbox.getChildren().add(azhLabel6);

			hbox.getChildren().add(pauseControlBox);

			hbox.getChildren().add(new Separator(Orientation.VERTICAL));

			VBox webcamBox = new VBox(6);
			webcamBox.setMinWidth(326);
			webcamBox.setMaxWidth(326);
			if (NMOConfiguration.INSTANCE.integrations.webUI.enabled)
			{
				Label l = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
				l.setPadding(new Insets(0, 0, 0, 2));
				l.textProperty().bind(webcamName);
				webcamBox.getChildren().add(l);
				ImageView webcamImageView = new ImageView();
				webcamImageView.imageProperty().bind(lastWebcamImage);
				webcamImageView.setPreserveRatio(true);
				webcamBox.getChildren().add(webcamImageView);
				webcamBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
				this.addIntegrationButtonsToVbox(IntegrationWebUI.INSTANCE, webcamBox);
				webcamBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
				final Label webMonitoringLabel = JavaFxHelper.createLabel("", Color.WHITE);
				webMonitoringLabel.textProperty().bind(webMonitoringString);
				webMonitoringLabel.setAlignment(Pos.TOP_LEFT);
				webMonitoringLabel.setMinHeight(64);
				//webMonitoringLabel.setMaxHeight(64);
				//webMonitoringLabel.setPrefHeight(64);
				webcamBox.getChildren().add(webMonitoringLabel);
			}
			else
			{
				webcamBox.getChildren().add(JavaFxHelper.createLabel("Remote monitoring is disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}
			hbox.getChildren().add(webcamBox);

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #6D81A3;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Monitoring Control", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);

			final Button jfxButtonWebUI = JavaFxHelper.createButton("Launch web UI", JavaFxHelper.createIcon(FontAwesomeIcon.LINK, "11", Color.BLACK));
			jfxButtonWebUI.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonWebUI.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent arg0)
				{
					try
					{
						if (NMOConfiguration.INSTANCE.integrations.webUI.readProxyForwardingHeaders && !NMOConfiguration.INSTANCE.integrations.webUI.openUiLocally)
						{
							DesktopHelper.browse("https://" + NMOConfiguration.INSTANCE.integrations.webUI.ddns.domain + "/ui/");
						}
						else
						{
							String hostname = NMOConfiguration.INSTANCE.integrations.webUI.openUiLocally ? "127.0.0.1" : !CommonUtils.isNullOrEmpty(NMOConfiguration.INSTANCE.integrations.webUI.ddns.domain) ? NMOConfiguration.INSTANCE.integrations.webUI.ddns.domain : PortForwarding.getExternalIP();
							DesktopHelper.browse("http://" + hostname + ":" + NMOConfiguration.INSTANCE.integrations.webUI.jettyPort + "/ui/");
						}
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
				}
			});
			jfxButtonWebUI.setDisable(!NMOConfiguration.INSTANCE.integrations.webUI.enabled);
			heading.getChildren().add(jfxButtonWebUI);

			final Button jfxButtonPortForward = JavaFxHelper.createButton("Attempt port auto-forward", JavaFxHelper.createIcon(FontAwesomeIcon.PLUG, "11", Color.BLACK));
			jfxButtonPortForward.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonPortForward.setOnAction(new EventHandler<ActionEvent>()
			{
				@Override
				public void handle(ActionEvent arg0)
				{
					try
					{
						ArrayList<String> messages = new ArrayList<>();
						PortForwarding.attemptAutomaticPortForwarding(messages);
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Port forwarding");
						alert.setHeaderText(null);
						alert.setContentText(StringUtils.join(messages, "\n"));
						alert.setResizable(true);
						alert.getDialogPane().setPrefSize(600, Region.USE_COMPUTED_SIZE);
						alert.showAndWait();
					}
					catch (Throwable e)
					{
						e.printStackTrace();
					}
				}
			});
			jfxButtonPortForward.setDisable(!NMOConfiguration.INSTANCE.integrations.webUI.enabled);
			heading.getChildren().add(jfxButtonPortForward);

			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #6D81A3; -fx-background-color: #333;");
			pane.add(frame, 1, 0, 3, 1);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// PAVLOK
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			if (IntegrationPavlok.INSTANCE.isEnabled())
			{
				final Label loginTokenValidUntil = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
				loginTokenValidUntil.textProperty().bind(loginTokenValidUntilString);
				statusBox.getChildren().add(loginTokenValidUntil);
				statusBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
				this.addIntegrationButtonsToVbox(IntegrationPavlok.INSTANCE, statusBox);
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #DEB026;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Pavlok", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #DEB026; -fx-background-color: #333;");
			pane.add(frame, 0, 1, 1, 1);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// HOME AUTOMATION
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			boolean enabled = IntegrationPhilipsHue.INSTANCE.isEnabled() || IntegrationTPLink.INSTANCE.isEnabled() || IntegrationWemo.INSTANCE.isEnabled();

			if (enabled)
			{
				if (IntegrationPhilipsHue.INSTANCE.isEnabled())
				{
					final Label lightingStateLabel = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
					lightingStateLabel.textProperty().bind(lightingStateString);
					statusBox.getChildren().add(lightingStateLabel);
					statusBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
					this.addIntegrationButtonsToVbox(IntegrationPhilipsHue.INSTANCE, statusBox);
				}
				if (IntegrationTPLink.INSTANCE.isEnabled())
				{
					final Label tplinkStateLabel = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
					tplinkStateLabel.textProperty().bind(tplinkStateString);
					statusBox.getChildren().add(tplinkStateLabel);
					statusBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
					this.addIntegrationButtonsToVbox(IntegrationTPLink.INSTANCE, statusBox);
				}
				if (IntegrationWemo.INSTANCE.isEnabled())
				{
					final Label wemoStateLabel = JavaFxHelper.createLabel("", Color.WHITE, "-fx-font-weight: bold;");
					wemoStateLabel.textProperty().bind(wemoStateString);
					statusBox.getChildren().add(wemoStateLabel);
					statusBox.getChildren().add(new Separator(Orientation.HORIZONTAL));
					this.addIntegrationButtonsToVbox(IntegrationWemo.INSTANCE, statusBox);
				}
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #839CA0;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Home Automation", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #839CA0; -fx-background-color: #333;");
			pane.add(frame, 1, 1, 1, 2);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// ALARM SOUNDS
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			if (IntegrationNoise.INSTANCE.isEnabled())
			{
				this.addIntegrationButtonsToVbox(IntegrationNoise.INSTANCE, statusBox);
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #B649C6;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Noise", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #B649C6; -fx-background-color: #333;");
			pane.add(frame, 2, 1, 1, 5);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// EVENT CONTROL
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setSpacing(0);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			this.addEventSummaryToStatusBox(statusBox, "When core is approaching", NMOConfiguration.INSTANCE.events.coreApproaching);
			this.addEventSummaryToStatusBox(statusBox, "When core starts", NMOConfiguration.INSTANCE.events.coreStarted);
			this.addEventSummaryToStatusBox(statusBox, "When core ends", NMOConfiguration.INSTANCE.events.coreEnded);
			this.addEventSummaryToStatusBox(statusBox, "When nap is approaching", NMOConfiguration.INSTANCE.events.napApproaching);
			this.addEventSummaryToStatusBox(statusBox, "When nap starts", NMOConfiguration.INSTANCE.events.napStarted);
			this.addEventSummaryToStatusBox(statusBox, "When nap ends", NMOConfiguration.INSTANCE.events.napEnded);
			this.addEventSummaryToStatusBox(statusBox, "When siesta is approaching", NMOConfiguration.INSTANCE.events.siestaApproaching);
			this.addEventSummaryToStatusBox(statusBox, "When siesta starts", NMOConfiguration.INSTANCE.events.siestaStarted);
			this.addEventSummaryToStatusBox(statusBox, "When siesta ends", NMOConfiguration.INSTANCE.events.siestaEnded);
			this.addEventSummaryToStatusBox(statusBox, "When AFK block is approaching", NMOConfiguration.INSTANCE.events.afkApproaching);
			this.addEventSummaryToStatusBox(statusBox, "When AFK block starts", NMOConfiguration.INSTANCE.events.afkStarted);
			this.addEventSummaryToStatusBox(statusBox, "When AFK block ends", NMOConfiguration.INSTANCE.events.afkEnded);
			this.addEventSummaryToStatusBox(statusBox, "On first activity warning", NMOConfiguration.INSTANCE.events.activityWarning1);
			this.addEventSummaryToStatusBox(statusBox, "On oversleep activity warning (#" + NMOConfiguration.INSTANCE.oversleepWarningThreshold + ")", NMOConfiguration.INSTANCE.events.oversleepWarning);
			this.addEventSummaryToStatusBox(statusBox, "On all other activity warnings", NMOConfiguration.INSTANCE.events.activityWarning2);
			this.addEventSummaryToStatusBox(statusBox, "When zombie penalty limit is reached", NMOConfiguration.INSTANCE.events.zombiePenaltyLimitReached);
			this.addEventSummaryToStatusBox(statusBox, "When manually pausing", NMOConfiguration.INSTANCE.events.pauseInitiated);
			this.addEventSummaryToStatusBox(statusBox, "When manually unpausing", NMOConfiguration.INSTANCE.events.pauseCancelled);
			this.addEventSummaryToStatusBox(statusBox, "When pause auto-expires", NMOConfiguration.INSTANCE.events.pauseExpired);
			for (CustomEvent action : NMOConfiguration.INSTANCE.events.custom)
			{
				this.addEventSummaryToStatusBox(statusBox, action.name + " (" + action.describe() + ")", action.actions);
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #6BA4A5;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Event Control", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #6BA4A5; -fx-background-color: #333;");
			pane.add(frame, 3, 1, 1, 5);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// TWILIO
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			if (IntegrationTwilio.INSTANCE.isEnabled())
			{
				this.addIntegrationButtonsToVbox(IntegrationTwilio.INSTANCE, statusBox);
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #A36E6D;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Twilio", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #A36E6D; -fx-background-color: #333;");
			pane.add(frame, 0, 2, 1, 1);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// CUSTOM COMMANDS
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			if (IntegrationCommandLine.INSTANCE.isEnabled())
			{
				this.addIntegrationButtonsToVbox(IntegrationCommandLine.INSTANCE, statusBox);
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #7BAD58;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Custom Commands", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #7BAD58; -fx-background-color: #333;");
			pane.add(frame, 1, 3, 1, 1);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// DISCORD
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			if (IntegrationDiscord.INSTANCE.isEnabled())
			{
				this.addIntegrationButtonsToVbox(IntegrationDiscord.INSTANCE, statusBox);
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #7289DA;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Discord", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #7289DA; -fx-background-color: #333;");
			pane.add(frame, 0, 3, 1, 1);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// FILEWRITER
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			if (IntegrationFileWriter.INSTANCE.isEnabled())
			{
				final Label scheduleName = JavaFxHelper.createLabel("scheduleName: ", Color.WHITE, "-fx-font-weight: bold;");
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleName)
				{
					scheduleName.setGraphic(JavaFxHelper.createLabel("ENABLED", Color.LIME));
					scheduleName.setContentDisplay(ContentDisplay.RIGHT);
				}
				else
				{
					scheduleName.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
					scheduleName.setContentDisplay(ContentDisplay.RIGHT);
				}
				statusBox.getChildren().add(scheduleName);

				final Label scheduleStartedOn = JavaFxHelper.createLabel("scheduleStartedOn: ", Color.WHITE, "-fx-font-weight: bold;");
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleStartedOn)
				{
					scheduleStartedOn.setGraphic(JavaFxHelper.createLabel("ENABLED", Color.LIME));
					scheduleStartedOn.setContentDisplay(ContentDisplay.RIGHT);
				}
				else
				{
					scheduleStartedOn.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
					scheduleStartedOn.setContentDisplay(ContentDisplay.RIGHT);
				}
				statusBox.getChildren().add(scheduleStartedOn);

				final Label scheduleLastOversleep = JavaFxHelper.createLabel("scheduleLastOversleep: ", Color.WHITE, "-fx-font-weight: bold;");
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleLastOversleep)
				{
					scheduleLastOversleep.setGraphic(JavaFxHelper.createLabel("ENABLED", Color.LIME));
					scheduleLastOversleep.setContentDisplay(ContentDisplay.RIGHT);
				}
				else
				{
					scheduleLastOversleep.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
					scheduleLastOversleep.setContentDisplay(ContentDisplay.RIGHT);
				}
				statusBox.getChildren().add(scheduleLastOversleep);

				final Label schedulePersonalBest = JavaFxHelper.createLabel("schedulePersonalBest: ", Color.WHITE, "-fx-font-weight: bold;");
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.schedulePersonalBest)
				{
					schedulePersonalBest.setGraphic(JavaFxHelper.createLabel("ENABLED", Color.LIME));
					schedulePersonalBest.setContentDisplay(ContentDisplay.RIGHT);
				}
				else
				{
					schedulePersonalBest.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
					schedulePersonalBest.setContentDisplay(ContentDisplay.RIGHT);
				}
				statusBox.getChildren().add(schedulePersonalBest);

				final Label timeToNextSleepBlock = JavaFxHelper.createLabel("timeToNextSleepBlock: ", Color.WHITE, "-fx-font-weight: bold;");
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.timeToNextSleepBlock)
				{
					timeToNextSleepBlock.setGraphic(JavaFxHelper.createLabel("ENABLED", Color.LIME));
					timeToNextSleepBlock.setContentDisplay(ContentDisplay.RIGHT);
				}
				else
				{
					timeToNextSleepBlock.setGraphic(JavaFxHelper.createLabel("DISABLED", Color.RED));
					timeToNextSleepBlock.setContentDisplay(ContentDisplay.RIGHT);
				}
				statusBox.getChildren().add(timeToNextSleepBlock);
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #AA3456;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("File Writer", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #AA3456; -fx-background-color: #333;");
			pane.add(frame, 0, 4, 1, 2);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// ITERATOR
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			if (IntegrationIterator.INSTANCE.isEnabled())
			{
				this.addIntegrationButtonsToVbox(IntegrationIterator.INSTANCE, statusBox);
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #D8C743;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Iterator", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #D8C743; -fx-background-color: #333;");
			pane.add(frame, 1, 4, 1, 1);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// RANDOMIZER
		{
			HBox hbox = new HBox(4);
			hbox.setPadding(new Insets(4));
			hbox.setAlignment(Pos.TOP_CENTER);

			VBox statusBox = new VBox(4);
			statusBox.setAlignment(Pos.TOP_LEFT);
			hbox.getChildren().add(statusBox);
			HBox.setHgrow(statusBox, Priority.ALWAYS);

			if (IntegrationRandomizer.INSTANCE.isEnabled())
			{
				this.addIntegrationButtonsToVbox(IntegrationRandomizer.INSTANCE, statusBox);
			}
			else
			{
				statusBox.getChildren().add(JavaFxHelper.createLabel("Integration disabled", Color.GRAY, "-fx-font-weight: bold;"));
			}

			final HBox heading = JavaFxHelper.createHorizontalBox(Control.USE_COMPUTED_SIZE, 24);
			heading.setStyle("-fx-background-color: #D88B43;");
			heading.setPadding(new Insets(2));
			heading.setSpacing(2);
			final Label label = JavaFxHelper.createLabel("Randomizer", Color.BLACK, "-fx-font-size: 11pt;");
			heading.getChildren().add(label);
			final StackPane spt = new StackPane();
			heading.getChildren().add(spt);
			HBox.setHgrow(spt, Priority.ALWAYS);
			heading.setAlignment(Pos.TOP_LEFT);
			final Button jfxButtonConfigure = JavaFxHelper.createButton("Configure", JavaFxHelper.createIcon(FontAwesomeIcon.COGS, "11", Color.BLACK));
			jfxButtonConfigure.setPadding(new Insets(2, 4, 2, 4));
			jfxButtonConfigure.setDisable(true); // temporary
			//heading.getChildren().add(jfxButtonConfigure);

			final BorderPane frame = new BorderPane();
			frame.setTop(heading);
			frame.setCenter(hbox);
			frame.setStyle("-fx-border-width: 1px; -fx-border-color: #D88B43; -fx-background-color: #333;");
			pane.add(frame, 1, 5, 1, 1);
			//GridPane.setVgrow(frame, Priority.SOMETIMES);
		}

		// build the log frame
		{
			final ListView<String> listView = new ListView<>(events);
			listView.getItems().addListener(new ListChangeListener<String>()
			{
				@Override
				public void onChanged(javafx.collections.ListChangeListener.Change<? extends String> c)
				{
					listView.scrollTo(c.getList().size() - 1);
				}
			});
			listView.setMinHeight(216);
			listView.setMaxHeight(216);
			innerPane.setBottom(listView);
		}
	}

	protected void openPauseDialog(final String hm, final int pp)
	{
		final Stage dialog = new Stage();
		dialog.setTitle("Pause for " + hm);
		dialog.getIcons().add(new Image(JavaFxHelper.buildResourcePath("icon.png")));
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(scene.getWindow());
		dialog.setResizable(false);
		BorderPane outerPane = new BorderPane();
		outerPane.setId("root");
		outerPane.setStyle("-fx-background-color: #222;");
		StackPane stopBg = new StackPane();
		stopBg.setPadding(new Insets(10));
		StackPane stopBg2 = new StackPane();
		stopBg2.setStyle("-fx-background-color: #990000; -fx-border-size: 2px; -fx-border-color: white;");
		stopBg2.setMinHeight(200);
		stopBg2.setMaxHeight(200);
		stopBg.getChildren().add(stopBg2);
		Label stopLabel = JavaFxHelper.createIconLabel(FontAwesomeIcon.EXCLAMATION_TRIANGLE, "72", " STOP AND THINK !!", ContentDisplay.LEFT, Color.WHITE, "-fx-font-size: 72px;");
		stopLabel.setPadding(new Insets(0, 0, 70, 0));
		Label stopLabel2 = JavaFxHelper.createLabel("DO YOU REALLY NEED TO PAUSE FOR " + hm + "?", Color.WHITE, "-fx-font-size: 24px;");
		stopLabel2.setPadding(new Insets(52, 0, 0, 0));
		Label stopLabel3 = JavaFxHelper.createLabel("*** FAILING YOUR SCHEDULE IS JUST ONE STUPID PAUSE AWAY ***", Color.WHITE, "-fx-font-size: 24px; -fx-font-weight: bold;");
		stopLabel3.setPadding(new Insets(130, 0, 0, 0));
		stopBg2.getChildren().add(stopLabel);
		stopBg2.getChildren().add(stopLabel2);
		stopBg2.getChildren().add(stopLabel3);
		outerPane.setTop(stopBg);
		final Captcha captcha = new Captcha.Builder(200, 50).addText().addBackground(new GradiatedBackgroundProducer()).addNoise().gimp(new FishEyeGimpyRenderer()).addBorder().build();
		WritableImage wimg = new WritableImage(200, 50);
		SwingFXUtils.toFXImage(captcha.getImage(), wimg);
		ImageView captchaImageView = new ImageView();
		captchaImageView.setImage(wimg);
		captchaImageView.setPreserveRatio(true);
		GridPane center = new GridPane();
		center.setPadding(new Insets(6, 16, 6, 16));
		center.setAlignment(Pos.TOP_LEFT);
		center.setStyle("-fx-font-size: 16px");
		center.setVgap(16);
		center.getColumnConstraints().add(new ColumnConstraints(150));
		center.getColumnConstraints().add(new ColumnConstraints(220));
		center.getColumnConstraints().add(new ColumnConstraints(410));
		Label a = JavaFxHelper.createLabel("If you are ABSOLUTELY SURE you need to pause...", Color.WHITE, "-fx-font-weight: bold;");
		center.addRow(0, a);
		GridPane.setColumnSpan(a, 2);
		final TextField reason = new TextField();
		Label b = JavaFxHelper.createLabel("Input pause reason:", Color.WHITE);
		GridPane.setColumnSpan(reason, 2);
		center.addRow(1, b, reason);
		Label c = JavaFxHelper.createLabel("Solve this captcha:", Color.WHITE);
		final TextField captchaField = new TextField();
		center.addRow(2, c, captchaImageView, captchaField);
		outerPane.setCenter(center);
		Separator s = new Separator(Orientation.HORIZONTAL);
		GridPane.setColumnSpan(s, 3);
		center.addRow(3, s);
		BooleanBinding bb = new BooleanBinding()
		{
			{
				super.bind(reason.textProperty(), captchaField.textProperty());
			}

			@Override
			protected boolean computeValue()
			{
				return (reason.getText().isEmpty() || !captchaField.getText().equals(captcha.getAnswer()));
			}
		};
		ButtonBar buttonBar = new ButtonBar();
		final Button okButton = new Button("Confirm pause");
		okButton.disableProperty().bind(bb);
		ButtonBar.setButtonData(okButton, ButtonData.OK_DONE);
		buttonBar.getButtons().addAll(okButton);
		buttonBar.setPadding(new Insets(0, 16, 16, 0));
		buttonBar.setStyle("-fx-font-size: 16px;");
		okButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				pausedUntil = now + (pp * 60000);
				pauseReason = reason.getText();
				pauseIsScheduleRelated = false;
				Map<String, String[]> parameters = new HashMap<>();
				String context = "Paused for " + hm + " (until " + CommonUtils.dateFormatter.get().format(pausedUntil) + ") for \"" + pauseReason + "\"";
				parameters.put("context", new String[] { context });
				triggerEvent(context, NMOConfiguration.INSTANCE.events.pauseInitiated, parameters);
				dialog.close();
			}
		});
		reason.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					if (!okButton.isDisabled())
					{
						okButton.fire();
					}
				}
			}
		});
		captchaField.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					if (!okButton.isDisabled())
					{
						okButton.fire();
					}
				}
			}
		});
		outerPane.setBottom(buttonBar);
		Scene dialogScene = new Scene(outerPane, 800, 430, Color.WHITE);
		dialogScene.getStylesheets().add(JavaFxHelper.buildResourcePath("application.css"));
		dialog.setScene(dialogScene);
		dialog.showAndWait();
	}

	protected void openAppCloseDialog()
	{
		final Stage dialog = new Stage();
		dialog.setTitle("Close NMO");
		dialog.getIcons().add(new Image(JavaFxHelper.buildResourcePath("icon.png")));
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(scene.getWindow());
		dialog.setResizable(false);
		BorderPane outerPane = new BorderPane();
		outerPane.setId("root");
		outerPane.setStyle("-fx-background-color: #222;");
		StackPane stopBg = new StackPane();
		stopBg.setPadding(new Insets(10));
		StackPane stopBg2 = new StackPane();
		stopBg2.setStyle("-fx-background-color: #990000; -fx-border-size: 2px; -fx-border-color: white;");
		stopBg2.setMinHeight(200);
		stopBg2.setMaxHeight(200);
		stopBg.getChildren().add(stopBg2);
		Label stopLabel = JavaFxHelper.createIconLabel(FontAwesomeIcon.EXCLAMATION_TRIANGLE, "72", " STOP AND THINK !!", ContentDisplay.LEFT, Color.WHITE, "-fx-font-size: 72px;");
		stopLabel.setPadding(new Insets(0, 0, 70, 0));
		Label stopLabel2 = JavaFxHelper.createLabel("IS CLOSING NMO A GOOD IDEA?", Color.WHITE, "-fx-font-size: 24px;");
		stopLabel2.setPadding(new Insets(52, 0, 0, 0));
		Label stopLabel3 = JavaFxHelper.createLabel("*** FAILING YOUR SCHEDULE IS VERY LIKELY IF NMO IS CLOSED ***", Color.WHITE, "-fx-font-size: 24px; -fx-font-weight: bold;");
		stopLabel3.setPadding(new Insets(130, 0, 0, 0));
		stopBg2.getChildren().add(stopLabel);
		stopBg2.getChildren().add(stopLabel2);
		stopBg2.getChildren().add(stopLabel3);
		outerPane.setTop(stopBg);
		final Captcha captcha = new Captcha.Builder(200, 50).addText().addBackground(new GradiatedBackgroundProducer()).addNoise().gimp(new FishEyeGimpyRenderer()).addBorder().build();
		WritableImage wimg = new WritableImage(200, 50);
		SwingFXUtils.toFXImage(captcha.getImage(), wimg);
		ImageView captchaImageView = new ImageView();
		captchaImageView.setImage(wimg);
		captchaImageView.setPreserveRatio(true);
		GridPane center = new GridPane();
		center.setPadding(new Insets(6, 16, 6, 16));
		center.setAlignment(Pos.TOP_LEFT);
		center.setStyle("-fx-font-size: 16px");
		center.setVgap(16);
		center.getColumnConstraints().add(new ColumnConstraints(150));
		center.getColumnConstraints().add(new ColumnConstraints(220));
		center.getColumnConstraints().add(new ColumnConstraints(410));
		Label a = JavaFxHelper.createLabel("If you are ABSOLUTELY SURE you need to close NMO...", Color.WHITE, "-fx-font-weight: bold;");
		center.addRow(0, a);
		GridPane.setColumnSpan(a, 2);
		Label c = JavaFxHelper.createLabel("Solve this captcha:", Color.WHITE);
		final TextField captchaField = new TextField();
		center.addRow(1, c, captchaImageView, captchaField);
		outerPane.setCenter(center);
		Separator s = new Separator(Orientation.HORIZONTAL);
		GridPane.setColumnSpan(s, 3);
		center.addRow(2, s);
		BooleanBinding bb = new BooleanBinding()
		{
			{
				super.bind(captchaField.textProperty());
			}

			@Override
			protected boolean computeValue()
			{
				return !captchaField.getText().equals(captcha.getAnswer());
			}
		};
		ButtonBar buttonBar = new ButtonBar();
		final Button okButton = new Button("Confirm close");
		okButton.disableProperty().bind(bb);
		ButtonBar.setButtonData(okButton, ButtonData.OK_DONE);
		buttonBar.getButtons().addAll(okButton);
		buttonBar.setPadding(new Insets(0, 16, 16, 0));
		buttonBar.setStyle("-fx-font-size: 16px;");
		okButton.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				Platform.exit();
			}
		});
		captchaField.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent ke)
			{
				if (ke.getCode().equals(KeyCode.ENTER))
				{
					if (!okButton.isDisabled())
					{
						okButton.fire();
					}
				}
			}
		});
		outerPane.setBottom(buttonBar);
		Scene dialogScene = new Scene(outerPane, 800, 380, Color.WHITE);
		dialogScene.getStylesheets().add(JavaFxHelper.buildResourcePath("application.css"));
		dialog.setScene(dialogScene);
		dialog.showAndWait();
	}

	private void addEventSummaryToStatusBox(VBox statusBox, String description, String[] eventTriggers)
	{
		if (eventTriggers.length != 0)
		{
			statusBox.getChildren().add(JavaFxHelper.createLabel(description + ":", Color.WHITE, "-fx-font-weight: bold;"));
			for (int i = 0; i < eventTriggers.length; i++)
			{
				// get the description
				String desc = null;
				for (Integration integration : Main.integrations)
				{
					Action action = integration.getActions().get(eventTriggers[i]);
					if (action != null)
					{
						desc = action.getName();
						break;
					}
				}
				if (desc == null)
				{
					statusBox.getChildren().add(JavaFxHelper.createLabel(eventTriggers[i], Color.RED, "", new Insets(0, 0, 0, 16)));
				}
				else
				{
					statusBox.getChildren().add(JavaFxHelper.createLabel(desc, Color.LIME, "", new Insets(0, 0, 0, 16)));
				}
			}
		}
	}

	protected void tick()
	{
		tick++;
		if (tick >= NMOConfiguration.INSTANCE.garbageCollectionFrequency)
		{
			tick -= NMOConfiguration.INSTANCE.garbageCollectionFrequency;
			System.gc();
		}
		if (tick % 2 == 1)
		{
			return;
		}

		long noww = System.currentTimeMillis();
		long zombieWeightDiff;
		if (now > noww)
		{
			triggerEvent("Clock went backwards by " + (now - noww) + "ms!", null, null);
			zombieWeightDiff = 0;
		}
		else
		{
			zombieWeightDiff = noww - now;
		}
		now = noww;
		boolean paused = pausedUntil > now;
		ScheduleEntryType currentSleepState = null;

		if (NMOStatistics.INSTANCE.scheduleStartedOn > 0)
		{
			startedString.set(CommonUtils.dateFormatter2.get().format(NMOStatistics.INSTANCE.scheduleStartedOn));
			startedString2.set("(" + FormattingHelper.formatTimeElapsedWithDays(now, NMOStatistics.INSTANCE.scheduleStartedOn) + " ago)");
			lastOversleepString.set(CommonUtils.dateFormatter2.get().format(NMOStatistics.INSTANCE.scheduleLastOversleep));
			lastOversleepString2.set("(" + FormattingHelper.formatTimeElapsedWithDays(now, NMOStatistics.INSTANCE.scheduleLastOversleep) + " ago)");
			if ((now - NMOStatistics.INSTANCE.scheduleLastOversleep) > NMOStatistics.INSTANCE.schedulePersonalBest)
			{
				NMOStatistics.INSTANCE.schedulePersonalBest = now - NMOStatistics.INSTANCE.scheduleLastOversleep;
			}
			personalBestString.set(FormattingHelper.formatTimeElapsedWithDays(NMOStatistics.INSTANCE.schedulePersonalBest, 0));
		}

		// Update next trigger time for all sleep blocks except the next sleep block
		for (SleepEntry entry : NMOConfiguration.INSTANCE.schedule)
		{
			if (entry != nextSleepBlock)
			{
				entry.updateNextTriggerTime();
			}
		}
		// Update next trigger time for the next sleep block but ONLY IF the end time is in the past
		if (nextSleepBlock != null && nextSleepBlock.nextEndTime <= now)
		{
			nextSleepBlock.updateNextTriggerTime();
		}

		SleepEntry nextSleepBlockDetected = null;
		long startTimeDetected = Long.MAX_VALUE;
		for (SleepEntry entry : NMOConfiguration.INSTANCE.schedule)
		{
			if (entry.containsTimeValue(now) || entry.nextStartTime >= now && entry.nextStartTime < startTimeDetected)
			{
				nextSleepBlockDetected = entry;
				startTimeDetected = entry.nextStartTime;
			}
		}
		if (nextSleepBlockDetected == null && !NMOConfiguration.INSTANCE.schedule.isEmpty())
		{
			nextSleepBlockDetected = NMOConfiguration.INSTANCE.schedule.get(0);
		}
		if (nextSleepBlockDetected != null)
		{
			if (nextSleepBlockDetected.containsTimeValue(now))
			{
				long tims = nextSleepBlockDetected.nextEndTime;
				if (!scheduleStatus.startsWith(nextSleepBlockDetected.type + " "))
				{
					String[] triggerArray = null;
					switch (nextSleepBlockDetected.type)
					{
					case CORE:
						triggerArray = NMOConfiguration.INSTANCE.events.coreStarted;
						break;
					case NAP:
						triggerArray = NMOConfiguration.INSTANCE.events.napStarted;
						break;
					case SIESTA:
						triggerArray = NMOConfiguration.INSTANCE.events.siestaStarted;
						break;
					case AFK:
						triggerArray = NMOConfiguration.INSTANCE.events.afkStarted;
						break;
					}
					triggerEvent("Entering " + nextSleepBlockDetected.type + ": " + nextSleepBlockDetected.name, triggerArray, null);
				}
				// determine second value
				long secondsRemaining = (((tims + 999) - now) / 1000);
				long secondsCounter = secondsRemaining % 60;
				long minutesCounter = (secondsRemaining / 60) % 60;
				long hoursCounter = (secondsRemaining / 60) / 60;
				// determine minute value
				long minutesRemaining = (((tims + 59999) - now) / 60000);
				// determine status
				String pros = nextSleepBlockDetected.type.name();
				// populate the display fields
				scheduleStatusString.set(pros);
				scheduleNextBlockString.set("Active block: " + nextSleepBlockDetected.name);
				scheduleCountdownString.set(nextSleepBlockDetected.type + " ENDS IN " + StringUtils.leftPad("" + hoursCounter, 2, "0") + ":" + StringUtils.leftPad("" + minutesCounter, 2, "0") + ":" + StringUtils.leftPad("" + secondsCounter, 2, "0"));
				scheduleStatus = nextSleepBlockDetected.type + " (" + nextSleepBlockDetected.name + ") -- ENDS IN " + StringUtils.leftPad("" + hoursCounter, 2, "0") + ":" + StringUtils.leftPad("" + minutesCounter, 2, "0") + ":" + StringUtils.leftPad("" + secondsCounter, 2, "0");
				scheduleStatusShort = nextSleepBlockDetected.type + " [" + minutesRemaining + "m LEFT]";
				nextSleepBlock = nextSleepBlockDetected;
				currentSleepState = nextSleepBlockDetected.type;
				if (!paused)
				{
					triggerEvent("Automatically pausing until " + CommonUtils.convertTimestamp(tims) + " due to sleep block '" + nextSleepBlockDetected.name + "' having started", null, null);
					paused = true;
					pausedUntil = tims;
					pauseReason = (nextSleepBlockDetected.type == ScheduleEntryType.AFK ? "AFK: " : "Sleep block: ") + nextSleepBlockDetected.name;
					pauseIsScheduleRelated = true;
				}
			}
			else
			{
				long tims = nextSleepBlockDetected.nextStartTime;
				// determine second value
				long secondsRemaining = (((tims + 999) - now) / 1000);
				long secondsCounter = secondsRemaining % 60;
				long minutesCounter = (secondsRemaining / 60) % 60;
				long hoursCounter = (secondsRemaining / 60) / 60;
				// determine minute value				
				long minutesRemaining = (((tims + 59999) - now) / 60000);
				// determine status
				String pros = nextActivityWarningID >= NMOConfiguration.INSTANCE.oversleepWarningThreshold ? "OVERSLEEPING" : nextActivityWarningID > 0 ? "MISSING" : "AWAKE";
				// populate the display fields
				scheduleStatusString.set(pros);
				scheduleNextBlockString.set("Next block: " + nextSleepBlockDetected.name);
				scheduleCountdownString.set(nextSleepBlockDetected.type + " IN " + StringUtils.leftPad("" + hoursCounter, 2, "0") + ":" + StringUtils.leftPad("" + minutesCounter, 2, "0") + ":" + StringUtils.leftPad("" + secondsCounter, 2, "0"));
				scheduleStatus = pros + " (" + nextActivityWarningID + ") -- " + nextSleepBlockDetected.name + " STARTS IN " + StringUtils.leftPad("" + hoursCounter, 2, "0") + ":" + StringUtils.leftPad("" + minutesCounter, 2, "0") + ":" + StringUtils.leftPad("" + secondsCounter, 2, "0");
				scheduleStatusShort = pros.equals("AWAKE") ? pros + " [" + minutesRemaining + "m LEFT]" : pros;
				currentSleepState = null;
				if (minutesRemaining <= nextSleepBlockDetected.approachWarning && lastSleepBlockWarning != nextSleepBlockDetected)
				{
					if (nextSleepBlockDetected.approachWarning != -1)
					{
						String[] triggerArray = null;
						switch (nextSleepBlockDetected.type)
						{
						case CORE:
							triggerArray = NMOConfiguration.INSTANCE.events.coreApproaching;
							break;
						case NAP:
							triggerArray = NMOConfiguration.INSTANCE.events.napApproaching;
							break;
						case SIESTA:
							triggerArray = NMOConfiguration.INSTANCE.events.siestaApproaching;
							break;
						case AFK:
							triggerArray = NMOConfiguration.INSTANCE.events.afkApproaching;
							break;
						}
						triggerEvent(minutesRemaining + " minute" + (minutesRemaining == 1 ? "" : "s") + " until " + nextSleepBlockDetected.type + ": " + nextSleepBlockDetected.name, triggerArray, null);
					}
					lastSleepBlockWarning = nextSleepBlockDetected;
				}
			}
		}
		else
		{
			String pros = nextActivityWarningID >= NMOConfiguration.INSTANCE.oversleepWarningThreshold ? "OVERSLEEPING" : nextActivityWarningID > 0 ? "MISSING" : "AWAKE";
			scheduleStatusString.set(pros);
			scheduleStatus = pros + " (" + nextActivityWarningID + ")";
			scheduleStatusShort = pros;
		}
		if (nextSleepBlock != null && (nextSleepBlock != nextSleepBlockDetected || (nextSleepBlock == nextSleepBlockDetected && currentSleepState == null && lastSleepState != null)))
		{
			String[] triggerArray = null;
			switch (nextSleepBlock.type)
			{
			case CORE:
				triggerArray = NMOConfiguration.INSTANCE.events.coreEnded;
				break;
			case NAP:
				triggerArray = NMOConfiguration.INSTANCE.events.napEnded;
				break;
			case SIESTA:
				triggerArray = NMOConfiguration.INSTANCE.events.siestaEnded;
				break;
			case AFK:
				triggerArray = NMOConfiguration.INSTANCE.events.afkEnded;
				break;
			}
			triggerEvent("Exiting " + nextSleepBlock.type + ": " + nextSleepBlock.name, triggerArray, null);
		}
		if (lastSleepState != null && currentSleepState == null && lastSleepBlockWarning == nextSleepBlock && NMOConfiguration.INSTANCE.schedule.size() == 1)
		{
			lastSleepBlockWarning = null; // allows for only 1 sleep block
		}
		lastSleepState = currentSleepState;
		boolean wasPaused = isCurrentlyPaused.get();
		isCurrentlyPaused.set(paused);
		if (!paused && wasPaused)
		{
			if (!pauseIsScheduleRelated)
			{
				Map<String, String[]> parameters = new HashMap<>();
				String context = "Unpaused automatically - time alotted for \"" + pauseReason + "\" has expired";
				parameters.put("context", new String[] { context });
				MainDialog.triggerEvent(context, NMOConfiguration.INSTANCE.events.pauseExpired, parameters);
			}
			pauseReason = "";
			pauseIsScheduleRelated = false;
		}
		if (nextSleepBlockDetected != null && nextSleepBlock != nextSleepBlockDetected)
		{
			if (nextSleepBlock != null)
			{
				nextSleepBlock.updateNextTriggerTime();
			}
			nextSleepBlock = nextSleepBlockDetected;
			long tims = nextSleepBlockDetected.nextStartTime;
			long minutesRemaining = (((tims + 59999) - now) / 60000);
			triggerEvent("The next sleep block is " + nextSleepBlockDetected.name + " which starts in " + minutesRemaining + " minute" + (minutesRemaining == 1 ? "" : "s"), null, null);
		}
		if (paused)
		{
			if (!((MainDialog.scheduleStatusShort.startsWith("CORE ") || MainDialog.scheduleStatusShort.startsWith("NAP ") || MainDialog.scheduleStatusShort.startsWith("SIESTA ")) && pauseReason.startsWith("Sleep block: ")))
			{
				long minutesRemaining = (((pausedUntil + 59999) - now) / 60000);
				scheduleStatusShort = "\"" + pauseReason + "\" [" + minutesRemaining + "m LEFT]";
			}
		}

		for (Integration integration : Main.integrations)
		{
			if (integration.isEnabled())
			{
				try
				{
					integration.update();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}

		if (paused)
		{
			resetActivityTimer(PAUSE_ACTIVITY_SOURCE);
			zombieDetectionPenaltyWait = 0;
			zombieDetectionPenalty = 0;
		}
		if (pendingTimer != null)
		{
			if (pendingTimer != MainDialog.timer)
			{
				resetActivityTimer(TIMER_ACTIVITY_SOURCE);
				this.setNextActivityWarningForTimer(pendingTimer, 0);
				zombieDetectionPenaltyWait = 0;
				zombieDetectionPenalty = 0;
			}
			pendingTimer = null;
		}
		ActivitySource source = lastActivitySourceObject;
		long lastActivityTime_ = source.time;
		String lastActivitySource_ = source.type;
		long timeDiff = paused ? 0 : (now - lastActivityTime_);
		if (paused)
		{
			lastActivityTimeString.set("PAUSED for \"" + pauseReason + "\"");
			timeDiffString.set("   until " + CommonUtils.dateFormatter.get().format(pausedUntil));
		}
		else
		{
			lastActivityTimeString.set("Last: " + CommonUtils.dateFormatter.get().format(lastActivityTime_) + " (" + lastActivitySource_ + ")");
			long nawtd = getNextActivityWarningTimeDiff(nextActivityWarningID);
			if (timeDiff > (1000 * nawtd))
			{
				this.setNextActivityWarningForTimer(timer, timeDiff);
				try
				{
					String pros = nextActivityWarningID >= NMOConfiguration.INSTANCE.oversleepWarningThreshold ? "OVERSLEEPING" : nextActivityWarningID >= 0 ? "MISSING" : "AWAKE";
					// the first time, you get an alternative lighter warning, just in case you forgot to pause
					if (nextActivityWarningID == 1)
					{
						triggerEvent(pros + "(" + nextActivityWarningID + "): No activity detected for " + nawtd + " seconds", NMOConfiguration.INSTANCE.events.activityWarning1, null);
						if (timer.zombiePenaltyLimit > 0)
						{
							zombieDetectionPenalty += timer.zombiePenaltyForFirstWarning * 1000;
							zombieDetectionPenaltyWait += timer.secondsForFirstWarning * 1000;
						}
					}
					else if (nextActivityWarningID >= NMOConfiguration.INSTANCE.oversleepWarningThreshold && !oversleepWarningTriggered)
					{
						triggerEvent(pros + "(" + nextActivityWarningID + "): No activity detected for " + nawtd + " seconds", NMOConfiguration.INSTANCE.events.oversleepWarning, null);
						if (timer.zombiePenaltyLimit > 0)
						{
							zombieDetectionPenalty += timer.zombiePenaltyForOversleepWarning * 1000;
							zombieDetectionPenaltyWait += timer.secondsForSubsequentWarnings * 1000;
						}
						oversleepWarningTriggered = true;
					}
					else
					{
						triggerEvent(pros + "(" + nextActivityWarningID + "): No activity detected for " + nawtd + " seconds", NMOConfiguration.INSTANCE.events.activityWarning2, null);
						if (timer.zombiePenaltyLimit > 0)
						{
							zombieDetectionPenalty += timer.zombiePenaltyForOtherWarnings * 1000;
							zombieDetectionPenaltyWait += timer.secondsForSubsequentWarnings * 1000;
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			String humanlyReadableTimeDiff = String.format("%.3f", timeDiff / 1000.0);
			timeDiffString.set("Time difference: " + humanlyReadableTimeDiff + "s (next warning: " + nawtd + "s)");

			if (timer.zombiePenaltyLimit > 0)
			{
				zombieDetectionPenaltyWait -= zombieWeightDiff;
				if (zombieDetectionPenaltyWait < 0)
				{
					zombieDetectionPenalty = Math.max(0, zombieDetectionPenalty + zombieDetectionPenaltyWait);
					zombieDetectionPenaltyWait = 0;
				}
				if (zombieDetectionPenalty >= (timer.zombiePenaltyLimit * 1000))
				{
					triggerEvent("Zombie penalty limit was reached", NMOConfiguration.INSTANCE.events.zombiePenaltyLimitReached, null);
					zombieDetectionPenalty = timer.zombiePenaltyLimit * 1000 - 1;
				}
			}
			else
			{
				zombieDetectionPenalty = 0;
				zombieDetectionPenaltyWait = 0;
			}
		}
		activeTimerString.set("Active timer:   " + timer.name + " (" + timer.secondsForFirstWarning + "s/" + timer.secondsForSubsequentWarnings + "s)");
		if (timer.zombiePenaltyLimit > 0)
		{
			zombiePenaltyEnableLabel.setTextFill(Color.LIME);
			zombiePenaltyEnableString.set("ENABLED");
			String humanlyReadablePenalty = String.format("%.3f", zombieDetectionPenalty / 1000.0);
			zombiePenaltyAccruedString.set("Current penalty: " + humanlyReadablePenalty + "s");
			zombiePenaltyAccruedString2.set("   (allowed limit is " + timer.zombiePenaltyLimit + "s)");
			String humanlyReadablePenaltyWait = String.format("%.3f", zombieDetectionPenaltyWait / 1000.0);
			zombiePenaltyWaitString.set("Penalty reduction starts in " + humanlyReadablePenaltyWait + "s");
			zombiePenaltyValString1.set("First warning gives " + timer.zombiePenaltyForFirstWarning + "s penalty");
			zombiePenaltyValString2.set("Oversleep warning gives " + timer.zombiePenaltyForOversleepWarning + "s penalty");
			zombiePenaltyValString3.set("Other warnings give " + timer.zombiePenaltyForOtherWarnings + "s penalty");
		}
		else
		{
			zombiePenaltyEnableLabel.setTextFill(Color.RED);
			zombiePenaltyEnableString.set("DISABLED");
			zombiePenaltyAccruedString.set("");
			zombiePenaltyAccruedString2.set("");
			zombiePenaltyWaitString.set("");
			zombiePenaltyValString1.set("");
			zombiePenaltyValString2.set("");
			zombiePenaltyValString3.set("");
		}

		if (NMOConfiguration.INSTANCE.integrations.pavlok.enabled)
		{
			loginTokenValidUntilString.set("Login expires: " + CommonUtils.dateFormatter.get().format(1000 * (NMOConfiguration.INSTANCE.integrations.pavlok.auth.created_at + NMOConfiguration.INSTANCE.integrations.pavlok.auth.expires_in)));
		}

		if (NMOConfiguration.INSTANCE.integrations.philipsHue.enabled)
		{
			String state = "";
			for (String key : IntegrationPhilipsHue.INSTANCE.lightStates.keySet())
			{
				state += (!state.isEmpty() ? "\n" : "");
				int val = IntegrationPhilipsHue.INSTANCE.lightStates.get(key);
				state += key + ":  " + (val == -2 ? "DISCONNECTED" : val == -1 ? "OFF" : "ON (" + String.format("%,.0f", val / 2.54f) + "%)");
			}
			lightingStateString.set(state);
		}

		if (NMOConfiguration.INSTANCE.integrations.tplink.enabled)
		{
			String state = "";
			for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.tplink.devices.length; i++)
			{
				TPLinkDeviceEntry tpde = NMOConfiguration.INSTANCE.integrations.tplink.devices[i];
				state += (!state.isEmpty() ? "\n" : "");
				state += tpde.name + ":  " + (tpde.isSwitchedOn ? "ON" : "OFF");
			}
			tplinkStateString.set(state);
		}

		if (NMOConfiguration.INSTANCE.integrations.wemo.enabled)
		{
			String state = "";
			for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.wemo.devices.length; i++)
			{
				WemoDeviceEntry wemodevice = NMOConfiguration.INSTANCE.integrations.wemo.devices[i];
				state += (!state.isEmpty() ? "\n" : "");
				state += wemodevice.name + ":  " + (wemodevice.isSwitchedOn ? "ON" : "OFF");
			}
			wemoStateString.set(state);
		}

		if (NMOConfiguration.INSTANCE.integrations.webUI.enabled)
		{
			WebcamWebSocketHandler[] sockets = WebcamCapture.webcams[0].getConnections();
			String socketString = sockets.length + " active web sockets";
			for (int i = 0; i < sockets.length; i++)
			{
				socketString += (i == 0 ? ":\n" : "\n") + sockets[i].udesc + " @ " + sockets[i].connectionIP;
			}
			webMonitoringString.set(socketString);
			try
			{
				BufferedImage img = WebcamCapture.getImage();
				if (img != null && tick % 4 < 2)
				{
					writableImage = SwingFXUtils.toFXImage(img, writableImage);
					img.flush();
					lastWebcamImage.set(writableImage);
				}
				webcamName.set(WebcamCapture.getCameraName());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		// trigger custom events
		if (!customEvents.isEmpty())
		{
			CustomEvent event = customEvents.get(0);
			while (event.nextTriggerTime < now)
			{
				triggerEvent("Custom event: " + event.name, event.actions, null);
				event.updateNextTriggerTime();
				triggerEvent(event.name + " will next occur on " + CommonUtils.convertTimestamp(event.nextTriggerTime), null, null);
				Collections.sort(customEvents);
				event = customEvents.get(0);
			}
		}
	}

	private void setNextActivityWarningForTimer(ActivityTimer activityWarningTimer, long timeDiff)
	{
		MainDialog.timer = activityWarningTimer;
		if (timeDiff == 0)
		{
			nextActivityWarningID = 0;
			oversleepWarningTriggered = false;
		}
		else if (timeDiff == -1)
		{
			timeDiff = now - lastActivitySourceObject.time;
		}
		long awid = 0;
		while (timeDiff > (1000 * getNextActivityWarningTimeDiff(awid))) // fixes shortening the gap in the middle of inactivity causing massive warning spam
		{
			awid++;
		}
		nextActivityWarningID = awid;
	}

	public static long getNextActivityWarningTimeDiff(long awid)
	{
		return timer.secondsForFirstWarning + (awid * timer.secondsForSubsequentWarnings);
	}

	public static void resetActivityTimer(ActivitySource source)
	{
		source.time = now;
		lastActivitySourceObject = source;
		nextActivityWarningID = 0;
		oversleepWarningTriggered = false;
	}

	public static void triggerEvent(String eventDescription, String[] actionArray, Map<String, String[]> parameters)
	{
		ArrayList<Action> actionsByLookup = new ArrayList<>();
		String actionString = "";
		if (actionArray != null)
		{
			actionLoop: for (String action : actionArray)
			{
				for (Integration integration : Main.integrations)
				{
					LinkedHashMap<String, Action> iactions = integration.getActions();
					Action aaction = iactions.get(action);
					if (aaction != null)
					{
						actionsByLookup.add(aaction);
						actionString += (actionString.isEmpty() ? "" : ", ") + aaction.getName();
						continue actionLoop;
					}
				}
			}
		}
		final String eventString = (actionString.isEmpty() ? "" : "<" + actionString + "> ") + eventDescription;
		log.info("APPEVENT: " + eventString);
		Platform.runLater(new Runnable()
		{
			@Override
			public void run()
			{
				events.add(CommonUtils.dateFormatter.get().format(now) + ": " + eventString);
			}
		});
		if (actionArray != null)
		{
			for (Action aaction : actionsByLookup)
			{
				try
				{
					aaction.onAction(parameters);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
