/*
 * KMY ライブラリ (LGPL)
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
