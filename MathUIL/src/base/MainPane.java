package base;

import fxutils.*;
import javafx.animation.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.util.*;

/**
 * @author Sam Hooper
 *
 */
public class MainPane extends StackPane {
	
	/**
	 * 
	 */
	private static final double SETTINGS_SCREEN_PERCENT = 0.25;
	private final ProblemPane problemPane;
	private final Pane settingsLayer;
	private final SettingsPane settingsPane;
	private final Animation settingsEnterAnimation ,settingsExitAnimation;
	private final Button settingsButton;
	
	private boolean settingsAnimationPlaying, settingsShowing;
	
	public MainPane() {
		super();
		this.problemPane = new ProblemPane(CompositeProblemSupplier.of(new AdditionProblemSupplier(2, 2, 2, 2)));
		this.settingsPane = new SettingsPane(this);
		settingsPane.maxWidthProperty().bind(this.widthProperty().divide(4));
		settingsPane.prefWidthProperty().bind(settingsPane.maxWidthProperty());
		settingsPane.maxHeightProperty().bind(this.heightProperty());
		settingsPane.prefHeightProperty().bind(settingsPane.maxHeightProperty());
		settingsPane.setVisible(false);
		settingsEnterAnimation = new Transition() {
			{
				setCycleDuration(Duration.millis(1000));
				setOnFinished(actionEvent -> {
					settingsShowing = true;
					settingsAnimationPlaying = false;
				});
			}
			@Override
			protected void interpolate(double frac) {
				double x = -(MainPane.this.getWidth() * SETTINGS_SCREEN_PERCENT * (1 - frac));
				settingsPane.setLayoutX(x);
			}
		};
		
		settingsExitAnimation = new Transition() {
			{
				setCycleDuration(Duration.millis(1000));
				setOnFinished(actionEvent -> {
					settingsShowing = false;
					settingsAnimationPlaying = false;
					settingsPane.setVisible(false);
				});
			}
			@Override
			protected void interpolate(double frac) {
				frac = 1 - frac;
				double x = -(MainPane.this.getWidth() * SETTINGS_SCREEN_PERCENT * (1 - frac));
				settingsPane.setLayoutX(x);
			}
		};
		
		settingsButton = Buttons.of("SET", () -> {
			if(settingsAnimationPlaying)
				return;
			if(!settingsShowing) {
				settingsPane.setVisible(true);
				settingsPane.setLayoutX(-this.getWidth() * SETTINGS_SCREEN_PERCENT);
				settingsAnimationPlaying = true;
				settingsEnterAnimation.play();
			}
			else{
				settingsPane.setLayoutX(0);
				settingsAnimationPlaying = true;
				settingsExitAnimation.play();
			}
		});
		settingsButton.setLayoutX(10);
		settingsButton.setLayoutY(10);
		
		settingsPane.layoutYProperty().bind(settingsButton.heightProperty().add(settingsButton.layoutYProperty()));
		this.settingsLayer = new Pane(settingsPane, settingsButton);
		this.settingsLayer.setPickOnBounds(false);
		getChildren().addAll(problemPane,settingsLayer);
	}
	
	public ProblemPane getProblemPane() {
		return problemPane;
	}
}
