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
package storycanvas.model.entityset;

import javafx.beans.property.ObjectProperty;
import storycanvas.model.entity.Entity;

/**
 * 複数の同一種類のエンティティをひとつにまとめるという概念
 *
 * @author KMY
 */
public interface EntitySetModel<E extends Entity> {

	/**
	 * 現在選択中のエンティティを取得
	 * @return 現在選択されているエンティティ
	 */
	public E getSelectedEntity();

	/**
	 * 現在選択中のエンティティのプロパティ
	 * @return プロパティ
	 */
	public ObjectProperty<E> selectedEntityProperty();

	/**
	 * エンティティを追加
	 * @param entity 追加するエンティティ
	 */
	public void add(E entity);

	/**
	 * エンティティを削除
	 * @param entity 削除するエンティティ
	 */
	public void delete(E entity);

	/**
	 * エンティティを全削除.
	 */
	public void clear();

	/**
	 * 現在選択されているエンティティを削除.
	 */
	public default void delete() {
		if (this.getSelectedEntity() != null) {
			this.delete(this.getSelectedEntity());
		}
	}

	/**
	 * 指定したエンティティを取得する
	 * @param id エンティティのID
	 * @return エンティティ。見つからなければnull
	 */
	public E get(long id);

}
