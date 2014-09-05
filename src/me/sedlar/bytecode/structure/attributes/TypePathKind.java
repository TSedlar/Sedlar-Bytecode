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
public enum TypePathKind {

	DEEPER_IN_ARRAY_TYPE(0),
	DEEPER_IN_NESTED_TYPE(1),
	WILDCARD_BOUND(2),
	TYPE_ARGUMENT(3);

	private static final TypePathKind[] TYPE_PATH_KINDS = new TypePathKind[values().length];

	static {
		for (int i = 0; i < values().length; i++)
			TYPE_PATH_KINDS[i] = values()[i];
	}

	public static TypePathKind getFromTag(int tag) throws InvalidByteCodeException {
		if (tag < 0 || tag >= TYPE_PATH_KINDS.length)
			throw new InvalidByteCodeException("Invalid type path kind: " + tag);
		return TYPE_PATH_KINDS[tag];
	}

	private int tag;

	TypePathKind(int tag) {
		this.tag = tag;
	}

	public int tag() {
		return tag;
	}
}
