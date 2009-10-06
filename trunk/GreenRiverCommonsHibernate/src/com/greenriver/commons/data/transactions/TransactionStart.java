
package com.greenriver.commons.data.transactions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark those methods where the transaction should be started
 * (if not active yet).
 * 
 * @author Miguel Angel
 */
@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TransactionStart {

}
