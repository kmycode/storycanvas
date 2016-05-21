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
package storycanvas.model.story;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.kmycode.javafx.Messenger;
import storycanvas.message.entity.edit.EmptyEditMessage;
import storycanvas.message.entity.edit.PersonEditMessage;
import storycanvas.message.entity.edit.PlaceEditMessage;
import storycanvas.message.entity.list.MainPersonTableInitializeMessage;
import storycanvas.message.entity.list.MainPlaceTableInitializeMessage;
import storycanvas.model.date.StoryCalendar;
import storycanvas.model.date.StoryDate;
import storycanvas.model.entity.Person;
import storycanvas.model.entity.Place;
import storycanvas.model.entity.Sex;
import storycanvas.model.entityset.EntityListModel;
import storycanvas.model.entityset.EntityTreeModel;

/**
 * ひとつのストーリーをあらわすモデル。
 * 1プロセスにつき1つのインスタンスが存在することを想定しているので、
 * （他のストーリーからのインポート・エクスポートを考慮して）
 * コンストラクタはpublicであるものの、事実上のシングルトンとして運用する
 *
 * @author KMY
 */
public class Story {

	public Story() {
		setCurrent(this);

		// リストで登場人物が選択された時
		this.persons.selectedEntityProperty().addListener(e -> {
			if (this.persons.getSelectedEntity() != null) {
				Messenger.getInstance().send(new PersonEditMessage(this.persons.getSelectedEntity()));
			} else {
				Messenger.getInstance().send(new EmptyEditMessage());
			}
		});

		// リストで場所が選択された時
		this.places.selectedEntityProperty().addListener(e -> {
			if (this.places.getSelectedEntity() != null) {
				Messenger.getInstance().send(new PlaceEditMessage(this.places.getSelectedEntity()));
			} else {
				Messenger.getInstance().send(new EmptyEditMessage());
			}
		});
		
		// TODO: テスト用データ
		Person p1 = new Person();
		p1.setLastName("中村");
		p1.setFirstName("翠");
		StoryDate p1d = StoryCalendar.ANNO_DOMINI.date(2004, 11, 30);
		p1.setBirthDay(p1d);
		p1.setSex(Sex.FEMALE);
		this.persons.add(p1);

		Place l1 = new Place();
		l1.setName("学校");
		this.places.add(l1);

		// TODO: 日付計算テスト
		/*
		StoryDate from = new StoryDate();
		from.setYear(1992);
		from.setMonth(4);
		from.setDay(16);
		StoryDate to = new StoryDate();
		to.setYear(2112);
		to.setMonth(9);
		to.setDay(3);
		System.out.println(StoryCalendar.ANNO_DOMINI.getWeekday(to).getName());
		//System.out.println(StoryCalendar.ANNO_DOMINI.getDayDistance(from, to));
		*/
	}

//<editor-fold defaultstate="collapsed" desc="事実上のシングルトンを実現する部分">
	private static final ObjectProperty<Story> current = new SimpleObjectProperty<>();

	public static Story getCurrent() {
		return current.get();
	}

	public static void setCurrent(Story s) {
		current.set(s);
	}
	
	static {
		current.addListener(e -> {
			Story mainStory = getCurrent();
			if (mainStory != null) {
				mainStory.reloadView();
			}
		});
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 登場人物一覧.
	 */
	private final EntityListModel<Person> persons = new EntityListModel<>();

	/**
	 * 場所一覧.
	 */
	private final EntityTreeModel<Place> places = new EntityTreeModel<>(new Place());
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="メソッド">
	/**
	 * 画面全体の表示を更新.
	 */
	private void reloadView() {
		// 画面表示を更新するメッセージを送信
		Messenger.getInstance().send(new MainPersonTableInitializeMessage(this.persons.getEntities(), this.persons.selectedEntityProperty()));
		Messenger.getInstance().send(new MainPlaceTableInitializeMessage(this.places.getRootTreeItem(), this.places.selectedTreeItemEntityProperty()));
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="人物の操作">
	/**
	 * 人物を追加.
	 */
	public void addPerson() {
		Person entity = new Person();
		this.persons.add(entity);
		Messenger.getInstance().send(new PersonEditMessage(entity));
	}

	/**
	 * 選択された人物を削除.
	 */
	public void deletePerson() {
		Messenger.getInstance().send(new EmptyEditMessage());
		this.persons.delete();
	}

	/**
	 * 選択された人物を上へ移動.
	 */
	public void upPerson() {
		this.persons.up();
	}

	/**
	 * 選択された人物を下へ移動.
	 */
	public void downPerson() {
		this.persons.down();
	}
//</editor-fold>

}
