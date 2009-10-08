<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
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
                isDebug: true
            };
        </script>
        <script type="text/javascript" src="js/dojo_toolkit/dojo/dojo.js"></script>
        <script type="text/javascript" src="js/testTrackingForm.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.registerModulePath("grc", "../../grc");
                dojo.require("dojo.parser");
                dojo.require("grc.dijit.form.EntityForm");
                dojo.require("grc.dijit.form.FormChangeTracker");
                dojo.require("dijit.form.TextBox");
                dojo.require("dijit.form.Button");
                dojo.require("dojox.form.BusyButton");
                dojo.require("dijit.layout.BorderContainer");
                dojo.require("dijit.layout.ContentPane");        

                dojo.parser.parse();

                testTrackingForm.init();
            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <form dojoType="grc.dijit.form.EntityForm" id="grcFormTest_testForm">
                <table>
                    <tr>
                        <td><label for="testText">Texto de prueba</label></td>
                        <td><input dojoType="dijit.form.TextBox" name="testText" id="grcFormTest_testText" value="" intermediateChanges ="true"/></td>
                    </tr><tr>
                        <td><label for="testText2">Texto de prueba2</label></td>
                        <td><input dojoType="dijit.form.TextBox" name="testText2" id="grcFormTest_testText2" value=""/></td>
                    </tr>
                    <tr>
                        <td colspan="2" style="text-align:right">
                            <button dojoType="dijit.form.Button"
                                    id="grcFormTest_submitButton"
                                    onClick="testTrackingForm.onSubmit">
                                Enviar
                            </button>
                            <button dojoType="dijit.form.Button"
                                    id="grcFormTest_cancelButton">
                                Reset
                            </button>
                        </td>
                    </tr>
                </table>
            </form>
        </div>
    </body>
</html>

