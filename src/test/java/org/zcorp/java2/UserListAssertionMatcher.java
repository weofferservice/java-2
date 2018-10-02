package org.zcorp.java2;

import org.assertj.core.matcher.AssertionMatcher;
import org.zcorp.java2.model.User;

import java.util.List;

public class UserListAssertionMatcher extends AssertionMatcher<List<User>> {

    private final List<User> expected;

    public UserListAssertionMatcher(List<User> expected) {
        this.expected = expected;
    }

    @Override
    public void assertion(List<User> actual) throws AssertionError {
        UserTestData.assertMatch(actual, expected);
    }

}