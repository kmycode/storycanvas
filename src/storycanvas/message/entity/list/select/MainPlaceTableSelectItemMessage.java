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
package storycanvas.message.entity.list.select;

import javafx.scene.control.TreeItem;
import net.kmycode.javafx.Message;
import storycanvas.model.entity.Place;

/**
 * 場所のリストで特定のアイテムを選択するメッセージ
 *
 * @author KMY
 */
public class MainPlaceTableSelectItemMessage extends Message {

	private final TreeItem<Place> item;

	public MainPlaceTableSelectItemMessage(TreeItem<Place> item) {
		this.item = item;
	}

	public TreeItem<Place> getItem() {
		return this.item;
	}

}
