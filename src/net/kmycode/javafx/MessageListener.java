/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import java.util.EventListener;

/**
 * メッセンジャ用のリスナ
 *
 * @author KMY
 */
public interface MessageListener<T extends Message> extends EventListener {
	public void call(T message);
}
