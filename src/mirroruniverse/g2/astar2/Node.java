package mirroruniverse.g2.astar2;

import java.util.Comparator;

public class Node<T> implements Comparable<Object> {
	T state;
	int f;
	int g;
	int h;
	Node<T> parent;

	public Node(T state, int h) {
		this.state = state;
		this.g = 0;
		this.h = h;
		this.f = g + h;
		this.parent = null;
	}

	public Node(T state, int stepCost, int h, Node<T> parent) {
		this.state = state;
		this.g =  stepCost + parent.g;
		this.h = h;
		this.f = g + h;
		this.parent = parent;
	}

	@Override
	public int compareTo(Object o) {
		Node<T> n = (Node<T>) o;
		return this.f - n.f;
	}
}
