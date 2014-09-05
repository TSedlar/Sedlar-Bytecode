/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node.filter;

import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.bytecode.tree.node.BranchNode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class BranchNodeFilter extends AbstractNodeFilter {

	@Override
	public boolean accept(AbstractNode an) {
		return an instanceof BranchNode && accept((BranchNode) an);
	}

	public abstract boolean accept(BranchNode bn);

	/**
	 * Creates a filter matching a BranchNode
	 * 
	 * @return a filter matching a BranchNode
	 */
	public static BranchNodeFilter create() {
		return new BranchNodeFilter() {
			public boolean accept(BranchNode bn) {
				return true;
			}
		};
	}
}
