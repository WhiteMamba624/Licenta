const functions = require('firebase-functions');
const admin = require('firebase-admin');

const cors = require('cors')({ origin: true });
const moment = require('moment');

admin.initializeApp();

exports.sendDailyNotifications = functions.https.onRequest((request, response) => {

    cors(request, response, () => {
  
       const now = moment();
       const dateFormatted = now.format('DDMMYYYY');

       admin.firestore()
       .collection("Users")
       .document()
       .collection("Documents")
       .where("expiryDate", "==", dateFormatted)
       .get()
       .then(function(querySnapshot) {

           const promises = []; 

           querySnapshot.forEach(doc => {
 
               const tokenId = doc.data().tokenId;  
               const notificationContent = {
                 notification: {
                    title: "Warning",
                    body: "Your document is about to expire!!",  
                    icon: "default",
                    sound : "default"
                 }
              };

              promises
              .push(admin.messaging().sendToDevice(tokenId, notificationContent));      
  
          });
          return Promise.all(promises);
       })
       .then(results => {
            response.send(data)
       })
       .catch(error => {
          console.log(error)
          response.status(500).send(error)
       });

    });

});