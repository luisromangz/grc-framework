dojo.require("grc.debug");
dojo.require("grc.validation");

function testNIFValidation () {
    var tests = [
        {
            nif: 'Q2818002D',
            valid: true
        },
        {
            nif: 'q2818002d',
            valid: true
        },
        {
            nif: 'B91789214',
            valid: true
        },
        {
            nif: 'G21163944',
            valid: true
        },
        {
            nif: 'B91789215',
            valid: false
        },
        {
            nif: 'C91789214',
            valid: false
        },
        {
            nif: '2222222222222222',
            valid: false
        },
        {
            nif: '2910832H',
            valid: false
        }
    ];

    var validateCif = function (data) {
        var result = grc.validation.nif.validate(data.nif);
        console.debug("Validating " + data.nif + ", expected: " + (data.valid? 'valid' : 'invalid'));
        assertEquals("NIF " + data.nif + " validation failed.", result, data.valid);
        console.debug("  OK");
    }

    for (var i=0; i< tests.length; i++) {
        validateCif(tests[i]);
    }
}

