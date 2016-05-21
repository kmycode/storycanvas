/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import javafx.scene.control.TableCell;

/**
 * TableViewで色付きテキストを表示する
 *
 * @author KMY
 */
public class ColoredTextTableCell<E, T extends ColorableTextItem> extends TableCell<E, T> {

	@Override
	protected void updateItem (T item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null || empty) {
			this.setText("");
		}
		else {
			this.setText(item.getText());
			String colorString = "#" + item.getColor().toString().substring(2, 8);
			this.setStyle("-fx-text-fill:" + colorString);
		}
	}
}
