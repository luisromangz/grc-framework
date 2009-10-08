<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="jsRoot" value="../../../../js"/>
<c:set var="dojoRoot" value="${jsRoot}/dojo_toolkit_132"/>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
        <title>Testing</title>
        <link rel="stylesheet" href="${dojoRoot}/dojo/resources/dojo.css" />
        <link rel="stylesheet" href="${dojoRoot}/dijit/themes/tundra/tundra.css" />
        <link rel="stylesheet" href="${jsRoot}/grc/dijit/themes/tundra/EventCalendar.css" />
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
        <script type="text/javascript" src="${dojoRoot}/dijit/_Calendar.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/dijit/EventCalendar.js"></script>
        <script type="text/javascript" src="${jsRoot}/grc/date.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.registerModulePath("grc", "../../grc");
                dojo.require("dojo.parser");
                dojo.require("grc.dijit.EventCalendar");
                dojo.require("dijit.Tooltip");
                dojo.parser.parse();
            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <div style="background-color: #cfcfcf;min-width: 700px; min-height: 450px;padding: 12px">
                <dl>
                    <dt>Calendario propio restringido a días posteriores o iguales a hoy</dt>
                    <dd>
                        <div dojoType="grc.dijit.EventCalendar" id="eventCalendar"
                             constraints="{min: 'today'}"></div>
                    </dd>
                    <dt>Clear</dt>
                    <dd><button onclick="dijit.byId('eventCalendar').selection.deselectAll()">Limpiar</button></dd>
                    <dt>Dias</dt>
                    <dd><button onclick="dijit.byId('eventCalendar').selection.setSelected([new Date(), dojo.date.add(new Date(), 'day', 1)])">Hoy y mañana</button></dd>
                    <dt>Invertir</dt>
                    <dd><button onclick="dijit.byId('eventCalendar').selection.toggleSelected(dijit.byId('eventCalendar').attr('constraints'))">Invertir</button></dd>
                    <dt>Calendario normal</dt>
                    <dd>
                        <div dojoType="dijit._Calendar" id="eventCalendarNormal"></div>
                    </dd>
                    <dt>Calendario propio restringido programáticamente</dt>
                    <dd>
                        <!--div dojoType="grc.dijit.EventCalendar" id="eventCalendarAlwaysToggle"
                             constraints="{min:'today'}"
                             singleClickToggle="true"></div-->
                    </dd>
                </dl>
            </div>
        </div>
    </body>
</html>

