/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node.filter;

import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.bytecode.tree.node.ConstantNode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class ConstantNodeFilter extends AbstractNodeFilter {

	@Override
	public boolean accept(AbstractNode an) {
		return an instanceof ConstantNode && accept((ConstantNode) an);
	}

	public abstract boolean accept(ConstantNode cn);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param constant
	 *            the constant object to match against.
	 * @return a filter matching the given parameters.
	 */
	public static ConstantNodeFilter create(final Object constant) {
		return new ConstantNodeFilter() {
			public boolean accept(ConstantNode cn) {
				return cn.insn().constant() == constant;
			}
		};
	}

	/**
	 * Creates a filter matching a ConstantNode
	 * 
	 * @return a filter matching a ConstantNode
	 */
	public static ConstantNodeFilter create() {
		return new ConstantNodeFilter() {
			public boolean accept(ConstantNode cn) {
				return true;
			}
		};
	}
}
