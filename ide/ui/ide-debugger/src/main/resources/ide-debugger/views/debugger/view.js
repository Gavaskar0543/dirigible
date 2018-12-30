/*
 * Copyright (c) 2010-2018 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
exports.getView = function() {
	var view = {
			"id":"debugger",
			"name":"Debugger",
			"factory":"frame",
			"region":"center-top",
			"label":"Debugger",
			"link":"../ide-debugger/views/debugger/index.html"
	};
	return view;
};