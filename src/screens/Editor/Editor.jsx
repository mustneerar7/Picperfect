import Slider from '@react-native-community/slider';
import {useEffect, useLayoutEffect, useState} from 'react';
import {Image, Text, TouchableOpacity, View} from 'react-native';

import {IconSet} from '../../hooks/useCustomIcons';
import {useLightingControls} from '../../hooks/useLightingControls';
import {styles} from '../../styles';
import {MenuStrip} from './MenuStrip';

/**
 * Renders the Editor Screen component.
 * Allow Editing images.
 * @param {object} route - To access passed properties.
 * @returns {JSX.Element} The rendered Home screen component.
 */
const Editor = ({navigation, route}) => {
  // HOOKS
  const {uri, imagePath} = route.params;
  const {LightingControls} = useLightingControls();

  // STATES
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

  // show header using useLayoutEffect
  useLayoutEffect(() => {
    navigation.setOptions({
      headerTitle: 'Edit',
      headerShown: true,
      // change background color of header
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

  // Presentation
  return (
    <View style={[styles.container, {paddingLeft: 0, paddingRight: 0}]}>
      {image && (
        <Image
          source={{uri: image}}
          style={{width: '90%', height: 500, padding: 8}}
          cache="only-if-cached"
          resizeMode="contain"
          fadeDuration={0}
        />
      )}

      {/* The menubar at the bottom showing controls. */}
      <MenuStrip />

      {/* Exposure Slider. Would be moved to sub menu. */}
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
