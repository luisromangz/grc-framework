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
        <link rel="stylesheet" href="${jsRoot}/grc/dijit/themes/tundra/tundra.css" />
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
        <script type="text/javascript" src="${jsRoot}/grc/dijit/form/YearDayTextBox.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.registerModulePath("grc", "../../grc");
                dojo.require("grc.string");
                dojo.require("grc.dijit.form.YearDayTextBox");
                dojo.require("grc.dijit.ModalDialog");
                dojo.require('grc.debug');
                dojo.require("dojo.parser");
                dojo.parser.parse();
            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <p>Basic test</p>
            <div>
                <button dojoType="grc.dijit.form.YearDayTextBox" id="yearDay"></button>
            </div>
            <br/>
            <p>Test in dialog</p>
            <div>
                <input type="button" onclick="dijit.byId('testDialog').show()" value="Test me!"/>
                <div dojoType="grc.dijit.ModalDialog" id="testDialog" title="test me!" style="width:25em;height:12em">
                    <table border="0" cellpadding="12" cellspacing="12">
                        <thead>
                            <tr>
                                <th>Etiqueta</th>
                                <th>Control</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>
                                    <label for="yearDay2">Pruebas</label>
                                </td>
                                <td>
                                    <button dojoType="grc.dijit.form.YearDayTextBox" id="yearDay2" value="today"></button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <p>Set date to null</p>
            <div>
                <button dojoType="grc.dijit.form.YearDayTextBox" id="yearDay3"></button>
            </div>
            <p>Set date to 28/12</p>
            <div>
                <button dojoType="grc.dijit.form.YearDayTextBox" id="yearDay4" displayedValue="28/12"></button>
            </div>
        </div>
    </body>
</html>