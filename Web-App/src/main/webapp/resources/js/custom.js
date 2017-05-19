/**
 * Copyright 2016 The Johns Hopkins University Applied Physics Laboratory LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
var linePlot;

$(document).ready(function() {

    $("#lineplot-canvas-id").hide();
    $("#image-canvas-id").hide();

    $("#text-input-type-rb").prop("checked", true);

    $("input[name='text']").change(function() {
        $("#speech-input-type-rb").prop("checked", false);
    });

    $("input[name='speech']").change(function() {
        $("#text-input-type-rb").prop("checked", false);
    });

    $("#start-button").click(function() {
        if ($("#text-input-type-rb").is(':checked')) {
            sendPost($("#question-input-id").val());
        } else if ($("#speech-input-type-rb").is(':checked')) {
            runSpeechToText();
        }
    });

});

function sendPost(question) {

    $("#lineplot-canvas-id").hide();
    $("#image-canvas-id").hide();

    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        type: "POST",
        url: "api/request",
        data: JSON.stringify({
            "text": question
        }),
        dataType: 'json',
        success: function(response) {

            $('#question-input-id').val(question);

            if (response.type == "error") {

                $('#answer-output-id').val(response.error.message);

                if ($("#speech-input-type-rb").is(':checked')) {
                    var msg = new SpeechSynthesisUtterance(response.error.message);
                    msg.lang = 'en-US';
                    window.speechSynthesis.speak(msg);
                }

            } else {

                $('#answer-output-id').val(response.text);

                if ($("#speech-input-type-rb").is(':checked')) {
                    var msg = new SpeechSynthesisUtterance(response.text);
                    msg.lang = 'en-US';
                    window.speechSynthesis.speak(msg);
                }

                if (response.type == "json") {
                    payloadObj = (JSON.parse(response.payload));
                    if (payloadObj.plotType == "lineplot") {
                        plotLineplot(payloadObj);
                    }
                }

                if (response.type == "image_embed") {
                    showImage(response.payload);
                }

            }

        },
        error: function(e) {

        }
    });
}

function plotLineplot(payloadObj) {

    if (linePlot != null) {
        linePlot.destroy();
    }

    dataObject = JSON.parse(payloadObj.data);

    datasetsObjectFormatted = [];

    //create color array to rotate through (color of line in line plot) 
    rgbColorFormatter = ["rgba(0,225,204,0.3)", //teal 
        "rgba(153,194,255,0.3)", //light blue 
        "rgba(51,133,255,0.3)", //royal blue
        "rgba(128,255,170,0.3)", //light green 
        "rgba(0,38,153,0.3)", //dark blue
        "rgba(0,102,0,0.3)", //dark green 
        "rgba(255,0,0,0.2)", //red 
        "rgba(255,51,204,0.3)", //pink 
        "rgba(255,136,77,0.3)", //orange 
        "rgba(153, 51, 255,0.2)" //purple
    ];

    counter = 0;
    for (var key in dataObject) {

        if (dataObject.hasOwnProperty(key)) {

            datasetsObjectFormatted.push({
                label: key,
                backgroundColor: rgbColorFormatter[counter % 10],
                borderColor: rgbColorFormatter[counter % 10],
                boarderWidth: 1,
                hoverBorderColor: rgbColorFormatter[counter % 10],
                hoverBackgroundColor: rgbColorFormatter[counter % 10],
                data: dataObject[key]
            });

        }

        counter++;
    }

    var labels = [];
    if (payloadObj.labels == null) {
        for (var i = 0; i < JSON.parse(payloadObj.data).length; i++) {
            labels.push("");
        }
    } else {
        labels = JSON.parse(payloadObj.labels);
    }

    $("#lineplot-canvas-title-id").html(payloadObj.title);
    $("#xaxis-lineplot-canvas-id").html(payloadObj.xaxis);
    $("#yaxis-lineplot-canvas-id").html(payloadObj.yaxis);

    var lineChartData = {
        labels: labels,
        datasets: datasetsObjectFormatted
    }

    $("#lineplot-canvas-id").show();

    var ctx = document.getElementById("lineplot-canvas-graph-id").getContext("2d");
    linePlot = new Chart(ctx, {
        type: 'line',
        data: lineChartData,
        options: {
            responsive: true,
            animation: {
                onComplete: lineplotDoneDrawing,
            },
        }
    });

    function lineplotDoneDrawing() {
        $("#export-canvas-button").html("<button type='button' class='pull-right btn btn-default' id='export-canvas-button'><a download='" + payloadObj.title + ".png' href=" + ctx.canvas.toDataURL() + ">Export</a></button>");
    }
}

function showImage(base64Str) {
    $("#image-canvas-id").show();
    $("#image-id").html("<img class='panel-body centerImage' src='data:image/jpeg;base64," + base64Str + "'>");
    $("#export-image-button").html("<button type='button' class='pull-right btn btn-default' id='export-image-button'><a download='image.png' href='data:image/png;base64," + base64Str + "'>Export</a>");
}