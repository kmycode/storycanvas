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
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import storycanvas.model.entity.Sex;

/**
 * 性選択ピッカー
 *
 * @author KMY
 */
public class SexPicker extends FlowPane implements Initializable {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 選択された値.
	 */
	private final ObjectProperty<Sex> value = new SimpleObjectProperty<>();

	public Sex getValue () {
		return value.get();
	}

	public void setValue (Sex value) {
		this.value.set(value);
	}

	public ObjectProperty<Sex> valueProperty () {
		return value;
	}
//</editor-fold>

	@FXML
	private ToggleGroup sexGroup;

	@FXML
	private ToggleButton maleButton;

	@FXML
	private ToggleButton femaleButton;

	// setValueによる変更、GUI操作による変更が競合してStackOverFlowが出ないようにするため
	private boolean inValueListener;

	public SexPicker() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SexPicker.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		// 値が変更された時
		this.value.addListener(e -> {
			if (!this.inValueListener) {
				this.inValueListener = true;
				this.setToggleButtonFromValue();
				this.inValueListener = false;
			}
		});

		// トグルボタンが変更された時
		this.sexGroup.selectedToggleProperty().addListener(e -> {
			if (!this.inValueListener) {
				this.inValueListener = true;
				this.setValueFromToggleButton();
				this.inValueListener = false;
			}
		});
	}

	/**
	 * 値から、トグルボタンの状態を設定する.
	 */
	private void setToggleButtonFromValue() {
		if (this.getValue() == Sex.MALE) {
			this.sexGroup.selectToggle(this.maleButton);
		} else if (this.getValue() == Sex.FEMALE) {
			this.sexGroup.selectToggle(this.femaleButton);
		} else {
			this.sexGroup.selectToggle(null);
		}
	}

	/**
	 * トグルボタンの状態から、値を設定する.
	 */
	private void setValueFromToggleButton() {
		if (this.sexGroup.getSelectedToggle() == this.maleButton) {
			this.setValue(Sex.MALE);
		} else if (this.sexGroup.getSelectedToggle() == this.femaleButton) {
			this.setValue(Sex.FEMALE);
		} else {
			this.setValue(null);
		}
	}

	/**
	 * Initializes the controller class.
	 */
	//@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}

}
