<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="jsRoot" value="../../../../../js"/>
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
        <script type="text/javascript">dojo.registerModulePath("grc", "../../grc");</script>
        <script type="text/javascript" src="${jsRoot}/grc/grc.js"></script>
        <script type="text/javascript" src="${dojoRoot}/dojox/grid/_Grid.js"></script>
        <script type="text/javascript" src="${dojoRoot}/dojox/grid/_Layout.js"></script>
        <script type="text/javascript" src="${dojoRoot}/dojox/grid/_View.js"></script>
        <script type="text/javascript" src="${dojoRoot}/dojox/grid/_ViewManager.js"></script>
        <script type="text/javascript" src="${dojoRoot}/dojox/grid/_Builder.js"></script>
        <script type="text/javascript" src="${dojoRoot}/dojox/grid/DataGrid.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/dijit/grid/GridView.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/dijit/grid/TableView.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/dijit/grid/_TableViewBuilder.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/dijit/grid/_TableViewHeaderBuilder.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/dijit/grid/_TableViewContentBuilder.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.require("dojo.parser");
                dojo.parser.parse();
                var data2 = [
                    {
                        uno: 1,
                        dos: 2
                    },
                    {
                        uno: 3,
                        dos: 4
                    }
                ];

                //grid.attr('structure', gridStructure);
                //grid.setDataSource(data);
                dijit.byId('grid2').setDataSource(data2);
            };
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <div style="background-color: #cfcfcf;min-width: 400px; min-height: 250px;padding: 12px">
                <table id="grid2"
                     height="14em"
                     dojoType="grc.dijit.grid.GridView">
                    <thead>
                        <tr type="grc.dijit.grid.TableView">
                            <th field="uno">Uno</th>
                            <th field="dos">Dos</th>
                        </tr>
                    </thead>
                </table>
            </div>
        </div>
    </body>
</html>

