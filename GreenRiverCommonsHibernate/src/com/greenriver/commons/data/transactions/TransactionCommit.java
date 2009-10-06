
package com.greenriver.commons.data.transactions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation to mark those methods where a commit should be done when they
 * successfully returns without exceptions.
 *
 * @author Miguel Angel
 */
@Target(value={ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface TransactionCommit {

}
