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
public class Part extends Entity {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 子となるストーリーライン.
	 */
	private final ListProperty<Storyline> storylines = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

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

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	public Part() {
		this.initialize();
	}

	@Override
	protected final void initialize() {

		super.initialize();

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
