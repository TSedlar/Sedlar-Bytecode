/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure.attributes.target;

import me.sedlar.bytecode.structure.AbstractStructure;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public abstract class TargetInfo extends AbstractStructure {

	protected TargetInfo() {
	}

	public abstract int length();

	public abstract String verbose();

	@Override
	protected String verboseAccessFlags(int accessFlags) {
		return "";
	}
}
