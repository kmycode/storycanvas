/*
 * jStorybook: すべての小説家・作者のためのオープンソース・ソフトウェア
 * Copyright (C) 2008 - 2012 Martin Mustun
 *   (このソースの製作者) KMY
 * 
 * このプログラムはフリーソフトです。
 * あなたは、自由に修正し、再配布することが出来ます。
 * あなたの権利は、the Free Software Foundationによって発表されたGPL ver.2以降によって保護されています。
 * 
 * このプログラムは、小説・ストーリーの制作がよりよくなるようにという願いを込めて再配布されています。
 * あなたがこのプログラムを再配布するときは、GPLライセンスに同意しなければいけません。
 *  <http://www.gnu.org/licenses/>.
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
