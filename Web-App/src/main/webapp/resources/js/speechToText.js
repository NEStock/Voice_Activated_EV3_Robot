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
var finalTranscript = '';
var recognizing = false;
var recognition;

$(document).ready(function() {
    
    if (!navigator.onLine) {
        $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                + "<strong> Error! </strong> Network communication is required to use speech recognition.");
        showAlert("recognition-error-id");
        $('#speech-input-type-rb').prop("disabled", true);
    }else if (!('webkitSpeechRecognition' in window)) {
        $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                + "<strong> Error! </strong> Sorry, your Browser does not support the Speech API.");
        showAlert("recognition-error-id");
        $('#speech-input-type-rb').prop("disabled", true);
              
    } else {

        recognition = new webkitSpeechRecognition();
        recognition.continuous = true;
        recognition.interimResults = true;
        recognition.lang = 'en-US';

        recognition.onstart = function() {
            $('#question-input-id').val('');
            $('#answer-output-id').val('');

            recognizing = true;
            $('#instructions').html('Speak slowly and clearly');
        };

        recognition.onerror = function(event) {
            if (event.error == "no-speech") {
                $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                           + "<strong> Error! </strong> No speech was detected.");
            } else if (event.error == "audio-capture") {
                $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                        + "<strong> Error! </strong> Audio capture failed.");
            } else if (event.error == "network") {
                $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                        + "<strong> Error! </strong> Some network communication that was required to complete the recognition failed.");
                $("#speech-input-type-rb").prop("checked", false);
                $("#text-input-type-rb").prop("checked", true);
            } else if (event.error == "not-allowed") {
                $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                        + "<strong> Error! </strong> The user agent is not allowing any speech input to occur for reasons of security, " 
                        + "privacy or user preference.");
            } else if (event.error == "bad-grammar") {
                $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                        + "<strong> Error! </strong> There was an error in the speech recognition grammar or semantic tags, or the grammar " 
                        + "format or semantic tag format is unsupported.");
            } else if (event.error == "language-not-supported") {
                $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                        + "<strong> Error! </strong> The language was not supported.");
            } else {
                $('#recognition-error-id').html("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a> " 
                        + "<strong> Error! </strong> There was a recognition error.");
            }
            showAlert("recognition-error-id");
        };

        recognition.onend = function() {
            recognizing = false;
            $('#instructions').html('&nbsp;');
            $('question-input-id').val(finalTranscript);

        };

        recognition.onresult = function(event) {
            var interimTranscript = '';
            for (var i = event.resultIndex; i < event.results.length; ++i) {
                if (event.results[i].isFinal) {
                    finalTranscript += event.results[i][0].transcript;
                } else {
                    interimTranscript += event.results[i][0].transcript;
                }
            }
            if (finalTranscript.length > 0) {
                sendPost(finalTranscript);
                recognition.stop();
                $('#start-button').html('Ask Question');
                recognizing = false;
            }
        }
    }
});

function runSpeechToText() {

    $('#start-button').html('Submit');

    if (recognizing) {
        recognition.stop();
        $('#start-button').html('Ask Question');
        recognizing = false;
    } else {
        finalTranscript = '';
        recognition.start();
        $('#instructions').html('Allow the browser to use your Microphone');
    }

}

function showAlert(alertId) {
    $("#" + alertId).addClass("in");
}