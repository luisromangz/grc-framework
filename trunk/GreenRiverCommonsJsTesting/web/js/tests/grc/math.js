dojo.require('grc.debug');
dojo.require('grc.math');

function testSumDigits() {
    var exp = 1;
    var sum = grc.math.sumDigits("000001");

    assertEquals(exp, sum);

    exp = 2;
    sum = grc.math.sumDigits("100001");

    assertEquals(exp, sum);

    exp = 6;
    sum = grc.math.sumDigits("111111");

    assertEquals(exp, sum);

    exp = 1;
    sum = grc.math.sumDigits("1234");

    assertEquals(exp, sum);

    exp = 3;
    sum = grc.math.sumDigits("123456");

    assertEquals(exp, sum);
}
