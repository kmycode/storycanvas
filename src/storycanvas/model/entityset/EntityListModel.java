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
import java.util.ArrayList;
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

//<editor-fold defaultstate="collapsed" desc="ファイル入出力メソッド">
	/**
	 * シリアライズを行う
	 * @throws IOException ストリームの読込に失敗した時スロー
	 */
	public void writeObject(ObjectOutputStream stream) throws IOException {

		// エンティティの書き込み
		stream.writeInt(this.entities.size());
		for (E entity : this.entities) {
			stream.writeObject(entity);
		}
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		// エンティティの取り込み
		// わざわざ新しいリストを作ることで、リスナの呼び出し回数を節約。GUI描画処理が入れられたりするので
		int size = stream.readInt();
		ArrayList<E> addList = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			addList.add((E)stream.readObject());
		}
		this.entities.addAll(addList);
	}
//</editor-fold>

	/**
	 * エンティティを追加
	 * @param entity 追加するエンティティ
	 */
	@Override
	public void add(E entity) {
		//if (this.getSelectedEntity() == null || this.entities.isEmpty()) {
			this.entities.add(entity);
			/*
		} else {
			this.insert(this.entities.indexOf(this.getSelectedEntity()), entity);
		}
		*/
	}

	/**
	 * エンティティを、現在選択されているエンティティの上に追加。
	 * 選択がなければ、通常のaddと同じ
	 * @param entity 追加するエンティティ
	 */
	public void insert(E entity) {
		if (this.getSelectedEntity() == null || this.entities.isEmpty()) {
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

		/*
		// 以降のエンティティの順番をリストアップ
		Queue<Long> orderStack = new ArrayDeque<>();
		for (int i = index; i < this.entities.size(); i++) {
			orderStack.add(this.entities.get(i).getOrder());
		}
		orderStack.add(entity.getOrder());
		
		// 挿入するエンティティの順番を取得
		long entityOrder = orderStack.poll();

		// 以降のエンティティを１つずつ下にずらす
		for (int i = index; i < this.entities.size(); i++) {
			this.entities.get(i).setOrder(orderStack.poll());
		}
		*/

		// 以降のエンティティの順番を１つずつずらす
		long oldTargetOrder = target.getOrder();
		this.shiftOrder(target);

		// エンティティの順番を設定
		entity.setOrder(oldTargetOrder);

		// エンティティを挿入
		// ここまでに一通りやっておくことで、リスナーに出来る限り正しい順番番号を渡す
		this.entities.add(index, entity);
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
	 * エンティティを全削除.
	 */
	@Override
	public void clear() {
		this.entities.clear();
	}
	
	/**
	 * 指定エンティティをリストの上へ移動
	 * @param entity 指定エンティティ
	 */
	public void up(E entity) {
		if (entity == null) {
			return;
		}

		// エンティティを順番通りに並べ替える
		FXCollections.sort(this.entities);

		// ひとつ順番が上のものを探す
		int entityIndex = this.entities.indexOf(entity);
		E target = entityIndex - 1 >= 0 ? this.entities.get(entityIndex - 1) : null;
		if (target == null) {
			return;
		}
		entity.replaceOrder(target);

		// エンティティを順番通りに並べ替える
		this.entities.add(entityIndex - 1, this.entities.remove(entityIndex));
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
		if (entity == null) {
			return;
		}

		// エンティティを順番通りに並べ替える
		FXCollections.sort(this.entities);
		
		// ひとつ順番が下のものを探す
		int entityIndex = this.entities.indexOf(entity);
		E target = entityIndex + 1 < this.entities.size() ? this.entities.get(entityIndex + 1) : null;
		if (target == null) {
			return;
		}
		entity.replaceOrder(target);

		// エンティティを順番通りに並べ替える
		this.entities.add(entityIndex + 1, this.entities.remove(entityIndex));
	}

	/**
	 * 現在選択されているエンティティを上へ移動.
	 */
	public void down() {
		this.down(this.getSelectedEntity());
	}

	/**
	 * orderが指定したシーン以上であるエンティティの順番を1つずつ足し算してずらす
	 * （引き算するメソッドは、orderの仕様上意味が無いので作らない）
	 * ただし、指定したエンティティ（startEntity）がリストに登録されていない場合、そのエンティティに対しては処理を行わない
	 * @param entity 指定するエンティティ。orderの値がこのエンティティのもの以上であるエンティティに対して処理される
	 */
	public void shiftOrder(E entity) {
		long baseOrder = entity.getOrder();
		for (E e : this.getEntities()) {
			if (e.getOrder() >= baseOrder) {
				e.setOrder(e.getOrder() + 1);
			}
		}
	}

	/**
	 * 指定のエンティティの１つ前のorderのエンティティを取得
	 * @param entity エンティティ
	 */
	public E getBack(E entity) {
		FXCollections.sort(this.entities);
		int index = this.entities.indexOf(entity);
		if (index > 0) {
			return this.entities.get(index - 1);
		} else {
			return null;
		}
	}

	/**
	 * 指定のエンティティの１つ後のorderのエンティティを取得
	 * @param entity エンティティ
	 */
	public E getNext(E entity) {
		FXCollections.sort(this.entities);
		int index = this.entities.indexOf(entity);
		if (index < this.entities.size() - 1) {
			return this.entities.get(index + 1);
		} else {
			return null;
		}
	}

	/**
	 * 指定したエンティティを取得する
	 * @param id エンティティのID
	 * @return エンティティ。見つからなければnull
	 */
	public E get(long id) {
		for (E e : this.entities) {
			if (e.getId() == id) {
				return e;
			}
		}
		return null;
	}

	/**
	 * エンティティの数を取得する
	 * @return エンティティの数
	 */
	public int count() {
		return this.entities.size();
	}

}
