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

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.kmycode.javafx.ListUtil;
import net.kmycode.javafx.Messenger;
import storycanvas.message.entity.edit.EmptyEditMessage;
import storycanvas.message.entity.edit.PersonEditMessage;
import storycanvas.message.entity.list.MainPersonListInitializeMessage;
import storycanvas.model.date.StoryCalendar;
import storycanvas.model.date.StoryDate;
import storycanvas.model.entity.Person;
import storycanvas.model.entity.Sex;

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
		this.selectedPerson.addListener(e -> {
			if (this.selectedPerson.get() != null) {
				Messenger.getInstance().send(new PersonEditMessage(this.selectedPerson.get()));
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
	 * 登場人物一覧
	 */
	private final ListProperty<Person> persons = new SimpleListProperty<>(FXCollections.observableArrayList());

	public ObservableList<Person> getPersons () {
		return persons.get();
	}

	public ObservableList<Person> getPersonsClone () {
		return ListUtil.getClone(persons);
	}

	public ListProperty<Person> personsProperty () {
		return persons;
	}
	
	/**
	 * 現在選択されている登場人物.
	 */
	private final ObjectProperty<Person> selectedPerson = new SimpleObjectProperty<>();
//</editor-fold>

	/**
	 * 画面全体の表示を更新.
	 */
	private void reloadView() {
		// 画面表示を更新するメッセージを送信
		Messenger.getInstance().send(new MainPersonListInitializeMessage(this.getPersons(), this.selectedPerson));
	}

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
		if (this.selectedPerson.get() != null) {
			Messenger.getInstance().send(new EmptyEditMessage());
			this.persons.remove(this.selectedPerson.get());
		}
	}

}
