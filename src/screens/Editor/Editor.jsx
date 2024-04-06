import Slider from '@react-native-community/slider';
import {useEffect, useState} from 'react';
import {Image, Text, View, Alert} from 'react-native';

import {useLightingControls} from '../../hooks/useLightingControls';
import {styles} from '../../styles';

const Editor = ({navigation, route}) => {
  const {uri, imagePath} = route.params;
  const {LightingControls} = useLightingControls();

  // State to store the image uri and path
  const [image, setImage] = useState(uri);
  const [original, setOriginal] = useState(imagePath);

  // Read the image from the gallery on mount
  useEffect(() => {
    if (!imagePath) return;
    LightingControls.readImage(imagePath, response => {
      console.log(response);
    });
  }, [original]);

  // Handle exposure change
  const handleExposureChange = value => {
    // keep the value to 2 decimal places
    value = Math.round(value * 100) / 100;

    console.log(value);
    LightingControls.changeExposure(value, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };

  // Presentation
  return (
    <View style={styles.container}>
      {image && (
        <Image
          source={{uri: image}}
          style={{width: '100%', height: 500, padding: 8}}
          cache="only-if-cached"
          fadeDuration={0}
        />
      )}

      <Text style={[styles.body, {marginTop: 24}]}>Exposure</Text>
      <Slider
        style={{width: '80%', height: 40}}
        minimumValue={0}
        maximumValue={3.0}
        defaultValue={1.0}
        thumbTintColor="#7E84F7"
        minimumTrackTintColor="#7E84F7"
        maximumTrackTintColor="#FFFFFF"
        // when drag is released
        onSlidingComplete={e => handleExposureChange(e)}
      />
    </View>
  );
};

export {Editor};
