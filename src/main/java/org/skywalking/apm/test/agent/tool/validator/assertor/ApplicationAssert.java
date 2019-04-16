package org.skywalking.apm.test.agent.tool.validator.assertor;

import java.util.List;
import org.skywalking.apm.test.agent.tool.validator.entity.RegistryApplication;
import org.skywalking.apm.test.agent.tool.validator.assertor.exception.RegistryApplicationNotFoundException;

public class ApplicationAssert {
    public static void assertEquals(List<RegistryApplication> expected,
        List<RegistryApplication> actual) {

        if (expected == null) {
            return;
        }

        for (RegistryApplication application : expected) {
            RegistryApplication actualApplication = getMatchApplication(actual, application);
            ExpressParser.parse(application.expressValue()).assertValue("registry application", actualApplication.expressValue());
        }
    }

    private static RegistryApplication getMatchApplication(List<RegistryApplication> actual,
        RegistryApplication application) {
        for (RegistryApplication registryApplication : actual) {
            if (registryApplication.applicationCode().equals(application.applicationCode())) {
                return registryApplication;
            }
        }
        throw new RegistryApplicationNotFoundException(application.applicationCode());
    }
}
