/**
 * Copyright (c) 2010-2018 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.database.persistence.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.dirigible.database.persistence.PersistenceManager;
import org.eclipse.dirigible.database.sql.SqlFactory;
import org.junit.Test;

/**
 * The Persistence Manager Test.
 */
public class PersistenceManagerTest extends AbstractPersistenceManagerTest {

	/**
	 * Ordered CRUD tests.
	 *
	 * @throws SQLException
	 *             the SQL exception
	 */
	@Test
	public void orderedCrudTests() throws SQLException {
		PersistenceManager<Customer> persistenceManager = new PersistenceManager<Customer>();
		Connection connection = null;
		try {
			connection = getDataSource().getConnection();
			// create table
			createTableForPojo(connection, persistenceManager);
			// check whether it is created successfully
			assertTrue(existsTable(connection, persistenceManager));
			// insert a record in the table for a pojo
			insertPojo(connection, persistenceManager);
			// retreive the record by the primary key
			findPojo(connection, persistenceManager);
			// get the list of all the records
			findAllPojo(connection, persistenceManager);
			// make a simple custom query
			queryAll(connection, persistenceManager);
			// make a bit more complicated query
			queryByName(connection, persistenceManager);
			// more complicated query with var args
			queryByNameVarArgs(connection, persistenceManager);
			// update one record
			updatePojo(connection, persistenceManager);
			// delete one record
			deletePojo(connection, persistenceManager);
			// delete one record by custom script
			deleteCustom(connection, persistenceManager);
			// drop the table
			dropTableForPojo(connection, persistenceManager);
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private void createTableForPojo(Connection connection, PersistenceManager<Customer> persistenceManager) {
		persistenceManager.tableCreate(connection, Customer.class);
	}

	private boolean existsTable(Connection connection, PersistenceManager<Customer> persistenceManager) {
		return persistenceManager.tableExists(connection, Customer.class);
	}

	private void insertPojo(Connection connection, PersistenceManager<Customer> persistenceManager) {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setFirstName("John");
		customer.setLastName("Smith");
		customer.setAge(33);
		persistenceManager.insert(connection, customer);
	}

	private void insertSecondPojo(Connection connection, PersistenceManager<Customer> persistenceManager) {
		Customer customer = new Customer();
		customer.setId(2);
		customer.setFirstName("Jane");
		customer.setLastName("Smith");
		customer.setAge(32);
		persistenceManager.insert(connection, customer);
	}

	private void findPojo(Connection connection, PersistenceManager<Customer> persistenceManager) {
		Customer customer = persistenceManager.find(connection, Customer.class, 1);
		assertEquals("John", customer.getFirstName());
	}

	private void findAllPojo(Connection connection, PersistenceManager<Customer> persistenceManager) {
		List<Customer> list = persistenceManager.findAll(connection, Customer.class);

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		Customer customer = list.get(0);
		assertEquals("John", customer.getFirstName());

		insertSecondPojo(connection, persistenceManager);

		list = persistenceManager.findAll(connection, Customer.class);

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		customer = list.get(1);
		assertEquals("Jane", customer.getFirstName());

	}

	private void queryAll(Connection connection, PersistenceManager<Customer> persistenceManager) {

		String sql = SqlFactory.getNative(connection).select().column("*").from("CUSTOMERS").build();

		List<Customer> list = persistenceManager.query(connection, Customer.class, sql);

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		Customer customer = list.get(0);
		assertEquals("John", customer.getFirstName());

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(2, list.size());
		customer = list.get(1);
		assertEquals("Jane", customer.getFirstName());

	}

	private void queryByName(Connection connection, PersistenceManager<Customer> persistenceManager) {

		String sql = SqlFactory.getNative(connection).select().column("*").from("CUSTOMERS").where("CUSTOMER_FIRST_NAME = ?").build();

		List<Object> values = new ArrayList<Object>();
		values.add("Jane");

		List<Customer> list = persistenceManager.query(connection, Customer.class, sql, values);

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		Customer customer = list.get(0);
		assertEquals("Jane", customer.getFirstName());

	}

	private void queryByNameVarArgs(Connection connection, PersistenceManager<Customer> persistenceManager) {

		String sql = SqlFactory.getNative(connection).select().column("*").from("CUSTOMERS").where("CUSTOMER_FIRST_NAME = ?").build();

		List<Customer> list = persistenceManager.query(connection, Customer.class, sql, "Jane");

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		Customer customer = list.get(0);
		assertEquals("Jane", customer.getFirstName());

	}

	private void updatePojo(Connection connection, PersistenceManager<Customer> persistenceManager) {
		Customer customer = persistenceManager.find(connection, Customer.class, 1);

		assertEquals("John", customer.getFirstName());
		assertEquals("Smith", customer.getLastName());

		customer.setId(1);
		customer.setLastName("Wayne");

		int result = persistenceManager.update(connection, customer);

		assertEquals(1, result);

		customer = persistenceManager.find(connection, Customer.class, 1);

		assertEquals("John", customer.getFirstName());
		assertEquals("Wayne", customer.getLastName());
	}

	private void deletePojo(Connection connection, PersistenceManager<Customer> persistenceManager) {
		Customer customer = persistenceManager.find(connection, Customer.class, 1);

		assertEquals("John", customer.getFirstName());

		int result = persistenceManager.delete(connection, Customer.class, 1);

		assertEquals(1, result);
	}

	private void deleteCustom(Connection connection, PersistenceManager<Customer> persistenceManager) {
		Customer customer = new Customer();
		customer.setId(3);
		customer.setFirstName("James");
		customer.setLastName("Smith");
		customer.setAge(34);
		persistenceManager.insert(connection, customer);

		customer = persistenceManager.find(connection, Customer.class, 3);

		assertEquals("James", customer.getFirstName());

		String sql = SqlFactory.getNative(connection).delete().from("CUSTOMERS").where("CUSTOMER_FIRST_NAME = ?").build();

		int result = persistenceManager.execute(connection, sql, "James");

		assertEquals(1, result);

		customer = persistenceManager.find(connection, Customer.class, 3);

		assertNull(customer);
	}

	private void dropTableForPojo(Connection connection, PersistenceManager<Customer> persistenceManager) {
		persistenceManager.tableDrop(connection, Customer.class);
	}

}
