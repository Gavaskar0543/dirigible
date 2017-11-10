/*
 * Copyright (c) 2017 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * Contributors:
 * SAP - initial API and implementation
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

// TODO: Auto-generated Javadoc
/**
 * The Class PersistenceManagerTest.
 */
public class PersistenceManagerTest extends AbstractPersistenceManagerTest {

	/**
	 * Ordered crud tests.
	 *
	 * @throws SQLException the SQL exception
	 */
	@Test
	public void orderedCrudTests() throws SQLException {
		PersistenceManager<Customer> persistenceManager = new PersistenceManager<Customer>();
		Connection connection = getDataSrouce().getConnection();
		try {
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
			connection.close();
		}
	}

	/**
	 * Creates the table for pojo.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void createTableForPojo(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
		persistenceManager.tableCreate(connection, Customer.class);
	}

	/**
	 * Exists table.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @return true, if successful
	 * @throws SQLException the SQL exception
	 */
	public boolean existsTable(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
		return persistenceManager.tableExists(connection, Customer.class);
	}

	/**
	 * Insert pojo.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void insertPojo(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
		Customer customer = new Customer();
		customer.setId(1);
		customer.setFirstName("John");
		customer.setLastName("Smith");
		customer.setAge(33);
		persistenceManager.insert(connection, customer);
	}

	/**
	 * Insert second pojo.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void insertSecondPojo(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
		Customer customer = new Customer();
		customer.setId(2);
		customer.setFirstName("Jane");
		customer.setLastName("Smith");
		customer.setAge(32);
		persistenceManager.insert(connection, customer);
	}

	/**
	 * Find pojo.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void findPojo(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
		Customer customer = persistenceManager.find(connection, Customer.class, 1);
		assertEquals("John", customer.getFirstName());
	}

	/**
	 * Find all pojo.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void findAllPojo(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
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

	/**
	 * Query all.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void queryAll(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {

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

	/**
	 * Query by name.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void queryByName(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {

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

	/**
	 * Query by name var args.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void queryByNameVarArgs(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {

		String sql = SqlFactory.getNative(connection).select().column("*").from("CUSTOMERS").where("CUSTOMER_FIRST_NAME = ?").build();

		List<Customer> list = persistenceManager.query(connection, Customer.class, sql, "Jane");

		assertNotNull(list);
		assertFalse(list.isEmpty());
		assertEquals(1, list.size());
		Customer customer = list.get(0);
		assertEquals("Jane", customer.getFirstName());

	}

	/**
	 * Update pojo.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void updatePojo(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
		Customer customer = persistenceManager.find(connection, Customer.class, 1);

		assertEquals("John", customer.getFirstName());
		assertEquals("Smith", customer.getLastName());

		customer.setLastName("Wayne");

		int result = persistenceManager.update(connection, customer, 1);

		assertEquals(1, result);

		customer = persistenceManager.find(connection, Customer.class, 1);

		assertEquals("John", customer.getFirstName());
		assertEquals("Wayne", customer.getLastName());
	}

	/**
	 * Delete pojo.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void deletePojo(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
		Customer customer = persistenceManager.find(connection, Customer.class, 1);

		assertEquals("John", customer.getFirstName());

		int result = persistenceManager.delete(connection, Customer.class, 1);

		assertEquals(1, result);
	}

	/**
	 * Delete custom.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void deleteCustom(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
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

	/**
	 * Drop table for pojo.
	 *
	 * @param connection the connection
	 * @param persistenceManager the persistence manager
	 * @throws SQLException the SQL exception
	 */
	public void dropTableForPojo(Connection connection, PersistenceManager<Customer> persistenceManager) throws SQLException {
		persistenceManager.tableDrop(connection, Customer.class);
	}

}
