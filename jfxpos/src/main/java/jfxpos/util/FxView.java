package jfxpos.util;

import javafx.scene.Parent;

public class FxView<T> {

	public final Parent root;
	public final T controller;

	public FxView(Parent root, T controller) {
		this.root = root;
		this.controller = controller;
	}
}
