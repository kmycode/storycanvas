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
import storycanvas.model.entity.Storyline;

/**
 * メイン画面のストーリーラインのビューを初期化するメッセージ
 *
 * @author KMY
 */
public class MainStorylineViewInitializeMessage extends Message {

	private final ObservableList<Storyline> list;
	private final ObjectProperty<Storyline> selectedItem;

	public MainStorylineViewInitializeMessage(ObservableList<Storyline> list, ObjectProperty<Storyline> selectedItem) {
		this.list = list;
		this.selectedItem = selectedItem;
	}

	public ObservableList<Storyline> getList() {
		return this.list;
	}

	public ObjectProperty<Storyline> selectedItemProperty() {
		return this.selectedItem;
	}
}
