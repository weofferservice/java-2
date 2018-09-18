package org.zcorp.java2;

import org.junit.rules.ExternalResource;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class TimingRules {
    private static final Logger log = getLogger("result");

    private static final StringBuilder results = new StringBuilder();

    //https://stackoverflow.com/questions/14892125/what-is-the-best-practice-to-determine-the-execution-time-of-the-business-releva
    public static final Stopwatch STOPWATCH = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String result = String.format("%-95s %7d", description.getDisplayName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result).append('\n');
            log.info(result + " ms\n");
        }
    };

    public static final ExternalResource SUMMARY = new ExternalResource() {
        @Override
        protected void before() {
            results.setLength(0);
        }

        @Override
        protected void after() {
            log.info("\n-------------------------------------------------------------------------------------------------------" +
                    "\nTest                                                                                       Duration, ms" +
                    "\n-------------------------------------------------------------------------------------------------------\n" +
                    results +
                    "-------------------------------------------------------------------------------------------------------\n");
        }
    };
}
