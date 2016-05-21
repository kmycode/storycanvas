/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import java.util.ArrayList;
import java.util.List;

/**
 * メッセンジャ
 *
 * @author KMY
 */
public class Messenger {

	private static final Messenger defaultInstance = new Messenger();
	private final List<MessageReceiver> receiverList = new ArrayList<>();

	public Messenger () {
	}

	public static Messenger getInstance () {
		return Messenger.defaultInstance;
	}

	public final <T extends Message> void apply (Class<T> messageType, Object actionApplier, MessageListener<T> listener) {
		this.receiverList.add(new MessageReceiver(messageType, actionApplier, listener));
	}

	public final void relay (Class<? extends Message> messageType, Object actionApplier, Messenger target) {
		this.receiverList.add(new MessageReceiver(messageType, actionApplier, (ev) -> target.send((Message) ev)));
	}

	public <T extends Message> void send (T message) {
		Class<? extends Message> messageType = message.getClass();
		for (MessageReceiver receiver : this.receiverList) {
			if (receiver.messageType == messageType) {
				receiver.listener.call((T)message);
			}
		}
	}

	public void remove (Object actionApplier) {
		List<MessageReceiver> removeList = new ArrayList<>();
		for (MessageReceiver receiver : this.receiverList) {
			if (receiver.actionApplier == actionApplier) {
				removeList.add(receiver);
			}
		}
		this.receiverList.removeAll(removeList);
	}

	public void remove (Object actionApplier, Message message) {
		List<MessageReceiver> removeList = new ArrayList<>();
		Class<? extends Message> messageType = message.getClass();
		for (MessageReceiver receiver : this.receiverList) {
			if (receiver.actionApplier == actionApplier && receiver.messageType == messageType) {
				removeList.add(receiver);
			}
		}
		this.receiverList.removeAll(removeList);
	}

	// -------------------------------------------------------
	// メッセージの受信に必要な情報を取りまとめたイミュータブルなクラス
	private static class MessageReceiver {

		public final Class<? extends Message> messageType;
		public final Object actionApplier;		// アクションを登録したオブジェクト
		public final MessageListener listener;

		public MessageReceiver (Class<? extends Message> messageType, Object actionApplier,
								MessageListener listener) {
			this.messageType = messageType;
			this.actionApplier = actionApplier;
			this.listener = listener;
		}
	}

}
