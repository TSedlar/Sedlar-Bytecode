/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node.filter;

import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.bytecode.tree.node.PushNode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class PushNodeFilter extends AbstractNodeFilter {

	@Override
	public boolean accept(AbstractNode an) {
		return an instanceof PushNode && accept((PushNode) an);
	}

	public abstract boolean accept(PushNode pn);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param value
	 *            The value to match against
	 * @return a filter matching the given parameters.
	 */
	public static PushNodeFilter create(final int value) {
		return new PushNodeFilter() {
			public boolean accept(PushNode pn) {
				return pn.insn().value() == value;
			}
		};
	}

	/**
	 * Creates a filter matching a PushNode
	 * 
	 * @return a filter matching a PushNode
	 */
	public static PushNodeFilter create() {
		return new PushNodeFilter() {
			public boolean accept(PushNode pn) {
				return true;
			}
		};
	}
}
