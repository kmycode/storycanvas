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
package storycanvas.view.window;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import storycanvas.model.story.Story;

/**
 * メインメニューのコントローラクラス。
 * MainWindowのメニューとして使用される
 *
 * @author KMY
 */
public class MainMenuController implements Initializable {

	/**
	 * コントローラを初期化.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}

	/**
	 * ファイル -> 開く.
	 */
	@FXML
	public void openAction(ActionEvent e) {
		Story.getCurrent().load();
	}

	/**
	 * ファイル -> 保存.
	 */
	@FXML
	public void saveAction(ActionEvent e) {
		Story.getCurrent().save();
	}

}
