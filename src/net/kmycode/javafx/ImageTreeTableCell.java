/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import javafx.scene.control.TreeTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * TableViewのセルで画像を表示する
 *
 * @author KMY
 */
public class ImageTreeTableCell<E> extends TreeTableCell<E, Image> {

	private double width;
	private double height;

	public ImageTreeTableCell(double w, double h) {
		this.width = w;
		this.height = h;
	}

	public ImageTreeTableCell() {
		this(-1, -1);
	}

	@Override
	protected void updateItem (Image item, boolean empty) {
		super.updateItem(item, empty);
		this.setText("");
		if (item != null) {
			ImageView iv = new ImageView(item);
			if (this.width > 0) {
				iv.setFitWidth(this.width);
			}
			if (this.height > 0) {
				iv.setFitHeight(this.height);
			}
			this.setGraphic(iv);
		}
		else {
			this.setGraphic(null);
		}
	}
}
