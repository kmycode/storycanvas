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
package storycanvas.view.part.table;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import net.kmycode.javafx.ImageTableCell;
import net.kmycode.javafx.Messenger;
import storycanvas.message.entity.list.MainPersonListInitializeMessage;
import storycanvas.model.date.StoryDate;
import storycanvas.model.entity.Person;
import storycanvas.view.control.StoryDateTableCell;

/**
 * 人物一覧のテーブルのコントローラクラス
 *
 * @author KMY
 */
public class PersonTableViewController implements Initializable {

	private static PersonTableViewController mainController = null;

	@FXML
	private TableView<Person> personTable;

	@FXML
	private TableColumn<Person, Image> iconColumn;

	@FXML
	private TableColumn<Person, StoryDate> birthDayColumn;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// データを設定
		//this.personTable.setItems(Story.getCurrent().getPersonsClone());

		// セルファクトリを設定
		this.iconColumn.setCellFactory((TableColumn<Person, Image> param) -> new ImageTableCell<Person>(16, 16));
		this.birthDayColumn.setCellFactory((TableColumn<Person, StoryDate> param) -> new StoryDateTableCell<Person>());

		// 選択時のイベント
		//this.personTable.selectionModelProperty().addListener();
	}

	/**
	 * 自分をメインのリストとして設定します.
	 */
	public void toMain() {
		if (mainController == null) {
			mainController = this;

			// メッセンジャにイベントを登録
			Messenger.getInstance().apply(MainPersonListInitializeMessage.class, this, (m) -> {
				this.personTable.setItems(m.getList());
				m.selectedItemProperty().bind(this.personTable.getSelectionModel().selectedItemProperty());
			});
		}
	}

}
