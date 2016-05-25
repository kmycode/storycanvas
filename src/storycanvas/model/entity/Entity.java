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

import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import storycanvas.resource.Resources;

/**
 * エンティティをあらわす抽象クラス
 * 
 * @author KMY
 */
public abstract class Entity implements Comparable<Entity> {

	private static long entityCount = 0;

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * エンティティのID.
	 */
	private final LongProperty id = new SimpleLongProperty();

	public long getId () {
		return id.get();
	}

	protected void setId (long value) {
		id.set(value);
	}

	public LongProperty idProperty () {
		return id;
	}

	/**
	 * エンティティの名前.
	 */
	private final StringProperty name = new SimpleStringProperty();

	public String getName () {
		return name.get();
	}

	public void setName (String value) {
		name.set(value);
	}

	public StringProperty nameProperty () {
		return name;
	}

	/**
	 * エンティティのアイコン.
	 */
	private final ObjectProperty<Image> icon = new SimpleObjectProperty<>(this.getDefaultIcon());

	public Image getDefaultIcon () {
		return Resources.getLargeIcon(this.getResourceName());
	}

	public Image getIcon () {
		return icon.get();
	}

	public void setIcon (Image value) {
		icon.set(value);
	}

	public ObjectProperty<Image> iconProperty () {
		return icon;
	}

	/**
	 * エンティティの順番.
	 */
	private final LongProperty order = new SimpleLongProperty();

	public long getOrder () {
		return order.get();
	}

	public void setOrder (long value) {
		order.set(value);
	}

	public LongProperty orderProperty () {
		return order;
	}
	
	/**
	 * 色.
	 */
	private final ObjectProperty<Color> color = new SimpleObjectProperty<>();

	public Color getColor () {
		return color.get();
	}

	public void setColor (Color value) {
		color.set(value);
	}

	public ObjectProperty<Color> colorProperty () {
		return color;
	}
	
	/**
	 * メモ.
	 */
	private final StringProperty memo = new SimpleStringProperty();

	public String getMemo () {
		return memo.get();
	}

	public void setMemo (String value) {
		memo.set(value);
	}

	public StringProperty memoProperty () {
		return memo;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="メソッド">
	/**
	 * 事実上のコンストラクタです。
	 * これは、シリアライズにあたってreadObjectメソッドから呼び出されることを想定しています。.
	 */
	protected void initialize() {
		long id = this.getNextID();
		this.setId(id);
		this.setOrder(id);
	}

	/**
	 * エンティティを新規作成する時、新しいIDを取得します。
	 * この数値は、エンティティの順番（order）として使われる場合があります。
	 * @return 新しいID
	 */
	private long getNextID() {
		return entityCount++;
	}

	/**
	 * そのエンティティの、リソース上の名前を取得します。
	 * 例えば人物の場合、「person」が返ります。
	 * @return リソース上の名前
	 */
	protected abstract String getResourceName();

	/**
	 * エンティティ同士の比較。一覧表示するときの初期配置に影響することを想定します。
	 * @param other 比較対象
	 * @return 比較結果
	 */
	@Override
	public int compareTo(Entity other) {
		return this.getOrder() > other.getOrder() ? 1 : this.getOrder() == other.getOrder() ? 0 : -1;
	}

	/**
	 * 指定したエンティティの順番を入れ替えます
	 * @param s1 エンティティ１
	 * @param s2 エンティティ２
	 */
	public void replaceOrder(Entity other) {
		long tmp = this.getOrder();
		this.setOrder(other.getOrder());
		other.setOrder(tmp);
	}
//</editor-fold>

}
