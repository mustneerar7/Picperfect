import Slider from '@react-native-community/slider';
import React, {useState} from 'react';
import {
  View,
  Text,
  StyleSheet,
  StatusBar,
  Image,
  Pressable,
} from 'react-native';

import useLightingControls from './src/hooks/useLightingControls';

const App = (): React.JSX.Element => {

  // state to hold the response from the native module
  const [responseFromNative, setResponseFromNative] = useState(
    'Results from native module will appear here',
  );

  // access Lighting Controls from the custom hook
  const {LightingControls} = useLightingControls();

  // sends the value to the native module.
  // The native module will then change the value and return the response.
  const getNativeResponse = (value: number) => {
    LightingControls.changeExposure(
      value,
      (error: string, response: string) => {
        if (error) {
          console.error(error);
          return;
        } else {
          setResponseFromNative(response);
          console.log(response);
        }
      },
    );
  }

  // Presentational component
  return (
    <View style={Styles.container}>
      <Image
        style={Styles.imageContainer}
        source={require('./src/assets/images/sample.jpg')}
      />
      <Text style={Styles.heading}>Awaken your inner artist</Text>
      <Text style={Styles.body}>
        Blend colors, illuminate scenes, recompose in post and much more
      </Text>
      <Slider
        style={{width: '86%', alignSelf: 'center', margin: 16}}
        minimumValue={0}
        maximumValue={1}
        minimumTrackTintColor="#7E84F7"
        maximumTrackTintColor="#7E84F7"
        thumbTintColor="#7E84F7"
        onValueChange={value => getNativeResponse(value)}
      />
      <Text style={Styles.body}>{responseFromNative}</Text>
      <Pressable style={Styles.buttonContainer}>
        <Text style={Styles.buttonText}>Get Started</Text>
      </Pressable>
      <StatusBar translucent={true} backgroundColor={'transparent'} />
    </View>
  );
};

const Styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#111111',
  },
  imageContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    resizeMode: 'cover',
    width: '100%',
    height: '50%',
    marginBottom: 32,
  },
  buttonContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#7E84F7',
    padding: 12,
    borderRadius: 8,
    margin: 16,
    width: '86%',
    height: 44,
    alignSelf: 'center',
    position: 'absolute',
    bottom: 64,
  },
  heading: {
    fontSize: 28,
    fontFamily: 'Inter-SemiBold',
    padding: 16,
    paddingBottom: 0,
    textAlign: 'center',
    color: '#ffffff',
  },
  body: {
    fontSize: 14,
    fontFamily: 'Inter-Regular',
    padding: 16,
    paddingTop: 12,
    textAlign: 'center',
    color: '#ffffff',
  },
  buttonText: {
    fontSize: 14,
    fontFamily: 'Inter-SemiBold',
    color: '#ffffff',
  },
});

export default App;
