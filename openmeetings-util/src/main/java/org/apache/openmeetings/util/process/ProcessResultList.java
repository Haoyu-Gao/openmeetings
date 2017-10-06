/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License") +  you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.openmeetings.util.process;

import static org.apache.openmeetings.util.OpenmeetingsVariables.getWebAppRootKey;
import static org.red5.logging.Red5LoggerFactory.getLogger;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;

/**
 *
 * Collects all results of conversion jobs
 *
 * @author sebawagner
 *
 */
public class ProcessResultList {
	private static final Logger log = getLogger(ProcessResultList.class, getWebAppRootKey());

	private Map<String, ProcessResult> jobs = new LinkedHashMap<>();

	public ProcessResult addItem(String name, ProcessResult processResult) {
		if (jobs.containsKey(name)) {
			log.error("Duplicate key in jobslist:: " + name);
			return null;
		}
		return jobs.put(name, processResult);
	}

	/**
	 * returns true if there was an job with exitValue "-1"
	 *
	 * @return
	 */
	public boolean hasError() {
		for (Entry<String, ProcessResult> entry : jobs.entrySet()) {
			if (!entry.getValue().isOk()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * converts all job results into a human readable format
	 *
	 * @return
	 */
	public String getLogMessage() {
		StringBuilder logMessage = new StringBuilder();
		for (Entry<String, ProcessResult> entry : jobs.entrySet()) {
			logMessage.append("key: ");
			logMessage.append(entry.getKey());
			logMessage.append("\r\n");
			logMessage.append(entry.getValue().buildLogMessage());
		}
		return logMessage.toString();
	}

	public int size() {
		return jobs.size();
	}

	public Map<String, ProcessResult> getJobs() {
		return jobs;
	}
}