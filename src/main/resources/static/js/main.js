$(document).ready(function () {

    $("#btnSubmit").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        uploadData();

    });
    
    $("#btnSubmitLuceneQueryLanguage").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        searchLuceneQueryLanguage();

    });
    
    $("#btnSubmitLuceneTermQuery").click(function (event) {

        //stop submit the form, we will post it manually.
        event.preventDefault();

        searchLuceneTermQuery();

    });

});

function uploadData() {

    // Get form
    var form = $('#fileUploadForm')[0];

    var data = new FormData(form);

    $("#btnSubmit").prop("disabled", true);

    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "/index/add",
        data: data,
        processData: false, //prevent jQuery from automatically transforming the data into a query string
        contentType: false,
        cache: false,
        timeout: 600000,
        success: function (data) {
        	$('#result').empty();
            $("#result").text(data);
            console.log("SUCCESS : ", data);
            $("#btnSubmit").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').empty();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmit").prop("disabled", false);

        }
    });

}

function searchLuceneQueryLanguage() {

    var value = $('#luceneQueryLanguage input[name=value]').val();
    var data = JSON.stringify({"value":value});
    $("#btnSubmitLuceneQueryLanguage").prop("disabled", true);

    $.ajax({
        type: "POST",
        url: "/search/queryParser",
        data: data,
        contentType: 'application/json',
        success: function (data) {
        	$('#result').empty();
            for(index = 0; index < data.length; index++){
                var result = data[index]
                $.each(result, function(key, value) {
                  $('#result').append('<li>' + key + ': ' + value + '</li>');
                });
            }
            console.log("SUCCESS : ", data);
            $("#btnSubmitLuceneQueryLanguage").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').empty();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmitLuceneQueryLanguage").prop("disabled", false);

        }
    });

}

function searchLuceneTermQuery() {

    var field = $('#luceneTermQuery input[name=field]').val();
    var value = $('#luceneTermQuery input[name=value]').val();
    var data = JSON.stringify({"field":field, "value":value});
   
    $("#btnSubmitLuceneTermQuery").prop("disabled", true);

    $.ajax({
        type: "POST",
        url: "/search/term",
        data: data,
        contentType: 'application/json',
        success: function (data) {

        	$('#result').empty();
            for(index = 0; index < data.length; index++){
                var result = data[index]
                $.each(result, function(key, value) {
                  $('#result').append('<li>' + key + ': ' + value + '</li>');
                });
            }
            console.log("SUCCESS : ", data);
            $("#btnSubmitLuceneTermQuery").prop("disabled", false);

        },
        error: function (e) {
        	$('#result').empty();
            $("#result").text(e.responseText);
            console.log("ERROR : ", e);
            $("#btnSubmitLuceneTermQuery").prop("disabled", false);

        }
    });

}