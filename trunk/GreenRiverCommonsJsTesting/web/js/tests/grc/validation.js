dojo.require("grc.debug");
dojo.require("grc.validation");

function testCIFValidation () {
    var tests = [
        {
            cif: 'Q2818002D',
            valid: true
        },
        {
            cif: 'B91789214',
            valid: true
        },
        {
            cif: 'G21163944',
            valid: true
        },
        {
            cif: 'B91789215',
            valid: false
        },
        {
            cif: 'C91789214',
            valid: false
        },
        {
            cif: '2222222222222222',
            valid: false
        }
    ];

    var validateCif = function (data) {
        var result = grc.validation.cif.validate(data.cif);
        console.debug("Validating " + data.cif);
        assertEquals("CIF " + data.cif + " validation failed.", result, data.valid);
        console.debug("  OK");
    }

    for (var i=0; i< tests.length; i++) {
        validateCif(tests[i]);
    }
}

