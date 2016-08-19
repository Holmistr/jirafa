package org.jirafa.writer;

import org.jirafa.writer.entity.TestRun;

/**
 * Interface for writing the results of Jirafa.
 *
 * @author Jiri Holusa (jholusa@redhat.com)
 */
public interface Writer {

    void write(TestRun testRun, String testName);
}
