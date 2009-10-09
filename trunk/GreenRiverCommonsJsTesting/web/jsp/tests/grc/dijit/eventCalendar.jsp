<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<c:set var="jsRoot" value="../../../../js"/>
<c:set var="dojoRoot" value="${jsRoot}/dojo_toolkit_131"/>

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
                dojo.require("dojo.parser");
                dojo.parser.parse();
            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <div style="background-color: #cfcfcf;min-width: 700px; min-height: 450px;padding: 12px">
                <dl>
                    <dt>Por defecto</dt>
                    <dd>
                        <div dojoType="grc.dijit.EventCalendar" id="eventCalendar1"></div>
                        <button onclick="dijit.byId('eventCalendar1').selection.deselectAll()">Limpiar</button>
                        <button onclick="dijit.byId('eventCalendar1').selection.setSelected([new Date(), dojo.date.add(new Date(), 'day', 1)])">Hoy y mañana</button>
                    </dd>
                    <dt>Restringido a días >= hoy</dt>
                    <dd>
                        <div dojoType="grc.dijit.EventCalendar" id="eventCalendar2"
                             constraints="{min: 'today'}"></div>
                    </dd>
                    <dt>Hacer click cambia la selección</dt>
                    <dd>
                        <div dojoType="grc.dijit.EventCalendar" id="eventCalendar3"
                             singleClickToggle="true"></div>
                    </dd>
                    <dt>Modo de selección de rangos forzado</dt>
                    <dd>
                        <div dojoType="grc.dijit.EventCalendar" id="eventCalendar4"
                             forceShiftForRangeSelection="true"></div>
                    </dd>
                    <dt>Restringido a días >= hoy cambio a meses deshabilitados no permitido</dt>
                    <dd>
                        <div dojoType="grc.dijit.EventCalendar" id="eventCalendar5"
                             allowViewOfDisabledMonths="false"
                             constraints="{min:'today'}"></div>
                    </dd>
                </dl>
            </div>
        </div>
    </body>
</html>

