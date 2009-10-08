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
                out.innerHTML = grc.string.toString(data);
                out.innerHTML = out.innerHTML + "\n" + "Search " + element + " : " + result;
                element = 18;
                result = grc.array.binarySearch(element, data);
                out.innerHTML = out.innerHTML + "\n" + "Search " + element + " : " + result;
                data.splice((result*-1) - 1, 0, element);
                out.innerHTML = out.innerHTML + "\n" + grc.string.toString(data);
                result = grc.array.binarySearch(element, data);
                out.innerHTML = out.innerHTML + "\n" + "Search " + element + " : " + result;
                element = '5009';
                result = grc.array.binarySearch(element, data);
                out.innerHTML = out.innerHTML + "\n" + "Search " + element + " : " + result;
                element = 6000;
                result = grc.array.binarySearch(element, data);
                out.innerHTML = out.innerHTML + "\n" + "Search " + element + " : " + result;
                //-------------------------------------------
                tmp = new Date();
                data = [tmp, dojo.date.add(tmp, 'day', 5), dojo.date.add(tmp, 'day', 10)];
                out.innerHTML = out.innerHTML + "\n" + grc.string.toString(data);

                element = data[0];
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                out.innerHTML = out.innerHTML + "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = data[1];
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                out.innerHTML = out.innerHTML + "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = data[2];
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                out.innerHTML = out.innerHTML + "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = dojo.date.add(tmp, 'day', 2);
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                out.innerHTML = out.innerHTML + "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = dojo.date.add(tmp, 'day', 6);
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                out.innerHTML = out.innerHTML + "\n" + "Search " + grc.string.toString(element) + " : " + result;

                element = dojo.date.add(tmp, 'day', 12);
                result = grc.array.binarySearch(element, data, function (a, b) {
                    return dojo.date.compare(a, b, 'date');
                });
                out.innerHTML = out.innerHTML + "\n" + "Search " + grc.string.toString(element) + " : " + result;

                /*var data = [1,4,6];
                var out = dojo.byId('results');
                var element = 2;
                var result = grc.array.binarySearch(element, data);
                var tmp = null;
                out.innerHTML = grc.string.toString(data);
                out.innerHTML = out.innerHTML + "\n" + "Search " + element + " : " + result;
                element = 5;
                result = grc.array.binarySearch(element, data);
                out.innerHTML = out.innerHTML + "\n" + "Search " + element + " : " + result;*/
            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <textarea id="results" cols="150" rows="64"></textarea>
        </div>
    </body>
</html>

