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
package storycanvas.model.date;

import java.io.Serializable;

/**
 * ストーリー内で利用する日付を格納するクラス
 *
 * @author KMY
 */
public class StoryDate implements Serializable, Comparable<StoryDate> {

	private final StoryCalendar calendar;

	/**
	 * コンストラクタ。publicでないため、外部から直接呼ぶことは出来ない。
	 * 外部からはStoryCalendar#dateメソッドから生成する
	 * @param c 暦をあらわすインスタンス
	 */
	StoryDate (StoryCalendar c) {
		this.calendar = c;
	}

	private int year;
	private int month;
	private int day;

	public int getYear () {
		return year;
	}

	public void setYear (int year) {
		this.year = year;
	}

	public int getMonth () {
		return month;
	}

	public void setMonth (int month) {
		this.month = month;
	}

	public int getDay () {
		return day;
	}

	public void setDay (int day) {
		this.day = day;
	}

	/**
	 * 有効な日付であるか確認
	 * @return 有効であればtrue
	 */
	public boolean isValid() {
		return this.calendar.isValid(this);
	}

	/**
	 * 指定日との日数差を計算
	 * @param other 指定日
	 * @return 日数差。指定日のほうが先ならば0より大きく、指定日のほうが古ければ0より小さい
	 */
	public int distanceDays(StoryDate other) {
		return this.calendar.getDayDistance(this, other);
	}

	/**
	 * 曜日を取得
	 * @return 曜日オブジェクト
	 */
	public StoryCalendar.Weekday getWeekday() {
		return this.calendar.getWeekday(this);
	}

	/**
	 * ２つの時間を比較し、どちらが先か判定します
	 * @param o
	 * @return thisのほうが新しい時は1、そうでないときは-1
	 */
	@Override
	public int compareTo (StoryDate o) {
		if (this.year > o.year) {
			return 1;
		} else if (this.year < o.year) {
			return -1;
		} else {
			if (this.month > o.month) {
				return 1;
			} else if (this.month < o.month) {
				return -1;
			} else {
				if (this.day > o.day) {
					return 1;
				} else if (this.day < o.day) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

}
