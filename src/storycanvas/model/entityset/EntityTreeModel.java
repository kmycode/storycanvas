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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.Queue;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
	private final E rootEntity;

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

	private final ObjectProperty<TreeItem<E>> selectedTreeItemEntity = new SimpleObjectProperty<>();

	public ObjectProperty<TreeItem<E>> selectedTreeItemEntityProperty() {
		return selectedTreeItemEntity;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	/**
	 * 新しいモデルを生成する
	 * @param root ルートオブジェクト。あらかじめ用意されてなければならない
	 */
	public EntityTreeModel(E root) {
		this.rootEntity = root;

		this.selectedTreeItemEntity.addListener(e -> this.selectedEntity.set(
				this.selectedTreeItemEntity.get() != null ? this.selectedTreeItemEntity.get().getValue() : null
		));
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ファイル入出力メソッド">
	/**
	 * シリアライズを行う
	 * @throws IOException ストリームの読込に失敗した時スロー
	 */
	public void writeObject(ObjectOutputStream stream) throws IOException {

		// エンティティの書き込み
		stream.writeObject(this.rootEntity);
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		// エンティティの取り込み
		E newRoot = (E)stream.readObject();
		this.rootEntity.getChildren().addAll(newRoot.getChildren());
	}
//</editor-fold>

	/**
	 * JavaFXのツリービューで利用するルートアイテムを返す
	 * @return ルートアイテム
	 */
	public TreeItem<E> getRootTreeItem() {
		return (TreeItem<E>)this.rootEntity.getTreeItem();
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
			if (this.getSelectedEntity().getParent() != null) {
				this.insert(this.getSelectedEntity().getParent().getChildren().indexOf(this.getSelectedEntity()),
						entity, (E)this.getSelectedEntity().getParent());
			} else {
				this.add(entity, this.rootEntity);
			}
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
	 * エンティティを所定箇所に挿入。
	 * このメソッドの呼出時点で、エンティティはparentのchildでなくてもよい。
	 * メソッド内で親子の関連付け処理も行う
	 * @param index 挿入するインデクス番号
	 * @param entity 挿入するエンティティ
	 * @param parent 親となるエンティティ
	 */
	public void insert(int index, E entity, E parent) {
		// 並べ替える前のインデクス番号にあるオブジェクトを取得
		E target = (E)parent.getChildren().get(index);

		// エンティティを順番通りに並べ替える
		FXCollections.sort(parent.getChildren());

		// 並べ替えた後の新しいインデクス番号を取得
		index = parent.getChildren().indexOf(target);

		// 以降のエンティティの順番をリストアップ
		Queue<Long> orderStack = new ArrayDeque<>();
		for (int i = index; i < parent.getChildren().size(); i++) {
			orderStack.add(parent.getChildren().get(i).getOrder());
		}
		orderStack.add(entity.getOrder());

		// エンティティを挿入
		parent.getChildren().add(index, entity);

		// 以降のエンティティを１つずつ下にずらす
		for (int i = index; i < parent.getChildren().size(); i++) {
			parent.getChildren().get(i).setOrder(orderStack.poll());
		}
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

	/**
	 * エンティティを全削除.
	 */
	@Override
	public void clear() {
		this.rootEntity.getChildren().clear();
	}

	/**
	 * 指定エンティティをリストの上へ移動
	 * @param entity 指定エンティティ
	 */
	public void up(E entity) {
		if (entity == null || entity.getParent() == null) {
			return;
		}

		// エンティティを順番通りに並べ替える
		FXCollections.sort(entity.getParent().getChildren());

		// ひとつ順番が上のものを探す
		E target = null;
		for (TreeEntity e : entity.getParent().getChildren()) {
			if (e.getOrder() < entity.getOrder() && (target == null || e.getOrder() > target.getOrder())) {
				target = (E)e;
			}
		}
		if (target != null) {
			long tmp = entity.getOrder();
			entity.setOrder(target.getOrder());
			target.setOrder(tmp);
		}

		// エンティティを順番通りに並べ替える
		FXCollections.sort(entity.getParent().getChildren());
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
		if (entity == null || entity.getParent() == null) {
			return;
		}

		// エンティティを順番通りに並べ替える
		FXCollections.sort(entity.getParent().getChildren());

		// ひとつ順番が下のものを探す
		E target = null;
		for (TreeEntity e : entity.getParent().getChildren()) {
			if (e.getOrder() > entity.getOrder() && (target == null || e.getOrder() < target.getOrder())) {
				target = (E)e;
			}
		}
		if (target != null) {
			long tmp = entity.getOrder();
			entity.setOrder(target.getOrder());
			target.setOrder(tmp);
		}

		// エンティティを順番通りに並べ替える
		FXCollections.sort(entity.getParent().getChildren());
	}

	/**
	 * 現在選択されているエンティティを下へ移動.
	 */
	public void down() {
		this.down(this.getSelectedEntity());
	}

	/**
	 * 指定エンティティをリストの右へ移動。ひとつ兄上のエンティティの子にする
	 * @param entity 指定エンティティ
	 */
	public void right(E entity) {
		if (entity == null || entity.getParent() == null) {
			return;
		}

		// エンティティのインデクス番号を取得
		int index = entity.getParent().getChildren().indexOf(entity);
		if (index < 1) {
			return;
		}

		// ひとつ前のエンティティを取得して処理
		E newParent = (E)entity.getParent().getChildren().get(index - 1);
		newParent.getChildren().add(entity);
	}

	/**
	 * 現在選択されているエンティティを左へ移動.
	 */
	public void right() {
		this.right(this.getSelectedEntity());
	}

	/**
	 * 指定エンティティをリストの左へ移動。祖父のエンティティの子にする
	 * @param entity 指定エンティティ
	 */
	public void left(E entity) {
		if (entity == null || entity.getParent() == null || entity.getParent().getParent() == null) {
			return;
		}

		// 親エンティティのインデクス番号を取得
		int index = entity.getParent().getParent().getChildren().indexOf(entity.getParent());
		if (index < entity.getParent().getParent().getChildren().size() - 1) {
			index++;
		}

		// 祖父のエンティティに挿入する処理
		this.insert(index, entity, (E)entity.getParent().getParent());
	}

	/**
	 * 現在選択されているエンティティを左へ移動.
	 */
	public void left() {
		this.left(this.getSelectedEntity());
	}

	/**
	 * 指定したエンティティを取得する
	 * @param id エンティティのID
	 * @return エンティティ。見つからなければnull
	 */
	public E get(long id) {
		return (E)this.rootEntity.get(id);
	}
}
