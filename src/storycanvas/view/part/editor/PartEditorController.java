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
package storycanvas.view.part.editor;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import storycanvas.model.entity.Part;

/**
 * 場所の編集画面のコントローラ
 *
 * @author KMY
 */
public class PartEditorController implements Initializable {

	private Part editingEntity;

	@FXML
	private GridPane root;

	@FXML
	private TextField nameInput;

	/**
	 * 編集
	 * @param entity 編集する人物
	 */
	public void edit(Part entity) {
		this.editingEntity = entity;
		this.nameInput.textProperty().bindBidirectional(entity.nameProperty());
		this.root.setVisible(true);
	}

	/**
	 * 編集を終了.
	 */
	public void exitEdit() {
		if (this.editingEntity != null) {
			this.nameInput.textProperty().unbindBidirectional(this.editingEntity.nameProperty());
		}
		this.root.setVisible(false);
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}

}
