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
        <link rel="stylesheet" href="${jsRoot}/grc/dijit/themes/tundra/form/TriStateCheckBox.css" />
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
        <script type="text/javascript" src="${jsRoot}/grc/dijit/form/TriStateCheckBox.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.registerModulePath("grc", "../../grc");
                dojo.require("dojo.parser");
                dojo.require("grc.dijit.form.TriStateCheckBox");
                dojo.require("dijit.form.CheckBox");
                dojo.parser.parse();

                var boton = dijit.byId('boton');
                boton.connect(boton, 'onClick', function () {
                    var ul = dojo.byId('values');
                    var newUl = dojo.create('ul', {id: 'values'}, ul, 'replace');
                    for (var i=1; i<=9; i++) {
                        var c = dijit.byId('c'+i);
                        var li = dojo.create('li',null, newUl);
                        li.innerHTML = 'id: c' +i+ ', value: ' + c.attr('value') + ", checked: " + c.attr('checked');
                    }
                });

                dojo.connect(dijit.byId('c2'), 'onValueChanged', function (before, now) {
                    alert(before + ' cambiado a  : ' + now);
                });

                var triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'off'
                }, 
                'programatic1');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'on'
                },
                'programatic2');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : '-'
                },
                'programatic3');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'off',
                    triStateCycle : false
                },
                'programatic4');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'on',
                    triStateCycle : false
                },
                'programatic5');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : '-',
                    triStateCycle : false
                },

                'programatic6');
                
                triState = new grc.dijit.form.TriStateCheckBox({
                    value : true
                },
                'programatic7');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'checked'
                },
                'programatic8');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'true'
                },
                'programatic9');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : false
                },
                'programatic10');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'false'
                },
                'programatic11');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'unchecked'
                },
                'programatic12');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : undefined
                },
                'programatic13');

                triState = new grc.dijit.form.TriStateCheckBox({
                    value : 'undefined'
                },
                'programatic14');

            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <div style="background-color: #cfcfcf;min-width: 700px; min-height: 450px;padding: 12px">
                <dl>
                    <dt>tristate checkbox</dt>
                    <dd><input id="c2" dojoType="grc.dijit.form.TriStateCheckBox"/></dd>
                    <dt>tristate checkbox two state cycle</dt>
                    <dd><input id="c3" triStateCycle="false" dojoType="grc.dijit.form.TriStateCheckBox" /></dd>
                    <dt>tristate checkbox disabled checked</dt>
                    <dd><input id="c4" value="on" disabled="true"
                               dojoType="grc.dijit.form.TriStateCheckBox"/></dd>
                    <dt>tristate checkbox disabled unchecked</dt>
                    <dd><input id="c5" value="off" disabled="true"
                               dojoType="grc.dijit.form.TriStateCheckBox"/></dd>
                    <dt>tristate checkbox disabled undefined</dt>
                    <dd><input id="c6" value="-" disabled="true"
                               dojoType="grc.dijit.form.TriStateCheckBox"/></dd>
                    <dt>tristate checkbox checked</dt>
                    <dd><input id="c7" value="on"
                               dojoType="grc.dijit.form.TriStateCheckBox"/></dd>
                    <dt>tristate checkbox unchecked</dt>
                    <dd><input id="c8" value="off"
                               dojoType="grc.dijit.form.TriStateCheckBox"/></dd>
                    <dt>tristate checkbox undefined</dt>
                    <dd><input id="c9" value="-"
                               dojoType="grc.dijit.form.TriStateCheckBox"/></dd>
                    <dt>Programatic one: tristate unchecked</dt>
                    <dd><div id="programatic1"></div></dd>
                    <dt>Programatic one: tristate checked</dt>
                    <dd><div id="programatic2"></div></dd>
                    <dt>Programatic one: tristate undefined</dt>
                    <dd><div id="programatic3"></div></dd>

                    <dt>Programatic one: bistate unchecked</dt>
                    <dd><div id="programatic4"></div></dd>
                    <dt>Programatic one: bistate checked</dt>
                    <dd><div id="programatic5"></div></dd>
                    <dt>Programatic one: bistate undefined</dt>
                    <dd><div id="programatic6"></div></dd>

                    <dt>Programatic one: value set to true</dt>
                    <dd><div id="programatic7"></div></dd>
                    <dt>Programatic one: value set to checked</dt>
                    <dd><div id="programatic8"></div></dd>
                    <dt>Programatic one: value set to 'true'</dt>
                    <dd><div id="programatic9"></div></dd>
                    <dt>Programatic one: value set to false</dt>
                    <dd><div id="programatic10"></div></dd>
                    <dt>Programatic one: value set to 'false'</dt>
                    <dd><div id="programatic11"></div></dd>
                    <dt>Programatic one: value set to 'unchecked'</dt>
                    <dd><div id="programatic12"></div></dd>
                    <dt>Programatic one: value set to undefined</dt>
                    <dd><div id="programatic13"></div></dd>
                    <dt>Programatic one: value set to 'undefined'</dt>
                    <dd><div id="programatic14"></div></dd>
                </dl>
                <button id="boton" dojoType="dijit.form.Button">Obtener valores</button>
                <ul id="values">
                    <li>Nothing</li>
                </ul>
                <ul id="checked">
                    <li>Nothing</li>
                </ul>
            </div>
        </div>
    </body>
</html>

