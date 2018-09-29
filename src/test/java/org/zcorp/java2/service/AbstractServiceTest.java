package org.zcorp.java2.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.zcorp.java2.ActiveDbProfileResolver;
import org.zcorp.java2.Profiles;
import org.zcorp.java2.TimingExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.zcorp.java2.util.ValidationUtil.getRootCause;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration
@SpringJUnitConfig(locations = {
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
@ExtendWith(TimingExtension.class)
public abstract class AbstractServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private Environment env;

    public boolean isJpaBased() {
//        return Arrays.stream(env.getActiveProfiles()).noneMatch(Profiles.JDBC::equals);
        return env.acceptsProfiles(Profiles.JPA, Profiles.DATAJPA);
    }

    //Check root cause in JUnit: https://github.com/junit-team/junit4/pull/778
    public <T extends Throwable> void validateRootCause(Runnable runnable, Class<T> exceptionClass) {
        assertThrows(
                exceptionClass,
                () -> {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        throw getRootCause(e);
                    }
                },
                "Expected " + exceptionClass.getName()
        );
    }

}
