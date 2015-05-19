package net.seleucus.wsp.server.request.filter;

public enum FilterResult {
    DROP, ACCEPT;

    public static boolean resultIsDrop(final FilterResult filterResult){
        return DROP == filterResult;
    }

    public static FilterResult evaluateCondition(boolean condition){
        return condition ? ACCEPT : DROP;
    }
}
