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
        <link rel="stylesheet" href="js/dojo_toolkit/dojox/form/resources/FileInput.css" />
        <link rel="stylesheet" href="js/grc/dijit/form/resources/FileInput.css" />
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
        <script type="text/javascript" src="js/grc/grc.js"></script>
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.registerModulePath("grc", "../../grc");
                dojo.require("dojo.parser");
                dojo.require("grc.string");
                dojo.require("grc.dijit.form");
                dojo.require("dijit.form.Form");
                dojo.require("dijit.form.Button");
                dojo.require("grc.dijit.form.FileInput");

                dojo.parser.parse();
            }
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <div style="background-color: #cfcfcf;width: 700px; height: 450px;padding: 12px">
                <dl>
                    <dt>File</dt>
                    <dd>
                        <input dojoType="grc.dijit.form.FileInput"
                               name="fileForSomething"
                               id="fileForSomething"
                               required="true"
                               invalidMessage = "El archivo tiene que ser una imagen jpg, png o gif de menos de 500kbytes"
                               constraints="{types:'jpg,png,gif', maxSize:512000, minSize:100}"
                               messages="{types:'El archivo tiene que ser de tipo jpg, png o gif', size : 'El tamaño tiene que estar entre 100 bytes y 512 kbytes'}"/>
                    </dd>
                    <dt>Button</dt>
                    <dd>
                        <button dojoType="dijit.form.Button"
                                onclick="grc.byId('fileForSomething').validate(true)"
                                >Enviar</button>
                    </dd>
                </dl>
            </div>
        </div>
    </body>
</html>

