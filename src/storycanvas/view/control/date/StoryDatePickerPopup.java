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
package storycanvas.view.control.date;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Popup;
import net.kmycode.javafx.IntegerNumberStringConverter;

/**
 * FXML Controller class
 *
 * @author KMY
 */
public class StoryDatePickerPopup extends Popup implements Initializable {

	@FXML
	private TextField yearInput;

	@FXML
	private TextField monthInput;

	@FXML
	private TextField dayInput;

	@FXML
	private CalendarControl calendar;

	public StoryDatePickerPopup() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StoryDatePickerPopup.fxml"));
		GridPane root = new GridPane();
		fxmlLoader.setRoot(root);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		this.getContent().add(root);

		// 範囲外のマウスクリックで自動的に閉じるようにする
		this.setAutoHide(true);

		// テキストインプットを、カレンダーにバインドさせる
		Bindings.bindBidirectional(this.yearInput.textProperty(), this.calendar.yearProperty(), new IntegerNumberStringConverter());
		Bindings.bindBidirectional(this.monthInput.textProperty(), this.calendar.monthProperty(), new IntegerNumberStringConverter());
		this.calendar.dayProperty().addListener((e) -> this.dayInput.setText(Integer.toString(this.calendar.getDay())));
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}

}
