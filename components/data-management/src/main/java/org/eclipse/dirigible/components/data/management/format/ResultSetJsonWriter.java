/*
 * Copyright (c) 2023 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * SPDX-FileCopyrightText: 2023 SAP SE or an SAP affiliate company and Eclipse Dirigible contributors
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.dirigible.components.data.management.format;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.apache.commons.lang3.ClassUtils;
import org.eclipse.dirigible.commons.api.helpers.GsonHelper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * The ResultSet JSON Writer.
 */
public class ResultSetJsonWriter extends AbstractResultSetWriter<String> {

	/** The limited. */
	private boolean limited = true;
	
	/** The stringify. */
	private boolean stringify = true;

	/**
	 * Checks if is limited.
	 *
	 * @return true, if is limited
	 */
	public boolean isLimited() {
		return limited;
	}

	/**
	 * Sets the limited.
	 *
	 * @param limited
	 *            the new limited
	 */
	public void setLimited(boolean limited) {
		this.limited = limited;
	}
	
	/**
	 * Checks if is stringified.
	 *
	 * @return true, if is stringified
	 */
	public boolean isStringified() {
		return stringify;
	}

	/**
	 * Sets the stringify.
	 *
	 * @param stringify
	 *            the new stringify
	 */
	public void setStringified(boolean stringify) {
		this.stringify = stringify;
	}

	/**
	 * Write.
	 *
	 * @param resultSet the result set
	 * @return the string
	 * @throws SQLException the SQL exception
	 */
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.dirigible.databases.processor.format.ResultSetWriter#write(java.sql.ResultSet)
	 */
	@Override
	public String write(ResultSet resultSet) throws SQLException {
		JsonArray records = new JsonArray();
		int count = 0;
		while (resultSet.next()) {
			ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
			JsonObject record = new JsonObject();
			for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
				String name = resultSetMetaData.getColumnName(i);
				Object value = resultSet.getObject(i);
				if (value == null
						&& stringify) {
					value = "[NULL]";
				}
				if (value != null && !ClassUtils.isPrimitiveOrWrapper(value.getClass())
						&& value.getClass() != String.class
						&& !java.util.Date.class.isAssignableFrom(value.getClass())) {
					if (stringify) {
						value = "[BINARY]";
					}
				}

				if (value instanceof String) {
					record.add(name, value == null ? null : new JsonPrimitive((String) value));
				} else if (value instanceof Character) {
					record.add(name, value == null ? null : new JsonPrimitive(new String(new char[] { (char) value })));
				} else if (value instanceof Float) {
					record.add(name, value == null ? null : new JsonPrimitive((Float) value));
				} else if (value instanceof Double) {
					record.add(name, value == null ? null : new JsonPrimitive((Double) value));
				} else if (value instanceof BigDecimal) {
					record.add(name, value == null ? null : new JsonPrimitive((BigDecimal) value));
				} else if (value instanceof Long) {
					record.add(name, value == null ? null : new JsonPrimitive((Long) value));
				} else if (value instanceof BigInteger) {
					record.add(name, value == null ? null : new JsonPrimitive((BigInteger) value));
				} else if (value instanceof Integer) {
					record.add(name, value == null ? null : new JsonPrimitive((Integer) value));
				} else if (value instanceof Byte) {
					record.add(name, value == null ? null : new JsonPrimitive((Byte) value));
				} else if (value instanceof Short) {
					record.add(name, value == null ? null : new JsonPrimitive((Short) value));
				} else if (value instanceof Boolean) {
					record.add(name, value == null ? null : new JsonPrimitive((Boolean) value));
				} else {
					record.add(name, value == null ? null : new JsonPrimitive(value.toString()));
				}
			}

			records.add(record);

			if (this.isLimited() && (++count > getLimit())) {
				break;
			}
		}

		return GsonHelper.toJson(records);
	}

}
