/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.constants;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import me.sedlar.bytecode.structure.ConstantPool;
import me.sedlar.bytecode.structure.InvalidByteCodeException;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 */
public class ConstantUtf8Info extends ConstantPool {

	private String string;

	@Override
	public byte tag() {
		return CONSTANT_UTF8;
	}

	@Override
	public String verboseTag() {
		return CONSTANT_UTF8_VERBOSE;
	}

	@Override
	public String verbose() throws InvalidByteCodeException {
		return string;
	}

	/**
	 * Get the byte array of the string in this entry.
	 *
	 * @return the array
	 */
	public byte[] bytes() {
		return string.getBytes();
	}

	/**
	 * Get the string in this entry.
	 *
	 * @return the string
	 */
	public String string() {
		return string;
	}

	/**
	 * Set the string in this entry.
	 *
	 * @param string
	 *            the string
	 */
	public void setString(String string) {
		this.string = string;
	}

	@Override
	public void read(DataInput in) throws InvalidByteCodeException, IOException {
		string = in.readUTF();
		if (debug)
			debug("read ");
	}

	@Override
	public void write(DataOutput out) throws InvalidByteCodeException, IOException {
		out.writeByte(CONSTANT_UTF8);
		out.writeUTF(string);
		if (debug)
			debug("wrote ");
	}

	@Override
	protected void debug(String message) {
		super.debug(message + verboseTag() + " with length " + string.length() + " (\"" + string + "\")");
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof ConstantUtf8Info))
			return false;
		ConstantUtf8Info constantUtf8Info = (ConstantUtf8Info) object;
		return super.equals(object) && constantUtf8Info.string.equals(string);
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ string.hashCode();
	}
}
