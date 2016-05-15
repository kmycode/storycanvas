/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.text.Font;

/**
 * JavaFXのフォントを取り扱うためのユーティリティクラス
 * @author KMY
 */
public class FontUtil {

	private FontUtil() {}

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * フォントファミリ.
	 */
	private static final StringProperty fontFamily = new SimpleStringProperty("Meiryo UI");

	public static StringProperty fontFamilyProperty() {
		return fontFamily;
	}

	/**
	 * フォントサイズ.
	 */
	private static final DoubleProperty fontSize = new SimpleDoubleProperty(11.);

	public static DoubleProperty fontSizeProperty() {
		return fontSize;
	}

	/**
	 * デフォルトのフォント.
	 */
	private static final ObjectProperty<Font> defaultFont = new SimpleObjectProperty(getFont(fontSize.get()));

	public static ObjectProperty<Font> defaultFontProperty() {
		return defaultFont;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	static {
		// デフォルトのフォントファミリが変更された時
		fontFamily.addListener((e) -> defaultFont.set(getFont(fontSize.get())));
		// デフォルトのフォントサイズが変更された時
		fontSize.addListener((e) -> defaultFont.set(getFont(fontSize.get())));
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="メソッド">
	/**
	 * 指定されたフォントサイズのフォントを取得します。
	 * ファミリーは、別途fontFamilyで指定されたファミリーになります。
	 * @param size フォントサイズ
	 * @return 指定されたフォントサイズのフォント
	 */
	public static Font getFont(double size) {
		return Font.font(fontFamily.get(), size);
	}
//</editor-fold>

}
