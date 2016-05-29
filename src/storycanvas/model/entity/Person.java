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
import java.io.Serializable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import storycanvas.model.date.StoryDate;

/**
 * 登場人物
 *
 * @author KMY
 */
public class Person extends EditableEntity implements Serializable {
	
//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 姓.
	 */
	private transient StringProperty lastName = new SimpleStringProperty("");
	
	public String getLastName () {
		return lastName.get();
	}
	
	public void setLastName (String value) {
		lastName.set(value);
	}
	
	public StringProperty lastNameProperty () {
		return lastName;
	}
	
	/**
	 * 名.
	 */
	private transient StringProperty firstName = new SimpleStringProperty("");
	
	public String getFirstName () {
		return firstName.get();
	}
	
	public void setFirstName (String value) {
		firstName.set(value);
	}
	
	public StringProperty firstNameProperty () {
		return firstName;
	}
	
	/**
	 * 性別.
	 */
	private transient ObjectProperty<Sex> sex = new SimpleObjectProperty<>();

	public Sex getSex () {
		return sex.get();
	}

	public void setSex (Sex value) {
		sex.set(value);
	}

	public ObjectProperty<Sex> sexProperty () {
		return sex;
	}

	/**
	 * 誕生日.
	 */
	private transient ObjectProperty<StoryDate> birthDay = new SimpleObjectProperty<>();

	public StoryDate getBirthDay () {
		return birthDay.get();
	}

	public void setBirthDay (StoryDate value) {
		birthDay.set(value);
	}

	public ObjectProperty<StoryDate> birthDayProperty () {
		return birthDay;
	}

	/**
	 * 死亡日.
	 */
	private transient ObjectProperty<StoryDate> deathDay = new SimpleObjectProperty<>();

	public StoryDate getDeathDay () {
		return deathDay.get();
	}

	public void setDeathDay (StoryDate value) {
		deathDay.set(value);
	}

	public ObjectProperty<StoryDate> deathDayProperty () {
		return deathDay;
	}
//</editor-fold>
	
//<editor-fold defaultstate="collapsed" desc="シリアライズ">
	private static final long serialVersionUID = 1L;
	private static final long serialInstanceVersionUID = 1_00000000001L;

	/**
	 * シリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームへの出力に失敗した時スロー
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		this.writeBaseObject(stream);

		// 固有UID書き込み
		stream.writeLong(serialInstanceVersionUID);

		// プロパティ書き込み
		stream.writeUTF(this.getFirstName());
		stream.writeUTF(this.getLastName());
		stream.writeObject(this.getBirthDay());
		stream.writeObject(this.getDeathDay());

		// 性
		stream.writeLong(this.getSex().getId());
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		this.readBaseObject(stream);

		long uid = stream.readLong();
		if (uid == serialInstanceVersionUID) {

			// コンストラクタ呼び出し
			this.initialize();

			// プロパティ読込
			this.setFirstName(stream.readUTF());
			this.setLastName(stream.readUTF());
			this.setBirthDay((StoryDate)stream.readObject());
			this.setDeathDay((StoryDate)stream.readObject());

			// TODO: 性。将来的にSexオブジェクトから取得するよう変更する
			long sexId = stream.readLong();
			this.setSex(sexId == Sex.MALE.getId() ? Sex.MALE : sexId == Sex.FEMALE.getId() ? Sex.FEMALE : null);
		}

	}
//</editor-fold>

	public Person() {
		this.initialize();
	}

	/**
	 * 初期化。事実上のコンストラクタ.
	 */
	@Override
	protected final void initialize() {
		super.initialize();

		this.firstName = new SimpleStringProperty("");
		this.lastName = new SimpleStringProperty("");
		this.sex = new SimpleObjectProperty<>();
		this.birthDay = new SimpleObjectProperty<>();
		this.deathDay = new SimpleObjectProperty<>();

		this.setNameBinding();
	}

	/**
	 * 人物の名前を変更したら、Entity.nameプロパティに影響するようにする。
	 * デシリアライズのことを考慮し、処理をコンストラクタから分離.
	 */
	private void setNameBinding() {
		this.firstName.addListener(e -> this.nameProperty().set(this.toString()));
		this.lastName.addListener(e -> this.nameProperty().set(this.toString()));
	}

	@Override
	protected String getResourceName () {
		return "person";
	}

	@Override
	public String toString() {
		String ln = this.getLastName();
		String fn = this.getFirstName();
		String s = "";
		if (ln != null) {
			s += ln;
		}
		if (fn != null) {
			s += fn;
		}
		return s;
	}

}
