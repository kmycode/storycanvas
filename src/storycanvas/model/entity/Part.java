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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * 物語の「編」.
 *
 * @author KMY
 */
public class Part extends Entity implements Serializable {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 子となるストーリーライン.
	 */
	private ListProperty<Storyline> storylines;

	public ObservableList<Storyline> getStorylines () {
		return storylines.get();
	}

	public void setStorylines (ObservableList<Storyline> value) {
		storylines.set(value);
	}

	public ListProperty<Storyline> storylinesProperty () {
		return storylines;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="シリアライズ">
	private static final long serialVersionUID = 1L;
	private static final long serialInstanceVersionUID = 9_00000000001L;

	/**
	 * シリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームへの出力に失敗した時スロー
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		this.writeBaseObject(stream);

		// 固有UID書き込み
		stream.writeLong(serialInstanceVersionUID);

		// プロパティ書き込み
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		this.readBaseObject(stream);

		long uid = stream.readLong();
		if (uid == serialInstanceVersionUID) {

			// コンストラクタ
			this.initialize();

			// プロパティ読込
		}

	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	public Part() {
		this.initialize();
	}

	@Override
	protected final void initialize() {

		super.initialize();

		this.storylines = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

		// シーンリストと、各シーンにおけるストーリーラインとのリンクを自動的に連動させる
		this.storylines.addListener((ListChangeListener.Change<? extends Storyline> e) -> {

			while (e.next()) {

				// 追加された項目の元親の子リストから削除して、自分を新しい親に設定する
				if (e.wasAdded()) {
					List<? extends Storyline> subList = e.getAddedSubList();
					for(Storyline el : subList) {
						el.removePart();
						el.newPart(this);
					}
				}

				// 削除された項目の親を削除
				else if (e.wasRemoved()) {
					List<? extends Storyline> subList = e.getRemoved();
					for(Storyline el : subList) {
						el.removePart();
					}
				}

				// ソートされた時
				// else if (e.wasPermutated()) {
				// }
			}
		});
	}
//</editor-fold>

	@Override
	protected String getResourceName () {
		return "part";
	}

}
