/*
 * Copyright (C) 2016 KMY
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package storycanvas.view.view;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import net.kmycode.javafx.FontUtil;
import net.kmycode.javafx.Messenger;
import storycanvas.message.entity.list.init.MainStorylineViewInitializeMessage;
import storycanvas.model.entity.Storyline;
import storycanvas.resource.Resources;

/**
 * ストーリーラインのデザイナ
 *
 * @author KMY
 */
public class StorylineDesigner extends HBox implements Initializable {

	/**
	 * ストーリーライン１つ分を表示する上でのの高さ.
	 */
	private static final double STORYLINE_HEIGHT = 100.0;

	@FXML
	private ScrollPane storylineTitlePaneScroll;

	@FXML
	private ScrollPane mainPaneScroll;

	@FXML
	private VBox storylineTitlePane;

	@FXML
	private VBox mainPane;

	private final ObservableList<StorylineShape> shapes = FXCollections.observableArrayList();

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * 現在選択されているストーリーライン.
	 */
	private final ObjectProperty<Storyline> selectedStoryline = new SimpleObjectProperty<>();

	public Storyline getSelectedStoryline () {
		return selectedStoryline.get();
	}

	public void setSelectedStoryline (Storyline value) {
		selectedStoryline.set(value);
	}

	public ObjectProperty<Storyline> selectedStorylineProperty () {
		return selectedStoryline;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="コンストラクタ">
	/**
	 * 初期化.
	 */
	public StorylineDesigner() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StorylineDesigner.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		// 2つのScrollPaneのスクロールバーが連動するようにする
		this.storylineTitlePaneScroll.vvalueProperty().bindBidirectional(this.mainPaneScroll.vvalueProperty());

		// ScrollPaneの内部処理によってスクロールバーが生成されるタイミングをうかがい、
		// 特定のスクロールバーを無効にする
		// 参考：http://aoe-tk.hatenablog.com/entry/2015/05/23/141948
		Platform.runLater(() -> {
			ScrollBar scrollBar = this.getScrollBar(this.storylineTitlePaneScroll, Orientation.HORIZONTAL);
			if (scrollBar != null) {
				scrollBar.setDisable(true);
			}
		});

		this.setStorylineShapeListener();
	}

	/**
	 * メインのビューに設定する.
	 */
	public void toMain() {

		// 初期化メッセージを受け取る
		Messenger.getInstance().apply(MainStorylineViewInitializeMessage.class, this, (m) -> {

			// まずは、すでにリスト内にあるものを表示する
			for (Storyline sl : m.getList()) {
				this.addStoryline(sl);
			}

			// リストの内容が変更されたらこっちの表示も変わるよう、リスナを生成する
			this.setListener(m.getList());

			// 選択されているシーンをバインド
			m.selectedItemProperty().bind(this.selectedStoryline);
		});
	}

	/**
	 * ストーリーラインのシェイプがリストに追加されたり削除されたりしたときに、
	 * それを画面に描画する処理を行う.
	 */
	private void setStorylineShapeListener() {
		this.shapes.addListener((ListChangeListener.Change<? extends StorylineShape> e) -> {

			while (e.next()) {

				// 追加されたシェイプを画面に表示する
				if (e.wasAdded()) {
					List<? extends StorylineShape> subList = e.getAddedSubList();
					for(StorylineShape el : subList) {
						this.storylineTitlePane.getChildren().add(el.title);
						this.mainPane.getChildren().add(el.view);
					}
				}

				// 削除されたストーリーラインを表示から除去
				else if (e.wasRemoved()) {
					List<? extends StorylineShape> subList = e.getRemoved();
					for(StorylineShape el : subList) {
					}
				}

				// ソートされたら、新しく最初からやり直す
				else if (e.wasPermutated()) {
				}
			}
		});
	}

	/**
	 * リストにリスナを設定し、リストの内容が変わったらデザイナの表示も自動的に変わるようにする
	 * @param list ストーリーラインのリスト
	 */
	private void setListener(ObservableList<Storyline> list) {
		list.addListener((ListChangeListener.Change<? extends Storyline> e) -> {

			while (e.next()) {

				// ストーリーラインとして新規に表示する
				if (e.wasAdded()) {
					List<? extends Storyline> subList = e.getAddedSubList();
					for(Storyline el : subList) {
						this.addStoryline(el);
					}
				}

				// 削除されたストーリーラインを表示から除去
				else if (e.wasRemoved()) {
					List<? extends Storyline> subList = e.getRemoved();
					for(Storyline el : subList) {
					}
				}

				// ソートされたら、新しく最初からやり直す
				else if (e.wasPermutated()) {
				}
			}
		});
	}
//</editor-fold>

	/**
	 * ストーリーラインをビューに追加する
	 * @param storyline 追加するストーリーライン
	 */
	private void addStoryline(Storyline storyline) {
		StorylineShape shape = new StorylineShape(storyline);
		this.shapes.add(shape);
	}

	/**
	 * ストーリーラインに関連付けられたシェイプをまとめる内部クラス.
	 */
	private class StorylineShape {

		private final Storyline storyline;

		private final Group title;
		private final Group view;

		public StorylineShape(Storyline line) {
			this.storyline = line;
			String colorCode = this.storyline.getColor() != null ? this.storyline.getColor().toString().substring(2, 8) : "000000";

			// タイトルの矩形を作成
			double titleWidth = StorylineDesigner.this.storylineTitlePane.getWidth();
			final int titleRectBorderWidth = 4;
			Rectangle titleRectBack = new Rectangle(titleWidth, STORYLINE_HEIGHT);
			titleRectBack.fillProperty().bind(line.colorProperty());
			Rectangle titleRect = new Rectangle(titleRectBorderWidth, titleRectBorderWidth,
					titleWidth - titleRectBorderWidth * 2,
					STORYLINE_HEIGHT - titleRectBorderWidth * 2);
			titleRect.setOpacity(0.9);
			titleRect.setFill(Color.WHITE);
			ImageView sceneIcon = Resources.getMiniIconNode("storyline");
			sceneIcon.setLayoutX(20);
			sceneIcon.setLayoutY(20);
			Label titleText = new Label();
			titleText.setWrapText(true);
			titleText.setLayoutX(20);
			titleText.setLayoutY(40);
			titleText.textProperty().bind(line.nameProperty());
			titleText.setMaxWidth(titleWidth - 40);
			titleText.setMaxHeight(40);
			titleText.setUnderline(true);
			titleText.setFont(FontUtil.toBold(titleText.getFont(), FontWeight.EXTRA_BOLD));
			this.title = new Group(titleRectBack, titleRect, sceneIcon, titleText);
			this.title.setOnMouseClicked(e -> setSelectedStoryline(this.storyline));
			this.title.setOnMouseEntered(e -> titleText.setTextFill(Color.BLUE));
			this.title.setOnMouseExited(e -> titleText.setTextFill(Color.BLACK));

			// ビュー部分を作成
			Rectangle viewBackground = new Rectangle(50, STORYLINE_HEIGHT);
			viewBackground.setFill(Color.TRANSPARENT);
			Line viewLine = new Line(0, STORYLINE_HEIGHT - 30, 50, STORYLINE_HEIGHT - 30);
			viewLine.setStrokeWidth(5);
			viewLine.strokeProperty().bind(line.colorProperty());
			this.view = new Group(viewBackground, viewLine);
		}
	}

//<editor-fold defaultstate="collapsed" desc="その他のメソッド">
	// TODO: staticメソッドへ移動
	// ソースはこちらから借用　http://aoe-tk.hatenablog.com/entry/2015/05/23/141948
	/**
	 * スクロールバーを取得
	 * @param n スクロールバーがついているノード
	 * @param orientation スクロールバーの方向
	 * @return スクロールバー。なければnull
	 */
	private ScrollBar getScrollBar(Node n, Orientation orientation) {
		Set<Node> nodes = n.lookupAll(".scroll-bar");
		for (Node node : nodes) {
			if (node instanceof ScrollBar) {
				ScrollBar scrollBar = (ScrollBar) node;
				if (scrollBar.getOrientation() == orientation) {
					return scrollBar;
				}
			}
		}
		return null;
	}

	/**
	 * Initializes the controller class.
	 */
	@Override
	public void initialize (URL url, ResourceBundle rb) {
		// TODO
	}
//</editor-fold>

}
