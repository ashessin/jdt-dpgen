package com.gof.structural.composite;

import java.util.List;

public class Composite extends Component {

	public List<Component> children = new java.util.ArrayList<>();

	@Override
	public void operation() {
		children.forEach(Component::operation);
	}

	public void add(Component component) {
		children.add(component);
	}

	public void remove(Component component) {
		children.remove(component);
	}

	public Component getChild(int index) {
		return children.get(index);
	}
}