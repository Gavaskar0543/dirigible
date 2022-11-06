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
package org.eclipse.dirigible.components.jobs.domain;

import org.eclipse.dirigible.components.base.artefact.Artefact;
import javax.persistence.*;

/**
 * The Class JobEmail.
 */
@Entity
@Table(name = "DIRIGIBLE_JOB_EMAILS")
public class JobEmail extends Artefact {

    /** The Constant ARTEFACT_TYPE. */
    public static final String ARTEFACT_TYPE = "jobEmail";

    /** The id. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JOBEMAIL_ID", nullable = false)
    private Long id;

    /** The job name. */
    @Column(name = "JOBEMAIL_JOB_NAME", columnDefinition = "VARCHAR", nullable = false, length = 255)
    private String jobName;

    /** The name. */
    @Column(name = "JOBEMAIL_EMAIL", columnDefinition = "VARCHAR", nullable = false, length = 255)
    private String email;

    /**
     * Instantiates a new job email.
     */
    public JobEmail() {
        super();
    }

    /**
     * Instantiates a new job email.
     *
     * @param location the location
     * @param name the name
     * @param type the type
     * @param description the description
     * @param dependencies the dependencies
     * @param id the id
     * @param jobName the job name
     * @param email the email
     */
    public JobEmail(String location, String name, String type, String description, String dependencies, Long id, String jobName, String email) {
        super(location, name, type, description, dependencies);
        this.id = id;
        this.jobName = jobName;
        this.email = email;
    }

    /**
     * Gets the id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the job name.
     *
     * @return the job name
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets the job name.
     *
     * @param jobName the new job name
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "JobEmail{" +
                "id=" + id +
                ", jobName='" + jobName + '\'' +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", key='" + key + '\'' +
                ", dependencies='" + dependencies + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", updatedBy=" + updatedBy +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
