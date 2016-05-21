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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import net.kmycode.javafx.Messenger;
import storycanvas.message.entity.edit.EmptyEditMessage;
import storycanvas.message.entity.edit.PersonEditMessage;
import storycanvas.model.entity.Entity;
import storycanvas.model.entity.Person;
import storycanvas.view.part.editor.EntityBaseEditorController;
import storycanvas.view.part.editor.EntityMemoEditorController;
import storycanvas.view.part.editor.PersonEditorController;

/**
 * エンティティの編集画面のノード
 *
 * @author KMY
 */
public class EntityEditorNode extends ScrollPane implements Initializable {

	/**
	 * 生成されているノード一覧.
	 */
	private static final List<EntityEditorNode> nodes = new ArrayList<>();
	private static EntityEditorNode mainNode;

	@FXML
	private TabPane editorTabPane;

	@FXML
	private Tab informationTab;

	@FXML
	private ImageView entityIcon;

	@FXML
	private Label entityName;

	@FXML
	private EntityBaseEditorController entityBaseEditorController;

	@FXML
	private EntityMemoEditorController entityMemoEditorController;

	@FXML
	private PersonEditorController personEditorController;

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

		// デフォルトで表示されるタブ
		this.editorTabPane.getSelectionModel().select(this.informationTab);
	}

	/**
	 * 自分がメインのノードであることを示すメソッド。
	 * メインのノードには、エンティティ編集などの様々なメッセージがやってくる.
	 */
	public void toMain() {
		if (mainNode != null) {
			// 古いメインノードのメッセージ登録を全削除
			Messenger.getInstance().remove(mainNode);
		}

		// エンティティを編集するメッセージ
		Messenger.getInstance().apply(EmptyEditMessage.class, this, m -> this.exitEditEntity());
		Messenger.getInstance().apply(PersonEditMessage.class, this, m -> this.editEntity(m.getPerson()));
	}
	
	/**
	 * すべてのエンティティを編集するときの共通処理
	 * @param entity 編集するエンティティ
	 */
	private void editAbstractEntity(Entity entity) {
		this.exitEditEntity();
		this.entityIcon.setImage(entity.getIcon());
		this.entityName.textProperty().bind(entity.nameProperty());
		this.entityBaseEditorController.edit(entity);
		this.entityMemoEditorController.edit(entity);
	}

	/**
	 * 人物を編集
	 * @param entity 編集する人物クラス
	 */
	private void editEntity(Person entity) {
		this.editAbstractEntity(entity);
		this.personEditorController.edit(entity);
	}

	/**
	 * 編集を終了.
	 */
	private void exitEditEntity() {
		this.entityBaseEditorController.exitEdit();
		this.entityMemoEditorController.exitEdit();
		this.personEditorController.exitEdit();

		// 編集画面の上部分に表示されるアイコン、エンティティ名の表示を消す
		this.entityIcon.setImage(null);
		this.entityName.textProperty().unbind();
		this.entityName.setText("エンティティの編集");
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
	}

}
