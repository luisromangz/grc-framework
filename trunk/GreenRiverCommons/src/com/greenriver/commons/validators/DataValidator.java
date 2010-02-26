
package com.greenriver.commons.validators;

/**
 * Interface to implement by those classes that provides some kind of validation
 * following al algorithm that can't be implemented as a regular expression.
 * @param <DT> Data type for the argument of the validation method
 * @author Miguel Angel
 */
public interface DataValidator <DT> {
    boolean validate(DT data);
}
