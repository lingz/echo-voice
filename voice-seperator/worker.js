var Firebase = require("firebase");
var WorkQueue = require("./workqueue.js");
var child_process = require("child_process");
var exec = child_process.exec;
var spawn = child_process.spawn;

var dirPath = __dirname;

var voiceRecognition = spawn("java -jar " + dirPath + "../voice-transcriber/target/voice-engine-0.1.0-jar-with-dependencies.jar", function(error, stdout, stderr) {
  if (err) {
    console.log("Err initiliazing voice transcriber");
  } else {
    console.log("Voice transcriber successfully started");
  }
});



var workCallback = function(data, base64Store, whenFinished) {

  //This is where we actually process the data. We need to call "whenFinished" when we're done
  //to let the queue know we're ready to handle a new job.
  console.log("Started Processing: " + data.number);
  console.log("Processing data at id: " + data.id);
  var child = base64Store.child(data.id);

  var jobPackage = child.val();

  if (!jobPackage) {
    return;
  }

  var conferenceCode = jobPackage.conferenceCode;
  var userId = jobPackage.userId;
  var base64Data = jobPackage.base64;

  var fileName = dirPath + "/tmp/" + userId + "_" + conferenceCode + ".64";

  fs.writeFile(fileName, base64Data, function(err) {
    if (err) {
      console.log(err);
    } else {
      var out_name = fileName.replace(/.64$/, ".wav");
      var convert_to_wav = exec("base64 -d < " + fileName + " >  " + out_name, function(error, stdout, stderr) {
        var split_to_fragments = exec("sphinx_cont_seg -infile " + out_name, function(error, stdout, stderr) {
          if (err) {
            console.log("ERR: " + err);
          } else {
	    files_to_convert = []
            while ((matches = /\d+ Utterance: ([^ ])+/g.exec(stdout)) != null) {
	      files_to_convert.push(matches[1])
	    }
          }
        });
      });
    }
  });

  var requestTranscript = function(files_list, current_index) {
    voiceRecognition.stdin.write(files_list[current_index))
    sentenceFragments = {sentences: [])
    voiceRecognition.on("data", function(data){
      if (data != "*****") {
	sentenceFragments.sentences.push(data);
      } else {
	// write out to firebase here
        if (current_index < files_list.length - 1) {
          requestTranscript(files_list, current_index + 1)
        } else {
          whenFinished();
        }
      }
      
    });
    
  }
}



new WorkQueue(itemsRef, workCallback);
