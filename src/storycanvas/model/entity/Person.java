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
package storycanvas.model.entity;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import storycanvas.model.date.StoryDate;

/**
 * 登場人物
 *
 * @author KMY
 */
public class Person extends EditableEntity {
	
//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 姓.
	 */
	private final StringProperty lastName = new SimpleStringProperty();
	
	public String getLastName () {
		return lastName.get();
	}
	
	public void setLastName (String value) {
		lastName.set(value);
	}
	
	public StringProperty lastNameProperty () {
		return lastName;
	}
	
	/**
	 * 名.
	 */
	private final StringProperty firstName = new SimpleStringProperty();
	
	public String getFirstName () {
		return firstName.get();
	}
	
	public void setFirstName (String value) {
		firstName.set(value);
	}
	
	public StringProperty firstNameProperty () {
		return firstName;
	}

	private final ObjectProperty<StoryDate> birthDay = new SimpleObjectProperty<>();

	public StoryDate getBirthDay () {
		return birthDay.get();
	}

	public void setBirthDay (StoryDate value) {
		birthDay.set(value);
	}

	public ObjectProperty<StoryDate> birthDayProperty () {
		return birthDay;
	}

	private final ObjectProperty<StoryDate> deathDay = new SimpleObjectProperty<>();

	public StoryDate getDeathDay () {
		return deathDay.get();
	}

	public void setDeathDay (StoryDate value) {
		deathDay.set(value);
	}

	public ObjectProperty<StoryDate> deathDayProperty () {
		return deathDay;
	}
//</editor-fold>

	public Person() {
		this.setNameBinding();
	}

	/**
	 * 人物の名前を変更したら、Entity.nameプロパティに影響するようにする。
	 * デシリアライズのことを考慮し、処理をコンストラクタから分離.
	 */
	private void setNameBinding() {
		this.firstName.addListener(e -> this.nameProperty().set(this.getLastName() + this.getFirstName()));
		this.lastName.addListener(e -> this.nameProperty().set(this.getLastName() + this.getFirstName()));
	}

	@Override
	protected String getResourceName () {
		return "person";
	}

}
