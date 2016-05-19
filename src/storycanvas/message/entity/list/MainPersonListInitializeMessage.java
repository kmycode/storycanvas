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
package storycanvas.message.entity.list;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import net.kmycode.javafx.Message;
import storycanvas.model.entity.Person;

/**
 * メイン画面の人物一覧を初期化するメッセージ
 *
 * @author KMY
 */
public class MainPersonListInitializeMessage extends Message {

	private final ObservableList<Person> list;
	private final ObjectProperty<Person> selectedItem;

	public MainPersonListInitializeMessage(ObservableList<Person> list, ObjectProperty<Person> selectedItem) {
		this.list = list;
		this.selectedItem = selectedItem;
	}

	public ObservableList<Person> getList() {
		return this.list;
	}

	public ObjectProperty<Person> selectedItemProperty() {
		return this.selectedItem;
	}
}
