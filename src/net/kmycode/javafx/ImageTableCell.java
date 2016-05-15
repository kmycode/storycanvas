/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * TableViewのセルで画像を表示する
 *
 * @author KMY
 */
public class ImageTableCell<E> extends TableCell<E, Image> {

	private double width;
	private double height;

	public ImageTableCell(double w, double h) {
		this.width = w;
		this.height = h;
	}

	public ImageTableCell() {
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
