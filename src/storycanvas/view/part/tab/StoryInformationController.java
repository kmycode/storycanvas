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
package storycanvas.view.part.tab;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import net.kmycode.javafx.Messenger;
import storycanvas.StoryCanvas;
import storycanvas.message.update.StoryInformationUpdateMessage;

/**
 * ストーリーの設定を行う画面
 *
 * @author KMY
 */
public class StoryInformationController implements Initializable {

	@FXML
	private TextField title;

	@FXML
	private TextField authorName;

	@FXML
	private Label personCount;
	
	@FXML
	private Label placeCount;

	@FXML
	private Label sceneCount;

	@FXML
	private Label storylineCount;

	@FXML
	private Label partCount;

	private StringProperty titleProperty = new SimpleStringProperty();
	private StringProperty authorNameProperty = new SimpleStringProperty();

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		Messenger.getInstance().apply(StoryInformationUpdateMessage.class, this, m -> {
			StoryCanvas.getMainObject().setStoryTitle(m.titleProperty());

			this.titleProperty.unbind();
			this.authorNameProperty.unbind();
			this.titleProperty = m.titleProperty();
			this.authorNameProperty = m.authorNameProperty();
			this.title.textProperty().bindBidirectional(this.titleProperty);
			this.authorName.textProperty().bindBidirectional(this.authorNameProperty);

			this.personCount.setText(Integer.toString(m.getPersonCount()));
			this.placeCount.setText(Integer.toString(m.getPlaceCount()));
			this.sceneCount.setText(Integer.toString(m.getSceneCount()));
			this.storylineCount.setText(Integer.toString(m.getStorylineCount()));
			this.partCount.setText(Integer.toString(m.getPartCount()));
		});
	}

}
