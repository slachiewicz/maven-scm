/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
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
package org.apache.maven.scm.provider.hg.command.status;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.scm.ScmFile;
import org.apache.maven.scm.ScmFileStatus;
import org.apache.maven.scm.provider.hg.command.HgConsumer;

/**
 * @author <a href="mailto:thurner.rupert@ymono.net">thurner rupert</a>
 *
 */
class HgStatusConsumer extends HgConsumer {
    private final List<ScmFile> repositoryStatus = new ArrayList<>();

    private final File workingDir;

    HgStatusConsumer(File workingDir) {
        this.workingDir = workingDir;
    }

    /** {@inheritDoc} */
    public void doConsume(ScmFileStatus status, String trimmedLine) {
        // Only include real files (not directories)
        File tmpFile = new File(workingDir, trimmedLine);
        if (!tmpFile.exists()) {
            if (logger.isInfoEnabled()) {
                logger.info("Not a file: " + tmpFile + ". Ignoring");
            }
        } else if (tmpFile.isDirectory()) {
            if (logger.isInfoEnabled()) {
                logger.info("New directory added: " + tmpFile);
            }
        } else {
            ScmFile scmFile = new ScmFile(trimmedLine, status);
            if (logger.isInfoEnabled()) {
                logger.info(scmFile.toString());
            }
            repositoryStatus.add(scmFile);
        }
    }

    List<ScmFile> getStatus() {
        return repositoryStatus;
    }
}
