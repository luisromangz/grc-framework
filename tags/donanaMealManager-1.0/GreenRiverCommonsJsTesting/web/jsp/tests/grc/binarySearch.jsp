<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="jsRoot" value="../../../js"/>
<c:set var="dojoRoot" value="${jsRoot}/dojo_toolkit_131"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Test</title>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
        <link rel="stylesheet" href="${dojoRoot}/dojo/resources/dojo.css" />
        <link rel="stylesheet" href="${dojoRoot}/dijit/themes/tundra/tundra.css" />
        <style media="all" type="text/css">
            #body {
                padding: 12px;
                margin: 0px auto 0px auto;
            }
        </style>
        <script type="text/javascript">
            var djConfig =  {
                isDebug: true,
                locale:'es'
            };
        </script>
        <script type="text/javascript" src="${dojoRoot}/dojo/dojo.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/grc.js"></script>
        <script type="text/javascript">dojo.registerModulePath("grc", "../../grc");</script>
        <script type="text/javascript" src="${jsRoot}/grc/array.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/string.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.require("dojo.parser");
                dojo.require("dojo.date");
                dojo.parser.parse();
                var data = [1,4,6,8,10,12,19,1005,2005,5009];
                var out = dojo.byId('results');
                var element = 1;
                var result = grc.array.binarySearch(element, data);
                var tmp = null;
                var output = '';
                out.innerHTML = grc.string.toString(data);
                output += "\n" + "Search " + element + " : " + result;
                element = 18;
                result = grc.array.binarySearch(element, data);
                output += "\n" + "Search " + element + " : " + result;
                data.splice((result*-1) - 1, 0, element);
                output += "\n" + grc.string.toString(data);
                result = grc.array.binarySearch(element, data);
                output += "\n" + "Search " + element + " : " + result;
                element = '5009';
                result = grc.array.binarySearch(element, data);
                output += "\n" + "Search " + element + " : " + result;
                element = 6000;
                result = grc.array.binarySearch(element, data);
                output += "\n" + "Search " + element + " : " + result;
                //-------------------------------------------
                tmp = new Date();
                data = [tmp, dojo.date.add(tmp, 'day', 5), dojo.date.add(tmp, 'day', 10)];
                output += "\n" + grc.string.toString(data);

                element = data[0];
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                output += "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = data[1];
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                output += "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = data[2];
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                output += "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = dojo.date.add(tmp, 'day', 2);
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                output += "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = dojo.date.add(tmp, 'day', 6);
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                output += "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = dojo.date.add(tmp, 'day', 12);
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                output += "\n" + "Search " + grc.string.toString(element) + " : " + result;

                var data = [];
                var pos = 0;
                var pos2 = 0;
                var r = 0;
                var criteria = function (arr, pos) {
                    if (pos < (arr.length - 1)) {
                        return arr[pos] <= arr[pos + 1];
                    } else {
                        return true;
                    }
                }

                for (var i=0; i<100; i++) {
                    r = Math.round(Math.random() * (500 - 1) + 1);
                    output += "\nInserting " + r;
                    pos = grc.array.binarySearch(r, data);
                    if (pos < 0) {
                        pos = (pos + 1) * -1;
                    }
                    
                    grc.array.insert(r, data, pos);
                    output += "\n\t" + grc.string.toString(data, 1);

                    if (!grc.array.forAll(data, criteria)) {
                        output += "\n\tFailed to do ordered insertion.";
                    } else {
                        pos2 = grc.array.binarySearch(r, data);
                        if (pos2 < 0) {
                            output += "\n\tFailed to search inserted element.";
                        } else if (data[pos] !== r) {
                            output += "\n\tElement found is " + data[pos] + " but should be " + r + ".";
                        }
                    }
                }

                out.innerHTML = output;
            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <textarea id="results" cols="550" rows="128"></textarea>
        </div>
    </body>
</html>

