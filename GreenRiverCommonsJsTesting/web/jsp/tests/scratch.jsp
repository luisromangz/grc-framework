<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="jsRoot" value="../../js"/>
<c:set var="dojoRoot" value="${jsRoot}/dojo_toolkit_131"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
        <title>Testing</title>
        <link rel="stylesheet" href="${dojoRoot}/dojo/resources/dojo.css" />
        <link rel="stylesheet" href="${dojoRoot}/dijit/themes/tundra/tundra.css" />
        <link rel="stylesheet" href="${dojoRoot}/dojox/grid/resources/tundraGrid.css" />
        <script type="text/javascript">
            var djConfig =  {
                isDebug: true,
                locale:'es'
            };
        </script>
        <script type="text/javascript" src="${dojoRoot}/dojo/dojo.js"></script>
        <script type="text/javascript">dojo.registerModulePath("grc", "../../grc");</script>
        <script type="text/javascript">dojo.registerModulePath("tasks", "../../tasks");</script>
        <script type="text/javascript" src="${jsRoot}/grc/grc.js"></script>

        <script type="text/javascript">
            dojo.addOnLoad(function() {
                dojo.declare("test.TestA", null, {
                    funcTest: function () {
                        console.debug("I'm A");
                    }
                });

                dojo.declare("test.TestB", null, {
                    funcTest: function () {
                        console.debug("I'm B");
                    }
                });

                dojo.declare("test.TestAB", [test.TestA, test.TestB], {
                    
                });

                var testObj = new test.TestAB();
                testObj.funcTest();
            });
        </script>
    </head>
    <body class="tundra">
        <div id="body">
        </div>
    </body>
</html>

