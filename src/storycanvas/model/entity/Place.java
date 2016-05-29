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
package storycanvas.model.entity;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 場所を表すクラス
 * @author KMY
 */
public class Place extends TreeEntity implements Serializable {

	public Place() {
		this.initialize();
	}

	@Override
	protected final void initialize() {
		super.initialize();
	}

//<editor-fold defaultstate="collapsed" desc="シリアライズ">
	private static final long serialVersionUID = 1L;
	private static final long serialInstanceVersionUID = 6_00000000001L;

	/**
	 * シリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームへの出力に失敗した時スロー
	 */
	private void writeObject(ObjectOutputStream stream) throws IOException {

		this.writeBaseObject(stream);

		// 固有UID書き込み
		stream.writeLong(serialInstanceVersionUID);

		// プロパティ書き込み

		// 子エンティティの書き込み
		super.writeChildren(stream);
	}

	/**
	 * デシリアライズを行う
	 * @param stream ストリーム
	 * @throws IOException ストリームの読込に失敗した時スロー
	 * @throws ClassNotFoundException 該当するバージョンのクラスが見つからなかった時にスロー
	 */
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {

		this.readBaseObject(stream);

		long uid = stream.readLong();
		if (uid == serialInstanceVersionUID) {

			// コンストラクタ
			this.initialize();

			// プロパティ読込

			// 子エンティティ読み込み
			super.readChildren(stream);
		}

	}
//</editor-fold>

	@Override
	protected String getResourceName () {
		return "place";
	}

}
