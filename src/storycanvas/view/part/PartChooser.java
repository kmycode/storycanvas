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
package storycanvas.view.part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import net.kmycode.javafx.Messenger;
import storycanvas.message.entity.list.init.MainPartTableInitializeMessage;
import storycanvas.model.entity.Part;

/**
 * FXML Controller class
 *
 * @author KMY
 */
public class PartChooser extends HBox implements Initializable {

	@FXML
	private ComboBox<Part> partChooser;

	@FXML
	private ImageView partIcon;

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	public PartChooser() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PartChooser.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		this.partChooser.setCellFactory((e) -> new EntityListCell());

		this.partChooser.getSelectionModel().selectedItemProperty().addListener(e -> {
			Part selectedItem = ((ReadOnlyObjectProperty<Part>)e).get();
			if (selectedItem != null) {
				this.partIcon.setImage(selectedItem.getIcon());
			} else {
				this.partIcon.setImage(null);
			}
		});

		// ComboBoxフリーズバグを避けるためのコード
		// 参考：http://stackoverflow.com/questions/31786980/javafx-windows-10-combobox-error
		this.partChooser.setOnMousePressed(e -> this.partChooser.requestFocus());
	}

	/**
	 * メインのビューに設定する.
	 */
	public void toMain() {

		// 初期化メッセージを受け取る
		Messenger.getInstance().apply(MainPartTableInitializeMessage.class, this, (m) -> {

			// バインド
			this.partChooser.setItems(m.getList());
			m.selectedViewItemProperty().bind(this.partChooser.getSelectionModel().selectedItemProperty());
		});

	}
//</editor-fold>

	/**
	 * 現在選択されている編を取得します
	 * @return 選択されている編
	 */
	public Part getSelectedPart() {
		return this.partChooser.getSelectionModel().getSelectedItem();
	}

	@FXML
	private void resetPart(ActionEvent e) {
		this.partChooser.getSelectionModel().select(null);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}

}
