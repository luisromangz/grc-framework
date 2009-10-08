<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
        <title>Testing</title>
        <link rel="stylesheet" href="js/dojo_toolkit/dojo/resources/dojo.css" />
        <link rel="stylesheet" href="js/dojo_toolkit/dijit/themes/tundra/tundra.css" />
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
        <script type="text/javascript" src="js/dojo_toolkit/dojo/dojo.js"></script>
        <script type="text/javascript">dojo.registerModulePath("grc", "../../grc");</script>
        <script type="text/javascript" src="js/grc/grc.js"></script>
        <script type="text/javascript" src="js/grc/dijit/form/EntityForm.js"></script>
        <script type="text/javascript" src="js/grc/dijit/form/FormChangeTracker.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.require("dojo.parser");
                dojo.require("grc.string");
                dojo.require("grc.dijit.form");
                dojo.require("dijit.form.Form");
                dojo.require("dijit.form.Button");
                dojo.require("dijit.form.TextBox");
                dojo.require("grc.dijit.form.EntityForm");

                dojo.parser.parse();

                var data = {name : 'Paco', address : 'aieieaieai'};
                dijit.byId('entityForm').setValuesFromObject(data, true);
                dijit.byId('entityForm2').enableChangeTracking();
                dijit.byId('entityForm2').setValuesFromObject(data, true);
            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <div style="background-color: #cfcfcf;width: 700px; height: 450px;padding: 12px">
                <p>change tracking by default, force intermediateChanges</p>
                <form dojoType="grc.dijit.form.EntityForm" id="entityForm" 
                      submitButton="submitButton" trackChanges="true">
                    <dl>
                        <dt>Nombre</dt>
                        <dd>
                            <input dojoType="dijit.form.TextBox"  id="entityForm_name"/>
                        </dd>
                        <dt>Dirección</dt>
                        <dd>
                            <input dojoType="dijit.form.TextBox"  id="entityForm_address"/>
                        </dd>
                    </dl>
                    <button dojoType="dijit.form.Button" id="submitButton">Enviar</button>
                    <button dojoType="dijit.form.Button" id="resetButton" onclick="dijit.byId('entityForm').reset();">Resetear</button>
                </form>
                <br/>
                <p>Change tracking programatically, force intermediate changes off</p>
                <form dojoType="grc.dijit.form.EntityForm" id="entityForm2" submitButton="submitButton2"
                      forceIntermediateChanges="false">
                    <dl>
                        <dt>Nombre</dt>
                        <dd>
                            <input dojoType="dijit.form.TextBox"  id="entityForm2_name"/>
                        </dd>
                        <dt>Dirección</dt>
                        <dd>
                            <input dojoType="dijit.form.TextBox"  id="entityForm2_address"/>
                        </dd>
                    </dl>
                    <button dojoType="dijit.form.Button" id="submitButton2">Enviar</button>
                    <button dojoType="dijit.form.Button" id="resetButton2" onclick="dijit.byId('entityForm2').reset();">Resetear</button>
                </form>
            </div>
        </div>
    </body>
</html>

