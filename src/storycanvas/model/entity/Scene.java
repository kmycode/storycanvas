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

import javafx.beans.property.ObjectProperty;
import net.kmycode.javafx.SimpleWeakObjectProperty;

/**
 * シーン
 *
 * @author KMY
 */
public class Scene extends Entity {

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 属するストーリーライン
	 */
	private final ObjectProperty<Storyline> storyline = new SimpleWeakObjectProperty<>();
	
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
//</editor-fold>

	public Scene() {
		this.initialize();
	}

	@Override
	protected String getResourceName () {
		return "scene";
	}

}
