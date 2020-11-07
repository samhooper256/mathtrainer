package base;

import fxutils.*;
import javafx.animation.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.util.*;
import suppliers.*;
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
	
	private boolean settingsAnimationPlaying, settingsShowing;
	
	public MainPane() {
		super();
		this.problemPane = new ProblemPane(CompositeProblemSupplier.of(new IntAddSubtractSupplier(2, 2, 2, 2)));
		this.settingsPane = new SettingsPane(this);
		this.settingsEnterAnimation = createSettingsEnterAnimation();
		this.settingsExitAnimation = createSettingsExitAnimation();
		this.settingsWheel = new ImageWrap(Images.getImage("SettingsWheel.png"), 10, 10);
		this.settingsWheelStackPane = new StackPane(settingsWheel);
		this.settingsLayer = new Pane(settingsPane, settingsWheelStackPane);
		initSettings();
		
		this.supplierChooser = new SupplierChooser(this);
		this.chooserLayer = new StackPane();
		initChooser();
		getChildren().addAll(problemPane, settingsLayer, chooserLayer);
	}

	private void initSettings() {
		settingsPane.maxHeightProperty().bind(this.heightProperty().subtract(settingsPane.layoutYProperty()));
//		settingsPane.prefHeightProperty().bind(settingsPane.maxHeightProperty());
		settingsPane.setVisible(false);
		
		
		settingsWheel.setPickOnBounds(true); //we want the user to still be able to open the settings even
		//if they click on one of the transparent parts of the settings wheel image.
		settingsWheel.setOnMouseClicked(mouseEvent -> {
			if(settingsAnimationPlaying)
				return;
			if(!settingsShowing) {
				settingsPane.setVisible(true);
				settingsPane.setLayoutX(-this.getWidth() * settingsPane.getWidth());
				settingsAnimationPlaying = true;
				settingsEnterAnimation.play();
			}
			else{
				settingsPane.setLayoutX(0);
				settingsAnimationPlaying = true;
				settingsExitAnimation.play();
			}
		});
		
		
		settingsWheelStackPane.setPrefSize(30,SETTINGS_WHEEL_SIZE);
		
		settingsWheelStackPane.setLayoutX(SETTINGS_WHEEL_INSET);
		settingsWheelStackPane.setLayoutY(10);
		
		
		settingsPane.layoutYProperty().bind(settingsWheelStackPane.layoutYProperty().add(settingsWheelStackPane.heightProperty()).add(10));
		
		this.settingsLayer.setPickOnBounds(false);
	}

	private Transition createSettingsExitAnimation() {
		return new Transition() {
			{
				setCycleDuration(Duration.millis(SETTINGS_EXIT_DURATION));
				setOnFinished(actionEvent -> {
					settingsShowing = false;
					settingsAnimationPlaying = false;
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
				setOnFinished(actionEvent -> {
					settingsShowing = true;
					settingsAnimationPlaying = false;
				});
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
		chooserLayer.getChildren().add(supplierChooser);
		hideChooser();
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
