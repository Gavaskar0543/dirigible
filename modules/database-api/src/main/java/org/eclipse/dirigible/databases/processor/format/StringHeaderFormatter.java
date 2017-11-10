/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
 */

package org.eclipse.dirigible.databases.processor.format;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class StringHeaderFormatter.
 */
public class StringHeaderFormatter implements HeaderFormatter<String>{

	/* (non-Javadoc)
	 * @see org.eclipse.dirigible.databases.processor.format.HeaderFormatter#write(java.util.List)
	 */
	@Override
	public String write(List<ColumnDescriptor> columnDescriptors){
		StringBuilder headerSb = new StringBuilder();
		if(columnDescriptors.size()>0)
			headerSb.append(ResultSetMonospacedWriter.DELIMITER);
		for (ColumnDescriptor columnDescriptor : columnDescriptors) {
			String lbl = String.format("%-"+columnDescriptor.getDisplaySize()+"s", columnDescriptor.getLabel());
			headerSb.append(lbl);
			headerSb.append(ResultSetMonospacedWriter.DELIMITER);
			lbl = "";
		}		
		int headerlength = headerSb.length();
		headerSb.append(ResultSetMonospacedWriter.NEWLINE_CHARACTER);

		for (int i = 0; i < headerlength; i++) {
			headerSb.append("-");	
		}
		headerSb.append(ResultSetMonospacedWriter.NEWLINE_CHARACTER);
		return headerSb.toString();
	}
	
}
