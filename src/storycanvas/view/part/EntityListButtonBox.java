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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author KMY
 */
public class EntityListButtonBox extends HBox implements Initializable {
	
	@FXML
	private Button newButton;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private Button upButton;
	
	@FXML
	private Button downButton;
	
	@FXML
	private Button leftButton;
	
	@FXML
	private Button rightButton;

//<editor-fold defaultstate="collapsed" desc="アクション">
	public void setOnNewAction(EventHandler<ActionEvent> value) {
		this.newButton.setOnAction(value);
	}

	public void setOnDeleteAction(EventHandler<ActionEvent> value) {
		this.deleteButton.setOnAction(value);
	}

	public void setOnUpAction(EventHandler<ActionEvent> value) {
		this.upButton.setOnAction(value);
	}

	public void setOnDownAction(EventHandler<ActionEvent> value) {
		this.downButton.setOnAction(value);
	}

	public void setOnLeftAction(EventHandler<ActionEvent> value) {
		this.leftButton.setOnAction(value);
	}

	public void setOnRightAction(EventHandler<ActionEvent> value) {
		this.rightButton.setOnAction(value);
	}
//</editor-fold>

	/**
	 * 初期化.
	 */
	public EntityListButtonBox() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EntityListButtonBox.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	/**
	 * 対象が階層をもったテーブルであるか
	 * trueならば、右・左のボタンが使えるようになる
	 * @param value 階層を持っていればtrue
	 */
	public void setTree(boolean value) {
		this.leftButton.setVisible(value);
		this.rightButton.setVisible(value);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}

}
