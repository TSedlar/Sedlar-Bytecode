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
import me.sedlar.bytecode.util.filter.InstructionFilter;
import me.sedlar.util.Filter;
import me.sedlar.util.collection.MultiHashMap;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class AbstractNodeFilter implements Filter<AbstractNode> {

    protected final MultiHashMap<String, AbstractNode> cache = new MultiHashMap<>();

	private List<AbstractNodeFilter> chained = new LinkedList<>();
    private String label = null;
    private Opcode opcode;

	public AbstractNodeFilter() {
		chained.add(this);
	}

    public MultiHashMap<String, AbstractNode> cache() {
        return cache;
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
     * Creates a filter with the given opcode
     *
     * @param opcode the opcode to set
     * @return a filter with the given opcode
     */
    public AbstractNodeFilter opcode(Opcode opcode) {
        this.opcode = opcode;
        return this;
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
     * @param cacheLabels
     *            <t>true</t> to cache results, otherwise <t>false</t>.
	 * @return <t>true</t> if this filter is valid against the given node,
	 *         otherwise <t>false</t>.
	 */
	public boolean validate(AbstractNode an, boolean cacheLabels) {
        if (opcode != null && an.opcode() != opcode)
            return false;
        cache.clear();
		for (AbstractNodeFilter filter : chained) {
			if (filter.accept(an)) {
                if (cacheLabels && filter.label() != null)
                    cache.add(filter.label(), an);
                return true;
            }
		}
		return false;
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
        return validate(an, false);
    }

    /**
     * Gets the label for this filter.
     *
     * @return the label for this filter.
     */
    public String label() {
        return label;
    }

    /**
     * Creates a filter with the given label.
     *
     * @param label
     *            The label
     * @return a filter with the given label.
     */
    public AbstractNodeFilter label(String label) {
        this.label = label;
        return this;
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

    /**
     * Creates a filter from the given string.
     *
     * @param filter the string to construct the filter from.
     * @return a filter from the given string.
     */
    public static AbstractNodeFilter fromString(String filter) {
        String[] keys = {"BNF", "CNF", "FNF", "INF", "NF", "MNF", "PNF", "TNF", "VNF"};
        String[] values = {"BIF", "CIF", "FIF", "IIF", "IF", "MIF", "PIF", "TIF", "VIF"};
        for (int i = 0; i < keys.length; i++) {
            if (filter.startsWith(keys[i]))
                filter = filter.replaceFirst(keys[i], values[i]);
        }
        final InstructionFilter iFilter = InstructionFilter.fromString(filter);
        return new AbstractNodeFilter() {
            public boolean accept(AbstractNode an) {
                return iFilter.accept(an.insn());
            }
        }.label(iFilter.label()).opcode(iFilter.opcode());
    }

    /**
     * Creates an array of filters from the given strings.
     *
     * @param filter
     *            the string to construct an array of filters from.
     * @return an array of filters from the given strings.
     */
    public static AbstractNodeFilter[] filtersFor(String filter) {
        String[] filters = filter.split(" > ");
        AbstractNodeFilter[] array = new AbstractNodeFilter[filters.length];
        for (int i = 0; i < filters.length; i++)
            array[i] = fromString(filters[i]);
        return array;
    }
}
