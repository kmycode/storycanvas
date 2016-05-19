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

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;

/**
 * メッセージの抽象クラス
 *
 * @author KMY
 */
public abstract class Message implements Observable {

	private boolean isUsed = false;

	public boolean isUsed () {
		return this.isUsed;
	}

	public void setUsed () {
		this.isUsed = true;
	}

	@Override
	public void addListener (InvalidationListener listener) {
		//Messenger.getInstance().send(new ExceptionMessage(new UnsupportedOperationException("Not supported yet.")));
	}

	@Override
	public void removeListener (InvalidationListener listener) {
		//Messenger.getInstance().send(new ExceptionMessage(new UnsupportedOperationException("Not supported yet.")));
	}

}
