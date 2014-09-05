/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node.filter;

import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.bytecode.tree.node.TypeNode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class TypeNodeFilter extends AbstractNodeFilter {

	@Override
	public boolean accept(AbstractNode an) {
		return an instanceof TypeNode && accept((TypeNode) an);
	}

	public abstract boolean accept(TypeNode tn);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param type
	 *            The type descriptor
	 * @return a filter matching the given parameters.
	 */
	public static TypeNodeFilter create(final String type) {
		return new TypeNodeFilter() {
			public boolean accept(TypeNode tn) {
				return tn.insn().type().equals(type);
			}
		};
	}

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param type
	 *            A regex pattern matching the type's descriptor
	 * @return a filter matching the given parameters.
	 */
	public static TypeNodeFilter createRegex(final String type) {
		return new TypeNodeFilter() {
			public boolean accept(TypeNode tn) {
				return tn.insn().type().matches(type);
			}
		};
	}

	/**
	 * Creates a filter matching a TypeNode
	 * 
	 * @return a filter matching a TypeNode
	 */
	public static TypeNodeFilter create() {
		return new TypeNodeFilter() {
			public boolean accept(TypeNode tn) {
				return true;
			}
		};
	}
}
