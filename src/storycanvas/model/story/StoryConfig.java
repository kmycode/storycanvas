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
package storycanvas.model.story;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * ストーリー全体の設定情報を保存するクラス
 *
 * @author KMY
 */
public class StoryConfig implements Serializable {

//<editor-fold defaultstate="collapsed" desc="シリアライズ">
	private static final long serialVersionUID = 1L;
	private static final long serialInstanceVersionUID = 101_00000000001L;

	/**
	 * シリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームへの出力に失敗した時スロー
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		// 固有UID書き込み
		stream.writeLong(serialInstanceVersionUID);

		// プロパティ書き込み
		stream.writeUTF(this.getTitle());
		stream.writeUTF(this.getAuthorName());
		stream.writeUTF(this.getOverview());
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		long uid = stream.readLong();

		// フィールドを初期化
		this.initialize();

		// プロパティ読込
		if (uid <= 101_00000000001L) {
			this.setTitle(stream.readUTF());
			this.setAuthorName(stream.readUTF());
			this.setOverview(stream.readUTF());
		}

	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	public StoryConfig() {
		this.initialize();
	}

	private void initialize() {
		this.title = new SimpleStringProperty("");
		this.authorName = new SimpleStringProperty("");
		this.overview = new SimpleStringProperty("");
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * ストーリーの題名.
	 */
	private StringProperty title;

	public String getTitle () {
		return title.get();
	}

	public void setTitle (String value) {
		title.set(value);
	}

	public StringProperty titleProperty () {
		return title;
	}

	/**
	 * 作者名.
	 */
	private StringProperty authorName;

	public String getAuthorName () {
		return authorName.get();
	}

	public void setAuthorName (String value) {
		authorName.set(value);
	}

	public StringProperty authorNameProperty () {
		return authorName;
	}

	/**
	 * 概要.
	 */
	private StringProperty overview;

	public String getOverview () {
		return overview.get();
	}

	public void setOverview (String value) {
		overview.set(value);
	}

	public StringProperty overviewProperty () {
		return overview;
	}
//</editor-fold>

}
