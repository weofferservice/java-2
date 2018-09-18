package org.zcorp.java2.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import org.zcorp.java2.service.AbstractUserServiceTest;

import static org.zcorp.java2.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
}
