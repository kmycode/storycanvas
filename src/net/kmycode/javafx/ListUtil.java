/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/**
 * JavaFXのリスト機能を扱うユーティリティクラス
 *
 * @author KMY
 */
public class ListUtil {

	private ListUtil() {}

	/**
	 * 指定したリスト（原本）のクローンを返す。
	 * 原本の内容が書き換えられると、同時にクローンの内容も書き換えられるようにする。
	 * ただし、クローンの順番をソートするなどしてクローンの内容を一時的に変更しても、原本の内容は変わらない
	 * @param <T> 原本リストが格納するオブジェクトの型
	 * @param list 原本リスト
	 * @return クローンリスト
	 */
	public static <T> ObservableList<T> getClone(ObservableList<T> list) {

		// 複製を作成
		ObservableList<T> c = FXCollections.observableArrayList(list);
		ListChangeListener<T> listener = new ListChangeListener<T>() {
			@Override
			public void onChanged (ListChangeListener.Change<? extends T> e) {
				// クローンリストがGCによって削除されてないかチェック
				checkDisposedClone();

				// データがないか探す
				CloneListData data = null;
				for (CloneListData d : cloneListDatas) {
					if (d.original.get() == e.getList()) {
						data = d;
						break;
					}
				}
				// データが存在しなければ、自身を削除する
				if (data == null) {
					e.getList().removeListener(this);
					return;
				}

				// クローンリストを取得
				ObservableList<T> clist = data.sub.get();

				if (clist != null) {
					// 元のリストに追加時
					if (e.wasAdded()) {
						clist.addAll(e.getAddedSubList());
					}

					// 元のリストから削除時
					else if (e.wasRemoved()) {
						clist.removeAll(e.getRemoved());
					}
				}
			}
		};
		c.addListener(listener);

		// データを登録
		CloneListData data = new CloneListData();
		data.original = new WeakReference<>(list);
		data.sub = new WeakReference<>(c);
		data.listener = new WeakReference<>(listener);
		cloneListDatas.add(data);

		// クローンを返す
		return c;
	}

	/**
	 * すでに削除されているクローンリストがないか確認し、
	 * もしあれば原本からのリスナーによる関連付けを解除し、データを削除する.
	 */
	private static void checkDisposedClone() {

		List<CloneListData> removeList = new ArrayList<>();

		for (CloneListData d : cloneListDatas) {

			// クローンリストがすでに削除されている
			if (d.sub.get() == null) {

				// 削除処理
				d.original.get().removeListener(d.listener.get());

				// 削除リストに追加
				removeList.add(d);
			}
		}

		// リストから削除
		cloneListDatas.removeAll(removeList);
	}

	/**
	 * クローンの削除処理を行い、原本に残っていた、クローンの内容を更新するためのリスナーを削除する。
	 * getCloneによって作成したリストを破棄する時、呼び出す必要がある
	 * @param list クローンのリスト
	 */
	public static void disposeClone(ObservableList list) {
		
		// データがないか探す
		CloneListData data = null;
		for (CloneListData d : cloneListDatas) {
			if (d.sub.get() == list) {
				data = d;
				break;
			}
		}
		if (data == null) return;
		
		// データを強い参照に移し替えてnullチェック
		ObservableList original = data.original.get();
		ObservableList sub = data.sub.get();
		ListChangeListener listener = data.listener.get();
		if (original == null) return;
		
		// リスナー削除処理
		original.removeListener(listener);
		
		// データをリストから削除
		cloneListDatas.remove(data);
	}

	/**
	 * リスナなどによって関連付けられたリストをまとめるデータクラス（構造体）
	 */
	private static class CloneListData {
		WeakReference<ObservableList> original;		// 原本
		WeakReference<ObservableList> sub;			// クローン
		WeakReference<ListChangeListener> listener;	// 原本につけられた、原本に従ってクローンの内容を書き換えるリスト
	}
	private static final List<CloneListData> cloneListDatas = new ArrayList<>();

}
