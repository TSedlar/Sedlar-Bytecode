/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes.target;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.AbstractStructure;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class LocalVariableTarget extends AbstractStructure {

	public int startPc;
	public int length;
	public int index;

	public int start() {
		return startPc;
	}

	public void setStart(int startPc) {
		this.startPc = startPc;
	}

	public int length() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int index() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		startPc = in.readUnsignedShort();
		length = in.readUnsignedShort();
		index = in.readUnsignedShort();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(startPc);
		out.writeShort(length);
		out.writeShort(index);
	}

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return "";
	}
}
