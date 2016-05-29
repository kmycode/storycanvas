/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javafx.scene.paint.Color;

/**
 * JavaFXの色を取り扱うためのユーティリティクラス
 * @author KMY
 */
public class ColorUtil {

	private ColorUtil() {}

//<editor-fold defaultstate="collapsed" desc="ファイル入出力メソッド">
	/**
	 * シリアライズを行う
	 * @throws IOException ストリームの読込に失敗した時スロー
	 */
	public static void writeObject(Color c, ObjectOutputStream stream) throws IOException {

		if (c == null) c = Color.TRANSPARENT;

		// エンティティの書き込み
		stream.writeDouble(c.getRed());
		stream.writeDouble(c.getGreen());
		stream.writeDouble(c.getBlue());
		stream.writeDouble(c.getOpacity());
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	public static Color readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		// エンティティの取り込み
		return new Color(stream.readDouble(), stream.readDouble(), stream.readDouble(), stream.readDouble());
	}
//</editor-fold>

}
