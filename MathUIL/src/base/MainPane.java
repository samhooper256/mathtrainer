package base;

import javafx.animation.*;
import javafx.animation.Animation.Status;
import javafx.scene.layout.*;
import javafx.util.Duration;
import suppliers.*;
import suppliers.bases.*;
import suppliers.divisors.*;
import suppliers.equations.*;
import suppliers.factorials.*;
import suppliers.fractions.*;
import suppliers.other.*;
import suppliers.pemdas.*;
import suppliers.sequences.*;
import suppliers.sets.*;
import utils.*;

/**
 * @author Sam Hooper
 *
 */
public class MainPane extends StackPane {

	/**
	 * in milliseconds
	 */
	private static final int SETTINGS_ENTER_DURATION = 800;
	/**
	 * in milliseconds
	 */
	private static final int SETTINGS_EXIT_DURATION = 800;
	private static final int SETTINGS_WHEEL_INSET = 10;
	private static final int SETTINGS_WHEEL_SIZE = 30;

	private final ProblemPane problemPane;
	private final Pane settingsLayer, chooserLayer;
	private final SettingsPane settingsPane;
	private final SupplierChooser supplierChooser;
	private final Animation settingsEnterAnimation, settingsExitAnimation;
	private final ImageWrap settingsWheel;
	private final StackPane settingsWheelStackPane;

	public MainPane() {
		super();
		problemPane = new ProblemPane(CompositeProblemSupplier.of(new CartesianProductSupplier()));
		settingsPane = new SettingsPane(this);
		settingsEnterAnimation = createSettingsEnterAnimation();
		settingsExitAnimation = createSettingsExitAnimation();
		settingsWheel = new ImageWrap(Images.getImage("SettingsWheel.png"), 10, 10);
		settingsWheelStackPane = new StackPane(settingsWheel);
		settingsLayer = new Pane(settingsPane, settingsWheelStackPane);
		initSettings();

		supplierChooser = new SupplierChooser(this);
		chooserLayer = new StackPane();
		initChooser();
		getChildren().addAll(problemPane, settingsLayer, chooserLayer);
	}

	private void initSettings() {
		settingsPane.maxHeightProperty().bind(this.heightProperty().subtract(settingsPane.layoutYProperty()));
		settingsPane.setVisible(false);
//		settingsPane.prefWidthProperty().bind(this.widthProperty());

		settingsWheel.setPickOnBounds(true); // we want the user to still be able to open the settings even
		// if they click on one of the transparent parts of the settings wheel image.
		settingsWheel.setOnMouseClicked(mouseEvent -> {
			if(settingsAnimationPlaying())
				return;
			if(!settingsPane.isVisible()) {
				settingsPane.setLayoutX(-this.getWidth() * settingsPane.getWidth());
				settingsPane.setVisible(true);
				settingsEnterAnimation.play();
			}
			else {
				settingsPane.setLayoutX(0);
				settingsExitAnimation.play();
			}
		});

		settingsWheelStackPane.setPrefSize(SETTINGS_WHEEL_SIZE, SETTINGS_WHEEL_SIZE);

		settingsWheelStackPane.setLayoutX(SETTINGS_WHEEL_INSET);
		settingsWheelStackPane.setLayoutY(SETTINGS_WHEEL_INSET);

		settingsPane.layoutYProperty()
				.bind(settingsWheelStackPane.layoutYProperty().add(settingsWheelStackPane.heightProperty()).add(SETTINGS_WHEEL_INSET));

		settingsLayer.setPickOnBounds(false);
	}
	
	private boolean settingsAnimationPlaying() {
		return settingsEnterAnimation.getStatus() == Status.RUNNING || settingsExitAnimation.getStatus() == Status.RUNNING;
	}
	
	private Transition createSettingsExitAnimation() {
		return new Transition() {
			{
				setCycleDuration(Duration.millis(SETTINGS_EXIT_DURATION));
				setOnFinished(actionEvent -> {
					settingsPane.setVisible(false);
				});
			}

			@Override
			protected void interpolate(double frac) {
				frac = Math.sqrt(frac);
				frac = 1 - frac;
				double x = -(settingsPane.getWidth() * (1 - frac));
				settingsWheel.setRotate(frac * 100);
				settingsPane.setLayoutX(x);
			}
		};
	}

	private Transition createSettingsEnterAnimation() {
		return new Transition() {
			{
				setCycleDuration(Duration.millis(SETTINGS_ENTER_DURATION));
			}

			@Override
			protected void interpolate(double frac) {
				frac = Math.sqrt(frac);
				double x = -(settingsPane.getWidth() * (1 - frac));
				settingsWheel.setRotate(frac * 100);
				settingsPane.setLayoutX(x);
			}
		};
	}

	private void initChooser() {
		hideChooser();
		chooserLayer.getChildren().add(supplierChooser);
		supplierChooser.maxWidthProperty().bind(this.widthProperty().divide(2));
		supplierChooser.maxHeightProperty().bind(this.heightProperty().divide(2));
	}

	public ProblemPane getProblemPane() {
		return problemPane;
	}

	public SettingsPane getSettingsPane() {
		return settingsPane;
	}

	public void showChooser() {
		problemPane.setMouseTransparent(true);
		settingsLayer.setMouseTransparent(true);
		chooserLayer.setMouseTransparent(false);
		chooserLayer.setPickOnBounds(true);
		supplierChooser.show();
	}

	public void hideChooser() {
		problemPane.setMouseTransparent(false);
		settingsLayer.setMouseTransparent(false);
		chooserLayer.setMouseTransparent(true);
		supplierChooser.hide();
	}

	public void addSupplierOrThrow(ProblemSupplier supplier) {
		problemPane.addSupplierOrThrow(supplier);
	}
}
