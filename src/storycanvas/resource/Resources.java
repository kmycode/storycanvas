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
package storycanvas.resource;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * このソフトで利用するリソースを取得するためのメソッド集
 *
 * @author KMY
 */
public class Resources {

	private Resources() {}

	public static ImageView getIconNode (String resourceKey) {
		return new ImageView(new Image(Resources.class.getResource("/storycanvas/resource/icon/" + resourceKey + ".png").toExternalForm()));
	}

	public static ImageView getMiniIconNode (String resourceKey) {
		Image image = new Image(Resources.class.getResource("/storycanvas/resource/icon/" + resourceKey + ".png").toExternalForm(), 16, 16,
								false, true);
		ImageView imageView = new ImageView(image);
		return imageView;
	}

	public static ImageView getMiddleIconNode (String resourceKey) {
		Image image = new Image(Resources.class.getResource("/storycanvas/resource/icon/" + resourceKey + "_large.png").toExternalForm(), 32, 32,
								false, true);
		ImageView imageView = new ImageView(image);
		return imageView;
	}

	public static Image getLargeIcon (String resourceKey) {
		return new Image(Resources.class.getResource("/storycanvas/resource/icon/" + resourceKey + "_large.png").toExternalForm());
	}

	public static ImageView getLargeIconNode (String resourceKey) {
		return new ImageView(new Image(Resources.class.getResource("/storycanvas/resource/icon/" + resourceKey + "_large.png").toExternalForm()));
	}

	// TODO: リソースメッセージ関連

}
