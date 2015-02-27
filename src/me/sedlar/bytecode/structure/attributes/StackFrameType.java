/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public enum StackFrameType {

	SAME(63), // 0-63
	SAME_LOCALS_1_STACK_ITEM(127), // 64-127
	SAME_LOCALS_1_STACK_ITEM_EXT(247), // 247
	CHOP(250), // 248-250
	SAME_EXT(251), // 251
	APPEND(254), // 252-254
	FULL(255); // 255

	public static StackFrameType getFromTag(int tag) throws InvalidByteCodeException {
		if (tag < 0)
			throw new InvalidByteCodeException("StackMapFrame tag must be positive");
		if (tag <= SAME.boundary) {
			return SAME;
		} else if (tag <= SAME_LOCALS_1_STACK_ITEM.boundary) {
			return SAME_LOCALS_1_STACK_ITEM;
		} else if (tag == SAME_LOCALS_1_STACK_ITEM_EXT.boundary) {
			return SAME_LOCALS_1_STACK_ITEM_EXT;
		} else if (tag > CHOP.boundary - 3 && tag <= CHOP.boundary) {
			return CHOP;
		} else if (tag == SAME_EXT.boundary) {
			return SAME_EXT;
		} else if (tag > APPEND.boundary - 3 && tag <= APPEND.boundary) {
			return APPEND;
		} else if (tag == FULL.boundary) {
			return FULL;
		} else {
			throw new InvalidByteCodeException("Unsupported StackMapFrame tag: " + tag);
		}
	}

	private final int boundary;

	StackFrameType(int boundary) {
		this.boundary = boundary;
	}

	public int boundary() {
		return boundary;
	}
}
