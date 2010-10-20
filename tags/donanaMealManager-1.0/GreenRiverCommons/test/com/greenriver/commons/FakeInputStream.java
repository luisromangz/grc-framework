
package com.greenriver.commons;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author mangelp
 */
public class FakeInputStream extends InputStream {

    @Override
    public int read() throws IOException {
	return 1;
    }
}
