/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.flow;

import java.util.LinkedList;
import java.util.List;

import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.bytecode.BranchInstruction;
import me.sedlar.bytecode.Opcode;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.tree.NodeTree;
import me.sedlar.bytecode.tree.util.TreeBuilder;
import me.sedlar.bytecode.util.Assembly;
import me.sedlar.bytecode.util.QueryableInstructionList;
import me.sedlar.bytecode.util.filter.InstructionFilter;
import me.sedlar.util.Filter;
import me.sedlar.util.MeasurableInteger;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class BasicBlock {

	private final String label;
	private final MethodInfo method;
	private final QueryableInstructionList list;
	private final int low, high;
	private NodeTree tree;
	private BasicBlock target, next, previous;
	private final List<BasicBlock> predecessors = new LinkedList<>(), successors = new LinkedList<>();

	public BasicBlock(String label, MethodInfo method, QueryableInstructionList list, int low, int high) {
		this.label = label;
		this.method = method;
		this.list = list;
		this.low = low;
		this.high = high;
	}

	public String label() {
		return label;
	}

	public QueryableInstructionList instructions() {
		return list;
	}

	public int low() {
		return low;
	}

	public int high() {
		return high;
	}

	public int size() {
		return instructions().size();
	}

	public BranchInstruction branch() {
		for (AbstractInstruction ai : list) {
			if (ai instanceof BranchInstruction)
				return (BranchInstruction) ai;
		}
		return null;
	}

	public int targetIndex() {
		BranchInstruction branch = branch();
		return branch != null ? branch.totalOffset() : -1;
	}

	public void setTarget(BasicBlock target) {
		this.target = target;
	}

	public BasicBlock target() {
		return target;
	}

	public void setNext(BasicBlock next) {
		this.next = next;
	}

	public BasicBlock next() {
		return next;
	}

	public void setPrevious(BasicBlock previous) {
		this.previous = previous;
	}

	public BasicBlock previous() {
		return previous;
	}

	public void addPredecessor(BasicBlock block) {
		predecessors.add(block);
	}

	public List<BasicBlock> predecessors() {
		return predecessors;
	}

	public void addSuccessor(BasicBlock block) {
		successors.add(block);
	}

	public List<BasicBlock> successors() {
		return successors;
	}

	public BlockType type() {
		if (BlockType.EMPTY.filter.accept(this)) {
			return BlockType.EMPTY;
		} else if (BlockType.END.filter.accept(this)) {
			return BlockType.END;
		}
		return BlockType.IMMEDIATE;
	}

	public boolean accept(Filter<AbstractInstruction> filter) {
		for (AbstractInstruction instruction : instructions()) {
			if (filter.accept(instruction))
				return true;
		}
		return false;
	}

	public int count(String filter) {
		return count(InstructionFilter.fromString(filter));
	}

	public int count(InstructionFilter filter) {
		int count = 0;
		for (AbstractInstruction instruction : list) {
			if (filter.accept(instruction))
				count++;
		}
		return count;
	}

	public int count(Opcode opcode) {
		int count = 0;
		for (AbstractInstruction instruction : list) {
			if (instruction.opcode() == opcode)
				count++;
		}
		return count;
	}

	public NodeTree tree() {
		if (tree != null)
			return tree;
		return (tree = TreeBuilder.build(this));
	}

	@Override
	public String toString() {
		String key = method.classInfo().name() + "." + method.name() + method.descriptor();
		StringBuilder builder = new StringBuilder(key + ": {");
		for (AbstractInstruction instruction : list)
			builder.append("\n\t").append(Assembly.toString(instruction));
		builder.append("\n}");
		return builder.toString();
	}

	public MethodInfo method() {
		return method;
	}
	
	public MeasurableInteger weight() {
		return new MeasurableInteger(instructions().size() * predecessors.size());
	}
}
