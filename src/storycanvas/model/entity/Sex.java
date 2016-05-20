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

import javafx.scene.paint.Color;

/**
 * 人物の性別
 *
 * @author KMY
 */
public class Sex extends Entity {

	public static final Sex MALE;
	public static final Sex FEMALE;

	static {
		MALE = new Sex();
		MALE.setName("男");
		MALE.setColor(Color.BLUE);
		MALE.setId(0);
		MALE.setOrder(0);
		FEMALE = new Sex();
		FEMALE.setName("女");
		FEMALE.setColor(Color.RED);
		FEMALE.setId(1);
		FEMALE.setOrder(1);
	}

	/**
	 * リソース名を取得
	 * @return リソース名
	 */
	@Override
	protected String getResourceName () {
		return "empty";
	}

}
