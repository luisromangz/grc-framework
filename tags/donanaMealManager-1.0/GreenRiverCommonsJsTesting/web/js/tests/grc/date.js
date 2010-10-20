dojo.require("grc.date");
dojo.require("grc.string");

function testDojoDate () {
    var target = new Date();
    var source = new Date();
    var result = false;

    result = dojo.date.compare(target, source, 'date') == 0;
    assertTrue(result);

    target = dojo.date.add(new Date(target), 'day', -1);
    result = dojo.date.compare(target, source, 'date') < 0;
    assertTrue(result);

    target = dojo.date.add(new Date(target), 'day', 1);
    result = dojo.date.compare(target, source, 'date') == 0;
    assertTrue(result);

    target = dojo.date.add(new Date(target), 'day', 1);
    result = dojo.date.compare(target, source, 'date') > 0;
    assertTrue(result);
}

function testIsDateRange () {
    var target = {
        min:'2009/10/08',
        max:'2009/10/09'
    };
    
    var name = "testIsDateRange(" + grc.string.toString(target) + ")";
    var result = grc.date.isDateRange(target);
    assertFalse(name, result);
    
    target = {
        min:'2009/10/08'
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertFalse(name, result);

    target = {
        min:new Date()
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertTrue(name, result);

    target = {
        min:'2009/10/08',
        max:null
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertFalse(name, result);

    target = {
        min:new Date(),
        max:null
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertTrue(name, result);

    target = {
        min:new Date(),
        max:new Date()
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertTrue(name, result);

    target = {
        max:'2009/10/08T21:21'
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertFalse(name, result);

    target = {
        max:new Date()
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertTrue(name, result);

    target = {
        max:'2009/10/08T20:20',
        min:null
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertFalse(name, result);

    target = {
        max:new Date(),
        min:null
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertTrue(name, result);

    target = {
        min:null,
        max:null
    };
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertTrue(name, result);

    target = {};
    name = "testIsDateRange(" + grc.string.toString(target) + ")";
    result = grc.date.isDateRange(target);
    assertFalse(name, result);

    target = null;
    try {
        name = "testIsDateRange(" + grc.string.toString(target) + ")";
        result = grc.date.isDateRange(target);
        fail(name, "Expected exception");
    } catch (e) {

    }

    target = undefined;
    try {
        name = "testIsDateRange(" + grc.string.toString(target) + ")";
        result = grc.date.isDateRange(target);
        fail(name, "Expected exception");
    } catch (e) {

    }

    target = new Date();
    result = grc.date.isDateRange(target);
    assertFalse(result);

    target = "hola";
    result = grc.date.isDateRange(target);
    assertFalse(result);
}

function testIsEmpty () {
    var target = {
        min:'2009/10/08',
        max:'2009/10/09'
    };
    var result = grc.date.isEmptyRange(target);
    assertFalse(result);

    target = {
        min:'2009/10/08',
        max: null
    };
    result = grc.date.isEmptyRange(target);
    assertFalse(result);

    target = {
        min:new Date(),
        max:new Date()
    };
    result = grc.date.isEmptyRange(target);
    assertFalse(result);

    target = {
        max:'2009/10/08',
        min:null
    };
    result = grc.date.isEmptyRange(target);
    assertFalse(result);

    target = {};
    result = grc.date.isEmptyRange(target);
    assertTrue(result);

    try {
        target = null;
        result = grc.date.isEmptyRange(target);
        fail("grc.date.isEmptyRange(" + target + ") must throw an exception");
    } catch (e) {

    }
}

function testIsInfinite() {
    var target = {
        min:'2009/10/08',
        max:'2009/10/09'
    };
    var result = grc.date.isInfiniteRange(target);
    var name = "testIsInfinite(" + grc.string.toString(target) + ")";
    assertFalse(name, result);

    name = "testIsInfinite(" + grc.string.toString(target) + ")";
    target = {
        min:'2009/10/08',
        max: null
    };
    result = grc.date.isInfiniteRange(target);
    assertTrue(name, result);

    name = "testIsInfinite(" + grc.string.toString(target) + ")";
    target = {
        min:new Date(),
        max:new Date()
    };
    result = grc.date.isInfiniteRange(target);
    assertFalse(name, result);

    name = "testIsInfinite(" + grc.string.toString(target) + ")";
    target = {
        max:'2009/10/08',
        min:null
    };
    result = grc.date.isInfiniteRange(target);
    assertTrue(name, result);

    name = "testIsInfinite(" + grc.string.toString(target) + ")";
    target = {};
    result = grc.date.isInfiniteRange(target);
    assertFalse(name, result);

    try {
        name = "testIsInfinite(" + grc.string.toString(target) + ")";
        target = null;
        result = grc.date.isInfiniteRange(target);
        fail("grc.date.isEmptyRange(" + target + ") must throw an exception");
    } catch (e) {

    }
}

function testIsDateInDateRange () {
    var result = null;
    var name = null;
    var target = null;
    var range = {min: null, max: null};
    range.min = dojo.date.add(new Date(), 'day', -5);
    range.max = dojo.date.add(new Date(), 'day', 5);

    target = new Date();
    name = "testIsDateInDateRange(" + target + ")[date]";
    result = grc.date.isDateInDateRange(target, range, 'date');
    assertTrue(name, result);
    name = "testIsDateInDateRange(" + target + ")[dateTime]";
    result = grc.date.isDateInDateRange(target, range);
    assertTrue(name, result);

    target = new Date(range.min);
    name = "testIsDateInDateRange(" + target + ")[date]";
    result = grc.date.isDateInDateRange(target, range, 'date');
    assertTrue(name, result);
    name = "testIsDateInDateRange(" + target + ")[dateTime]";
    result = grc.date.isDateInDateRange(target, range);
    assertTrue(name, result);

    target = new Date(range.max);
    name = "testIsDateInDateRange(" + target + ")[date]";
    result = grc.date.isDateInDateRange(target, range, 'date');
    assertTrue(name, result);
    name = "testIsDateInDateRange(" + target + ")[dateTime]";
    result = grc.date.isDateInDateRange(target, range);
    assertTrue(name, result);

    target = dojo.date.add(new Date(), 'day', -6);
    name = "testIsDateInDateRange(" + target + ")[date]";
    result = grc.date.isDateInDateRange(target, range, 'date');
    assertFalse(name, result);
    name = "testIsDateInDateRange(" + target + ")[dateTime]";
    result = grc.date.isDateInDateRange(target, range);
    assertFalse(name, result);

    target = dojo.date.add(new Date(), 'day', -5);
    name = "testIsDateInDateRange(" + grc.string.toString(target) + ", " + grc.string.toString(range) + ")";
    result = grc.date.isDateInDateRange(target, range, 'date');
    assertTrue(name, result);

    target = dojo.date.add(new Date(), 'day', 5);
    name = "testIsDateInDateRange(" + grc.string.toString(target) + ", " + grc.string.toString(range) + ")";
    result = grc.date.isDateInDateRange(target, range, 'date');
    assertTrue(name, result);
}

function testIsRangeContained () {
    var result = null;
    var name = null;
    var target = null;
    var range = {min: null, max: null};
    range.min = dojo.date.add(new Date(), 'day', -5);
    range.max = dojo.date.add(new Date(), 'day', 5);

    target = range;
    name = "testIsRangeContained(" + grc.string.toString(target) + ", " + grc.string.toString(range) + ")";
    result = grc.date.isRangeContained(range, range, 'date');
    assertTrue(name, result);

    target = {
        min : dojo.date.add(new Date(range.min), 'day', 1),
        max : dojo.date.add(new Date(range.max), 'day', -1)
    }
    name = "testIsRangeContained(" + grc.string.toString(target) + ", " + grc.string.toString(range) + ")";
    result = grc.date.isRangeContained(target, range, 'date');
    assertTrue(name, result);

    target = {
        min : dojo.date.add(new Date(range.min), 'day', -1),
        max : dojo.date.add(new Date(range.max), 'day', -1)
    }
    name = "testIsRangeContained(" + grc.string.toString(target) + ", " + grc.string.toString(range) + ")";
    result = grc.date.isRangeContained(target, range, 'date');
    assertFalse(name, result);

    target = {
        min : dojo.date.add(new Date(range.min), 'day', 1),
        max : dojo.date.add(new Date(range.max), 'day', 1)
    }
    name = "testIsRangeContained(" + grc.string.toString(target) + ", " + grc.string.toString(range) + ")";
    result = grc.date.isRangeContained(target, range, 'date');
    assertFalse(name, result);

    target = {
        min : dojo.date.add(new Date(range.min), 'day', -1),
        max : dojo.date.add(new Date(range.max), 'day', 1)
    }
    name = "testIsRangeContained(" + grc.string.toString(target) + ", " + grc.string.toString(range) + ")";
    result = grc.date.isRangeContained(target, range, 'date');
    assertFalse(name, result);

    var tmp = target;
    target = range;
    range = tmp;
    name = "testIsRangeContained(" + grc.string.toString(target) + ", " + grc.string.toString(range) + ")";
    result = grc.date.isRangeContained(target, range, 'date');
    assertTrue(name, result);
}

function testIsInDateRangeArray () {
    var result = null;
    var name = null;
    var target = null;
    var ranges = [];
    var range = {min: null, max: null};
    range.min = dojo.date.add(new Date(), 'day', -5);
    range.max = dojo.date.add(new Date(), 'day', 5);

    ranges.push(range);
    range = {
        min : dojo.date.add(new Date(), 'day', 10),
        max : dojo.date.add(new Date(), 'day', 20)
    };
    ranges.push(range);

    target = new Date();
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertTrue(name, result);

    target = dojo.date.add(new Date(), 'day', 5);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertTrue(name, result);

    target = dojo.date.add(new Date(ranges[0].max), 'day', 1);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertFalse(name, result);

    target = dojo.date.add(new Date(ranges[1].min), 'day', -1);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertFalse(name, result);

    target = new Date(ranges[1].min);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertTrue(name, result);

    target = dojo.date.add(new Date(ranges[1].min), 'day', 1);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertTrue(name, result);

    target = new Date(ranges[1].max);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertTrue(name, result);

    ranges = [{
            min: new Date(),
            max : null
    }];

    target = new Date(ranges[0].min);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertTrue(name, result);

    target = dojo.date.add(new Date(ranges[0].min), 'day', 1);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertTrue(name, result);

    target = dojo.date.add(new Date(ranges[0].min), 'day', -1);
    name = "testIsInDateRangeArray(" + grc.string.toString(target) + ", " + grc.string.toString(ranges) + ")";
    result = grc.date.isInDateRangeArray(target, ranges, 'date');
    assertFalse(name, result);
}

function testSplitRange() {
    var range = {min: new Date(), max: dojo.date.add(new Date(), 'day', 5)};
    var result = grc.date.splitRange(range);

    console.debug("**  testSplitRange  **");
    console.debug("  Target range: " + grc.string.toString(range));
    console.debug("  Splitted range: " + grc.string.toString(result));

    assertFalse("Split result not empty.", grc.isEmpty(result));
    assertTrue("Split result array.", dojo.isArray(result));
    
    assertEquals(
        "Split result 5 items.",
        6,
        result.length);

    assertEquals(
        "First date equals range start.",
        0,
        grc.date.compareDate(result[0], range.min));

    assertEquals(
        "Second date greater than range start.",
        1,
        grc.date.compareDate(result[1], range.min));

    assertEquals(
        "Last date equals range end.",
        0,
        grc.date.compareDate(result[result.length-1], range.max));

    assertEquals(
        "Before last date lower than range end.",
        -1,
        grc.date.compareDate(result[result.length-2], range.max));
}