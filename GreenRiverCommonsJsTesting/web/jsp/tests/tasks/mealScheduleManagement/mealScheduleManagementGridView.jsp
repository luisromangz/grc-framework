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
        <link rel="stylesheet" href="${dojoRoot}/dojox/grid/resources/tundraGrid.css" />
        <style media="all" type="text/css">
            #body {
                padding: 12px;
                margin: 0px auto 0px auto;
            }
            .fakeLinkElement {
                color: blue;
                cursor: pointer;
            }
            .fakeLinkElement:hover {
                text-decoration:underline;
            }
            .grcMealSelectionGridViewHeaderCell {
                text-align: center;
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
        <script type="text/javascript">dojo.registerModulePath("tasks", "../../tasks");</script>
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
        <script type="text/javascript" src="${jsRoot}/tasks/mealScheduleManagement/_MealScheduleData.js"></script>
        <script type="text/javascript" src="${jsRoot}/tasks/mealScheduleManagement/_MealSelectionHelper.js"></script>
        <script type="text/javascript" src="${jsRoot}/tasks/mealScheduleManagement/_MealSelectionPanel.js"></script>
        <script type="text/javascript" src="${jsRoot}/tasks/mealScheduleManagement/MealSelectionTableView.js"></script>
        <script type="text/javascript" src="${jsRoot}/tasks/mealScheduleManagement/MealSelectionGridView.js"></script>
       
        <script type="text/javascript">
            var onDojoLoad = function() {
                dojo.require("dojo.parser");
                dojo.parser.parse();

                var structure = [
                    {
                        cells: [
                            {width: 'auto', headerStyles:'text-align:center'},
                            {width: 'auto', headerStyles:'text-align:center'},
                            {width: 'auto', headerStyles:'text-align:center'},
                            {width: 'auto', headerStyles:'text-align:center'},
                            {width: 'auto', headerStyles:'text-align:center'}
                        ],
                        type: tasks.mealScheduleManagement.MealSelectionTableView
                    }
                ];
                var data = {
                    columns: [
                        {
                            mealKind: 'Desayuno',
                            ids: [1],
                            hours: [
                                {
                                    index: 8*100,
                                    hour: grc.date.createDate(8, 0, 0),
                                    menus: [{
                                       id: 14,
                                       name: 'Invierno',
                                       type: 'tururú'
                                    }]
                                }
                            ]
                        },
                        {
                            mealKind: 'Almuerzo',
                            ids: [2],
                            hours: [
                                {
                                    index: 14*100 + 30,
                                    hour: grc.date.createDate(14, 30, 0),
                                    menus: [{
                                       id: 14,
                                       name: 'Invierno',
                                       type: 'tururú'
                                    }]
                                }
                            ]
                        },
                        {
                            mealKind: 'Merienda',
                            ids: [3],
                            hours: [
                                {
                                    index: 18*100,
                                    hour: grc.date.createDate(18, 0, 0),
                                    menus: [{
                                       id: 14,
                                       name: 'Invierno',
                                       type: 'tururú'
                                    }]
                                }
                            ]
                        },
                        {
                            mealKind: 'Cena',
                            ids: [4],
                            hours: [
                                {
                                    index: 22*100 + 30,
                                    hour: grc.date.createDate(22, 30, 0),
                                    menus: [{
                                       id: 14,
                                       name: 'Invierno',
                                       type: 'tururú'
                                    }]
                                }
                            ]
                        }
                    ],
                    days: [
                        {
                            day:new Date(), 
                            meals:[
                                {
                                    everyDayMealId: 1,
                                    mealKind: 'Desayuno',
                                    menuName: 'Invierno',
                                    selected: true,
                                    optionalDishes: [
                                        {
                                            id: 11,
                                            name: 'Jalbóndigas',
                                            selected: false
                                        }
                                    ]
                                },
                                {
                                    everyDayMealId: 2,
                                    mealKind: 'Almuerzo',
                                    menuName: 'Invierno',
                                    selected: false,
                                    optionalDishes: [
                                        {
                                            id: 21,
                                            name: 'Papas al ali-jolí',
                                            selected: false
                                        }
                                    ]
                                },
                                {
                                    everyDayMealId: 3,
                                    mealKind: 'Merienda',
                                    menuName: 'Invierno',
                                    selected: true,
                                    optionalDishes: [
                                        {
                                            id: 31,
                                            name: 'Pastel puaf!',
                                            selected: true
                                        }
                                    ]
                                },
                                {
                                    everyDayMealId: 4,
                                    mealKind: 'Cena',
                                    menuName: 'Invierno',
                                    selected: false,
                                    optionalDishes: [
                                        {
                                            id: 41,
                                            name: 'Vaca al orneut rellenè de pajaritès',
                                            selected: false
                                        }
                                    ]
                                }
                            ]}
                    ]/*,
                    structure: structure*/
                };
                var grid = dijit.byId('grid');
                grid.setDataSource(data);
                //grid.attr('structure', structure);
                setTimeout(function() {
                    grid.setDataSource(data);
                }, 10000);
            };
            dojo.addOnLoad(onDojoLoad);
        </script>
    </head>
    <body class="tundra">
        <div id="body">
            <div style="background-color: #cfcfcf;min-width: 400px; min-height: 250px;padding: 12px">
                <div id="grid"
                     height="14em"
                     dojoType="tasks.mealScheduleManagement.MealSelectionGridView">
                </div>
                <div id="target"></div>
            </div>
        </div>
    </body>
</html>

