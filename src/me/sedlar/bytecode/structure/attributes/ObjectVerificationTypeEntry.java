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

import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class ObjectVerificationTypeEntry extends VerificationTypeInfoEntry {

	private int cpIndex;

	public ObjectVerificationTypeEntry() {
		super(VerificationType.OBJECT);
	}

	public int index() {
		return cpIndex;
	}

	public void setIndex(int cpIndex) {
		this.cpIndex = cpIndex;
	}

	@Override
	public void readExtra(DataInput in) throws InvalidByteCodeException, IOException {
		super.readExtra(in);
		cpIndex = in.readUnsignedShort();
	}

	@Override
	public void writeExtra(DataOutput out) throws InvalidByteCodeException, IOException {
		super.writeExtra(out);
		out.writeShort(cpIndex);
	}

	@Override
	public void appendTo(StringBuilder buffer) {
		super.appendTo(buffer);
		buffer.append(" <a href=\"").append(cpIndex).append("\">cp_info #").append(cpIndex).append("</a> &lt;").append(
				verbose()).append("&gt;");
	}

	private String verbose() {
		try {
			return classInfo().constantPoolEntryName(cpIndex);
		} catch (InvalidByteCodeException e) {
			return "invalid constant pool index " + cpIndex;
		}
	}

	@Override
	public int length() {
		return super.length() + 2;
	}
}
