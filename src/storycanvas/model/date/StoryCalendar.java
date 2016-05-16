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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.EventListener;
import java.util.List;
import javafx.scene.paint.Color;

/**
 * ストーリー内の暦を定義するクラス
 *
 * @author KMY
 */
public class StoryCalendar implements Serializable {

	public static final StoryCalendar ANNO_DOMINI = new StoryCalendar();

	static {
		// 西暦
		ANNO_DOMINI.months.add(new Month("January", 31));
		ANNO_DOMINI.months.add(new Month("Febrary", 28));
		ANNO_DOMINI.months.add(new Month("March", 31));
		ANNO_DOMINI.months.add(new Month("April", 30));
		ANNO_DOMINI.months.add(new Month("May", 31));
		ANNO_DOMINI.months.add(new Month("June", 30));
		ANNO_DOMINI.months.add(new Month("July", 31));
		ANNO_DOMINI.months.add(new Month("August", 31));
		ANNO_DOMINI.months.add(new Month("September", 30));
		ANNO_DOMINI.months.add(new Month("October", 31));
		ANNO_DOMINI.months.add(new Month("November", 30));
		ANNO_DOMINI.months.add(new Month("December", 31));
		ANNO_DOMINI.weekdays.add(new Weekday("日", Color.RED));
		ANNO_DOMINI.weekdays.add(new Weekday("月", Color.BLACK));
		ANNO_DOMINI.weekdays.add(new Weekday("火", Color.BLACK));
		ANNO_DOMINI.weekdays.add(new Weekday("水", Color.BLACK));
		ANNO_DOMINI.weekdays.add(new Weekday("木", Color.BLACK));
		ANNO_DOMINI.weekdays.add(new Weekday("金", Color.BLACK));
		ANNO_DOMINI.weekdays.add(new Weekday("土", Color.BLUE));
		ANNO_DOMINI.hourMax = 24;
		ANNO_DOMINI.minuteMax = 60;
		ANNO_DOMINI.secondMax = 60;
		ANNO_DOMINI.leapYearListener = y -> {
			if (y % 4 == 0) {
				if (y % 400 == 0) {
					return true;
				} else if (y % 100 == 0) {
					return false;
				} else {
					return true;
				}
			}
			return false;
		};
		ANNO_DOMINI.leapMonthListener = m -> {
			return m == 2;
		};
		ANNO_DOMINI.aSunday = ANNO_DOMINI.date(2, 1, 2000);
		ANNO_DOMINI.locked = true;
	}
	
	private boolean locked = false;
	private final List<Month> months = new CheckedArrayList<>();
	private final List<Weekday> weekdays = new CheckedArrayList<>();
	private int hourMax = 24;
	private int minuteMax = 60;
	private int secondMax = 60;
	private StoryDate aSunday;			// ある日曜日（どの日曜日でも構わない）曜日計算の基準

	// うるう年の日数を判定するリスナ
	public interface LeapSizeListener extends EventListener {
		public int leapSize(int year, int month);
	}
	private LeapSizeListener leapSizeListener = (y,m) -> { return this.leapYearListener.isLeapYear(y) && this.leapMonthListener.isLeapMonth(m) ? 1 : 0; };
	public interface IsLeapYearListener extends EventListener {
		public boolean isLeapYear(int year);
	}
	private IsLeapYearListener leapYearListener = y -> false;
	public interface IsLeapMonthListener extends EventListener {
		public boolean isLeapMonth(int month);
	}
	private IsLeapMonthListener leapMonthListener = m -> false;

	public int getHourMax() {
		return this.hourMax;
	}

	public void setHourMax(int num) {
		this.checkLocked();
		this.hourMax = num;
	}

	public int getMinuteMax() {
		return this.minuteMax;
	}

	public void setMinuteMax(int num) {
		this.checkLocked();
		this.minuteMax = num;
	}

	public int getSecondMax() {
		return this.secondMax;
	}

	public void setSecondMax(int num) {
		this.checkLocked();
		this.secondMax = num;
	}

	public void setASunday(StoryDate date) {
		this.checkLocked();
		this.aSunday = date;
	}

	public LeapSizeListener getLeapSizeListener() {
		return this.leapSizeListener;
	}

	public void setLeapSizeListener(LeapSizeListener l) {
		this.checkLocked();
		this.leapSizeListener = l;
	}

	public int getLeapSize(int year, int month) {
		return this.leapSizeListener.leapSize(year, month);
	}

	private void checkLocked() {
		if (this.locked) {
			throw new IllegalStateException("This StoryCalendar instance is locked!");
		}
	}

	public boolean isLocked() {
		return this.locked;
	}

	public List<Month> getMonths() {
		return this.months;
	}

	public List<Weekday> getWeekdays() {
		return this.weekdays;
	}

	/**
	 * 西暦における現在日付をStoryDateに変換して返す
	 * @return 西暦における現在日付
	 */
	public static StoryDate current() {
		Calendar curr = Calendar.getInstance();
		return ANNO_DOMINI.date(curr.get(Calendar.YEAR), curr.get(Calendar.MONTH) + 1, curr.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 現在の暦に基づいた、指定した日付のオブジェクトを作成する
	 * @param year 年
	 * @param month 月
	 * @param day 日
	 * @return 日付のオブジェクト
	 */
	public StoryDate date(int year, int month, int day) {
		StoryDate d = new StoryDate(this);
		d.setYear(year);
		d.setMonth(month);
		d.setDay(day);
		return d;
	}

	/**
	 * 日付が有効かチェック
	 * @return 有効ならtrue
	 * @param date チェックする日付
	 */
	public boolean isValid(StoryDate date) {

		// 整合性チェック
		if (date.getMonth() <= 0 || date.getMonth() > this.months.size()) {
			return false;
		}
		Month month = this.months.get(date.getMonth() - 1);
		if (date.getDay() <= 0 || date.getDay() > month.getDayCount()) {
			return false;
		}

		return true;
	}
	
	public int getDayDistance(StoryDate from, StoryDate to) {

		// 古い方を古い方にする
		boolean isReplaced = false;
		if (from.compareTo(to) > 0) {
			StoryDate tmp = from;
			from = to;
			to = tmp;
			isReplaced = true;
		}

		// うるうが入る可能性のある月をピックアップ
		List<Month> leapMonths = new ArrayList<>();
		for (int i = 0; i < this.months.size(); i++) {
			if (this.leapMonthListener.isLeapMonth(i + 1)) {
				leapMonths.add(this.months.get(i));
			}
		}
		
		// うるうが入る年をピックアップ
		List<Integer> leapYears = new ArrayList<>();
		for (int i = from.getYear(); i <= to.getYear(); i++) {
			if (this.leapYearListener.isLeapYear(i)) {
				leapYears.add(i);
			}
		}
		
		// うるうによって余計に増えた日数を計算
		int leapDays = 0;
		final int monthCount = this.months.size();
		for (Integer year : leapYears) {
			int y = year.intValue();
			for (int m = 1; m <= monthCount; m++) {
				leapDays += this.leapSizeListener.leapSize(y, m);
			}
		}
		
		// 最初の年と最後の年のうるうによる影響日数をそれぞれ引く
		for (int m = 1; m < from.getMonth(); m++) {
			leapDays -= this.leapSizeListener.leapSize(from.getYear(), m);
		}
		for (int m = to.getMonth(); m <= monthCount; m++) {
			leapDays -= this.leapSizeListener.leapSize(to.getYear(), m);
		}
		
		// 通年の日数を計算する
		int daysOfYear = 0;
		for (Month m : this.months) {
			daysOfYear += m.dayCount;
		}
		
		// 単純に日数を計算する
		int days = leapDays;
		if (to.getYear() != from.getYear()) {
			days += (to.getYear() - from.getYear() - 1) * daysOfYear;
			for (int m = 1; m <= to.getMonth() - 1; m++) {
				days += this.months.get(m - 1).dayCount;
			}
			for (int m = from.getMonth() + 1; m <= monthCount; m++) {
				days += this.months.get(m - 1).dayCount;
			}
			days += this.months.get(from.getMonth() - 1).dayCount - from.getDay();
			days += to.getDay();
		} else {
			for (int m = from.getMonth() + 1; m <= to.getMonth() - 1; m++) {
				days += this.months.get(m - 1).dayCount;
			}
			if (to.getMonth() == from.getMonth()) {
				days += to.getDay() - from.getDay();
			} else {
				days += this.months.get(from.getMonth() - 1).dayCount - from.getDay();
				days += to.getDay();
			}
		}
		
		return isReplaced ? -days : days;
	}

	/**
	 * 指定した日付の曜日を取得
	 * @param date 指定した日付
	 * @return 曜日
	 */
	public Weekday getWeekday(StoryDate date) {
		int days = this.getDayDistance(this.aSunday, date);
		int weekdaynum = days % this.weekdays.size();
		if (days < 0) {
			weekdaynum += this.weekdays.size();
			if (weekdaynum == this.weekdays.size()) {
				weekdaynum = 0;
			}
		}
			if (--weekdaynum == -1) {
				weekdaynum = this.weekdays.size() - 1;
			}
		return this.weekdays.get(weekdaynum);
	}

	public static class Month {
		public Month(String name, int dayCount) {
			this.name = name;
			this.dayCount = dayCount;
		}
		private final String name;			// 月の別名（卯月、葉月、皐月、など）
		private final int dayCount;			// １ヶ月間の日数
		public String getName() {
			return this.name;
		}
		public int getDayCount() {
			return this.dayCount;
		}
	}

	public static class Weekday {
		public Weekday(String name, Color color) {
			this.name = name;
			this.color = color;
		}
		private final String name;			// 週の別名（日曜日、月曜日、火曜日、など）
		private final Color color;			// 週の色
		public String getName() {
			return this.name;
		}
		public Color getColor() {
			return this.color;
		}
	}

	private class CheckedArrayList<E> extends ArrayList<E> {

		@Override
		public boolean add(E e) {
			checkLocked();
			return super.add(e);
		}

		@Override
		public void add(int index, E element) {
			checkLocked();
			super.add(index, element);
		}

		@Override
		public E remove(int index) {
			checkLocked();
			return super.remove(index);
		}

		@Override
		public boolean remove(Object o) {
			checkLocked();
			return super.remove(o);
		}

		@Override
		public void clear() {
			checkLocked();
			super.clear();
		}

		@Override
		public boolean addAll(Collection<? extends E> c) {
			checkLocked();
			return super.addAll(c);
		}

		@Override
		public boolean addAll(int index, Collection<? extends E> c) {
			checkLocked();
			return super.addAll(index, c);
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			checkLocked();
			return super.removeAll(c);
		}
	}
}
