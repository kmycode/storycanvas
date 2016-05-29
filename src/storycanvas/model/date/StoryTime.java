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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * ストーリー内で利用する時刻を格納するクラス
 *
 * @author KMY
 */
public class StoryTime implements Serializable, Comparable<StoryTime> {

	private transient StoryCalendar calendar;

	/**
	 * コンストラクタ。publicでないため、外部から直接呼ぶことは出来ない。
	 * 外部からはStoryCalendar#timeメソッドから生成する
	 * @param c 暦をあらわすインスタンス
	 */
	StoryTime (StoryCalendar c) {
		this.calendar = c;
	}

	private transient int hour;
	private transient int minute;
	private transient int second;

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

//<editor-fold defaultstate="collapsed" desc="シリアライズ">
	private static final long serialVersionUID = 1L;
	private static final long serialInstanceVersionUID = 3_00000000001L;

	/**
	 * シリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームへの出力に失敗した時スロー
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		// 固有UID書き込み
		stream.writeLong(serialInstanceVersionUID);

		// プロパティ書き込み
		stream.writeLong(0L);			// CalendarのIDのために予約
		stream.writeInt(this.getHour());
		stream.writeInt(this.getMinute());
		stream.writeInt(this.getSecond());
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		long uid = stream.readLong();
		if (uid == serialInstanceVersionUID) {

			// 暫定：暦を西暦にしておく
			this.calendar = StoryCalendar.ANNO_DOMINI;

			// プロパティ読込
			stream.readLong();
			this.setHour(stream.readInt());
			this.setMinute(stream.readInt());
			this.setSecond(stream.readInt());
		}

	}
//</editor-fold>

}
