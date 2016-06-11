/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storycanvas;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.dockfx.DockPane;
import storycanvas.model.story.Story;

/**
 *
 * @author KMY
 */
public class StoryCanvas extends Application {

	private static Stage mainStage;
	private static StoryCanvas mainObject;

	@Override
	public void start (Stage stage) throws Exception {
		mainObject = this;

		Parent root = FXMLLoader.load(getClass().getResource("view/window/MainWindow.fxml"));

		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.show();
		mainStage = stage;

		// DockPaneのスタイルシートを初期化
		DockPane.initializeDefaultUserAgentStylesheet();

		// ウィンドウタイトルをバインディング
		this.storyTitle.addListener(e -> {
			mainStage.setTitle("StoryCanvas 1.0.0.dev - [" + this.storyTitle.get() + "]");
		});
		this.storyTitle.set("");

		// ストーリーを新規作成
		Story.setCurrent(new Story());
	}

	public static Stage getMainStage() {
		return mainStage;
	}

	public static StoryCanvas getMainObject() {
		return mainObject;
	}

	private final StringProperty storyTitle = new SimpleStringProperty();

	public void setStoryTitle(StringProperty value) {
		this.storyTitle.unbind();
		if (value != null) {
			this.storyTitle.bind(value);
		}
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main (String[] args) {
		launch(args);
	}

}
