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
package storycanvas.view.control;

import javafx.scene.control.TableCell;
import storycanvas.model.date.StoryDate;

/**
 * ストーリーの日付を表示するテーブルセル
 *
 * @author KMY
 */
public class StoryDateTableCell<E> extends TableCell<E, StoryDate> {

	@Override
	protected void updateItem (StoryDate item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			this.setText(item.getYear() + "/" + item.getMonth() + "/" + item.getDay());
		} else {
			this.setText("");
		}
	}

}
