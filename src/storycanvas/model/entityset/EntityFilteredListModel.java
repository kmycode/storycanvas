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

import java.util.EventListener;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.kmycode.javafx.ListUtil;
import storycanvas.model.entity.Entity;

/**
 * エンティティのリストモデル。
 * ある決められた条件に合致するエンティティのみを外から見えるようにする。
 * orderのシフトなどの内部処理では、隠されたエンティティに対しても処理を実施し、
 * 隠されたエンティティがいつ外部に公開されてもいい状態にする
 *
 * @author KMY
 */
public class EntityFilteredListModel<E extends Entity> extends EntityListModel<E> {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * エンティティのリスト本体.
	 */
	private final ListProperty<E> hiddenEntities = new SimpleListProperty<>(FXCollections.observableArrayList());

	public ObservableList<E> getHiddenEntities () {
		return hiddenEntities.get();
	}

	public ObservableList<E> getHiddenEntitiesClone () {
		return ListUtil.getClone(hiddenEntities);
	}

	public ListProperty<E> hiddenEntitiesProperty () {
		return hiddenEntities;
	}
//</editor-fold>
	
	/**
	 * 公開されたエンティティ、隠されたエンティティ、全てを組み合わせたリストを返す
	 * @return 全てのエンティティのリスト
	 */
	public ObservableList<E> getAllEntities() {
		ObservableList<E> list = FXCollections.observableArrayList();
		list.addAll(this.getEntities());
		list.addAll(this.getHiddenEntities());
		return list;
	}
	
	/**
	 * 指定したリスナに従いフィルタリングを行う
	 * @param listener フィルタリングの条件を指定したリスナ
	 */
	public void filtering(EntityFilterListener<E> listener) {
		ObservableList<E> list = this.getAllEntities();
		this.hiddenEntities.clear();
		this.entitiesProperty().clear();
		for (E entity : list) {
			if (listener.isShow(entity)) {
				this.getEntities().add(entity);
			} else {
				this.hiddenEntities.add(entity);
			}
		}
	}

	/**
	 * orderが指定したシーン以上であるエンティティの順番を1つずつ足し算してずらす
	 * 隠されたエンティティのリストに対しても処理を行う
	 * @param entity 指定するエンティティ。orderの値がこのエンティティのもの以上であるエンティティに対して処理される
	 */
	@Override
	public void shiftOrder(E entity) {
		super.shiftOrder(entity);
		long baseOrder = entity.getOrder();
		for (E e : this.getHiddenEntities()) {
			if (e.getOrder() >= baseOrder) {
				e.setOrder(e.getOrder() + 1);
			}
		}
	}
	
//<editor-fold defaultstate="collapsed" desc="リスナインターフェイス">
	/**
	 * エンティティのフィルタ条件を指定するリスナ.
	 */
	@FunctionalInterface
	public interface EntityFilterListener<E extends Entity> extends EventListener {
		public boolean isShow(E entity);
	}
//</editor-fold>

}
