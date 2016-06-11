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
package storycanvas.model.story;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import javafx.scene.paint.Color;
import net.kmycode.javafx.Messenger;
import storycanvas.message.dialog.file.ShowDirectoryPickerMessage;
import storycanvas.message.entity.edit.EmptyEditMessage;
import storycanvas.message.entity.edit.PartEditMessage;
import storycanvas.message.entity.edit.PersonEditMessage;
import storycanvas.message.entity.edit.PlaceEditMessage;
import storycanvas.message.entity.edit.SceneEditMessage;
import storycanvas.message.entity.edit.StorylineEditMessage;
import storycanvas.message.entity.list.SceneOrderChangeMessage;
import storycanvas.message.entity.list.init.MainPartTableInitializeMessage;
import storycanvas.message.entity.list.init.MainPersonTableInitializeMessage;
import storycanvas.message.entity.list.init.MainPlaceTableInitializeMessage;
import storycanvas.message.entity.list.init.MainStorylineViewInitializeMessage;
import storycanvas.message.entity.list.select.MainPartTableSelectItemMessage;
import storycanvas.message.entity.list.select.MainPersonTableSelectItemMessage;
import storycanvas.message.entity.list.select.MainPlaceTableSelectItemMessage;
import storycanvas.message.update.StoryInformationUpdateMessage;
import storycanvas.model.application.Config;
import storycanvas.model.entity.Entity;
import storycanvas.model.entity.Part;
import storycanvas.model.entity.Person;
import storycanvas.model.entity.Place;
import storycanvas.model.entity.Scene;
import storycanvas.model.entity.Storyline;
import storycanvas.model.entityset.EntityFilteredListModel;
import storycanvas.model.entityset.EntityListModel;
import storycanvas.model.entityset.EntityTreeModel;

/**
 * ひとつのストーリーをあらわすモデル。
 * 1プロセスにつき1つのインスタンスが存在することを想定しているので、
 * （他のストーリーからのインポート・エクスポートを考慮して）
 * コンストラクタはpublicであるものの、事実上のシングルトンとして運用する
 *
 * @author KMY
 */
public class Story {

	public Story() {

		setCurrent(this);
		this.places = new EntityTreeModel<>(new Place());

		// TODO: メイン画面に表示する処理。テストのため挿入
		this.reloadView();

		// リストで登場人物が選択された時
		this.persons.selectedEntityProperty().addListener(e -> {
			if (this.persons.getSelectedEntity() != null) {
				Messenger.getInstance().send(new PersonEditMessage(this.persons.getSelectedEntity()));
			} else {
				Messenger.getInstance().send(new EmptyEditMessage());
			}
		});

		// リストで場所が選択された時
		this.places.selectedEntityProperty().addListener(e -> {
			if (this.places.getSelectedEntity() != null) {
				Messenger.getInstance().send(new PlaceEditMessage(this.places.getSelectedEntity()));
			} else {
				Messenger.getInstance().send(new EmptyEditMessage());
			}
		});

		// 画面でストーリーラインが選択された時
		this.storylines.selectedEntityProperty().addListener(e -> {
			if (this.storylines.getSelectedEntity() != null) {
				Messenger.getInstance().send(new StorylineEditMessage(this.storylines.getSelectedEntity()));
			} else {
				Messenger.getInstance().send(new EmptyEditMessage());
			}
		});

		// 画面でシーンが選択された時
		this.selectedScene.addListener(e -> {
			if (this.selectedScene.get() != null) {
				Messenger.getInstance().send(new SceneEditMessage(this.selectedScene.get()));
			} else {
				Messenger.getInstance().send(new EmptyEditMessage());
			}
		});

		// 画面で編が選択された時
		this.parts.selectedEntityProperty().addListener(e -> {
			if (this.parts.getSelectedEntity() != null) {
				Messenger.getInstance().send(new PartEditMessage(this.parts.getSelectedEntity()));
			} else {
				Messenger.getInstance().send(new EmptyEditMessage());
			}
		});

		/*
		// TODO: テスト用データ
		Person p1 = new Person();
		p1.setLastName("中村");
		p1.setFirstName("翠");
		StoryDate p1d = StoryCalendar.ANNO_DOMINI.date(2004, 11, 30);
		p1.setBirthDay(p1d);
		p1.setSex(Sex.FEMALE);
		p1.setColor(Color.PINK);
		this.persons.add(p1);

		Place l1 = new Place();
		l1.setName("学校");
		this.places.add(l1);

		Part t1 = new Part();
		t1.setName("死をもって償え編");
		this.parts.add(t1);

		Storyline s1 = new Storyline();
		s1.setName("本筋");
		s1.setColor(Color.GREEN);
		t1.getStorylines().add(s1);
		this.storylines.add(s1);

		Scene c1 = new Scene();
		c1.setName("主人公が女の子に会う");
		s1.getScenes().add(c1);

		Storyline s2 = new Storyline();
		s2.setName("別の場面");
		s2.setColor(Color.BLUE);
		t1.getStorylines().add(s2);
		this.storylines.add(s2);

		Scene c2 = new Scene();
		c2.setName("女の子たちが殺しあう");
		s2.getScenes().add(c2);
		
		this.storylines.filtering(e -> false);
		*/

		// TODO: シリアライズテスト
		//this.save("testdata");
		//this.load("testdata");

		// TODO: 日付計算テスト
		/*
		StoryDate from = new StoryDate();
		from.setYear(1992);
		from.setMonth(4);
		from.setDay(16);
		StoryDate to = new StoryDate();
		to.setYear(2112);
		to.setMonth(9);
		to.setDay(3);
		System.out.println(StoryCalendar.ANNO_DOMINI.getWeekday(to).getName());
		//System.out.println(StoryCalendar.ANNO_DOMINI.getDayDistance(from, to));
		*/
	}

//<editor-fold defaultstate="collapsed" desc="事実上のシングルトンを実現する部分">
	private static final ObjectProperty<Story> current = new SimpleObjectProperty<>();

	public static Story getCurrent() {
		return current.get();
	}

	public static void setCurrent(Story s) {
		current.set(s);
	}
	
	static {
		current.addListener(e -> {
			Story mainStory = getCurrent();
			if (mainStory != null) {
				Entity.setup(mainStory);
			}
		});
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="プロパティ">
	/**
	 * ストーリーの設定.
	 */
	private StoryConfig storyConfig = new StoryConfig();

	/**
	 * 登場人物一覧.
	 */
	private final EntityListModel<Person> persons = new EntityListModel<>();

	/**
	 * 場所一覧.
	 */
	private final EntityTreeModel<Place> places;

	/**
	 * ストーリーライン一覧.
	 */
	private final EntityFilteredListModel<Storyline> storylines = new EntityFilteredListModel<>();

	private final ObjectProperty<Scene> selectedScene = new SimpleObjectProperty<>();

	/**
	 * 編一覧.
	 */
	private final EntityListModel<Part> parts = new EntityListModel<>();

	// ストーリーラインデザイナ、その他、編の変更によって画面表示が大きく変わるところ用
	private final ObjectProperty<Part> viewSelectedPart = new SimpleObjectProperty<>();
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="メソッド">
	/**
	 * 画面全体の表示を更新.
	 */
	private void reloadView() {
		// 画面表示を更新するメッセージを送信
		Messenger.getInstance().send(new MainPersonTableInitializeMessage(this.persons.getEntities(), this.persons.selectedEntityProperty()));
		Messenger.getInstance().send(new MainPlaceTableInitializeMessage(this.places.getRootTreeItem(), this.places.selectedTreeItemEntityProperty()));
		Messenger.getInstance().send(new MainStorylineViewInitializeMessage(this.storylines.getEntities(), this.storylines.selectedEntityProperty(), this.selectedScene));
		Messenger.getInstance().send(new MainPartTableInitializeMessage(this.parts.getEntities(), this.parts.selectedEntityProperty(), this.viewSelectedPart));

		// 表示する編が変わったら、それに対応したストーリーラインを表示させる
		this.viewSelectedPart.addListener(e -> this.openPart());
	}

	/**
	 * ストーリー全体の情報を更新する.
	 */
	public void reloadStoryInformation() {
		int sceneCount = 0;
		for (Storyline line : this.storylines.getAllEntities()) {
			sceneCount += line.getScenes().size();
		}
		Messenger.getInstance().send(new StoryInformationUpdateMessage.Builder()
														.setTitleProperty(this.storyConfig.titleProperty())
														.setAuthorNameProperty(this.storyConfig.authorNameProperty())
														.setPersonCount(this.persons.count())
														.setPlaceCount(this.places.count())
														.setSceneCount(sceneCount)
														.setStorylineCount(this.storylines.count())
														.setPartCount(this.parts.count())
														.build());
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ファイル入出力メソッド">
	/**
	 * フォルダ選択画面を表示し、選択されたものをロードする.
	 */
	public void load() {
		StringProperty selected = new SimpleStringProperty();
		StringProperty defaultPath = new SimpleStringProperty(Config.getInstance().getFolderOpenPath());
		Messenger.getInstance().send(new ShowDirectoryPickerMessage(selected, defaultPath));
		if (selected.get() != null && !selected.get().isEmpty()) {
			this.load(selected.get());
		}
		Config.getInstance().setFolderOpenPath(defaultPath.get());
	}

	public void load(String folderName) {
		try {
			this.readObject(folderName);
			this.reloadStoryInformation();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
	/**
	 * フォルダ選択画面を表示し、選択された場所へセーブする.
	 */
	public void save() {
		StringProperty selected = new SimpleStringProperty();
		StringProperty defaultPath = new SimpleStringProperty(Config.getInstance().getFolderOpenPath());
		Messenger.getInstance().send(new ShowDirectoryPickerMessage(selected, defaultPath));
		if (selected.get() != null && !selected.get().isEmpty()) {
			this.save(selected.get());
		}
		Config.getInstance().setFolderOpenPath(defaultPath.get());
	}

	public void save(String folderName) {
		try {
			this.writeObject(folderName);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * シリアライズを行う
	 * @param folderName フォルダ名
	 * @throws IOException ストリームの読込に失敗した時スロー
	 */
	public void writeObject(String folderName) throws IOException {

		File dir = new File(folderName);
		if (!dir.isDirectory()) {
			// フォルダ作成
			if (!dir.mkdir()) {
				throw new IOException("make folder failed!");
			}
		}

		ObjectOutputStream stream;

		// エンティティの書き込み
		stream = new ObjectOutputStream(new FileOutputStream(folderName + "/" + "storyconfig.scb"));
		stream.writeObject(this.storyConfig);
		stream.close();
		stream = new ObjectOutputStream(new FileOutputStream(folderName + "/" + "person.scb"));
		this.persons.writeObject(stream);
		stream.close();
		stream = new ObjectOutputStream(new FileOutputStream(folderName + "/" + "place.scb"));
		this.places.writeObject(stream);
		stream.close();
		stream = new ObjectOutputStream(new FileOutputStream(folderName + "/" + "part.scb"));
		this.parts.writeObject(stream);
		stream.close();
		stream = new ObjectOutputStream(new FileOutputStream(folderName + "/" + "storyline.scb"));
		this.storylines.writeObject(stream);
		stream.close();
		stream = new ObjectOutputStream(new FileOutputStream(folderName + "/" + "scene.scb"));
		{
			List<Storyline> lines = this.storylines.getAllEntities();
			int size = 0;
			for (Storyline line : lines) {
				size += line.getScenes().size();
			}
			stream.writeInt(size);
			for (Storyline line : lines) {
				for (Scene s : line.getScenes()) {
					stream.writeObject(s);
				}
			}
		}
		stream.close();
	}

	/**
	 * デシリアライズを行う
	 * @param folderName フォルダ名
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	private void readObject(String folderName) throws IOException, ClassNotFoundException {

		File dir = new File(folderName);
		if (!dir.isDirectory()) {
			throw new IOException("folder is not exist!");
		}

		ObjectInputStream stream;

		// エンティティの読み込み
		try {
			// 後のコミットで追加されたファイルなので、存在しない場合に対応
			stream = new ObjectInputStream(new FileInputStream(folderName + "/" + "storyconfig.scb"));
			this.storyConfig = (StoryConfig)stream.readObject();
			stream.close();
		} catch (FileNotFoundException e) {
			this.storyConfig = new StoryConfig();
		}
		stream = new ObjectInputStream(new FileInputStream(folderName + "/" + "person.scb"));
		this.persons.clear();
		this.persons.readObject(stream);
		stream.close();
		stream = new ObjectInputStream(new FileInputStream(folderName + "/" + "place.scb"));
		this.places.clear();
		this.places.readObject(stream);
		stream.close();
		stream = new ObjectInputStream(new FileInputStream(folderName + "/" + "part.scb"));
		this.parts.clear();
		this.parts.readObject(stream);
		stream.close();
		stream = new ObjectInputStream(new FileInputStream(folderName + "/" + "storyline.scb"));
		this.storylines.clear();
		this.storylines.readObject(stream);
		stream.close();
		stream = new ObjectInputStream(new FileInputStream(folderName + "/" + "scene.scb"));
		{
			int size = stream.readInt();
			for (int i = 0; i < size; i++) {
				stream.readObject();
			}
		}
		stream.close();
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="人物の操作">
	/**
	 * 人物を追加.
	 */
	public void addPerson() {
		Person entity = new Person();
		this.persons.insert(entity);
		Messenger.getInstance().send(new MainPersonTableSelectItemMessage(entity));
	}

	/**
	 * 選択された人物を削除.
	 */
	public void deletePerson() {
		Messenger.getInstance().send(new EmptyEditMessage());
		this.persons.delete();
	}

	/**
	 * 選択された人物を上へ移動.
	 */
	public void upPerson() {
		Person entity = this.persons.getSelectedEntity();
		this.persons.up();
		Messenger.getInstance().send(new MainPersonTableSelectItemMessage(entity));
	}

	/**
	 * 選択された人物を下へ移動.
	 */
	public void downPerson() {
		Person entity = this.persons.getSelectedEntity();
		this.persons.down();
		Messenger.getInstance().send(new MainPersonTableSelectItemMessage(entity));
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="場所の操作">
	/**
	 * 場所を追加.
	 */
	public void addPlace() {
		Place entity = new Place();
		this.places.add(entity);
		Messenger.getInstance().send(new MainPlaceTableSelectItemMessage((TreeItem)entity.getTreeItem()));
	}

	/**
	 * 選択された場所を削除.
	 */
	public void deletePlace() {
		Messenger.getInstance().send(new EmptyEditMessage());
		this.places.delete();
	}

	/**
	 * 選択された場所を上へ移動.
	 */
	public void upPlace() {
		if (this.places.getSelectedEntity() != null) {
			Place entity = this.places.getSelectedEntity();
			this.places.up();
			Messenger.getInstance().send(new MainPlaceTableSelectItemMessage((TreeItem)entity.getTreeItem()));
		} else {
			this.places.up();
		}
	}

	/**
	 * 選択された場所を下へ移動.
	 */
	public void downPlace() {
		if (this.places.getSelectedEntity() != null) {
			Place entity = this.places.getSelectedEntity();
			this.places.down();
			Messenger.getInstance().send(new MainPlaceTableSelectItemMessage((TreeItem)entity.getTreeItem()));
		} else {
			this.places.down();
		}
	}

	/**
	 * 選択された場所を左へ移動.
	 */
	public void leftPlace() {
		if (this.places.getSelectedEntity() != null) {
			Place entity = this.places.getSelectedEntity();
			this.places.left();
			Messenger.getInstance().send(new MainPlaceTableSelectItemMessage((TreeItem)entity.getTreeItem()));
		} else {
			this.places.left();
		}
	}

	/**
	 * 選択された場所を右へ移動.
	 */
	public void rightPlace() {
		if (this.places.getSelectedEntity() != null) {
			Place entity = this.places.getSelectedEntity();
			this.places.right();
			Messenger.getInstance().send(new MainPlaceTableSelectItemMessage((TreeItem)entity.getTreeItem()));
		} else {
			this.places.right();
		}
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="ストーリーラインの操作">
	/**
	 * 指定したIDを持つストーリーラインを取得
	 * @param id ストーリーラインのID
	 * @return ストーリーライン。なければnull
	 */
	public Storyline getStoryline(long id) {
		return this.storylines.get(id);
	}

	/**
	 * ストーリーラインの末尾への追加.
	 */
	public void addStoryline() {
		if (this.viewSelectedPart.get() != null) {
			Storyline line = this.createStoryline();
			this.viewSelectedPart.get().getStorylines().add(line);
			this.storylines.add(line);
			Messenger.getInstance().send(new StorylineEditMessage(line));
		}
	}

	/**
	 * 現在選択されているストーリーラインを削除.
	 */
	public void deleteStoryline() {
		if (this.viewSelectedPart.get() != null) {
			Messenger.getInstance().send(new EmptyEditMessage());
			this.viewSelectedPart.get().getStorylines().remove(this.storylines.getSelectedEntity());
			this.storylines.delete();
		}
	}

	/**
	 * ストーリーラインを、現在選択されているものの前に追加.
	 */
	public void addBackStoryline() {
		if (this.viewSelectedPart.get() != null) {
			Storyline line = this.createStoryline();
			this.viewSelectedPart.get().getStorylines().add(line);
			this.storylines.insert(line);
			Messenger.getInstance().send(new StorylineEditMessage(line));
		}
	}

	/**
	 * ストーリーラインを、現在選択されているものの次に追加.
	 */
	public void addNextStoryline() {
		if (this.viewSelectedPart.get() != null) {
			Storyline line = this.createStoryline();
			this.viewSelectedPart.get().getStorylines().add(line);
			this.storylines.insert(line);
			this.storylines.down(line);
			Messenger.getInstance().send(new StorylineEditMessage(line));
		}
	}

	/**
	 * 現在選択されているストーリーラインを、１つ前へ移動.
	 */
	public void upStoryline() {
		this.storylines.up();
	}

	/**
	 * 指定のストーリーラインを、１つ前へ移動.
	 * @param line ストーリーライン
	 */
	public void upStoryline(Storyline line) {
		this.storylines.up(line);
	}

	/**
	 * 現在選択されているストーリーラインを、１つ後へ移動.
	 */
	public void downStoryline() {
		this.storylines.down();
	}

	/**
	 * 指定のストーリーラインを、１つ後へ移動.
	 * @param line ストーリーライン
	 */
	public void downStoryline(Storyline line) {
		this.storylines.down(line);
	}

	/**
	 * デフォルトのストーリーラインを生成。実質ファクトリメソッド
	 * @return 生成されたストーリーライン
	 */
	private Storyline createStoryline() {
		Storyline line = new Storyline();
		line.setColor(Color.GRAY);
		return line;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="シーンの操作">
	/**
	 * シーンの追加.
	 */
	public void addScene(Storyline storyLine) {
		Scene entity = new Scene();
		storyLine.getScenes().add(entity);
		Messenger.getInstance().send(new SceneEditMessage(entity));
	}

	/**
	 * シーンの削除.
	 */
	public void deleteScene() {
		if (this.selectedScene.get() != null && this.selectedScene.get().getStoryline() != null) {
			Messenger.getInstance().send(new EmptyEditMessage());
			boolean r = this.selectedScene.get().getStoryline().getScenes().remove(this.selectedScene.get());
		}
	}

	/**
	 * シーンを、現在選択されているシーンの次へ追加。
	 * 現在選択されているストーリーラインの中のシーンとして追加される.
	 */
	public void addNextScene() {
		if (this.selectedScene.get() != null && this.selectedScene.get().getStoryline() != null) {
			Scene entity = new Scene();
			entity.setOrder(this.selectedScene.get().getOrder());
			this.shiftSceneOrder(this.selectedScene.get());
			entity.replaceOrder(this.selectedScene.get());
			this.selectedScene.get().getStoryline().getScenes().add(entity);
			Messenger.getInstance().send(new SceneEditMessage(entity));
		}
	}

	/**
	 * シーンを、現在選択されているシーンの前へ追加。
	 * 現在選択されているストーリーラインの中のシーンとして追加される.
	 */
	public void addBackScene() {
		if (this.selectedScene.get() != null && this.selectedScene.get().getStoryline() != null) {
			Scene entity = new Scene();
			entity.setOrder(this.selectedScene.get().getOrder());
			this.shiftSceneOrder(entity);
			this.selectedScene.get().getStoryline().getScenes().add(entity);
			Messenger.getInstance().send(new SceneEditMessage(entity));
		}
	}

	/**
	 * シーンを、現在選択されているシーンの左へ移動.
	 */
	public void leftScene() {
		if (this.selectedScene.get() != null && this.selectedScene.get().getStoryline() != null) {
			this.leftScene(this.selectedScene.get());
		}
	}

	/**
	 * 指定のシーンを左へ移動.
	 */
	public void leftScene(Scene scene) {
		Scene backScene = this.findBackScene(scene);
		if (backScene != null) {
			scene.replaceOrder(backScene);
			Messenger.getInstance().send(new SceneOrderChangeMessage());
		}
	}

	/**
	 * シーンを、現在選択されているシーンの右へ移動.
	 */
	public void rightScene() {
		if (this.selectedScene.get() != null && this.selectedScene.get().getStoryline() != null) {
			this.rightScene(this.selectedScene.get());
		}
	}

	/**
	 * 指定のシーンを右へ移動.
	 */
	public void rightScene(Scene scene) {
		Scene nextScene = this.findNextScene(scene);
		if (nextScene != null) {
			scene.replaceOrder(nextScene);
			Messenger.getInstance().send(new SceneOrderChangeMessage());
		}
	}

	/**
	 * 現在選択中のシーンを、orderが１つ前のストーリーラインへ移す.
	 */
	public void sceneToBackStoryline() {
		this.sceneToBackStoryline(this.selectedScene.get());
	}

	/**
	 * 指定のシーンを、orderが１つ前のストーリーラインへうつす
	 * @param scene シーン
	 */
	public void sceneToBackStoryline(Scene scene) {
		Storyline line = this.storylines.getBack(scene.getStoryline());
		if (line != null) {
			this.sceneToStoryline(scene, line);
		}
	}

	/**
	 * 現在選択中のシーンを、orderが１つ次のストーリーラインへ移す.
	 */
	public void sceneToNextStoryline() {
		this.sceneToNextStoryline(this.selectedScene.get());
	}

	/**
	 * 指定のシーンを、orderが１つ後のストーリーラインへうつす
	 * @param scene シーン
	 */
	public void sceneToNextStoryline(Scene scene) {
		Storyline line = this.storylines.getNext(scene.getStoryline());
		if (line != null) {
			this.sceneToStoryline(scene, line);
		}
	}

	/**
	 * 指定のシーンを、指定のストーリーラインへうつす
	 * @param scene シーン
	 * @param line ストーリーライン
	 */
	public void sceneToStoryline(Scene scene, Storyline line) {
		line.getScenes().add(scene);
	}

	/**
	 * orderが指定したシーン以上であるシーンの順番を1つずつ足し算してずらす
	 * （引き算するメソッドは、orderの仕様上意味が無いので作らない）
	 * ただし、指定したシーンがStorylineに登録されていない場合、そのシーンに対しては処理を行わない
	 * @param startScene 指定するシーン
	 */
	private void shiftSceneOrder(Scene startScene) {
		long baseOrder = startScene.getOrder();
		for (Storyline storyline : this.storylines.getEntities()) {
			for (Scene scene : storyline.getScenes()) {
				if (scene.getOrder() >= baseOrder) {
					scene.setOrder(scene.getOrder() + 1);
				}
			}
		}
	}

	/**
	 * 次のシーンを取得
	 * @param scene 基準となるシーン
	 * @return 次のシーン。なければnull
	 */
	private Scene findNextScene(Scene scene) {
		Scene ret = null;
		long baseOrder = scene.getOrder();
		long lastOrder = -1;

		outside:
		for (Storyline storyline : this.storylines.getEntities()) {
			for (Scene s : storyline.getScenes()) {
				long order = s.getOrder();
				if (order > baseOrder && (order < lastOrder || lastOrder < 0)) {
					ret = s;
					lastOrder = order;
					if (baseOrder == lastOrder - 1) {
						break outside;
					}
				}
			}
		}

		return ret;
	}

	/**
	 * 前のシーンを取得
	 * @param scene 基準となるシーン
	 * @return 前のシーン。なければnull
	 */
	private Scene findBackScene(Scene scene) {
		Scene ret = null;
		long baseOrder = scene.getOrder();
		long lastOrder = -1;

		outside:
		for (Storyline storyline : this.storylines.getEntities()) {
			for (Scene s : storyline.getScenes()) {
				long order = s.getOrder();
				if (order < baseOrder && (lastOrder < 0 || order > lastOrder)) {
					ret = s;
					lastOrder = order;
					if (baseOrder == lastOrder + 1) {
						break outside;
					}
				}
			}
		}

		return ret;
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="編の操作">
	/**
	 * 指定したIDを持つ編を取得
	 * @param id 編のID
	 * @return 編。なければnull
	 */
	public Part getPart(long id) {
		return this.parts.get(id);
	}

	/**
	 * 編を追加.
	 */
	public void addPart() {
		Part entity = new Part();
		this.parts.insert(entity);
		Messenger.getInstance().send(new MainPartTableSelectItemMessage(entity));
	}

	/**
	 * 選択された編を削除.
	 */
	public void deletePart() {
		Messenger.getInstance().send(new EmptyEditMessage());
		this.parts.delete();
	}

	/**
	 * 選択された編を上へ移動.
	 */
	public void upPart() {
		Part entity = this.parts.getSelectedEntity();
		this.parts.up();
		Messenger.getInstance().send(new MainPartTableSelectItemMessage(entity));
	}

	/**
	 * 選択された編を下へ移動.
	 */
	public void downPart() {
		Part entity = this.parts.getSelectedEntity();
		this.parts.down();
		Messenger.getInstance().send(new MainPartTableSelectItemMessage(entity));
	}

	/**
	 * 選択された編に関連する画面を開くようにする.
	 */
	public void openPart() {
		if (this.viewSelectedPart.get() != null) {
			this.storylines.filtering(e -> {
				Storyline line = (Storyline)e;
				if (line.getPart() != null) {
					return line.getPart().getId() == this.viewSelectedPart.get().getId();
				} else {
					return false;
				}
			});
		} else {
			this.storylines.filtering(e -> false);
		}
	}
//</editor-fold>
}
