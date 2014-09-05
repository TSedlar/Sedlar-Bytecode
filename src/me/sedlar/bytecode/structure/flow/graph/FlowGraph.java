/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.flow.graph;

import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.bytecode.structure.flow.BasicBlock;
import me.sedlar.util.graph.Digraph;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class FlowGraph extends Digraph<BasicBlock, BasicBlock> {

	private final MethodInfo method;

	public FlowGraph(MethodInfo method) {
		this.method = method;
	}

	public MethodInfo method() {
		return method;
	}
}
