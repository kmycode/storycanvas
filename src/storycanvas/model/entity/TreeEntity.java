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
	private TreeItem<TreeEntity> rootTreeItem = new TreeItem<>(this);

	public TreeItem<TreeEntity> getRootTreeItem() {
		return this.rootTreeItem;
	}
//</editor-fold>

	public TreeEntity() {
		this.initialize();
	}

	/**
	 * 初期化処理。子エンティティのリストを操作した時、親エンティティを設定するリスナ
	 */
	@Override
	protected void initialize() {
		super.initialize();
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
			}
		});
	}
	
	/**
	 * TreeItemのchildrenを更新する
	 * （再帰しないので、子エンティティのTreeItemの更新には別途呼出が必要）.
	 */
	private void updateTreeItemChildren() {
		this.rootTreeItem.getChildren().clear();
		for (TreeEntity el : this.getChildren()) {
			this.rootTreeItem.getChildren().add(el.getRootTreeItem());
		}
	}

	/**
	 * 親を削除したときのイベント.
	 */
	private void removeParent() {
		if (this.getParent() != null) {
			this.getParent().getChildren().remove(this);
			this.getParent().updateTreeItemChildren();
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

}
