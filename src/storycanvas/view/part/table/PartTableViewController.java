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
import javafx.scene.paint.Color;
import net.kmycode.javafx.ColorTableCell;
import net.kmycode.javafx.ImageTableCell;
import net.kmycode.javafx.Messenger;
import storycanvas.message.entity.list.init.MainPartTableInitializeMessage;
import storycanvas.message.entity.list.select.MainPartTableSelectItemMessage;
import storycanvas.model.entity.Part;

/**
 * 編一覧のテーブルのコントローラクラス
 *
 * @author KMY
 */
public class PartTableViewController implements Initializable {

	private static PartTableViewController mainController = null;

	@FXML
	private TableView<Part> partTable;

	@FXML
	private TableColumn<Part, Image> iconColumn;

	@FXML
	private TableColumn<Part, Color> colorColumn;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// セルファクトリを設定
		this.iconColumn.setCellFactory((TableColumn<Part, Image> param) -> new ImageTableCell<Part>(16, 16));
		this.colorColumn.setCellFactory((TableColumn<Part, Color> param) -> new ColorTableCell<Part>());
	}

	/**
	 * 自分をメインのリストとして設定します.
	 */
	public void toMain() {
		if (mainController == null) {
			mainController = this;

			// メッセンジャにイベントを登録
			Messenger.getInstance().apply(MainPartTableInitializeMessage.class, this, (m) -> {
				this.partTable.setItems(m.getList());
				m.selectedItemProperty().bind(this.partTable.getSelectionModel().selectedItemProperty());
			});

			// 特定のアイテムを選択するメッセージが来た時の対応
			Messenger.getInstance().apply(MainPartTableSelectItemMessage.class, this, (m) -> {
				this.partTable.getSelectionModel().select(m.getItem());
			});
		}
	}

	/**
	 * 何も選んでいない状態にします.
	 */
	public void unselect() {
		this.partTable.getSelectionModel().clearSelection();
	}

}
