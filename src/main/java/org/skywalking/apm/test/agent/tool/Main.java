package org.skywalking.apm.test.agent.tool;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.skywalking.apm.test.agent.tool.report.entity.Report;
import org.skywalking.apm.test.agent.tool.report.entity.TestCase;
import org.skywalking.apm.test.agent.tool.report.entity.TestCaseDesc;
import org.skywalking.apm.test.agent.tool.validator.assertor.DataAssert;
import org.skywalking.apm.test.agent.tool.validator.exception.AssertFailedException;
import org.skywalking.apm.test.agent.tool.validator.entity.Data;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        logger.info("Begin to validate data.");

        Report report = new Report();
        String testCasePath = ConfigHelper.testCaseBaseDir();

        if (ConfigHelper.isV2()) {
            TestCaseDesc desc = TestCaseDesc.Parser.parse(new File(testCasePath, "testcase.desc"));
            verify(new File(testCasePath), desc, report);
        } else {
            String[] testCases = ConfigHelper.testCases().split(",");

            for (String aCase : testCases) {
                File casePath = new File(testCasePath, aCase);
                File descFile = new File(casePath, "testcase.desc");
                verify(casePath, TestCaseDesc.Parser.parse(descFile), report);
            }
        }

        try {
            report.generateReport(ConfigHelper.reportFilePath());
        } catch (Exception e) {
            logger.error("Failed to generate report file", e);
        }
    }

    static void verify(File casePath, TestCaseDesc testCaseDesc, Report report) {
        TestCase testCase = new TestCase(testCaseDesc.getTestComponents());
        try {
            logger.info("start to assert data of test case[{}]", testCase.getCaseName());
            File actualData = new File(casePath, "actualData.yaml");
            File expectedData = new File(casePath, "expectedData.yaml");

            if (actualData.exists() && expectedData.exists()) {
                try {
                    DataAssert.assertEquals(Data.Loader.loadData("expectedData.yaml", expectedData),
                            Data.Loader.loadData("actualData.yaml", actualData));
                    testCase.testedSuccessfully();
                } catch (AssertFailedException e) {
                    logger.error("\nassert failed.\n{}\n", e.getCauseMessage());
                }
            } else {
                logger.error("assert failed. because actual data {} and expected data {}", actualData.exists() ? "founded" : "not founded", actualData.exists() ? "founded" : "not founded");
            }
        } catch (Exception e) {
            logger.error("assert test case {} failed.", testCase.getCaseName(), e);
        }
        report.addTestCase(testCaseDesc.getProjectName(), testCaseDesc.getTestFramework(), testCase);
    }

}
