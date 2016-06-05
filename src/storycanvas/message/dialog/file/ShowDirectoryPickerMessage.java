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
package storycanvas.message.dialog.file;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import net.kmycode.javafx.Message;

/**
 * ディレクトリ選択画面を開く
 *
 * @author KMY
 */
public class ShowDirectoryPickerMessage extends Message {

	private final StringProperty selectedFileName;
	private final StringProperty defaultPath;

	public ShowDirectoryPickerMessage(StringProperty selectedFileName, String defaultPath) {
		this.selectedFileName = selectedFileName;
		this.defaultPath = new SimpleStringProperty(defaultPath);
	}

	public ShowDirectoryPickerMessage(StringProperty selectedFileName, StringProperty defaultPath) {
		this.selectedFileName = selectedFileName;
		this.defaultPath = defaultPath;
	}

	public StringProperty selectedFileNameProperty() {
		return this.selectedFileName;
	}

	public String getDefaultPath() {
		return this.defaultPath.get();
	}

	public void setDefaultPath(String var) {
		this.defaultPath.set(var);
	}

	public StringProperty defaultPathProperty() {
		return this.defaultPath;
	}

}
