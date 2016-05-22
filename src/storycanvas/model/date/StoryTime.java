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
 * ストーリー内で利用する時刻を格納するクラス
 *
 * @author KMY
 */
public class StoryTime implements Serializable, Comparable<StoryTime> {

	private final StoryCalendar calendar;

	/**
	 * コンストラクタ。publicでないため、外部から直接呼ぶことは出来ない。
	 * 外部からはStoryCalendar#timeメソッドから生成する
	 * @param c 暦をあらわすインスタンス
	 */
	StoryTime (StoryCalendar c) {
		this.calendar = c;
	}

	private int hour;
	private int minute;
	private int second;

	public int getHour () {
		return hour;
	}

	public void setHour (int hour) {
		this.hour = hour;
	}

	public int getMinute () {
		return minute;
	}

	public void setMinute (int minute) {
		this.minute = minute;
	}

	public int getSecond () {
		return second;
	}

	public void setSecond (int second) {
		this.second = second;
	}

	/**
	 * 有効な時刻であるか確認
	 * @return 有効であればtrue
	 */
	public boolean isValid() {
		return this.calendar.isValid(this);
	}

	/**
	 * 指定時刻との時刻差を秒数で計算
	 * @param other 指定日
	 * @return 日数差。指定日のほうが先ならば0より大きく、指定日のほうが古ければ0より小さい
	 */
	public int distanceSeconds(StoryTime other) {
		return this.calendar.getTimeDistance(this, other);
	}

	/**
	 * 時刻を文字形式にします
	 * @return 文字形式になった時刻
	 */
	public String toString() {
		return this.calendar.toString(this);
	}

	/**
	 * ２つの時刻を比較し、どちらが先か判定します
	 * @param o 比較対象
	 * @return thisのほうが新しい時は1、そうでないときは-1
	 */
	@Override
	public int compareTo(StoryTime o) {
		if (this.hour > o.hour) {
			return 1;
		} else if (this.hour < o.hour) {
			return -1;
		} else {
			if (this.minute > o.minute) {
				return 1;
			} else if (this.minute < o.minute) {
				return -1;
			} else {
				if (this.second > o.second) {
					return 1;
				} else if (this.second < o.second) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}

}
