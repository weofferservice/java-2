package org.zcorp.java2;

import org.junit.jupiter.api.extension.*;
import org.slf4j.Logger;
import org.springframework.util.StopWatch;

import static org.slf4j.LoggerFactory.getLogger;

public class TimingExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback, BeforeAllCallback, AfterAllCallback {

    private static final Logger log = getLogger("result");

    private StopWatch stopWatch;

    @Override
    public void beforeAll(ExtensionContext extensionContext) {
        stopWatch = new StopWatch("Execution time of " + extensionContext.getRequiredTestClass().getSimpleName());
    }

    @Override
    public void beforeTestExecution(ExtensionContext extensionContext) {
        log.info("Start - Task name '" + getTaskName(extensionContext) + "'");
        stopWatch.start(extensionContext.getDisplayName());
    }

    @Override
    public void afterTestExecution(ExtensionContext extensionContext) {
        stopWatch.stop();
        log.info("Stop - Task name '" + getTaskName(extensionContext) + "'");
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) {
        log.info('\n' + stopWatch.prettyPrint() + '\n');
    }

    private static String getTaskName(ExtensionContext extensionContext) {
        return extensionContext.getRequiredTestClass().getSimpleName() + "." + extensionContext.getDisplayName();
    }

}
