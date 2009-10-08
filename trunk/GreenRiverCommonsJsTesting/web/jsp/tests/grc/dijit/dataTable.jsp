<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="dojoRoot" value="js/dojo_toolkit_131"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
        <title>Testing</title>
        <link rel="stylesheet" href="${dojoRoot}/dojo/resources/dojo.css" />
        <link rel="stylesheet" href="${dojoRoot}/dijit/themes/tundra/tundra.css" />
        <link rel="stylesheet" href="${dojoRoot}/dojox/grid/resources/Grid.css" />
        <link rel="stylesheet" href="${dojoRoot}/dojox/grid/resources/tundraGrid.css" />
        <style media="all" type="text/css">
        </style>
        <script type="text/javascript">
            var djConfig =  {
                isDebug: true,
                locale:'es'
            };
        </script>
        <script type="text/javascript" src="${dojoRoot}/dojo/dojo.js"></script>
        <script type="text/javascript">dojo.registerModulePath("grc", "../../grc");</script>
        <script type="text/javascript" src="js/grc/grc.js"></script>
        <script type="text/javascript" src="js/grc/dijit/DataTable.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.require("dojo.parser");
                dojo.require("dojox.data.AndOrWriteStore");
                dojo.require("grc.dijit.DataTable");
                
                dojo.parser.parse();
            };
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <div style="min-width:450px;min-height:550px;padding: 12px;margin:24px;border: solid 2px #cfcfcf;">
                    <table dojoType="grc.dijit.DataTable">
                        <thead>
                            <tr>
                                <th rowSpan="2"></th>
                                <th rowSpan="2">
                                    Hora
                                </th>
                                <th rowSpan="2">
                                    Comida
                                </th>
                                <th colSpan="2">Platos opcionales</th>
                            </tr>
                            <tr>
                                <th></th>
                                <th>Plato</th>
                            </tr>
                        </thead>
                    </table>
            </div>
        </div>
    </body>
</html>