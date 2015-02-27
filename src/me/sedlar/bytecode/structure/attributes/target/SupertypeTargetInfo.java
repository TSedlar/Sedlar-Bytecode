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

import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class SupertypeTargetInfo extends TargetInfo {

	private int supertypeIndex;

	public int index() {
		return supertypeIndex;
	}

	public void setIndex(int supertypeIndex) {
		this.supertypeIndex = supertypeIndex;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		super.read(in);
		supertypeIndex = in.readUnsignedShort();
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		super.write(out);
		out.writeShort(supertypeIndex);
	}

	@Override
	public int length() {
		return 2;
	}

	@Override
	public String verbose() {
		if (supertypeIndex == 65535) {
			return "Super class (" + supertypeIndex + ")";
		} else {
			return "<a href=\"I" + supertypeIndex + "\">interface index " + supertypeIndex + "</a>";
		}
	}
}
