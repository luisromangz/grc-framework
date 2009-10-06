
package com.greenriver.commons.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark those arbitrary methods that should appear in the trace
 * log.
 * @author Miguel Angel
 */
@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface LogTraceMethod {

}
