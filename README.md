# React Native Otp Listener ✉️
- This serves as a demonstration project for accessing SMS using a broadcast receiver. To integrate this functionality into your React Native project, kindly follow the outlined steps below. :fire:

# Getting Started

>**Note**: Make sure you have completed the [React Native - Environment Setup](https://reactnative.dev/docs/environment-setup) instructions till "Creating a new application" step, before proceeding.

## Step 1: Start the Metro Server

First, you will need to start **Metro**, the JavaScript _bundler_ that ships _with_ React Native.

To start Metro, run the following command from the _root_ of your React Native project:

```bash
# using npm
npm start

# OR using Yarn
yarn start
```

## Step 2: Start your Application

Let Metro Bundler run in its _own_ terminal. Open a _new_ terminal from the _root_ of your React Native project. Run the following command to start your _Android_ or _iOS_ app:

### For Android

```bash
# using npm
npm run android

# OR using Yarn
yarn android
```

## Step 3: Configure your App with this changes

Now that you have successfully run the app, let's modify it.

1. Open android/app/build.gradle file and add following code in dependencies.
```bash
implementation 'com.google.android.gms:play-services-auth-api-phone:18.0.1'
```

2. Copy Following files from android/app/src/main/java to your project.
>**Note**: Don't forgot to change your package name on top of this files.
- Constants.kt
- SmsRetrieveBroadcastReceiver.java
- SMSRetrievedModules.kt
- SMSRetrivedPackage.kt

3. Now change your MainApplication.kt file to import SMSRetriver package.
>**eg**: You can check my project's MainApplication.kt for your reference.
```bash
packages.add(SMSRetrivedPackage())
``` 

4. Now, This code will use to implement SMS listener in React native.
```bash
import React, {useEffect, useState} from 'react';
import { Text, View, NativeModules, DeviceEventEmitter, Platform } from 'react-native';

const App = () => {
  const SMSRetrived = NativeModules.SMSRetrived; //Getting module from NativeModules.
  const [otp, setOTP] = useState('');

  useEffect(() => {
    setupMsgListener();

    return () => {
      DeviceEventEmitter.removeAllListeners('SMS_CONSTANT_EVENT');
    };
  }, []);

  const setupMsgListener = async () => {
    try {
      if (Platform.OS == 'android') {
        const getSMSMessage = await SMSRetrived.checkUpdate();

        DeviceEventEmitter.addListener('SMS_CONSTANT_EVENT', listenOtp); //Adding Event liteners for SMS
      }
    } catch (e) {
      // error
    }
  };

  // Handler for recieved SMS
  const listenOtp = (data: any) => {
    if (data && data.receivedOtpMessage != null) {
      var msg = data.receivedOtpMessage;
      var code = (msg.match(/\d{6}/) || [false])[0];
      setOTP(code); //Setting code in State
    }
  };

  return (
    <View style={{flex: 1, justifyContent: 'center', alignItems: 'center'}}>
      <Text style={{fontSize: 18, color: '#000'}}>Received OTP: {otp}</Text>
    </View>
  );
};

export default App;
``` 

## Congratulations! :tada:

You've successfully run and modified your React Native App. :partying_face:

### Let's See Preview 🙈

![](./resource/otp_demo.gif)

## Authors

- [@NirmalsinhRathod](https://github.com/NirmalsinhRathod)

![Logo](https://cdn-ggkmd.nitrocdn.com/BzULJouLEmmtjCpJwHCmTIgakvECFbms/assets/images/optimized/rev-f1e70e0/www.creolestudios.com/wp-content/uploads/2021/07/cs-logo.svg)