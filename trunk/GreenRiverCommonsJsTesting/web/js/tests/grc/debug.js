dojo.require("grc.debug");
dojo.require('grc.Object');
dojo.require('grc.Exception');

function testDebugExceptionToArray() {
    var result = [];
    var testObj = null;

    try {
        testObj = {
            testFunc: function () {
                var testVar = new grc.Object();
                testVar._throwEx('Testing the exception');
                throw testVar;
            }
        };
        testObj.testFunc();
    } catch (ex) {
        result = grc.debug.exceptionToArray(ex);
    }

    console.debug(result.join('\n'));

    try {
        testObj = {
            testFunc: function () {
                throw new Error('Testing the exception');
            }
        };
        testObj.testFunc();
    } catch (ex) {
        result = grc.debug.exceptionToArray(ex);
    }

    console.debug(result.join('\n'));
}
