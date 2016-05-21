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
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import storycanvas.model.entity.TreeEntity;

/**
 * エンティティをツリー状にしたモデル
 *
 * @author KMY
 */
public class EntityTreeModel<E extends TreeEntity> implements EntitySetModel<E> {

	/**
	 * ルートとなるエンティティ.
	 */
	private E rootEntity;

//<editor-fold defaultstate="collapsed" desc="プロパティ">
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

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	/**
	 * 新しいモデルを生成する
	 * @param root ルートオブジェクト。あらかじめ用意されてなければならない
	 */
	public EntityTreeModel(E root) {
		this.rootEntity = root;
	}
//</editor-fold>

	/**
	 * JavaFXのツリービューで利用するルートアイテムを返す
	 * @return ルートアイテム
	 */
	public TreeItem<E> getRootTreeItem() {
		return (TreeItem<E>)this.rootEntity.getRootTreeItem();
	}

	/**
	 * エンティティを追加
	 * @param entity 追加するエンティティ
	 */
	@Override
	public void add (E entity) {
		if (this.getSelectedEntity() == null) {
			this.add(entity, this.rootEntity);
		} else {
			this.add(entity, this.getSelectedEntity());
		}
	}

	/**
	 * エンティティを親を指定して追加
	 * @param entity 追加するエンティティ
	 * @param parent 追加するエンティティの親
	 */
	public void add(E entity, E parent) {
		parent.getChildren().add(entity);
	}

	/**
	 * エンティティを削除
	 * @param entity 削除するエンティティ
	 */
	@Override
	public void delete (E entity) {
		if (entity.getParent() != null) {
			entity.getParent().getChildren().remove(entity);
		}
	}

}
