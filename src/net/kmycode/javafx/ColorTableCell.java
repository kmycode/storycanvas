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

import javafx.geometry.Insets;
import javafx.scene.control.TableCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * TableViewで色を表示する
 *
 * @author KMY
 */
public class ColorTableCell<E> extends TableCell<E, Color> {

	private static Color OPAQUE = new Color(0, 0, 0, 0);

	@Override
	protected void updateItem (Color item, boolean empty) {
		super.updateItem(item, empty);
		this.setText("");
		if (item != null) {
			this.setText("");
			this.setBackground(new Background(new BackgroundFill(item, CornerRadii.EMPTY, Insets.EMPTY)));
		}
		else {
			this.setBackground(new Background(new BackgroundFill(ColorTableCell.OPAQUE, CornerRadii.EMPTY, Insets.EMPTY)));
		}
	}
}
