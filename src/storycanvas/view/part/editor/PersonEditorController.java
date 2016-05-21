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
import storycanvas.model.entity.Person;
import storycanvas.view.control.SexPicker;
import storycanvas.view.control.date.StoryDatePicker;

/**
 * 人物の編集画面のコントローラ
 *
 * @author KMY
 */
public class PersonEditorController implements Initializable {
	
	private Person editingEntity;

	@FXML
	private GridPane root;

	@FXML
	private TextField lastNameInput;

	@FXML
	private TextField firstNameInput;

	@FXML
	private SexPicker sexInput;

	@FXML
	private StoryDatePicker birthDayInput;

	@FXML
	private StoryDatePicker deathDayInput;

	/**
	 * 編集
	 * @param entity 編集する人物
	 */
	public void edit(Person entity) {
		this.editingEntity = entity;
		this.lastNameInput.textProperty().bindBidirectional(entity.lastNameProperty());
		this.firstNameInput.textProperty().bindBidirectional(entity.firstNameProperty());
		this.sexInput.valueProperty().bindBidirectional(entity.sexProperty());
		this.birthDayInput.dateProperty().bindBidirectional(entity.birthDayProperty());
		this.deathDayInput.dateProperty().bindBidirectional(entity.deathDayProperty());
		this.root.setVisible(true);
	}

	/**
	 * 編集を終了.
	 */
	public void exitEdit() {
		if (this.editingEntity != null) {
			this.lastNameInput.textProperty().unbindBidirectional(this.editingEntity.lastNameProperty());
			this.firstNameInput.textProperty().unbindBidirectional(this.editingEntity.firstNameProperty());
			this.sexInput.valueProperty().unbindBidirectional(this.editingEntity.sexProperty());
			this.birthDayInput.dateProperty().unbindBidirectional(this.editingEntity.birthDayProperty());
			this.deathDayInput.dateProperty().unbindBidirectional(this.editingEntity.deathDayProperty());
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
