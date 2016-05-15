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
package net.kmycode.javafx;

import javafx.util.StringConverter;

/**
 * IntegerPropertyはなぜかProperty<Integer>ではなくProperty<Number>なので、それに対応するコンバータ。
 * 変換に失敗しても例外をスローせず、3桁ごとにカンマ入れたりしないすぐれもの
 * @author KMY
 */
public class IntegerNumberStringConverter extends StringConverter<Number> {

	@Override
	public String toString (Number object) {
		return Integer.toString((Integer)object);
	}

	@Override
	public Number fromString (String string) {
		Integer num;
		try {
			num = Integer.valueOf(string);
		} catch (NumberFormatException e) {
			num = 0;
		}
		return num;
	}

}
