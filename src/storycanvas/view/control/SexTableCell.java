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
package storycanvas.view.control;

import javafx.scene.control.TableCell;
import storycanvas.model.entity.Sex;

/**
 * TableViewで性を表示する
 *
 * @author KMY
 */
public class SexTableCell<E> extends TableCell<E, Sex> {

	@Override
	protected void updateItem (Sex item, boolean empty) {
		super.updateItem(item, empty);
		if (item == null || empty) {
			this.setText("");
		}
		else {
			boolean hit = false;
			this.setText(item.getName());
			this.setTextFill(item.getColor());
		}
	}
}
