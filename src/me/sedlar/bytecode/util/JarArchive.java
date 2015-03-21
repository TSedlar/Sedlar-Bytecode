/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the license, or (at your option) any later version.
 */
package me.sedlar.bytecode.util;

import me.sedlar.bytecode.structure.ClassInfo;
import me.sedlar.bytecode.structure.InvalidByteCodeException;
import me.sedlar.bytecode.structure.MethodInfo;
import me.sedlar.util.io.Streams;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * @author <a href="mailto:t@sedlar.me">Tyler Sedlar</a>
 */
public class JarArchive {

	private final File file;
	private final long size;
	private final boolean keepResources;

	private final Map<String, ClassInfo> classes = new HashMap<>();
	private final Map<String, byte[]> resources = new HashMap<>();

	private long nanos;

	/**
	 * An archive that is used for gathering ClassInfo data.
	 *
	 * @param file          a jar file on the local system
	 * @param keepResources <t>true</t> to keep files outside of .class, otherwise <t>false</t>
	 */
	public JarArchive(File file, boolean keepResources) {
		if (!file.getName().endsWith(".jar"))
			throw new IllegalArgumentException("File is not a jar");
		this.file = file;
		this.size = file.length();
		this.keepResources = keepResources;
	}

	/**
	 * An archive that is used for gathering ClassInfo data.
	 *
	 * @param file a jar file on the local system
	 */
	public JarArchive(File file) {
		this(file, true);
	}

	/**
	 * Nanoseconds spent during the build process
	 *
	 * @return nanoseconds spent during the build process
	 */
	public long nanos() {
		return nanos;
	}

    /**
     * Adds bytes to either resources or classes.
     *
     * @param entryName a String that is the name of an entry to be added from the jar file
     * @param inputStream an InputStream to be added to the HashMap with the entryName
     */
    private void addInfo(String entryName, InputStream inputStream) {
        byte[] bytes = Streams.binary(inputStream);
        ClassInfo classInfo;
        try {
            classInfo = new ClassInfo(bytes);
        } catch (IOException|InvalidByteCodeException e) {
            if (keepResources)
                resources.put(entryName, bytes);
            return;
        }
        classes.put(entryName, classInfo);

    }

    /**
     * Get a map of every class within the given jar.
     *
     * @param parallel a boolean that decides if the classes and resources should be built in parallel
     *
     * @return a map of every class within the given jar
     */
    public Map<String, ClassInfo> build(boolean parallel) {
        if (!classes.isEmpty())
            return classes;
        long start = System.nanoTime();
        try (JarFile jar = new JarFile(file)) {
            ConcurrentHashMap<String, InputStream> entries = new ConcurrentHashMap();
            for (JarEntry entry : Collections.list(jar.entries())) {
                String entryName = entry.getName();
                if (entryName.endsWith(".class")) {
                    entryName = entryName.replace(".class", "");
                    classes.put(entryName, null);
                } else if (keepResources) {
                    resources.put(entryName, null);
                }
                entries.put(entryName, jar.getInputStream(entry));
            }
            entries.forEach((parallel ? 1 : Long.MAX_VALUE), (key, value) -> addInfo(key, value));
            if (entries.size() != (classes.size() + resources.size())) {
                classes.clear();
                resources.clear();
                return null;
            }
        } catch (IOException e) {
            return null;
        }
        long end = System.nanoTime();
        nanos = (end - start);
        return classes;
    }

	/**
	 * Get a map of every class within the given jar.
	 *
	 * @return a map of every class within the given jar
	 */
	public Map<String, ClassInfo> build() {
        return build(false);
    }

	/**
	 * Gets the file-size of the given archive.
	 *
	 * @return the file-size of the given archive.
	 */
	public long size() {
		return size;
	}

	/**
	 * Gets the field count for the archive.
	 *
	 * @return the field count for the archive.
	 */
	public int fieldCount() {
		int count = 0;
		for (ClassInfo ci : classes.values())
			count += ci.fields().size();
		return count;
	}

	/**
	 * Gets the method count for the archive.
	 *
	 * @return the method count for the archive.
	 */
	public int methodCount() {
		int count = 0;
		for (ClassInfo ci : classes.values())
			count += ci.methods().size();
		return count;
	}

	/**
	 * Gets the block count for the archive.
	 *
	 * @return the block count for the archive.
	 */
	public int blockCount() {
		int count = 0;
		for (ClassInfo ci : classes.values()) {
			for (MethodInfo mi : ci.methods())
				count += mi.graph().size();
		}
		return count;
	}

	/**
	 * Dumps the archive to a new file.
	 *
	 * @param file the file to dump to.
	 * @return the file-size of the newly dumped jar.
	 */
	public long dump(File file) {
		try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(file))) {
			for (ClassInfo ci : classes.values()) {
				jos.putNextEntry(new JarEntry(ci.name() + ".class"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				DataOutputStream dos = new DataOutputStream(baos);
				ci.write(dos);
				byte[] bytes = baos.toByteArray();
				jos.write(bytes, 0, bytes.length);
				jos.closeEntry();
			}
			if (keepResources) {
				for (Entry<String, byte[]> entry : resources.entrySet()) {
					jos.putNextEntry(new JarEntry(entry.getKey()));
					byte[] bytes = entry.getValue();
					jos.write(bytes, 0, bytes.length);
					jos.closeEntry();
				}
			}
			jos.flush();
			return file.length();
		} catch (IOException | InvalidByteCodeException e) {
			e.printStackTrace();
			return -1L;
		}
	}
}
