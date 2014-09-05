/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node.filter;

import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.bytecode.tree.node.VariableNode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class VariableNodeFilter extends AbstractNodeFilter {

	@Override
	public boolean accept(AbstractNode an) {
		return an instanceof VariableNode && accept((VariableNode) an);
	}

	public abstract boolean accept(VariableNode vn);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param variable
	 *            The variable number
	 * @return a filter matching the given parameters.
	 */
	public static VariableNodeFilter create(final int variable) {
		return new VariableNodeFilter() {
			public boolean accept(VariableNode vn) {
				return vn.insn().variable() == variable;
			}
		};
	}

	/**
	 * Creates a filter matching a VariableNode
	 * 
	 * @return a filter matching a VariableNode
	 */
	public static VariableNodeFilter create() {
		return new VariableNodeFilter() {
			public boolean accept(VariableNode vn) {
				return true;
			}
		};
	}
}
