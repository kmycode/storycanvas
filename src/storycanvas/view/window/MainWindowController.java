/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storycanvas.view.window;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import storycanvas.model.story.Story;
import storycanvas.view.node.EntityEditorNode;

/**
 * メインウィンドウのコントローラクラス
 * @author KMY
 */
public class MainWindowController implements Initializable {

	@FXML
	private Label label;

	@FXML
	private EntityEditorNode entityEditor;

	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO: ビジネスロジック部からリスナーを送る処理に置き換える予定
		this.entityEditor.editEntity(Story.getCurrent().getPersons().get(0));
	}

}
