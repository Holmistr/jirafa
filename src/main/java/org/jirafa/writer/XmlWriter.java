package org.jirafa.writer;

import org.jirafa.writer.entity.Jira;
import org.jirafa.writer.entity.TestRun;
import org.jirafa.writer.entity.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;

/**
 * XML-based implementation. Outputs Jirafa report to JIRA-<testname>.xml file
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public class XmlWriter implements Writer {

    public static final String OUTPUT_DIR = System.getProperty("outputDir", ".");
    public static final String JIRAFA_PREFIX = "JIRAFA";

    public void write(TestRun testRun, String testName) {
        try {
            File outputDirectory = new File(OUTPUT_DIR);
            if (!outputDirectory.exists()) {
                outputDirectory.mkdir();
            } else {
                if (!outputDirectory.isDirectory()) {
                    throw new IllegalArgumentException("Output directory " + OUTPUT_DIR + " is not a directory!");
                }
            }

            File file = new File(outputDirectory + "/" + JIRAFA_PREFIX + "-" + testName);
            JAXBContext jaxbContext = JAXBContext.newInstance(TestRun.class, Test.class, Jira.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // output pretty printed
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            jaxbMarshaller.marshal(testRun, file);
            jaxbMarshaller.marshal(testRun, System.out);
        } catch (JAXBException ex) {
            throw new RuntimeException("Error occurred while writing results to XML file", ex);
        }
    }
}
