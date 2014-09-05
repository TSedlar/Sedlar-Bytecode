/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.structure;

import me.sedlar.bytecode.structure.values.ValuePair;

/**
 * @author <a href="mailto:jclasslib@ej-technologies.com">Ingo Kegel</a>
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public interface AnnotationData {

	/**
	 * Get the list of element variable pair associations of the parent
	 * structure as an array of <tt>ValuePair</tt> structure.
	 *
	 * @return the array
	 */
	public ValuePair[] pair();

	/**
	 * Get the <tt>type_index</tt> of this annotation.
	 *
	 * @return the <tt>type_index</tt>
	 */
	public int index();
}
