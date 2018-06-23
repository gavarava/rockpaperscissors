package org.hamcrest;

public class MatchesRegex extends TypeSafeMatcher<String> {

    private String expectedRegex;

    public MatchesRegex(String expectedRegex) {
        this.expectedRegex = expectedRegex;
    }

    public static <T> Matcher<T> matchesRegex(String regularExpression) {
        return (Matcher<T>) new MatchesRegex(regularExpression);
    }

    @Override
    protected boolean matchesSafely(String item) {
        return item.matches(this.expectedRegex);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matches regular exception");
    }
}
