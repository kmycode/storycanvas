/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package storycanvas.view.window;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import net.kmycode.javafx.Messenger;
import org.dockfx.DockNode;
import storycanvas.StoryCanvas;
import storycanvas.message.dialog.file.ShowDirectoryPickerMessage;
import storycanvas.message.dialog.file.ShowOpenFilePickerMessage;
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

	@FXML
	private DockNode entityEditorDockNode;

	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// 自分がメインのエディタであることを教える
		this.entityEditor.toMain();

		//this.entityEditorDockNode.setMaxWidth(300.0);

		// ダイアログ表示のメッセージを受け取る
		Messenger.getInstance().apply(ShowOpenFilePickerMessage.class, this, m -> {
			FileChooser fc = new FileChooser();
			fc.setTitle("ファイルを開く");
			fc.setInitialDirectory(new File(m.getDefaultPath()));
			fc.showOpenDialog(StoryCanvas.getMainStage());
		});
		Messenger.getInstance().apply(ShowDirectoryPickerMessage.class, this, m -> {
			DirectoryChooser dc = new DirectoryChooser();
			dc.setTitle("ディレクトリを開く");
			if (m.getDefaultPath() != null) {
				dc.setInitialDirectory(new File(m.getDefaultPath()));
			}
			File f = dc.showDialog(StoryCanvas.getMainStage());
			if (f != null) {
				m.selectedFileNameProperty().set(f.getPath());
			} else {
				m.selectedFileNameProperty().set("");
			}
		});
	}

}
