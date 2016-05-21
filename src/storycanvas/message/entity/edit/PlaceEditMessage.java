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
package storycanvas.message.entity.edit;

import net.kmycode.javafx.Message;
import storycanvas.model.entity.Place;

/**
 * 場所を編集するメッセージ
 *
 * @author KMY
 */
public class PlaceEditMessage extends Message {

	private final Place entity;

	public PlaceEditMessage(Place e) {
		this.entity = e;
	}

	public Place getPlace() {
		return this.entity;
	}

}
