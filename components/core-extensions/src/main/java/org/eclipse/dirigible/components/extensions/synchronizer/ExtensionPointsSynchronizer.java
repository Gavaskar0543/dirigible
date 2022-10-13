/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2022 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.components.extensions.synchronizer;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

import org.eclipse.dirigible.commons.api.helpers.GsonHelper;
import org.eclipse.dirigible.commons.api.topology.TopologicalDepleter;
import org.eclipse.dirigible.components.base.artefact.Artefact;
import org.eclipse.dirigible.components.base.artefact.ArtefactLifecycle;
import org.eclipse.dirigible.components.base.artefact.ArtefactService;
import org.eclipse.dirigible.components.base.artefact.ArtefactState;
import org.eclipse.dirigible.components.base.artefact.topology.TopologyWrapper;
import org.eclipse.dirigible.components.base.synchronizer.Synchronizer;
import org.eclipse.dirigible.components.base.synchronizer.SynchronizerCallback;
import org.eclipse.dirigible.components.extensions.domain.ExtensionPoint;
import org.eclipse.dirigible.components.extensions.service.ExtensionPointService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * The Class ExtensionPointsSynchronizer.
 *
 * @param <A> the generic type
 */
@Component
@Order(10)
public class ExtensionPointsSynchronizer<A extends Artefact> implements Synchronizer<ExtensionPoint> {
	
	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ExtensionPointsSynchronizer.class);
	
	/** The Constant FILE_EXTENSION_EXTENSIONPOINT. */
	public static final String FILE_EXTENSION_EXTENSIONPOINT = ".extensionpoint";
	
	/** The extension point service. */
	private ExtensionPointService extensionPointService;
	
	/**
	 * Instantiates a new extension points synchronizer.
	 *
	 * @param extensionPointService the extension point service
	 */
	@Autowired
	public ExtensionPointsSynchronizer(ExtensionPointService extensionPointService) {
		this.extensionPointService = extensionPointService;
	}
	
	/**
	 * Gets the service.
	 *
	 * @return the service
	 */
	@Override
	public ArtefactService<ExtensionPoint> getService() {
		return extensionPointService;
	}

	/**
	 * Checks if is accepted.
	 *
	 * @param file the file
	 * @param attrs the attrs
	 * @return true, if is accepted
	 */
	@Override
	public boolean isAccepted(Path file, BasicFileAttributes attrs) {
		return file.toString().endsWith(FILE_EXTENSION_EXTENSIONPOINT);
	}

	/**
	 * Checks if is accepted.
	 *
	 * @param artefact the artefact
	 * @return true, if is accepted
	 */
	@Override
	public boolean isAccepted(String type) {
		return ExtensionPoint.ARTEFACT_TYPE.equals(type);
	}

	/**
	 * Load.
	 *
	 * @param location the location
	 * @param content the content
	 * @return the list
	 */
	@Override
	public List load(String location, byte[] content) {
		ExtensionPoint extensionPoint = GsonHelper.GSON.fromJson(new String(content, StandardCharsets.UTF_8), ExtensionPoint.class);
		extensionPoint.setLocation(location);
		extensionPoint.setType(ExtensionPoint.ARTEFACT_TYPE);
		extensionPoint.updateKey();
		try {
			getService().save(extensionPoint);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {logger.error(e.getMessage(), e);}
			if (logger.isErrorEnabled()) {logger.error("extension point: {}", extensionPoint);}
			if (logger.isErrorEnabled()) {logger.error("content: {}", new String(content));}
		}
		return List.of(extensionPoint);
	}

	/**
	 * Prepare.
	 *
	 * @param wrappers the wrappers
	 * @param depleter the depleter
	 * @param callback the callback
	 */
	@Override
	public void prepare(List<TopologyWrapper<? extends Artefact>> wrappers, TopologicalDepleter<TopologyWrapper<? extends Artefact>> depleter,
			SynchronizerCallback callback) {
	}
	
	/**
	 * Process.
	 *
	 * @param wrappers the wrappers
	 * @param depleter the depleter
	 * @param callback the callback
	 */
	@Override
	public void process(List<TopologyWrapper<? extends Artefact>> wrappers, TopologicalDepleter<TopologyWrapper<? extends Artefact>> depleter,
			SynchronizerCallback callback) {
		try {
			List<TopologyWrapper<? extends Artefact>> results = depleter.deplete(wrappers, ArtefactLifecycle.CREATED.toString());
			callback.registerErrors(this, results, ArtefactLifecycle.CREATED, ArtefactState.FAILED_CREATE_UPDATE);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {logger.error(e.getMessage(), e);}
			callback.addError(e.getMessage());
		}
	}

	/**
	 * Complete.
	 *
	 * @param flow the flow
	 * @return true, if successful
	 */
	@Override
	public boolean complete(String flow) {
		// TODO successful state
		return true;
	}

	/**
	 * Cleanup.
	 *
	 * @param extensionPoint the extension point
	 * @param callback the callback
	 */
	@Override
	public void cleanup(ExtensionPoint extensionPoint, SynchronizerCallback callback) {
		try {
			getService().delete(extensionPoint);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {logger.error(e.getMessage(), e);}
			callback.addError(e.getMessage());
		}
	}

}