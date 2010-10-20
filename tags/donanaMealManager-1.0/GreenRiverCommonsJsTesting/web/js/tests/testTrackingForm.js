var testTrackingForm = {
    form: null,
    saveButton: null,
    saveBusyButton: null,
    cancelButton: null,
    data: {
        testText: 'Texto de pruebas',
        testText2: 'Otro texto de pruebas',
        hiddenValue: 'valor oculto'
    }
};

testTrackingForm.init = function() {
    testTrackingForm.form = dijit.byId('grcFormTest_testForm');
    testTrackingForm.saveButton = dijit.byId('grcFormTest_submitButton');
    testTrackingForm.saveBusyButton = dijit.byId('grcFormTest_submitBusyButton');
    testTrackingForm.cancelButton = dijit.byId('grcFormTest_cancelButton');

    testTrackingForm.form = new grc.dijit.form.FormChangeTracker(testTrackingForm.form);
    testTrackingForm.form.setSubmitButton(testTrackingForm.saveButton);
    testTrackingForm.form.initializeValuesFrom(testTrackingForm.data, 'grcFormTest_');
    dojo.connect(testTrackingForm.cancelButton, 'onClick', testTrackingForm.form, testTrackingForm.form.reset);
    
    testTrackingForm.form.enableChangeTracking();
}

testTrackingForm.onSubmit = function() {
    var changedData = dojo.mixin({}, testTrackingForm.data);
    var str = "";
    
    testTrackingForm.form.copyValuesTo(changedData, 'grcFormTest_');

    for (var field in changedData) {
        str += field + " = " + changedData[field] + "\n";
    }

    alert(str);
}
