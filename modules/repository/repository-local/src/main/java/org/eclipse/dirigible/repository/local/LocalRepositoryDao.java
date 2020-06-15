/**
 * Copyright (c) 2010-2020 SAP and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v20.html
 *
 * Contributors:
 *   SAP - initial API and implementation
 */
package org.eclipse.dirigible.repository.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.dirigible.api.v3.security.UserFacade;
import org.eclipse.dirigible.commons.api.helpers.ContentTypeHelper;
import org.eclipse.dirigible.commons.api.helpers.FileSystemUtils;
import org.eclipse.dirigible.repository.api.RepositoryWriteException;
import org.eclipse.dirigible.repository.fs.FileSystemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Local Repository Dao.
 */
public class LocalRepositoryDao {

	private static final Logger logger = LoggerFactory.getLogger(LocalRepositoryDao.class);

	private static final String LAST = "last";

	private static final String MODIFIED_AT = "modifiedAt";

	private static final String MODIFIED_BY = "modifiedBy";

	private static final String CREATED_AT = "createdAt";

	private static final String CREATED_BY = "createdBy";

	static final int OBJECT_TYPE_FOLDER = 0;

	static final int OBJECT_TYPE_DOCUMENT = 1;

	static final int OBJECT_TYPE_BINARY = 2;

	private FileSystemRepository repository;

	/**
	 * Instantiates a new local repository dao.
	 *
	 * @param repository
	 *            the repository
	 */
	public LocalRepositoryDao(FileSystemRepository repository) {
		this.repository = repository;
	}

	/**
	 * Gets the repository.
	 *
	 * @return the repository
	 */
	public FileSystemRepository getRepository() {
		return this.repository;
	}

	/**
	 * Creates the file.
	 *
	 * @param path
	 *            the path
	 * @param content
	 *            the content
	 * @param isBinary
	 *            the is binary
	 * @param contentType
	 *            the content type
	 * @throws LocalRepositoryException
	 *             the local repository exception
	 */
	public void createFile(String path, byte[] content, boolean isBinary, String contentType) throws LocalRepositoryException {
		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			FileSystemUtils.saveFile(workspacePath, content);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}

	}

	/**
	 * Check initialized.
	 */
	public void checkInitialized() {
		// TODO Auto-generated method stub

	}

	/**
	 * Sets the file content.
	 *
	 * @param localFile
	 *            the local file
	 * @param content
	 *            the content
	 */
	public void setFileContent(LocalFile localFile, byte[] content) {
		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(getRepository(), localFile.getPath());
			FileSystemUtils.saveFile(workspacePath, content);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}
	}

	/**
	 * Gets the file content.
	 *
	 * @param localFile
	 *            the local file
	 * @return the file content
	 */
	public byte[] getFileContent(LocalFile localFile) {
		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(getRepository(), localFile.getPath());
			return FileSystemUtils.loadFile(workspacePath);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}
	}

	/**
	 * Rename file.
	 *
	 * @param path
	 *            the path
	 * @param newPath
	 *            the new path
	 */
	public void renameFile(String path, String newPath) {
		try {
			String workspacePathOld = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			String workspacePathNew = LocalWorkspaceMapper.getMappedName(getRepository(), newPath);
			FileSystemUtils.moveFile(workspacePathOld, workspacePathNew);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}
	}

	/**
	 * Copy file.
	 *
	 * @param path
	 *            the path
	 * @param newPath
	 *            the new path
	 */
	public void copyFile(String path, String newPath) {
		try {
			String workspacePathOld = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			String workspacePathNew = LocalWorkspaceMapper.getMappedName(getRepository(), newPath);
			FileSystemUtils.copyFile(workspacePathOld, workspacePathNew);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}
	}

	/**
	 * Removes the file by path.
	 *
	 * @param path
	 *            the path
	 */
	public void removeFileByPath(String path) {
		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			FileSystemUtils.removeFile(workspacePath);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}
	}

	/**
	 * Removes the folder by path.
	 *
	 * @param path
	 *            the path
	 */
	public void removeFolderByPath(String path) {
		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			FileSystemUtils.removeFile(workspacePath);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}
	}

	/**
	 * Creates the folder.
	 *
	 * @param normalizePath
	 *            the normalize path
	 */
	public void createFolder(String normalizePath) {
		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(getRepository(), normalizePath);
			FileSystemUtils.createFolder(workspacePath);
		} catch (RepositoryWriteException e) {
			throw new LocalRepositoryException(e);
		}
	}

	/**
	 * Rename folder.
	 *
	 * @param path
	 *            the path
	 * @param newPath
	 *            the new path
	 */
	public void renameFolder(String path, String newPath) {
		try {
			String workspacePathOld = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			String workspacePathNew = LocalWorkspaceMapper.getMappedName(getRepository(), newPath);
			FileSystemUtils.moveFile(workspacePathOld, workspacePathNew);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}

	}

	/**
	 * Copy folder.
	 *
	 * @param path
	 *            the path
	 * @param newPath
	 *            the new path
	 */
	public void copyFolder(String path, String newPath) {
		try {
			String workspacePathOld = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			String workspacePathNew = LocalWorkspaceMapper.getMappedName(getRepository(), newPath);
			FileSystemUtils.copyFolder(workspacePathOld, workspacePathNew);
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}

	}

	/**
	 * Gets the object by path.
	 *
	 * @param path
	 *            the path
	 * @return the object by path
	 */
	public LocalObject getObjectByPath(String path) {

		LocalObject localObject = null;

		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			File objectFile = new File(workspacePath);
			if (!objectFile.exists()) {
				// This is folder, that was not created
				if (ContentTypeHelper.getExtension(workspacePath).isEmpty() && !workspacePath.endsWith(".")) {
					return null;
				}
			}
			if (objectFile.isFile()) {
				String contentType = ContentTypeHelper.getContentType(FileSystemUtils.getExtension(workspacePath));
				localObject = new LocalFile(repository, ContentTypeHelper.isBinary(contentType), contentType);
			} else {
				localObject = new LocalFolder(repository);
			}
			localObject.setName(objectFile.getName());
			localObject.setPath(workspacePath);
			localObject.setModifiedAt(new Date(objectFile.lastModified()));
			String owner;
			try {
				owner = Files.getOwner(objectFile.toPath()).getName();
			} catch (Exception e) {
				owner = "SYSTEM";
			}
			localObject.setModifiedBy(owner);

		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}
		return localObject;

	}

	/**
	 * Gets the children by folder.
	 *
	 * @param path
	 *            the path
	 * @return the children by folder
	 */
	public List<LocalObject> getChildrenByFolder(String path) {
		List<LocalObject> localObjects = new ArrayList<LocalObject>();
		try {
			String workspacePath = LocalWorkspaceMapper.getMappedName(getRepository(), path);
			File objectFile = new File(workspacePath);
			if (objectFile.isDirectory()) {
				File[] children = FileSystemUtils.listFiles(objectFile);
				if (children != null) {
					for (File file : children) {
						localObjects.add(getObjectByPath(file.getAbsolutePath()));
					}
				}
			}
		} catch (IOException e) {
			throw new LocalRepositoryException(e);
		}
		return localObjects;
	}
	
	/**
	 * Gets the user.
	 *
	 * @return the user
	 */
	private String getUser() {
		return UserFacade.getName();
	}

}
