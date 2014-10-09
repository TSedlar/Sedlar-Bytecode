/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util;

import java.util.*;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.util.filter.InstructionFilter;
import me.sedlar.util.collection.QueryableList;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class QueryableInstructionList extends QueryableList<AbstractInstruction> {

	protected final Map<String, AbstractInstruction> cache = new HashMap<>();
	
	@Override
	public AbstractInstruction set(int index, AbstractInstruction ai) {
		AbstractInstruction old = get(index);
		ai.setNext(old.next());
		ai.setPrevious(old.previous());
		ai.setOffset(old.offset());
		return super.set(index, ai);
	}

    /**
     * Locates all lists of instructions matching each filter in order.
     *
     * @param filters
     *            The filters to match instructions against
     * @return list of instructions, each matching each filter in order.
     */
    public List<QueryableInstructionList> findAll(InstructionFilter... filters) {
        List<QueryableInstructionList> res = new ArrayList<>();
        QueryableInstructionList list = new QueryableInstructionList();
        Iterator<AbstractInstruction> iterator = iterator();
        int index = 0, radius = 0;
        while (iterator.hasNext()) {
            if (index >= filters.length) {
                res.add(list);
                list = new QueryableInstructionList();
                index = 0;
                radius = 0;
            }
            if (!filters[index].accept(radius)) {
                index = 0;
                radius = 0;
                list.clear();
                list.cache.clear();
            }
            AbstractInstruction ai = iterator.next();
            radius++;
            if (filters[index].validate(ai)) {
                String label = filters[index].label();
                if (label != null)
                    list.cache.put(label, ai);
                index++;
                list.add(ai);
                radius = 0;
            }
        }
        return res.size() > 0 ? res : null;
    }

    /**
     * Locates instructions matching each filter in order.
     *
     * @param filters
     *            The filters to match instructions against
     * @return instructions matching each filter in order.
     */
    public QueryableInstructionList find(InstructionFilter... filters) {
        List<QueryableInstructionList> res = this.findAll(filters);
        return res != null ? res.get(0): null;
    }

	/**
	 * Gets the instruction with the given label.
	 * 
	 * @param label
	 *            The label to search with.
	 * @return the instruction with the given label.
	 */
	public AbstractInstruction get(String label) {
		return cache.get(label);
	}

	/**
	 * Prints out the instructions.
	 * 
	 * @param mi
	 *            The method this list is set to.
	 */
	public void debug(MethodInfo mi) {
		System.out.println(mi.classInfo().name() + "." + mi.name() + mi.descriptor() + ": {");
		forEach(ai -> System.out.println("\t" + Assembly.toString(ai)));
		System.out.println("}");
	}
}
