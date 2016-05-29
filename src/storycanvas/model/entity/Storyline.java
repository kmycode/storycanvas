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
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import net.kmycode.javafx.SimpleWeakObjectProperty;

/**
 * ストーリーライン
 *
 * @author KMY
 */
public class Storyline extends Entity implements Serializable {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 子となるシーン.
	 */
	private ListProperty<Scene> scenes;
	
	public ObservableList<Scene> getScenes () {
		return scenes.get();
	}
	
	public void setScenes (ObservableList<Scene> value) {
		scenes.set(value);
	}
	
	public ListProperty<Scene> scenesProperty () {
		return scenes;
	}

	/**
	 * 所属する編.
	 */
	private ObjectProperty<Part> part;

	public Part getPart () {
		return part.get();
	}

	public void setPart (Part value) {
		part.set(value);
	}

	public ObjectProperty partProperty () {
		return part;
	}

	/**
	 * 編を削除.
	 */
	public void removePart() {
		if (this.getPart() != null) {
			this.getPart().getStorylines().remove(this);
		}
		this.setPart(null);
	}

	/**
	 * 新しい編を設定
	 * @param line 新しい編
	 */
	public void newPart(Part line) {
		if (line == null) {
			this.removePart();
		} else {
			this.setPart(line);
		}
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="シリアライズ">
	private static final long serialVersionUID = 1L;
	private static final long serialInstanceVersionUID = 8_00000000001L;

	/**
	 * シリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームへの出力に失敗した時スロー
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		this.writeBaseObject(stream);

		// 固有UID書き込み
		stream.writeLong(serialInstanceVersionUID);

		// 所属する編
		stream.writeLong(this.getPart().getId());

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

			// 所属する編
			Part part = Entity.getPart(stream.readLong());
			if (part != null) {
				part.getStorylines().add(this);
			}

			// プロパティ読込
		}

	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	public Storyline() {
		this.initialize();
	}

	@Override
	protected final void initialize() {

		super.initialize();

		this.scenes = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
		this.part = new SimpleWeakObjectProperty<>();

		// シーンリストと、各シーンにおけるストーリーラインとのリンクを自動的に連動させる
		this.scenes.addListener((ListChangeListener.Change<? extends Scene> e) -> {

			while (e.next()) {

				// 追加された項目の元親の子リストから削除して、自分を新しい親に設定する
				if (e.wasAdded()) {
					List<? extends Scene> subList = e.getAddedSubList();
					for(Scene el : subList) {
						el.removeStoryline();
						el.newStoryline(this);
					}
				}

				// 削除された項目の親を削除
				else if (e.wasRemoved()) {
					List<? extends Scene> subList = e.getRemoved();
					for(Scene el : subList) {
						el.removeStoryline();
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
		return "storyline";
	}

}
