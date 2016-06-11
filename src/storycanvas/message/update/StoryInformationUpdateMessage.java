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
package storycanvas.message.update;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.kmycode.javafx.Message;

/**
 * ストーリーの情報を更新するメッセージ
 *
 * @author KMY
 */
public class StoryInformationUpdateMessage extends Message {

	private StringProperty title = new SimpleStringProperty();
	private StringProperty authorName = new SimpleStringProperty();
	private int personCount = 0;
	private int placeCount = 0;
	private int sceneCount = 0;
	private int storylineCount = 0;
	private int partCount = 0;

	private StoryInformationUpdateMessage() {}

	public static class Builder implements javafx.util.Builder<StoryInformationUpdateMessage> {

		private final StoryInformationUpdateMessage instance = new StoryInformationUpdateMessage();

		public Builder setTitleProperty(StringProperty value) {
			this.instance.title = value;
			return this;
		}

		public Builder setAuthorNameProperty(StringProperty value) {
			this.instance.authorName = value;
			return this;
		}

		public Builder setPersonCount(int value) {
			this.instance.personCount = value;
			return this;
		}

		public Builder setPlaceCount(int value) {
			this.instance.placeCount = value;
			return this;
		}

		public Builder setSceneCount(int value) {
			this.instance.sceneCount = value;
			return this;
		}

		public Builder setStorylineCount(int value) {
			this.instance.storylineCount = value;
			return this;
		}

		public Builder setPartCount(int value) {
			this.instance.partCount = value;
			return this;
		}

		@Override
		public StoryInformationUpdateMessage build () {
			return this.instance;
		}

	}

	public StringProperty titleProperty() {
		return this.title;
	}

	public StringProperty authorNameProperty() {
		return this.authorName;
	}

	public int getPersonCount() {
		return this.personCount;
	}

	public int getPlaceCount() {
		return this.placeCount;
	}

	public int getSceneCount() {
		return this.sceneCount;
	}

	public int getStorylineCount() {
		return this.storylineCount;
	}

	public int getPartCount() {
		return this.partCount;
	}

}
