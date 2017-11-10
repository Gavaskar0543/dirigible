/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.repository.api;

// TODO: Auto-generated Javadoc
/**
 * The Class RepositoryCreationException.
 */
public class RepositoryCreationException extends RepositoryException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -163847774919514248L;

	/**
	 * Instantiates a new repository creation exception.
	 */
	public RepositoryCreationException() {
		super();
	}

	/**
	 * Instantiates a new repository creation exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public RepositoryCreationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new repository creation exception.
	 *
	 * @param message the message
	 */
	public RepositoryCreationException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new repository creation exception.
	 *
	 * @param cause the cause
	 */
	public RepositoryCreationException(Throwable cause) {
		super(cause);
	}

}
