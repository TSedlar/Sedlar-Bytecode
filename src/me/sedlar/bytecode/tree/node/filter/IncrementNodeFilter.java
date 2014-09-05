/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node.filter;

import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.bytecode.tree.node.IncrementNode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class IncrementNodeFilter extends AbstractNodeFilter {

	@Override
	public boolean accept(AbstractNode an) {
		return an instanceof IncrementNode && accept((IncrementNode) an);
	}

	public abstract boolean accept(IncrementNode in);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param increment
	 *            The increment number
	 * @return a filter matching the given parameters.
	 */
	public static IncrementNodeFilter create(final int increment) {
		return new IncrementNodeFilter() {
			public boolean accept(IncrementNode in) {
				return in.insn().increment() == increment;
			}
		};
	}

	/**
	 * Creates a filter matching an IncrementNode
	 * 
	 * @return a filter matching an IncrementNode
	 */
	public static IncrementNodeFilter create() {
		return new IncrementNodeFilter() {
			public boolean accept(IncrementNode in) {
				return true;
			}
		};
	}
}
