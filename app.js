var crypto  = require('crypto');
var fs  = require('fs');

var algorithm =  'aes-128-cbc';

var key = new Buffer([1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6]); //sample encryption key
var iv = new Buffer([1,2,3,4,5,6,7,8,9,10,1,2,3,4,5,6]);  //initialization vector


function encryptThenDecrypt(inputFilePath, encryptedFilePath, decryptedFilePath ){

    doEncryption(inputFilePath, encryptedFilePath, function (err) {
        if(err) return console.log(err);

        console.log("Encryption completed");

        doDecryption(encryptedFilePath, decryptedFilePath, function(err){
            if(err) return console.log(err);

            console.log("Decryption completed");
        })
    })
}

function doEncryption(inputPath, outputPath, callback){
    var cipher = crypto.createCipheriv(algorithm, key, iv);

    var inputStream = fs.createReadStream(inputPath);
    var outputStream = fs.createWriteStream(outputPath);

    inputStream.on('data', function(data) {
        var buf = new Buffer(cipher.update(data), 'binary');
        outputStream.write(buf);
    });

    inputStream.on('end', function() {
        try {
            var buf = new Buffer(cipher.final('binary'), 'binary');
            outputStream.write(buf);
            outputStream.end();
            outputStream.on('close', function() {
                return callback();
            });
        } catch(e) {
            fs.unlink(outputPath);
            return callback(e);
        }
    });
}

function doDecryption(inputPath, outputPath, callback){
    var decipher = crypto.createDecipheriv(algorithm, key, iv);

    var inputStream = fs.createReadStream(inputPath);
    var outputStream = fs.createWriteStream(outputPath);

    inputStream.on('data', function(data) {
        var buf = new Buffer(decipher.update(data), 'binary');
        outputStream.write(buf);
    });

    inputStream.on('end', function() {
        try {
            var buf = new Buffer(decipher.final('binary'), 'binary');
            outputStream.write(buf);
            outputStream.end();
            outputStream.on('close', function() {
                return callback();
            });
        } catch(e) {
            fs.unlink(outputPath);
            return callback(e);
        }
    });
}

//TODO: uncomment and use the desired file locations
//encryptThenDecrypt('/Users/x-man/Documents/video.mp4', '/Users/x-man/Documents/encrypted.video.dat', '/Users/x-man/Documents/output.video.mp4');

