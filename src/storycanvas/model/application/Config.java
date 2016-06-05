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
package storycanvas.model.application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * アプリケーションの設定
 *
 * @author KMY
 */
public final class Config implements Serializable {

//<editor-fold defaultstate="collapsed" desc="シングルトン">
	private Config() {}

	private static Config instance = null;

	public static Config getInstance() {
		loadConfig();
		return instance;
	}

	/**
	 * 設定ファイルを読み込む.
	 */
	private static void loadConfig() {
		if (instance == null) {

			String folderName = System.getProperty("user.home") + "/.storycanvas";

			try {
				File dir = new File(folderName);
				if (!dir.isDirectory()) {

					// フォルダが存在しなければ、ファイルから読み込まずに戻る
					instance = new Config();
					instance.initialize();
					return;
				}

				// データ読み込み
				ObjectInputStream stream = new ObjectInputStream(new FileInputStream(folderName + "/config.db"));
				instance = (Config)stream.readObject();
				stream.close();
			} catch (FileNotFoundException ex) {
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 設定ファイルを保存.
	 */
	private static void saveConfig() {
		String folderName = System.getProperty("user.home") + "/.storycanvas";

		try {
			File dir = new File(folderName);
			if (!dir.isDirectory()) {
				// フォルダ作成
				if (!dir.mkdir()) {
					throw new IOException("make folder failed!");
				}
			}

			// データ読み込み
			ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(folderName + "/config.db"));
			stream.writeObject(instance);
			stream.close();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="シリアライズ">
	private static final long serialVersionUID = 1L;
	private static final long serialInstanceVersionUID = 100_00000000001L;

	/**
	 * シリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームへの出力に失敗した時スロー
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		// 固有UID書き込み
		stream.writeLong(serialInstanceVersionUID);

		// プロパティ書き込み
		stream.writeUTF(this.folderOpenPath);
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		long uid = stream.readLong();

		// フィールドを初期化
		this.initialize();

		// プロパティ読込
		if (uid <= 100_00000000001L) {
			this.folderOpenPath = stream.readUTF();
		}

	}
//</editor-fold>

	private void initialize() {
		this.folderOpenPath = System.getProperty("user.home");
	}

	private transient String folderOpenPath;

	public String getFolderOpenPath() {
		return this.folderOpenPath;
	}

	public void setFolderOpenPath(String str) {
		this.folderOpenPath = str;
		saveConfig();
	}

}
