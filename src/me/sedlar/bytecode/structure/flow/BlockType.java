/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.flow;

import static me.sedlar.bytecode.Opcode.GOTO;
import static me.sedlar.bytecode.Opcode.GOTO_W;
import me.sedlar.bytecode.AbstractInstruction;
import me.sedlar.util.Filter;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public enum BlockType {
	EMPTY(new Filter<BasicBlock>() {
		public boolean accept(BasicBlock block) {
			return block.instructions().size() == 1 && block.accept(new Filter<AbstractInstruction>() {
				public boolean accept(AbstractInstruction ai) {
					return ai.opcode() == GOTO || ai.opcode() == GOTO_W;
				}
			});
		}
	}),
	END(new Filter<BasicBlock>() {
		public boolean accept(BasicBlock block) {
			AbstractInstruction ai = block.instructions().getLast();
			return ai != null && ai.opcode().verbose().endsWith("return");
		}
	}),
	IMMEDIATE(new Filter<BasicBlock>() {
		public boolean accept(BasicBlock block) {
			return !EMPTY.filter.accept(block) && !END.filter.accept(block);
		}
	});

	protected final Filter<BasicBlock> filter;

	BlockType(Filter<BasicBlock> filter) {
		this.filter = filter;
	}
}
