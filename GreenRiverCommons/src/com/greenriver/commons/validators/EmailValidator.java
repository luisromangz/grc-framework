package com.greenriver.commons.validators;

import com.greenriver.commons.Strings;
import com.greenriver.commons.data.validation.ValidationRegex;
import java.util.regex.Pattern;

/**
 * Implements a validator for emails
 * @author luisro
 */
public class EmailValidator implements DataValidator<String> {

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^" + ValidationRegex.EMAIL + "$",
            Pattern.CASE_INSENSITIVE);

    @Override
    public boolean validate(String email) {
        if(Strings.isNullOrEmpty(email)) {
            return false;
        }
        
        return EMAIL_PATTERN.matcher((CharSequence) email).matches();
    }
    
    public static boolean validateMail(String email) {
        return (new EmailValidator()).validate(email);
    }
}
