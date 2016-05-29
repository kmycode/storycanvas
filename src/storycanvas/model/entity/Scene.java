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
import net.kmycode.javafx.SimpleWeakObjectProperty;
import storycanvas.model.date.StoryDate;
import storycanvas.model.date.StoryTime;

/**
 * シーン
 *
 * @author KMY
 */
public class Scene extends Entity implements Serializable {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 属するストーリーライン.
	 */
	private ObjectProperty<Storyline> storyline;
	
	public Storyline getStoryline () {
		return storyline.get();
	}
	
	public void setStoryline (Storyline value) {
		storyline.set(value);
	}
	
	public ObjectProperty<Storyline> storylineProperty () {
		return storyline;
	}

	/**
	 * ストーリーラインを削除.
	 */
	public void removeStoryline() {
		if (this.getStoryline() != null) {
			this.getStoryline().getScenes().remove(this);
		}
		this.setStoryline(null);
	}

	/**
	 * 新しいストーリーラインを設定
	 * @param line 新しいストーリーライン
	 */
	public void newStoryline(Storyline line) {
		if (line == null) {
			this.removeStoryline();
		} else {
			this.setStoryline(line);
		}
	}

	/**
	 * テキスト.
	 */
	private StringProperty text = new SimpleStringProperty("");

	public String getText () {
		return text.get();
	}

	public void setText (String value) {
		text.set(value);
	}

	public StringProperty textProperty () {
		return text;
	}

	/**
	 * 開始日付.
	 */
	private ObjectProperty<StoryDate> startDate = new SimpleObjectProperty<>();

	public StoryDate getStartDate () {
		return startDate.get();
	}

	public void setStartDate (StoryDate value) {
		startDate.set(value);
	}

	public ObjectProperty startDateProperty () {
		return startDate;
	}

	/**
	 * 開始時刻.
	 */
	private ObjectProperty<StoryTime> startTime = new SimpleObjectProperty<>();

	public StoryTime getStartTime () {
		return startTime.get();
	}

	public void setStartTime (StoryTime value) {
		startTime.set(value);
	}

	public ObjectProperty startTimeProperty () {
		return startTime;
	}

	/**
	 * 終了日付.
	 */
	private ObjectProperty<StoryDate> endDate = new SimpleObjectProperty<>();

	public StoryDate getEndDate () {
		return endDate.get();
	}

	public void setEndDate (StoryDate value) {
		endDate.set(value);
	}

	public ObjectProperty endDateProperty () {
		return endDate;
	}

	/**
	 * 終了時刻.
	 */
	private ObjectProperty<StoryTime> endTime = new SimpleObjectProperty<>();

	public StoryTime getEndTime () {
		return endTime.get();
	}

	public void setEndTime (StoryTime value) {
		endTime.set(value);
	}

	public ObjectProperty endTimeProperty () {
		return endTime;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="シリアライズ">
	private static final long serialVersionUID = 1L;
	private static final long serialInstanceVersionUID = 7_00000000001L;

	/**
	 * シリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームへの出力に失敗した時スロー
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		this.writeBaseObject(stream);

		// 固有UID書き込み
		stream.writeLong(serialInstanceVersionUID);

		// 所属するストーリーライン
		stream.writeLong(this.getStoryline().getId());

		// プロパティ書き込み
		stream.writeUTF(this.getText());
		stream.writeObject(this.getStartDate());
		stream.writeObject(this.getStartTime());
		stream.writeObject(this.getEndDate());
		stream.writeObject(this.getEndTime());
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

			// コンストラクタ
			this.initialize();

			// 所属するストーリーライン
			Storyline line = Entity.getStoryline(stream.readLong());
			if (line != null) {
				line.getScenes().add(this);
			}

			// プロパティ読込
			this.setText(stream.readUTF());
			this.setStartDate((StoryDate)stream.readObject());
			this.setStartTime((StoryTime)stream.readObject());
			this.setEndDate((StoryDate)stream.readObject());
			this.setEndTime((StoryTime)stream.readObject());
		}

	}
//</editor-fold>

	public Scene() {
		this.initialize();
	}

	@Override
	protected void initialize() {
		super.initialize();
		
		this.storyline = new SimpleWeakObjectProperty<>();
		this.text = new SimpleStringProperty("");
		this.startDate = new SimpleObjectProperty<>();
		this.startTime = new SimpleObjectProperty<>();
		this.endDate = new SimpleObjectProperty<>();
		this.endTime = new SimpleObjectProperty<>();
	}

	@Override
	protected String getResourceName () {
		return "scene";
	}

}
