package org.jirafa.writer.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@XmlRootElement(name = "test")
public class Test {

    private String name;
    private List<Jira> jiras;

    public Test() { }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElementWrapper(name = "jiras")
    @XmlElement(name = "jira")
    public List<Jira> getJiras() {
        return jiras;
    }

    public void setJiras(List<Jira> jiras) {
        this.jiras = jiras;
    }

    public void setName(String name) {
        this.name = name;
    }
}
