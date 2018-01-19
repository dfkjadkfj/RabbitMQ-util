package com.test;

import java.util.List;

public class TestMethod {
    private String name;

    private String value;

    private List<TestMethod> testList;

    public TestMethod(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<TestMethod> getTestList() {
        return testList;
    }

    public void setTestList(List<TestMethod> testList) {
        this.testList = testList;
    }
}
