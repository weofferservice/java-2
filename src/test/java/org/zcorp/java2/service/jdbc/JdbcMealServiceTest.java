package org.zcorp.java2.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import org.zcorp.java2.service.AbstractMealServiceTest;

import static org.zcorp.java2.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {
}
