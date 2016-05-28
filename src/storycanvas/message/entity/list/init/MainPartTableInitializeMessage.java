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
package storycanvas.message.entity.list.init;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import net.kmycode.javafx.Message;
import storycanvas.model.entity.Part;

/**
 * メイン画面の編一覧を初期化するメッセージ
 *
 * @author KMY
 */
public class MainPartTableInitializeMessage extends Message {

	private final ObservableList<Part> list;
	private final ObjectProperty<Part> selectedItem;
	private final ObjectProperty<Part> selectedViewItem;

	public MainPartTableInitializeMessage(ObservableList<Part> list, ObjectProperty<Part> selectedItem, ObjectProperty<Part> selectedViewItem) {
		this.list = list;
		this.selectedItem = selectedItem;
		this.selectedViewItem = selectedViewItem;
	}

	public ObservableList<Part> getList() {
		return this.list;
	}

	public ObjectProperty<Part> selectedItemProperty() {
		return this.selectedItem;
	}

	public ObjectProperty<Part> selectedViewItemProperty() {
		return this.selectedViewItem;
	}
}
