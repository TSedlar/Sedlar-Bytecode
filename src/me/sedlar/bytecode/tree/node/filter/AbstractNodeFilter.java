/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.tree.node.filter;

import java.util.LinkedList;
import java.util.List;

import me.sedlar.bytecode.Opcode;
import me.sedlar.bytecode.tree.node.AbstractNode;
import me.sedlar.util.Filter;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class AbstractNodeFilter implements Filter<AbstractNode> {

	private List<AbstractNodeFilter> chained = new LinkedList<>();

	public AbstractNodeFilter() {
		chained.add(this);
	}

	private void addAll(AbstractNodeFilter filter) {
		if (filter.chained.size() > 1) {
			for (AbstractNodeFilter f : filter.chained) {
				chained.add(f);
				if (f.chained.size() > 1)
					addAll(f);
			}
		}
	}

	/**
	 * Constructs a filter matching this filter and the given filter.
	 * 
	 * @param filter
	 *            the filter to match with.
	 * @return a filter matching this filter and the given filter.
	 */
	public AbstractNodeFilter or(AbstractNodeFilter filter) {
		addAll(filter);
		return this;
	}

	/**
	 * Checks if this filter is valid against the given node.
	 * 
	 * @param an
	 *            the node to check this filter against.
	 * @return <t>true</t> if this filter is valid against the given node,
	 *         otherwise <t>false</t>.
	 */
	public boolean validate(AbstractNode an) {
		for (AbstractNodeFilter filter : chained) {
			if (filter.accept(an))
				return true;
		}
		return false;
	}

	/**
	 * Creates a filter for the given opcode.
	 * 
	 * @param opcode
	 *            the opcode to match against.
	 * @return a filter for the given opcode.
	 */
	public static AbstractNodeFilter create(final Opcode opcode) {
		return new AbstractNodeFilter() {
			public boolean accept(AbstractNode an) {
				return an.opcode() == opcode;
			}
		};
	}
}
