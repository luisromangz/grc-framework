dojo.require("grc.Object");
dojo.require("grc.string");

dojo.declare("tests.grc.objectTest1", [grc.Object], {
    param : null,
    constructor : function () {
        console.debug(this.declaredClass + ".constructor");
    },
    init : function () {
        console.debug(this.declaredClass + ".init");
        this.inherited(arguments);
    },
    _setParamField: function (value) {
        this.param = value;
    }
});

dojo.declare("tests.grc.objectTest2", [grc.Object], {
    param : null,
    propertyMap : {
        parameterProperty : 'param'
    },
    constructor : function () {
        console.debug(this.declaredClass + ".constructor");
    },
    init : function () {
        console.debug(this.declaredClass + ".init");
        this.inherited(arguments);
    },
    _setParamField: function (value) {
        this.param = value;
    }
});

function testInitiallization () {
    var testObj = new tests.grc.objectTest1({param : 'test'});
    assertEquals("Object initiallization failed", 'test', testObj.param);
    
    testObj = new tests.grc.objectTest2({parameterProperty : 'test'});
    assertEquals("Object initiallization failed", 'test', testObj.param);

    testObj = new tests.grc.objectTest1();
    assertEquals(null, testObj.param);
    testObj.field('param', 'test1');
    assertEquals('test1', testObj.param);
    assertEquals(testObj.param, testObj.field('param'));
    assertNotEquals('x', testObj.param);

    testObj = new tests.grc.objectTest2();
    assertEquals(null, testObj.param);
    testObj.field('parameterProperty', 'test2');
    assertEquals('test2', testObj.param);
    assertEquals(testObj.param, testObj.field('parameterProperty'));
    assertNotEquals('x', testObj.param);
}