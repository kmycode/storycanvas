/*
 * KMY ライブラリ (LGPL)
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
