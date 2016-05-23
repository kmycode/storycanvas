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
import java.util.ArrayList;
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
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import net.kmycode.javafx.FontUtil;
import net.kmycode.javafx.Messenger;
import storycanvas.message.entity.list.SceneOrderChangeMessage;
import storycanvas.message.entity.list.init.MainStorylineViewInitializeMessage;
import storycanvas.model.entity.Scene;
import storycanvas.model.entity.Storyline;
import storycanvas.model.story.Story;
import storycanvas.resource.Resources;

/**
 * ストーリーラインのデザイナ
 *
 * @author KMY
 */
public class StorylineDesigner extends HBox implements Initializable {

	/**
	 * ストーリーライン１つ分を表示する上での高さ.
	 */
	private static final double STORYLINE_HEIGHT = 100.0;

	/**
	 * シーンノードの横幅.
	 */
	private static final double SCENE_WIDTH = 85.0;
	private static final double SCENE_H_MARGIN = 10.0;

	@FXML
	private Button addFirstStorylineButton;

	@FXML
	private ScrollPane storylineTitlePaneScroll;

	@FXML
	private ScrollPane mainPaneScroll;

	@FXML
	private VBox storylineTitlePane;

	@FXML
	private VBox mainPane;

	private final ObservableList<StorylineShape> storylines = FXCollections.observableArrayList();
	private final ObservableList<SceneShape> scenes = FXCollections.observableArrayList();

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
	
	/**
	 * 現在選択されているシーン.
	 */
	private final ObjectProperty<Scene> selectedScene = new SimpleObjectProperty<>();

	public Scene getSelectedScene () {
		return selectedScene.get();
	}

	public void setSelectedScene (Scene value) {
		selectedScene.set(value);
	}

	public ObjectProperty<Scene> selectedSceneProperty () {
		return selectedScene;
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

		this.setSceneShapeListener();
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
			m.selectedSceneProperty().bind(this.selectedScene);
		});

		// シーンの順番が変更されたことを示すメッセージ
		// このメッセージは、今回のプログラミングの方針に反するが、特例的に存在する。
		// 詳しくは SceneOrderChangeMessage.java を参照のこと
		Messenger.getInstance().apply(SceneOrderChangeMessage.class, this, m -> FXCollections.sort(this.scenes));
	}

	/**
	 * ストーリーラインのシェイプがリストに追加されたり削除されたりしたときに、
	 * それを画面に描画する処理を行う.
	 */
	private void setStorylineShapeListener() {
		this.storylines.addListener((ListChangeListener.Change<? extends StorylineShape> e) -> {

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
						this.storylineTitlePane.getChildren().remove(el.title);
						this.mainPane.getChildren().remove(el.view);
					}
				}

				// ソートされたら、新しく最初からやり直す
				else if (e.wasPermutated()) {
					// TODO
				}
			}
		});
	}

	/**
	 * シーンのシェイプがリストに追加されたり削除されたりしたときに、
	 * それを画面に描画する処理を行う.
	 */
	private void setSceneShapeListener() {
		this.scenes.addListener((ListChangeListener.Change<? extends SceneShape> e) -> {

			while (e.next()) {

				// 追加されたシェイプを画面に表示する
				if (e.wasAdded()) {
					List<? extends SceneShape> subList = e.getAddedSubList();
					for(SceneShape el : subList) {
						StorylineShape storylineShape = null;
						if (el.scene.getStoryline() != null) {
							storylineShape = this.findStorylineShape(el.scene.getStoryline());
						}
						if (storylineShape != null) {
							storylineShape.addSceneShape(el);
						}
					}
				}

				// 削除されたシーンシェイプを表示から除去
				else if (e.wasRemoved()) {
					List<? extends SceneShape> subList = e.getRemoved();
					for(SceneShape el : subList) {
						StorylineShape storylineShape = null;
						if (el.scene.getStoryline() != null) {
							storylineShape = this.findStorylineShape(el.scene.getStoryline());
						} else {
							for (StorylineShape ssl : this.storylines) {
								if (ssl.hasSceneShape(el)) {
									storylineShape = ssl;
									break;
								}
							}
						}
						if (storylineShape != null) {
							storylineShape.removeSceneShape(el);
						}
					}
				}

				// ソートされたら、新しく最初からやり直す
				else if (e.wasPermutated()) {
					List<? extends SceneShape> clone = new ArrayList<>(e.getList());
					e.getList().clear();
					((List<SceneShape>)e.getList()).addAll(clone);
				}
			}

			// 全シーンのX座標を調整
			int i = 0;
			for (SceneShape shape : e.getList()) {
				shape.setSceneLayoutX(i++);
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
						this.deleteStoryline(el);
					}
				}

				// ソートされたら、順番が変わったものだけとりかえる
				else if (e.wasPermutated()) {
					for (int oldIndex = 0; oldIndex < e.getList().size(); oldIndex++) {
						int newIndex = e.getPermutation(oldIndex);
						if (newIndex != oldIndex) {
							Node oldS = this.mainPane.getChildren().get(newIndex);
							Node newS = this.mainPane.getChildren().get(oldIndex);
							this.mainPane.getChildren().remove(oldS);
							this.mainPane.getChildren().add(oldIndex > newIndex ? oldIndex - 1 : oldIndex, oldS);
							this.mainPane.getChildren().remove(newS);
							this.mainPane.getChildren().add(oldIndex < newIndex ? newIndex - 1 : newIndex, newS);
						}
					}
				}
			}

			// 最初のストーリーラインを追加するボタン表示の有無
			if (e.getList().size() != 0) {
				this.storylineTitlePane.getChildren().remove(this.addFirstStorylineButton);
			} else if (!this.storylineTitlePane.getChildren().contains(this.addFirstStorylineButton)) {
				this.storylineTitlePane.getChildren().add(this.addFirstStorylineButton);
			}
		});
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="メソッド">
	/**
	 * ストーリーライン（model.entity）内のシーンリストに変更があった時に呼び出されるリスナ
	 * モデルとしてのストーリーラインに直接設定する.
	 */
	private final ListChangeListener<? super Scene> storylineSceneChangeListener = (ListChangeListener.Change<? extends Scene> e) -> {

		while (e.next()) {

			// シーンを画面に追加する
			if (e.wasAdded()) {
				List<? extends Scene> subList = e.getAddedSubList();
				for(Scene el : subList) {
					this.scenes.add(new SceneShape(el));
				}
				FXCollections.sort(this.scenes);
			}

			// 削除されたシーンを表示から除去
			else if (e.wasRemoved()) {
				List<? extends Scene> subList = e.getRemoved();
				for(Scene el : subList) {
					this.scenes.remove(this.findSceneShape(el));
				}
			}

			// Storyline.scenesのほうの配列をソートされても、
			// Sceneのorderが変わらないかぎり表示に直接影響はないので無視する
			else if (e.wasPermutated()) {
			}
		}
	};

	/**
	 * ストーリーラインをビューに追加する
	 * @param storyline 追加するストーリーライン
	 */
	private void addStoryline(Storyline storyline) {
		if (this.findStorylineShape(storyline) == null) {
			StorylineShape shape = new StorylineShape(storyline);
			this.storylines.add(shape);

			// ストーリーラインに含まれるシーンを作成
			for (Scene sc : storyline.getScenes()) {
				this.scenes.add(new SceneShape(sc));
			}
			FXCollections.sort(this.scenes);

			// シーンが追加削除された時に教えてくれるリスナを追加
			storyline.getScenes().addListener(this.storylineSceneChangeListener);
		}
	}

	private void deleteStoryline(Storyline storyline) {
		this.storylines.remove(this.findStorylineShape(storyline));

		// ストーリーラインに含まれるシーンを削除
		List<SceneShape> delss = new ArrayList<>();
		for (SceneShape sc : this.scenes) {
			if (sc.scene.getStoryline() == storyline) {
				delss.add(sc);
			}
		}
		this.scenes.removeAll(delss);

		// シーンが追加削除された時に教えてくれるリスナを削除
		storyline.getScenes().removeListener(this.storylineSceneChangeListener);
	}

	/**
	 * 指定したストーリーラインが含まれているシェイプを取得する
	 * @param storyline 検索するストーリーライン
	 * @return 検索するストーリーラインが含まれたシェイプ
	 */
	private StorylineShape findStorylineShape(Storyline storyline) {
		StorylineShape shape = null;
		for (StorylineShape s : this.storylines) {
			if (s.storyline == storyline) {
				shape = s;
				break;
			}
		}
		return shape;
	}


	/**
	 * 指定したシーンが含まれているシェイプを取得する
	 * @param scene 検索するシーン
	 * @return 検索するシーンが含まれたシェイプ
	 */
	private SceneShape findSceneShape(Scene scene) {
		SceneShape shape = null;
		for (SceneShape s : this.scenes) {
			if (s.scene == scene) {
				shape = s;
				break;
			}
		}
		return shape;
	}

	/**
	 * 何も選んでいない状態にします.
	 */
	public void unselect() {
		this.selectedScene.set(null);
		this.selectedStoryline.set(null);
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ストーリーラインシェイプ">
	/**
	 * ストーリーラインに関連付けられたシェイプをまとめる内部クラス.
	 */
	private class StorylineShape {

		private final Storyline storyline;

		private final ContextMenu viewPopup;
		private final Group title;
		private final Group view;
		private final Line viewLine;

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
			titleRect.setCursor(Cursor.HAND);
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

			// イベントを設定
			this.title.setOnMouseClicked(e -> {
				setSelectedScene(null);
				setSelectedStoryline(this.storyline);
			});
			this.title.setOnMouseEntered(e -> titleText.setTextFill(Color.BLUE));
			this.title.setOnMouseExited(e -> titleText.setTextFill(Color.BLACK));

			// ビュー部分を作成
			Rectangle viewBackground = new Rectangle(50, STORYLINE_HEIGHT);
			viewBackground.setFill(Color.TRANSPARENT);
			this.viewLine = new Line(0, STORYLINE_HEIGHT - 30, 50, STORYLINE_HEIGHT - 30);
			this.viewLine.setStrokeWidth(5);
			this.viewLine.strokeProperty().bind(line.colorProperty());
			this.view = new Group(viewBackground, this.viewLine);

			// ストーリーラインのタイトル部分を右クリックしたときのメニューを作成
			MenuItem sceneNewMenu = new MenuItem("新規シーン");
			sceneNewMenu.setOnAction(e -> Story.getCurrent().addScene(this.storyline));
			MenuItem addStorylineMenu = new MenuItem("ストーリーラインを末尾に追加");
			addStorylineMenu.setOnAction(e -> Story.getCurrent().addStoryline());
			MenuItem deleteStorylineMenu = new MenuItem("ストーリーラインを削除");
			deleteStorylineMenu.setOnAction(e -> Story.getCurrent().deleteStoryline());
			this.viewPopup = new ContextMenu(sceneNewMenu, new SeparatorMenuItem(),
						addStorylineMenu, new SeparatorMenuItem(),
						deleteStorylineMenu);
			this.title.setOnContextMenuRequested(e -> this.viewPopup.show(this.title, e.getScreenX(), e.getScreenY()));
		}

		public void addSceneShape(SceneShape s) {
			this.view.getChildren().add(s.sceneNode);
			this.updateLine();
		}

		public void removeSceneShape(SceneShape s) {
			this.view.getChildren().remove(s.sceneNode);
			for (StorylineShape sls : storylines) {
				sls.updateLine();
			}
		}

		public boolean hasSceneShape(SceneShape s) {
			return this.view.getChildren().contains(s.sceneNode);
		}

		private void updateLine() {
			int firstSceneIndex = -1;
			int lastSceneIndex = 0;
			int i = 0;
			for (SceneShape ss : scenes) {
				if (ss.scene.getStoryline() == this.storyline) {
					if (firstSceneIndex < 0) {
						firstSceneIndex = i;
						lastSceneIndex = i;
					} else {
						lastSceneIndex = i;
					}
				}
				i++;
			}

			double startX = (SCENE_WIDTH + SCENE_H_MARGIN) * firstSceneIndex - 10;
			double endX = (SCENE_WIDTH + SCENE_H_MARGIN) * (lastSceneIndex + 1) + 10;
			if (startX < 0) {
				startX = 0;
				endX += SCENE_WIDTH + 5;
			}
			this.viewLine.setLayoutX(startX);
			this.viewLine.setEndX(endX - startX);
		}
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="シーンシェイプ">
	/**
	 * １つのシーンのシェイプをまとめる内部クラス.
	 */
	private class SceneShape implements Comparable<SceneShape> {

		private final Scene scene;

		private final ContextMenu scenePopup;
		private final Group sceneNode;

		public SceneShape(Scene scene) {
			this.scene = scene;

			// シーンノードを作成
			Rectangle sceneNodeBack = new Rectangle(5, 5, SCENE_WIDTH, STORYLINE_HEIGHT - 10);
			sceneNodeBack.setFill(Color.BLACK);
			Rectangle sceneNodeFor = new Rectangle(6, 6, SCENE_WIDTH - 2, STORYLINE_HEIGHT - 12);
			sceneNodeFor.setFill(Color.WHITE);
			sceneNodeFor.setCursor(Cursor.HAND);
			ImageView sceneIcon = Resources.getMiniIconNode("scene");
			sceneIcon.setLayoutX(10);
			sceneIcon.setLayoutY(8);
			Label sceneText = new Label();
			sceneText.setWrapText(true);
			sceneText.setLayoutX(10);
			sceneText.setLayoutY(20);
			sceneText.textProperty().bind(this.scene.nameProperty());
			sceneText.setMaxWidth(SCENE_WIDTH - 10);
			sceneText.setMaxHeight(80);
			this.sceneNode = new Group(sceneNodeBack, sceneNodeFor, sceneIcon, sceneText);

			// イベントを設定
			this.sceneNode.setOnMouseClicked(e -> {
				setSelectedStoryline(null);
				setSelectedScene(this.scene);
			});

			// シーンノードを右クリックしたときのメニューを作成
			MenuItem addNextSceneMenu = new MenuItem("次のシーンを追加");
			addNextSceneMenu.setOnAction(e -> Story.getCurrent().addNextScene());
			MenuItem addBackSceneMenu = new MenuItem("前のシーンを追加");
			addBackSceneMenu.setOnAction(e -> Story.getCurrent().addBackScene());
			MenuItem sceneLeftMenu = new MenuItem("前へ移動");
			sceneLeftMenu.setOnAction(e -> Story.getCurrent().leftScene());
			MenuItem sceneRightMenu = new MenuItem("次へ移動");
			sceneRightMenu.setOnAction(e -> Story.getCurrent().rightScene());
			MenuItem sceneDelMenu = new MenuItem("削除");
			sceneDelMenu.setOnAction(e -> Story.getCurrent().deleteScene());
			this.scenePopup = new ContextMenu(addNextSceneMenu, addBackSceneMenu, new SeparatorMenuItem(),
					sceneRightMenu, sceneLeftMenu, new SeparatorMenuItem(),
					sceneDelMenu);
			this.sceneNode.setOnContextMenuRequested(e -> this.scenePopup.show(this.sceneNode, e.getScreenX(), e.getScreenY()));
		}

		public void setSceneLayoutX(int index) {
			this.sceneNode.setLayoutX(index * (SCENE_WIDTH + SCENE_H_MARGIN / 2));
		}

		@Override
		public int compareTo(SceneShape o) {
			return this.scene.compareTo(o.scene);
		}

	}
//</editor-fold>

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
	 * 最初のストーリーラインを追加.
	 */
	@FXML
	private void addFirstStoryline() {
		Story.getCurrent().addStoryline();
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
