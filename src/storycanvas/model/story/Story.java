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
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.kmycode.javafx.ListUtil;
import storycanvas.model.date.StoryCalendar;
import storycanvas.model.date.StoryDate;
import storycanvas.model.entity.Person;

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
		
		// TODO: テスト用データ
		Person p1 = new Person();
		p1.setLastName("中村");
		p1.setFirstName("翠");
		StoryDate p1d = StoryCalendar.ANNO_DOMINI.date(2004, 11, 30);
		p1.setBirthDay(p1d);
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
	private static Story current;

	public static Story getCurrent() {
		return current;
	}

	public static void setCurrent(Story s) {
		current = s;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="プロパティ">
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
//</editor-fold>

}
