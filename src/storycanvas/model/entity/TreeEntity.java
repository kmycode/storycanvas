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
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import net.kmycode.javafx.SimpleWeakObjectProperty;

/**
 * ツリー状の情報を持つエンティティ（親子関係がある）.
 *
 * @author KMY
 */
public abstract class TreeEntity extends Entity {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 親エンティティ.
	 */
	private final ObjectProperty<TreeEntity> parent = new SimpleWeakObjectProperty<>();
	
	public TreeEntity getParent () {
		return parent.get();
	}
	
	private void setParent (TreeEntity value) {
		parent.set(value);
	}
	
	public ReadOnlyObjectProperty<TreeEntity> parentProperty () {
		return parent;
	}
	
	/**
	 * 子エンティティのリスト.
	 */
	private final ListProperty<TreeEntity> children = new SimpleListProperty<>(FXCollections.observableArrayList());

	public ObservableList<TreeEntity> getChildren () {
		return children.get();
	}

	public void setChildren (ObservableList<TreeEntity> value) {
		children.set(value);
	}

	public ListProperty<TreeEntity> childrenProperty () {
		return children;
	}

	/**
	 * JavaFXのツリーで利用するアイテム.
	 */
	private WeakReference<TreeItem<TreeEntity>> rootTreeItem = new WeakReference<TreeItem<TreeEntity>>(new TreeItem<>(this));

	public TreeItem<TreeEntity> getTreeItem() {
		return this.rootTreeItem.get();
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ファイル入出力メソッド">
	/**
	 * シリアライズを行う
	 * @throws IOException ストリームの読込に失敗した時スロー
	 */
	protected final void writeChildren(ObjectOutputStream stream) throws IOException {

		// エンティティの書き込み
		stream.writeInt(this.getChildren().size());
		for (TreeEntity entity : this.getChildren()) {
			stream.writeObject(entity);
		}
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	public void readChildren(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		// エンティティの取り込み
		// わざわざ新しいリストを作ることで、リスナの呼び出し回数を節約。GUI描画処理が入れられたりするので
		int size = stream.readInt();
		ArrayList<TreeEntity> addList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			addList.add((TreeEntity)stream.readObject());
		}
		this.getChildren().addAll(addList);
	}
//</editor-fold>

	/**
	 * 初期化処理。子エンティティのリストを操作した時、親エンティティを設定するリスナ
	 */
	@Override
	protected void initialize() {
		super.initialize();
		
		// ツリーが最初から開かれているようにする
		this.rootTreeItem.get().setExpanded(true);

		// エンティティの子リストと、TreeItemの子リストを自動的に連動させる
		this.children.addListener((ListChangeListener.Change<? extends TreeEntity> e) -> {

			while (e.next()) {

				// 追加された項目の元親の子リストから削除して、自分を新しい親に設定する
				if (e.wasAdded()) {
					List<? extends TreeEntity> subList = e.getAddedSubList();
					for(TreeEntity el : subList) {
						el.removeParent();
						el.newParent(this);
					}
				}

				// 削除された項目の親を削除
				else if (e.wasRemoved()) {
					List<? extends TreeEntity> subList = e.getRemoved();
					for(TreeEntity el : subList) {
						el.removeParent();
					}
				}

				// ソートされた時
				else if (e.wasPermutated()) {
					this.updateTreeItemChildren();
				}
			}
		});
	}
	
	/**
	 * TreeItemのchildrenを更新する
	 * （再帰しないので、子エンティティのTreeItemの更新には別途呼出が必要）.
	 */
	private void updateTreeItemChildren() {
		this.getTreeItem().getChildren().clear();
		for (TreeEntity el : this.getChildren()) {
			this.getTreeItem().getChildren().add(el.getTreeItem());
		}
	}

	/**
	 * 親を削除したときのイベント.
	 */
	private void removeParent() {
		if (this.getParent() != null) {
			TreeEntity oldParent = this.getParent();
			this.getParent().getChildren().remove(this);
			oldParent.updateTreeItemChildren();
		}
		this.setParent(null);
	}

	/**
	 * 親を設定した時のイベント.
	 */
	private void newParent(TreeEntity parent) {
		if (parent == null) {
			this.removeParent();
		} else {
			this.setParent(parent);
			parent.updateTreeItemChildren();
		}
	}

	/**
	 * 指定したIDを持つエンティティを取得する
	 * @param ID
	 * @return エンティティ。見つからなければnull
	 */
	public TreeEntity get(long id) {
		for (TreeEntity te : this.children) {
			TreeEntity e = te.get(id);
			if (e != null) {
				return e;
			}
		}
		return null;
	}

	/**
	 * 全ての子孫の総数を返す
	 * @return 子孫の総数
	 */
	public int descendantsCount() {
		int c = 1;		// 自身を1カウント
		for (TreeEntity e : this.children) {
			c += e.descendantsCount();
		}
		return c;
	}

}
