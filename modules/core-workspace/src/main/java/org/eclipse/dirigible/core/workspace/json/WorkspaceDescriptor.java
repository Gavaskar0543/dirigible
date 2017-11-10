/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.core.workspace.json;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class WorkspaceDescriptor.
 */
public class WorkspaceDescriptor {

	/** The name. */
	private String name;

	/** The path. */
	private String path;

	/** The type. */
	private String type = "workspace";

	/** The projects. */
	private List<ProjectDescriptor> projects = new ArrayList<ProjectDescriptor>();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the path.
	 *
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the path.
	 *
	 * @param path the new path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets the projects.
	 *
	 * @return the projects
	 */
	public List<ProjectDescriptor> getProjects() {
		return projects;
	}

	/**
	 * Sets the.
	 *
	 * @param projects the projects
	 */
	public void set(List<ProjectDescriptor> projects) {
		this.projects = projects;
	}

}
