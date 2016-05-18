/*
 * Copyright (C) 2016 KMY
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package storycanvas.view.node;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import storycanvas.model.entity.Entity;
import storycanvas.model.entity.Person;

/**
 * エンティティの編集画面のノード
 *
 * @author KMY
 */
public class EntityEditorNode extends ScrollPane implements Initializable {

	@FXML
	private ImageView entityIcon;

	@FXML
	private Label entityName;

	@FXML
	private Pane personEditor;

	/**
	 * 初期化.
	 */
	public EntityEditorNode() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EntityEditorNode.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}
	
	/**
	 * すべてのエンティティを編集するときの共通処理
	 * @param entity 編集するエンティティ
	 */
	private void editAbstractEntity(Entity entity) {
		this.entityIcon.setImage(entity.getIcon());
		this.entityName.textProperty().bind(entity.nameProperty());
		this.hideAllEntityEditor();
	}

	/**
	 * すべての編集画面を隠す.
	 */
	private void hideAllEntityEditor() {
		this.personEditor.setVisible(false);
	}

	/**
	 * 人物を編集
	 * @param entity 編集する人物クラス
	 */
	public void editEntity(Person entity) {
		this.editAbstractEntity(entity);
		this.personEditor.setVisible(true);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
	}

}
