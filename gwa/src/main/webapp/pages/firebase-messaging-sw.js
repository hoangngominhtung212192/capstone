importScripts('https://www.gstatic.com/firebasejs/5.5.0/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/5.5.0/firebase-messaging.js');

var config = {
    apiKey: "AIzaSyCACMwhbLcmYliWyHJgfkd8IW6oPUoupIM",
    authDomain: "gunplaworld-51eee.firebaseapp.com",
    databaseURL: "https://gunplaworld-51eee.firebaseio.com",
    projectId: "gunplaworld-51eee",
    storageBucket: "gunplaworld-51eee.appspot.com",
    messagingSenderId: "22850579681"
};

firebase.initializeApp(config);
var messaging = firebase.messaging();
//
// messaging.setBackgroundMessageHandler(function (payload) {
//     console.log('[firebase-messaging-sw.js] Received background message ', payload);
//     // Customize notification here
//     var notificationTitle = 'Gunpla World';
//     var notificationOptions = {
//         body: 'aaaaaa'
//     };
//
//     return self.registration.showNotification(notificationTitle,
//         notificationOptions);
// })