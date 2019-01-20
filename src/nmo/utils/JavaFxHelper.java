package nmo.utils;

import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.GlyphsDude;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class JavaFxHelper
{
	public static StackPane createRootPane()
	{
		StackPane root = new StackPane();
		root.setStyle("-fx-background-color: black");
		return root;
	}

	public static HBox createHorizontalBox(double width, double height)
	{
		HBox box = new HBox();
		box.setMinWidth(width);
		box.setMaxWidth(width);
		box.setMinHeight(height);
		box.setMaxHeight(height);
		return box;
	}

	public static HBox createHorizontalBox(double width, double height, String style)
	{
		HBox box = createHorizontalBox(width, height);
		box.setStyle(style);
		return box;
	}

	public static HBox createHorizontalBox(double width, double height, Insets insets)
	{
		HBox box = createHorizontalBox(width, height);
		box.setPadding(insets);
		return box;
	}

	public static HBox createHorizontalBox(double width, double height, String style, Insets insets)
	{
		HBox box = createHorizontalBox(width, height, style);
		box.setPadding(insets);
		return box;
	}

	public static HBox createHorizontalBox(double width, double height, double translateX, double translateY, String style)
	{
		HBox box = createHorizontalBox(width, height, style);
		box.setTranslateX(translateX);
		box.setTranslateY(translateY);
		return box;
	}

	public static HBox createHorizontalBox(double width, double height, double translateX, double translateY, String style, Insets insets)
	{
		HBox box = createHorizontalBox(width, height, translateX, translateY, style);
		box.setPadding(insets);
		return box;
	}

	public static HBox createHorizontalBox(double width, double height, double translateX, double translateY, String style, Pos alignment, Insets insets)
	{
		HBox box = createHorizontalBox(width, height, translateX, translateY, style, insets);
		box.setAlignment(alignment);
		return box;
	}

	public static VBox createVerticalBox(double width, double height)
	{
		VBox box = new VBox();
		box.setMinWidth(width);
		box.setMaxWidth(width);
		box.setMinHeight(height);
		box.setMaxHeight(height);
		return box;
	}

	public static VBox createVerticalBox(double width, double height, String style)
	{
		VBox box = createVerticalBox(width, height);
		box.setStyle(style);
		return box;
	}

	public static VBox createVerticalBox(double width, double height, Insets insets)
	{
		VBox box = createVerticalBox(width, height);
		box.setPadding(insets);
		return box;
	}

	public static VBox createVerticalBox(double width, double height, String style, Insets insets)
	{
		VBox box = createVerticalBox(width, height, style);
		box.setPadding(insets);
		return box;
	}

	public static VBox createVerticalBox(double width, double height, double translateX, double translateY, String style)
	{
		VBox box = createVerticalBox(width, height, style);
		box.setTranslateX(translateX);
		box.setTranslateY(translateY);
		return box;
	}

	public static VBox createVerticalBox(double width, double height, double translateX, double translateY, String style, Insets insets)
	{
		VBox box = createVerticalBox(width, height, translateX, translateY, style);
		box.setPadding(insets);
		return box;
	}

	public static VBox createVerticalBox(double width, double height, double translateX, double translateY, String style, Pos alignment, Insets insets)
	{
		VBox box = createVerticalBox(width, height, translateX, translateY, style, insets);
		box.setAlignment(alignment);
		return box;
	}

	public static StackPane createStackPane(double width, double height)
	{
		StackPane pane = new StackPane();
		pane.setMinWidth(width);
		pane.setMaxWidth(width);
		pane.setMinHeight(height);
		pane.setMaxHeight(height);
		return pane;
	}

	public static StackPane createStackPane(double width, double height, String style)
	{
		StackPane pane = createStackPane(width, height);
		pane.setStyle(style);
		return pane;
	}

	public static StackPane createStackPane(double width, double height, Insets padding)
	{
		StackPane pane = createStackPane(width, height);
		pane.setPadding(padding);
		return pane;
	}

	public static StackPane createStackPane(double width, double height, Pos alignment)
	{
		StackPane pane = createStackPane(width, height);
		pane.setAlignment(alignment);
		return pane;
	}

	public static StackPane createStackPane(double width, double height, String style, Pos alignment)
	{
		StackPane pane = createStackPane(width, height, style);
		pane.setAlignment(alignment);
		return pane;
	}

	public static BorderPane createBorderPane(double width, double height)
	{
		BorderPane pane = new BorderPane();
		pane.setMinWidth(width);
		pane.setMaxWidth(width);
		pane.setMinHeight(height);
		pane.setMaxHeight(height);
		return pane;
	}

	public static Rectangle createRectangle(double width, double height, Paint fill)
	{
		Rectangle rectangle = new Rectangle(width, height);
		rectangle.setFill(fill);
		return rectangle;
	}

	public static Rectangle createRectangle(double width, double height, double arcWidth, double arcHeight)
	{
		Rectangle rectangle = new Rectangle(width, height);
		rectangle.setArcWidth(arcWidth);
		rectangle.setArcHeight(arcHeight);
		return rectangle;
	}

	public static Rectangle createRectangle(double width, double height, double arcWidth, double arcHeight, Paint fill)
	{
		Rectangle rectangle = createRectangle(width, height, arcWidth, arcHeight);
		rectangle.setFill(fill);
		return rectangle;
	}

	public static Rectangle createRectangle(double width, double height, double arcWidth, double arcHeight, Paint fill, Paint stroke)
	{
		Rectangle rectangle = createRectangle(width, height, arcWidth, arcHeight, fill);
		rectangle.setStroke(stroke);
		return rectangle;
	}

	public static Separator createSeparator(Orientation orientation, double width, Insets padding)
	{
		Separator separator = new Separator(orientation);
		separator.setPrefWidth(width);
		separator.setPadding(padding);
		return separator;
	}

	public static Label createLabel(String text, Paint colour)
	{
		Label label = new Label(text);
		label.setTextFill(colour);
		return label;
	}

	public static Label createLabel(String text, Paint colour, String style)
	{
		Label label = createLabel(text, colour);
		label.setStyle(style);
		return label;
	}

	public static Label createLabel(String text, Paint colour, String style, Insets insets)
	{
		Label label = createLabel(text, colour, style);
		label.setPadding(insets);
		return label;
	}

	public static Label createLabel(String text, Paint colour, String style, Insets insets, double width, double height)
	{
		Label label = createLabel(text, colour, style, insets);
		label.setMinWidth(width);
		label.setMaxWidth(width);
		label.setMinHeight(height);
		label.setMaxHeight(height);
		return label;
	}

	public static CheckBox createCheckbox(String text, Paint colour)
	{
		CheckBox checkbox = new CheckBox(text);
		checkbox.setTextFill(colour);
		return checkbox;
	}

	public static Button createButton()
	{
		final Button button = new Button();
		button.setOnKeyPressed(new EventHandler<KeyEvent>()
		{
			@Override
			public void handle(KeyEvent evt)
			{
				if (evt.getCode() == KeyCode.ENTER)
					button.fire();
			}
		});
		return button;
	}

	public static Button createButton(String text)
	{
		Button button = createButton();
		button.setText(text);
		return button;
	}

	public static Button createButton(String text, Node graphic)
	{
		Button button = createButton(text);
		button.setGraphic(graphic);
		return button;
	}

	public static Button createButton(String text, Node graphic, String style, Insets insets)
	{
		Button button = createButton(text);
		button.setGraphic(graphic);
		button.setStyle(style);
		button.setPadding(insets);
		return button;
	}

	public static Button createButtonMinW(String text, double width)
	{
		Button button = createButton();
		button.setText(text);
		button.setMinWidth(width);
		return button;
	}

	public static Button createButtonMaxW(String text, double width)
	{
		Button button = createButton();
		button.setText(text);
		button.setMaxWidth(width);
		return button;
	}

	public static TextField createTextField(String promptText)
	{
		TextField field = new TextField();
		field.setPromptText(promptText);
		return field;
	}

	public static PasswordField createPasswordField(String promptText)
	{
		PasswordField field = new PasswordField();
		field.setPromptText(promptText);
		return field;
	}

	public static ProgressIndicator createSwirlingProgressIndicator(double width, double height)
	{
		ProgressIndicator indicator = new ProgressIndicator();
		indicator.setStyle("-fx-progress-color: white;");
		indicator.setMinWidth(width);
		indicator.setMaxWidth(width);
		indicator.setMinHeight(height);
		indicator.setMaxHeight(height);
		return indicator;
	}

	public static ScrollPane createScrollPane(double width, double height, String style, String styleClass, ScrollBarPolicy hPolicy, ScrollBarPolicy vPolicy, boolean pannable)
	{
		ScrollPane pane = new ScrollPane();
		pane.setMinWidth(width);
		pane.setMaxWidth(width);
		pane.setMinHeight(height);
		pane.setMaxHeight(height);
		pane.setStyle(style);
		pane.getStyleClass().add(styleClass);
		pane.hbarPolicyProperty().set(hPolicy);
		pane.vbarPolicyProperty().set(vPolicy);
		pane.pannableProperty().set(pannable);
		return pane;
	}

	public static ImageView createImageView(String imagePath, double width, double height)
	{
		ImageView view = imagePath == null ? new ImageView() : new ImageView(imagePath);
		view.setFitWidth(width);
		view.setFitHeight(height);
		return view;
	}

	public static ImageView createImageView(String imagePath, double width, double height, double arcWidth, double arcHeight)
	{
		ImageView view = createImageView(imagePath, width, height);
		view.setClip(createRectangle(width, height, arcWidth, arcHeight));
		return view;
	}

	public static ImageView createImageView(String imagePath, double width, double height, String style)
	{
		ImageView view = createImageView(imagePath, width, height);
		view.setStyle(style);
		return view;
	}

	public static Text createIcon(GlyphIcons icon, String iconSize, Paint colour)
	{
		Text iconLabel = GlyphsDude.createIcon(icon, iconSize);
		iconLabel.setFill(colour);
		return iconLabel;
	}

	public static Label createIconLabel(GlyphIcons icon, String iconSize, String text, ContentDisplay contentDisplay, Paint colour, String style)
	{
		Text iconLabel = GlyphsDude.createIcon(icon, iconSize);
		iconLabel.setFill(colour);
		Label label = new Label(text);
		label.setTextFill(colour);
		label.setStyle(style);
		label.setGraphic(iconLabel);
		label.setContentDisplay(contentDisplay);
		return label;
	}

	public static Label createIconLabel(GlyphIcons icon, String iconSize, String text, ContentDisplay contentDisplay, Paint colour, String style, Pos alignment, int width)
	{
		Text iconLabel = GlyphsDude.createIcon(icon, iconSize);
		iconLabel.setFill(colour);
		Label label = new Label(text);
		label.setTextFill(colour);
		label.setStyle(style);
		label.setGraphic(iconLabel);
		label.setContentDisplay(contentDisplay);
		label.setMinWidth(width);
		label.setMaxWidth(width);
		label.setAlignment(alignment);
		return label;
	}

	public static Label createIconLabel(GlyphIcons icon, String iconSize, String text, ContentDisplay contentDisplay, Paint colour, String style, Insets insets)
	{
		Label label = createIconLabel(icon, iconSize, text, contentDisplay, colour, style);
		label.setPadding(insets);
		return label;
	}

	public static Label createIconLabel(GlyphIcons icon, String iconSize, String text, ContentDisplay contentDisplay, Paint iconColour, Paint textColour, String style)
	{
		Text iconLabel = GlyphsDude.createIcon(icon, iconSize);
		iconLabel.setFill(iconColour);
		Label label = new Label(text);
		label.setTextFill(textColour);
		label.setStyle(style);
		label.setGraphic(iconLabel);
		label.setContentDisplay(contentDisplay);
		return label;
	}

	public static Label createIconLabel(GlyphIcons icon, String iconSize, String text, ContentDisplay contentDisplay, Paint iconColour, Paint textColour, String style, Insets insets)
	{
		Label label = createIconLabel(icon, iconSize, text, contentDisplay, iconColour, textColour, style);
		label.setPadding(insets);
		return label;
	}

	public static StackPane createStretchingImagePane(final String image, final Scene scene)
	{
		// the amount of work required to emulate the GinENGINE background scaler was quite excessive :/

		final ImageView backgroundImageView = new ImageView(JavaFxHelper.buildResourcePath(image));
		final double bgWidth = backgroundImageView.getImage().getWidth();
		final double bgHeight = backgroundImageView.getImage().getHeight();
		final SimpleDoubleProperty scaledWidth = new SimpleDoubleProperty();
		final SimpleDoubleProperty scaledHeight = new SimpleDoubleProperty();
		ChangeListener<? super Number> chl = new ChangeListener<Number>()
		{
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
			{
				double scalingFactor = Math.max((scene.widthProperty().get() / bgWidth), (scene.heightProperty().get() / bgHeight));
				scaledWidth.set(bgWidth * scalingFactor);
				scaledHeight.set(bgHeight * scalingFactor);
			}
		};
		scene.widthProperty().addListener(chl);
		scene.heightProperty().addListener(chl);
		chl.changed(null, null, null);
		backgroundImageView.setPreserveRatio(true);
		backgroundImageView.fitWidthProperty().bind(scaledWidth);
		backgroundImageView.fitHeightProperty().bind(scaledHeight);
		StackPane backgroundImageStack = new StackPane();
		backgroundImageStack.getChildren().add(backgroundImageView);
		return backgroundImageStack;
	}

	public static BorderPane createCenterLoadingBox(final int height)
	{
		BorderPane pane = createBorderPane(450, height);
		pane.setStyle("-fx-background-color: rgba(20, 20, 20, 0.6); -fx-background-radius: 10px; -fx-border-radius: 10px; -fx-border-color: rgba(255, 255, 255, 0.1); -fx-border-width: 2px;");
		pane.setPadding(new Insets(10));
		return pane;
	}

	public static String buildResourcePath(String string)
	{
		try
		{
			return ClassLoader.getSystemClassLoader().getResource("resources/" + string).toExternalForm();
		}
		catch (Throwable t)
		{
			throw new RuntimeException("Bad URL", t);
		}
	}

	public static Hyperlink createHyperlink(final String string)
	{
		Hyperlink hyperlink = new Hyperlink(string);
		hyperlink.setPadding(new Insets(0, 0, 0, 1));
		hyperlink.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				try
				{
					DesktopHelper.browse(string);
				}
				catch (Throwable e)
				{
					e.printStackTrace();
				}
			}
		});
		return hyperlink;
	}
}
