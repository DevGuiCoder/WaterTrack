const firebaseConfig = {
    apiKey: "AIzaSyB_kPPmOi8QwzHlIKZ_ka1M8jhcM3Lh5bs",
    authDomain: "watertrack-e0dfa.firebaseapp.com",
    projectId: "watertrack-e0dfa",
    storageBucket: "watertrack-e0dfa.appspot.com",
    messagingSenderId: "100430156284294668077",
    appId: "watertrack-e0dfa" // Substitua pelo seu App ID real
  };
  
  // Inicialização do Firebase
  firebase.initializeApp(firebaseConfig);
  const auth = firebase.auth();