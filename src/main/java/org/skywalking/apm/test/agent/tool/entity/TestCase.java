/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.skywalking.apm.test.agent.tool.entity;

public class TestCase {
    private String caseName;
    private boolean success;
    private String testFramework;
    private String projectName;

    public TestCase(TestCaseDesc caseDesc) {
        this.caseName = String.join("-", caseDesc.getTestFramework(), caseDesc.getTestVersion());
        this.testFramework = caseDesc.getTestFramework();
        this.projectName = caseDesc.getProjectName();
        this.success = false;
    }

    public String getCaseName() {
        return caseName;
    }

    public String getTestFramework() {
        return testFramework;
    }

    public boolean supported() {
        return this.success;
    }

    public String getProjectName() {
        return projectName;
    }

    public void testedSuccessfully() {
        this.success = true;
    }

    public boolean isSuccess() {
        return success;
    }
}
