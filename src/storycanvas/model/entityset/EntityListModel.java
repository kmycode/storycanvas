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

import java.util.ArrayDeque;
import java.util.Queue;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.kmycode.javafx.ListUtil;
import storycanvas.model.entity.Entity;

/**
 * 複数のエンティティを一次元のリストにあらわしたモデル
 *
 * @author KMY
 */
public class EntityListModel<E extends Entity> implements EntitySetModel<E> {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * エンティティのリスト本体.
	 */
	private final ListProperty<E> entities = new SimpleListProperty<>(FXCollections.observableArrayList());

	public ObservableList<E> getEntities () {
		return entities.get();
	}

	public ObservableList<E> getEntitiesClone () {
		return ListUtil.getClone(entities);
	}

	public ListProperty<E> entitiesProperty () {
		return entities;
	}

	/**
	 * 現在選択されているエンティティ.
	 */
	private final ObjectProperty<E> selectedEntity = new SimpleObjectProperty<>();

	@Override
	public E getSelectedEntity () {
		return selectedEntity.get();
	}

	@Override
	public ObjectProperty<E> selectedEntityProperty () {
		return selectedEntity;
	}
//</editor-fold>

	/**
	 * エンティティを追加
	 * @param entity 追加するエンティティ
	 */
	@Override
	public void add(E entity) {
		if (this.getSelectedEntity() == null) {
			this.entities.add(entity);
		} else {
			this.insert(this.entities.indexOf(this.getSelectedEntity()), entity);
		}
	}

	/**
	 * エンティティを所定箇所に挿入
	 * @param index 挿入するインデクス番号
	 * @param entity 挿入するエンティティ
	 */
	public void insert(int index, E entity) {
		// 並べ替える前のインデクス番号にあるオブジェクトを取得
		E target = this.entities.get(index);

		// エンティティを順番通りに並べ替える
		FXCollections.sort(this.entities);

		// 並べ替えた後の新しいインデクス番号を取得
		index = this.entities.indexOf(target);

		// 以降のエンティティの順番をリストアップ
		Queue<Integer> orderStack = new ArrayDeque<Integer>();
		for (int i = index; i < this.entities.size(); i++) {
			orderStack.add(this.entities.get(i).getOrder());
		}
		orderStack.add(entity.getOrder());

		// エンティティを挿入
		this.entities.add(index, entity);

		// 以降のエンティティを１つずつ下にずらす
		for (int i = index; i < this.entities.size(); i++) {
			this.entities.get(i).setOrder(orderStack.poll());
		}
	}

	/**
	 * エンティティを削除
	 * @param entity 削除するエンティティ
	 */
	@Override
	public void delete(E entity) {
		this.entities.remove(entity);
	}
	
	/**
	 * 指定エンティティをリストの上へ移動
	 * @param entity 指定エンティティ
	 */
	public void up(E entity) {
		// エンティティを順番通りに並べ替える
		FXCollections.sort(this.entities);

		// ひとつ順番が上のものを探す
		E target = null;
		for (E e : this.entities) {
			if (e.getOrder() < entity.getOrder() && (target == null || e.getOrder() > target.getOrder())) {
				target = e;
			}
		}
		if (target != null) {
			int tmp = entity.getOrder();
			entity.setOrder(target.getOrder());
			target.setOrder(tmp);
		}

		// エンティティを順番通りに並べ替える
		FXCollections.sort(this.entities);
	}

	/**
	 * 現在選択されているエンティティを上へ移動.
	 */
	public void up() {
		this.up(this.getSelectedEntity());
	}

	/**
	 * 指定エンティティをリストの下へ移動
	 * @param entity 指定エンティティ
	 */
	public void down(E entity) {
		// エンティティを順番通りに並べ替える
		FXCollections.sort(this.entities);
		
		// ひとつ順番が下のものを探す
		E target = null;
		for (E e : this.entities) {
			if (e.getOrder() > entity.getOrder() && (target == null || e.getOrder() < target.getOrder())) {
				target = e;
			}
		}
		if (target != null) {
			int tmp = entity.getOrder();
			entity.setOrder(target.getOrder());
			target.setOrder(tmp);
		}

		// エンティティを順番通りに並べ替える
		FXCollections.sort(this.entities);
	}

	/**
	 * 現在選択されているエンティティを上へ移動.
	 */
	public void down() {
		this.down(this.getSelectedEntity());
	}

}
