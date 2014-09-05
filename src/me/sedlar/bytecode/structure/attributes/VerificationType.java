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
public enum VerificationType {

	TOP(0),
	INTEGER(1),
	FLOAT(2),
	DOUBLE(3),
	LONG(4),
	ITEM_Null(5),
	NULL(6),
	OBJECT(7),
	UNINITIALIZED(8);

	public static VerificationType getFromTag(int tag) throws InvalidByteCodeException {
		VerificationType[] values = values();
		if (tag < 0 || tag >= values.length)
			throw new InvalidByteCodeException("Invalid verification tag " + tag);
		return values[tag];
	}

	private final int tag;

	VerificationType(int tag) {
		this.tag = tag;
	}

	public int tag() {
		return tag;
	}

	public VerificationTypeInfoEntry createEntry() {
		switch (this) {
			case OBJECT: {
				return new ObjectVerificationTypeEntry();
			}
			case UNINITIALIZED: {
				return new UninitializedVerificationTypeEntry();
			}
			default: {
				return new VerificationTypeInfoEntry(this);
			}
		}
	}
}
