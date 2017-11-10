/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.core.git.project;

import com.google.gson.Gson;

// TODO: Auto-generated Javadoc
/**
 * The Class ProjectMetadataUtils.
 */
public class ProjectMetadataUtils {

	/** The gson. */
	private static Gson gson = new Gson();

	/**
	 * To json.
	 *
	 * @param projectMetadata the project metadata
	 * @return the string
	 */
	public static String toJson(ProjectMetadata projectMetadata) {
		String json = gson.toJson(projectMetadata);
		return json;
	}

	/**
	 * From json.
	 *
	 * @param json the json
	 * @return the project metadata
	 */
	public static ProjectMetadata fromJson(String json) {
		ProjectMetadata projectMetadata = gson.fromJson(json, ProjectMetadata.class);
		return projectMetadata;
	}

}
