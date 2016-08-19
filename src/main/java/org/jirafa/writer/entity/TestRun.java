package org.jirafa.writer.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Collection;

/**
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@XmlRootElement(name = "test-run")
public class TestRun {

    private String name;
    private String packageName;
    private Collection<Test> tests;

    public TestRun() { }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement(name = "package")
    public String getPackageName() {
        return packageName;
    }

    @XmlElementWrapper(name = "tests")
    @XmlElement(name = "test")
    public Collection<Test> getTests() {
        return tests;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setTests(Collection<Test> tests) {
        this.tests = tests;
    }
}
