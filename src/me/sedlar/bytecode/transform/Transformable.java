/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.transform;

import java.util.Map;

import me.sedlar.bytecode.structure.ClassInfo;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public interface Transformable {

	public abstract void transform(Map<String, ClassInfo> classes);
}
