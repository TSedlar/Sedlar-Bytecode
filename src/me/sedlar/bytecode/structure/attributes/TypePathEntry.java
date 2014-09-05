/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.AbstractStructure;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class TypePathEntry extends AbstractStructure {

	private TypePathKind typePathKind;
	private int typeArgumentIndex;

	public TypePathKind kind() {
		return typePathKind;
	}

	public void setKind(TypePathKind typePathKind) {
		this.typePathKind = typePathKind;
	}

	public int argumentIndex() {
		return typeArgumentIndex;
	}

	public void setArgumentIndex(int typeArgumentIndex) {
		this.typeArgumentIndex = typeArgumentIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		typePathKind = TypePathKind.getFromTag(in.readUnsignedByte());
		typeArgumentIndex = in.readUnsignedByte();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeByte(typePathKind.tag());
		out.writeByte(typeArgumentIndex);
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return "";
	}
}
