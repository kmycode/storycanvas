/*
 * KMY ライブラリ (LGPL)
 */
package net.kmycode.javafx;

import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * 弱い参照をもつオブジェクトプロパティ
 *
 * @author KMY
 */
public class SimpleWeakObjectProperty<T> extends ObjectProperty<T> {

	private final ObjectProperty<WeakReference<T>> wrapper = new SimpleObjectProperty<>();

	@Override
	public T get () {
		if (this.wrapper.get() == null) {
			return null;
		}
		return this.wrapper.get().get();
	}

	@Override
	public void set (T value) {
		if (this.isBound()) {
            throw new java.lang.RuntimeException((this.getBean() != null && this.getName() != null ?
                    this.getBean().getClass().getSimpleName() + "." + this.getName() + " : ": "") + "A bound value cannot be set.");
		}
		this.wrapper.set(new WeakReference<>(value));
	}

	@Override
	public void addListener (ChangeListener<? super T> listener) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void removeListener (ChangeListener<? super T> listener) {
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void addListener (InvalidationListener listener) {
		this.wrapper.addListener(listener);
	}

	@Override
	public void removeListener (InvalidationListener listener) {
		this.wrapper.removeListener(listener);
	}

	@Override
	public Object getBean () {
		return this.wrapper.getBean();
	}

	@Override
	public String getName () {
		return this.wrapper.getName();
	}

	private ObservableValue bindProperty;
	private InvalidationListener bindListener = e -> {
		this.set((T)this.bindProperty.getValue());
	};

	@Override
	public void bind (ObservableValue<? extends T> observable) {
		this.unbind();
		this.bindProperty = observable;
		observable.addListener(this.bindListener);
	}

	@Override
	public void unbind () {
		if (this.isBound()) {
			this.bindProperty.removeListener(this.bindListener);
			this.bindProperty = null;
		}
	}

	@Override
	public boolean isBound () {
		return this.bindProperty != null;
	}


}
