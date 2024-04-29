import Slider from '@react-native-community/slider';
import { useEffect, useLayoutEffect, useState } from 'react';
import { Image, Text, TouchableOpacity, View } from 'react-native';

import { IconSet } from '../../hooks/useCustomIcons';
import { useLightingControls } from '../../hooks/useLightingControls';
import { styles } from '../../styles';
import { MenuStrip } from './MenuStrip';

const Editor = ({ navigation, route }) => {
  const { uri, imagePath } = route.params;
  const { LightingControls } = useLightingControls();

  const [image, setImage] = useState(uri);
  const [original, setOriginal] = useState(imagePath);
  const [exposure, setExposure] = useState(0.0);
  const [contrast, setContrast] = useState(1);
  const [sharpness, setSharpness] = useState(0.0); 
  useEffect(() => {
    if (!imagePath) return;
    LightingControls.readImage(imagePath, response => {
      console.log(response);
    });
  }, [original]);

  const handleExposureChange = value => {
    value = Math.round(value * 100) / 100;
    setExposure(value);

    LightingControls.changeExposure(value, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };

  const handleContrastChange = value => {
    value = Math.round(value * 100) / 100;
    setContrast(value);
  
    // Normalize the slider value to the desired contrast range
    const normalizedValue = value * 2;
  
    LightingControls.changeContrast(normalizedValue, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };
  const handleSharpnessChange = value => {
    value = Math.round(value * 100) / 100;
    setSharpness(value);

    // Call the changeSharpness method with the updated sharpness value
    LightingControls.changeSharpness(value, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };

  
  

  useLayoutEffect(() => {
    navigation.setOptions({
      headerTitle: 'Edit',
      headerShown: true,
      headerStyle: {
        backgroundColor: '#000000',
      },
      headerTintColor: '#FFFFFF',
      headerRight: () => (
        <TouchableOpacity
          style={[
            styles.button,
            {
              marginTop: 0,
              width: 120,
              backgroundColor: '#2b2b2b',
              flexDirection: 'row',
              alignItems: 'center',
              justifyContent: 'space-evenly',
            },
          ]}
          onPress={() => {
            // STUB: Save the image
          }}>
          <IconSet name="export" size={20} color="#FFFFFF" />
          <Text style={styles.buttonText}>Export</Text>
        </TouchableOpacity>
      ),
    });
  }, [navigation]);

  return (
    <View style={[styles.container, { paddingLeft: 0, paddingRight: 0 }]}>
      {image && (
        <Image
          source={{ uri: image }}
          style={{ width: '90%', height: 500, padding: 8 }}
          cache="only-if-cached"
          resizeMode="contain"
          fadeDuration={0}
        />
      )}

      <MenuStrip />

      {/* Slider for exposure */}
      <Slider
        style={{ width: '80%', height: 40, marginTop: 16, marginBottom: 16 }}
        minimumValue={2.0}
        maximumValue={0.2}
        thumbTintColor="#7E84F7"
        minimumTrackTintColor="#7E84F7"
        maximumTrackTintColor="#FFFFFF"
        onSlidingComplete={e => handleExposureChange(e)}
        value={0.0}
      />

      {/* Slider for contrast */}
      <Slider
        style={{ width: '80%', height: 40, marginTop: 16, marginBottom: 16 }}
        minimumValue={0.1}
        maximumValue={2.0}
        thumbTintColor="#7E84F7"
        minimumTrackTintColor="#7E84F7"
        maximumTrackTintColor="#FFFFFF"
        onSlidingComplete={e => handleSharpnessChange(e)}
        value={1.0}
      />
    </View>
  );
};

export { Editor };
