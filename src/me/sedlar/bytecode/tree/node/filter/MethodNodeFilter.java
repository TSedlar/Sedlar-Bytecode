/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node.filter;

import me.sedlar.bytecode.MethodInstruction;
import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.bytecode.tree.node.MethodNode;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class MethodNodeFilter extends AbstractNodeFilter {

	@Override
	public boolean accept(AbstractNode an) {
		return an instanceof MethodNode && accept((MethodNode) an);
	}

	public abstract boolean accept(MethodNode fn);

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param owner
	 *            The field's owner or null to match any
	 * @param name
	 *            The field's name or null to match any
	 * @param descriptor
	 *            The field's descriptor or null to match any
	 * @return a filter matching the givenr parameters.
	 */
	public static MethodNodeFilter create(final String owner, final String name, final String descriptor) {
		return new MethodNodeFilter() {
			public boolean accept(MethodNode fn) {
				MethodInstruction fi = fn.insn();
				if (owner == null || fi.owner().equals(owner)) {
					if (name == null || fi.name().equals(name))
						return descriptor == null || fi.descriptor().equals(descriptor);
				}
				return false;
			}
		};
	}

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param owner
	 *            The field's owner or null to match any
	 * @param descriptor
	 *            The field' descriptor or null to match any
	 * @return a filter matching the given parameters.
	 */
	public static MethodNodeFilter create(final String owner, final String descriptor) {
		return create(owner, null, descriptor);
	}

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param descriptor
	 *            The field's descriptor or null to match any
	 * @return a filter matching the givenr parameters.
	 */
	public static MethodNodeFilter create(final String descriptor) {
		return create(null, descriptor);
	}

	/**
	 * Creates a filter matching the given parameters.
	 * 
	 * @param descriptor
	 *            A regex pattern matching the field's descriptor or null to
	 *            match any
	 * @return a filter matching the given parameters.
	 */
	public static MethodNodeFilter createRegex(final String descriptor) {
		return new MethodNodeFilter() {
			public boolean accept(MethodNode fn) {
				return descriptor == null || fn.insn().descriptor().matches(descriptor);
			}
		};
	}

	/**
	 * Creates a filter matching a MethodNode
	 * 
	 * @return a filter matching a MethodNode
	 */
	public static MethodNodeFilter create() {
		return new MethodNodeFilter() {
			public boolean accept(MethodNode fn) {
				return true;
			}
		};
	}
}