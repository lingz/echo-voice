var Firebase = require("firebase");
var WorkQueue = require("./workqueue.js");

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

  var fileName = "./tmp/" + userId + "_" + conferenceCode + ".64";

  fs.writeFile(fileName, base64Data, function(err) {
    if (err) {
      console.log(err);
    } else {
      var out_name = fileName.replace(/.64$/, ".wav");
      var convert_to_wav = exec("base64 -d < " + fileName + " >  " + out_name, function(error, stdout, stderr) {
        var split_to_fragments = exec("sphinx_cont_seg -infile " + out_name, function(error, stdout, stderr) {
          if (err) {
	    /\d+ Utterance: [^ ]+/.
          }
        });
      });
    }
  }

  //Call finished notifier when done
  whenFinished();
}

new WorkQueue(itemsRef, workCallback);
