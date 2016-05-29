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
package storycanvas.view.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author KMY
 */
public class SCColorPicker extends HBox implements Initializable {

	@FXML
	private ColorPicker colorPicker;

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	public Color getValue() {
		return this.colorPicker.getValue();
	}

	public void setValue(Color value) {
		this.colorPicker.setValue(value);
	}

	public ObjectProperty<Color> valueProperty() {
		return this.colorPicker.valueProperty();
	}
//</editor-fold>

	/**
	 * 初期化.
	 */
	public SCColorPicker() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SCColorPicker.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * 選択中のカラーを削除
	 * @param e イベントオブジェクト
	 */
	@FXML
	private void deleteColorAction(ActionEvent e) {
		this.setValue(Color.TRANSPARENT);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}

}
