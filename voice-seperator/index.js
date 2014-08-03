var Firebase = require('firebase');
var fs = require("fs");
var exec = require("child_process").exec;

var fb = new Firebase("https://echo-transcript.firebaseio.com/");

var jobQueue = fb.child("jobs/0");

//var currentJob = 0;
//var jobCount = jobQueue.child("count", function(snapshot) {
  //currentJob()
//});

var processOne = jobQueue.on("value", function(snapshot) {
  var jobPackage = snapshot.val();
  if (!jobPackage) {
    return;
  }
  var conferenceCode = jobPackage.conferenceCode;
  var userId = jobPackage.userId;
  var base64Data = jobPackage.base64;

  var fileName = "./tmp/" + userId + "_" + conferenceCode + ".64";

  fs.writeFile(fileName, base64Data, function(err) {
    if (err) {
      console.log(err);
    } else {
      var out_name = fileName.replace(/.64$/, ".wav");
      var convert_to_wav = exec("base64 -d < " + fileName + " >  " + out_name, function(error, stdout, stderr) {
        var split_to_fragments = exec("sphinx_cont_seg -infile " + out_name, function(error, stdout, stderr) {
          if (err) {
            console.log(err);
          }
        });
      });
    }
  });

});

console.log("hello world");
