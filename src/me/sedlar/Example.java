/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar;

import java.io.File;
import java.util.Map;

import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.util.JarArchive;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 **/
public class Example {

	public static void main(String[] args) throws Exception {
		JarArchive archive = new JarArchive(new File("./example.jar"));
		Map<String, ClassInfo> classes = archive.build();
		classes.values().forEach(ci -> ci.methods().forEach(mi -> mi.instructions().debug(mi)));
	}
}