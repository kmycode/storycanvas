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
package storycanvas.view.node;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import storycanvas.model.story.Story;
import storycanvas.view.part.EntityListButtonBox;
import storycanvas.view.part.table.PersonTableViewController;
import storycanvas.view.part.table.PlaceTableViewController;

/**
 * メインとなるノードのコントローラ
 *
 * @author KMY
 */
public class StoryMainNodeController implements Initializable {

	@FXML
	private Tab personTab;

	@FXML
	private Tab placeTab;

	@FXML
	private PersonTableViewController mainPersonTableController;

	@FXML
	private EntityListButtonBox personButtonBox;

	@FXML
	private PlaceTableViewController mainPlaceTableController;

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// メインのビューを設定
		this.mainPersonTableController.toMain();
		this.mainPlaceTableController.toMain();

		// 人物一覧のButtonBox
		this.personButtonBox.setOnNewAction(e -> Story.getCurrent().addPerson());
		this.personButtonBox.setOnDeleteAction(e -> Story.getCurrent().deletePerson());
		this.personButtonBox.setOnUpAction(e -> Story.getCurrent().upPerson());
		this.personButtonBox.setOnDownAction(e -> Story.getCurrent().downPerson());
	}

}
