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
package storycanvas.view.control.date;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import storycanvas.model.date.StoryCalendar;
import storycanvas.model.date.StoryDate;

/**
 * FXML Controller class
 *
 * @author KMY
 */
public class CalendarControl extends GridPane implements Initializable {

	private List<Button> dayButtons = new ArrayList<>();

	public CalendarControl() {
		// 年月のリスナを設定
		this.calendar.addListener(e -> this.storyCalendarChanged());
		this.year.addListener(e -> this.makeCalendar());
		this.month.addListener(e -> this.makeCalendar());

		// TODO: とりあえず西暦固定で
		this.setCalendar(StoryCalendar.ANNO_DOMINI);
		this.setYear(2016);
		this.setMonth(5);
	}

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 暦.
	 */
	private final ObjectProperty<StoryCalendar> calendar = new SimpleObjectProperty<>();

	public StoryCalendar getCalendar () {
		return calendar.get();
	}

	public void setCalendar (StoryCalendar value) {
		calendar.set(value);
	}

	public ObjectProperty<StoryCalendar> calendarProperty () {
		return calendar;
	}

	/**
	 * 年.
	 */
	private final IntegerProperty year = new SimpleIntegerProperty();

	public int getYear () {
		return year.get();
	}

	public void setYear (int value) {
		year.set(value);
	}

	public IntegerProperty yearProperty () {
		return year;
	}

	/**
	 * 月.
	 */
	private final IntegerProperty month = new SimpleIntegerProperty();

	public int getMonth () {
		return month.get();
	}

	public void setMonth (int value) {
		month.set(value);
	}

	public IntegerProperty monthProperty () {
		return month;
	}

	/**
	 * 日.
	 */
	private final IntegerProperty day = new SimpleIntegerProperty();

	public int getDay () {
		return day.get();
	}

	public ReadOnlyIntegerProperty dayProperty () {
		return day;
	}

//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="メソッド">
	/**
	 * 暦を設定し、それに応じてカレンダーの枠組みを生成
	 * @param calendar 暦
	 */
	private void storyCalendarChanged() {
		StoryCalendar calendar = this.getCalendar();
		int weekdaysCount = calendar.getWeekdays().size();

		// 一番日数が多い月の日数を取得
		int maxMonthDayCount = 0;
		for (StoryCalendar.Month m : calendar.getMonths()) {
			if (m.getDayCount() > maxMonthDayCount) {
				maxMonthDayCount = m.getDayCount();
			}
		}

		// 行数、列数
		int rows = (int)((maxMonthDayCount - 1) / weekdaysCount) + 2;
		int cols = weekdaysCount;

		// 曜日の表示を配置
		int i = 0;
		for (StoryCalendar.Weekday w : calendar.getWeekdays()) {
			Button b = new Button(w.getName());
			b.setTextFill(w.getColor());
			b.setBackground(Background.EMPTY);
			GridPane.setColumnIndex(b, i++);
			GridPane.setRowIndex(b, 0);
			this.getChildren().add(b);
		}

		// 日付ボタンを配置
		for (int y = 1; y < rows + 1; y++) {
			for (int x = 0; x < cols; x++) {
				Button b = new Button();
				b.setPrefWidth(35);
				b.setTextFill(calendar.getWeekdays().get(x).getColor());
				GridPane.setColumnIndex(b, x);
				GridPane.setRowIndex(b, y);
				this.getChildren().add(b);
				this.dayButtons.add(b);

				// ボタンクリックしたら、日付をプロパティに設定する
				b.setOnAction((e) -> {
					Button source = (Button)e.getSource();
					Object day = source.getUserData();
					if (day != null && day instanceof Integer) {
						this.day.set((Integer)day);
					}
				});
			}
		}
	}

	/**
	 * 年月を指定してカレンダーを作成する
	 * @param calendar 暦
	 * @param year 年
	 * @param mon 月
	 */
	private void makeCalendar() {

		int mon = this.getMonth();
		int year = this.getYear();
		StoryCalendar calendar = this.getCalendar();

		// 設定値が無効な数字ならば戻る
		if (mon < 1 || mon >= calendar.getMonths().size()) {
			return;
		}

		StoryDate date = calendar.date(year, mon, 1);
		this.clearCalendar();

		// 月初の曜日を取得する
		StoryCalendar.Weekday firstWeekday = date.getWeekday();
		int firstWeekdayIndex = calendar.getWeekdays().indexOf(firstWeekday);
		if (firstWeekdayIndex < 0) return;

		// 月の日数を取得する
		int monthDayCount = calendar.getMonths().get(mon - 1).getDayCount();
		monthDayCount += calendar.getLeapSize(year, mon);

		// カレンダーを作成
		int day = 1;
		for (int i = firstWeekdayIndex; i < this.dayButtons.size(); i++) {
			Button b = this.dayButtons.get(i);
			b.setUserData(day);
			b.setText(Integer.toString(day++));
			b.setVisible(true);
			if (day > monthDayCount) {
				break;
			}
		}
	}

	/**
	 * カレンダーの日付表示をクリアする.
	 */
	private void clearCalendar() {
		for (Button b : this.dayButtons) {
			b.setText("");
			b.setVisible(false);
		}
	}

	/**
	 * 次の年にする.
	 */
	public void nextYear() {
		this.setYear(this.getYear() + 1);
	}

	/**
	 * 前の年にする.
	 */
	public void prevYear() {
		this.setYear(this.getYear() - 1);
	}

	/**
	 * 次の月にする.
	 */
	public void nextMonth() {
		int m = this.getMonth() + 1;
		if (m > this.getCalendar().getMonths().size()) {
			m = 1;
			this.nextYear();
		}
		this.setMonth(m);
	}

	/**
	 * 前の月にする.
	 */
	public void prevMonth() {
		int m = this.getMonth() - 1;
		if (m <= 0) {
			m = this.getCalendar().getMonths().size();
			this.prevYear();
		}
		this.setMonth(m);
	}
//</editor-fold>

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}

}
