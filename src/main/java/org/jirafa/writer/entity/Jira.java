package org.jirafa.writer.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Jiri Holusa (jholusa@redhat.com)
 */
@XmlRootElement(name = "jira")
public class Jira {

    private String key;
    private String link;

    public Jira() { }

    @XmlElement
    public String getKey() {
        return key;
    }

    @XmlElement
    public String getLink() {
        return link;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
