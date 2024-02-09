import React, {useEffect, useState} from 'react';
import {
  StyleSheet,
  Text,
  View,
  NativeModules,
  DeviceEventEmitter,
  Platform,
  Alert,
} from 'react-native';

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

const styles = StyleSheet.create({});
