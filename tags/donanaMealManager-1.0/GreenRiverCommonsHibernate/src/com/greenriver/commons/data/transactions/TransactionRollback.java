
package com.greenriver.commons.data.transactions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark those methods where a rollback should be done if an
 * exception is trowed before it returns.
 *
 * @author Miguel Angel
 */
@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TransactionRollback {

}
