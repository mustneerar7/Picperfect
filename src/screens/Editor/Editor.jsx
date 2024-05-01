import Slider from '@react-native-community/slider';
import {useEffect, useLayoutEffect, useState} from 'react';
import {Image, Text, TouchableOpacity, View,ScrollView} from 'react-native';

import { IconSet } from '../../hooks/useCustomIcons';
import { useLightingControls } from '../../hooks/useLightingControls';
import { styles } from '../../styles';
import { MenuStrip } from './MenuStrip';

import { Alert } from 'react-native';

/**
 * Renders the Editor Screen component.
 * Allow Editing images.
 * @param {object} route - To access passed properties.
 * @returns {JSX.Element} The rendered Home screen component.
 */

const Editor = ({ navigation, route }) => {
  // HOOKS
  const { uri, imagePath } = route.params;
  const { LightingControls } = useLightingControls();

  // STATES
  const [image, setImage] = useState(uri);
  const [original, setOriginal] = useState(imagePath);
  const [mode, setMode] = useState('Exposure');
  const [isExporting, setIsExporting] = useState(false);

  // Read the image from the gallery on mount
  useEffect(() => {
    if (!imagePath) return;
    LightingControls.readImage(imagePath, response => {
      console.log(response);
    });
  }, [original]);

  // Handle exposure change
  const handleExposureChange = value => {
    // Keep the value to 2 decimal places
    value = Math.round(value * 100) / 100;
    console.log(value);
    LightingControls.changeExposure(value, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };

  // Handle shadow change
  const handleShadowChange = value => {
    // Keep the value to 2 decimal places
    value = Math.round(value * 100) / 100;
    console.log(value);
    LightingControls.changeShadows(value, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };

  // Handle Mid Tone Changes

  const handleMidToneChange = value => {
    // Keep the value to 2 decimal places
    value = Math.round(value * 100) / 100;
    console.log(value);
    LightingControls.changeMidtones(value, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };

  // Handle Highlight Changes
  const handleHighlightChange = value => {
    // Keep the value to 2 decimal places
    value = Math.round(value * 100) / 100;
    console.log(value);
    LightingControls.changeHighlights(value, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };

  // handle Noise Removal
  const handleNoiseRemoval = value => {
 // Keep the value to 2 decimal places
 value = Math.round(value * 100) / 100;
 console.log(value);
 LightingControls.reduceNoise(value, base64String => {
   setImage(`data:image/png;base64,${base64String}`);
 });
  };

  // handle Unsharp Masking
  const handleUnsharpMasking = value => {
    // Keep the value to 2 decimal places
    value = Math.round(value * 100) / 100;
    console.log(value);
    LightingControls.unsharpMask(value, base64String => {
      setImage(`data:image/png;base64,${base64String}`);
    });
  };

    // handle Export
    const handleExport = () => {
      setIsExporting(true);
      LightingControls.compressImage(response => {
        setIsExporting(false);
        Alert.alert('Image Exported', 'Image has been exported successfully')
      });
    };

    useEffect(() => {

      if(isExporting){
        Alert.alert('Exporting Image', 'Please wait while we export the image')
      }
  
    }, [isExporting]);


  // Show header using useLayoutEffect
  useLayoutEffect(() => {
    navigation.setOptions({
      headerTitle: 'Edit',
      headerShown: true,
      // Change background color of header
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
          onPress={handleExport}>
          <IconSet name="export" size={20} color="#FFFFFF" />
          <Text style={styles.buttonText}>Export</Text>
        </TouchableOpacity>
      ),
    });
  }, [navigation]);

  // Presentation
  return (
    <View style={[styles.container, { paddingLeft: 0, paddingRight: 0 }]}>
      {/* Image display */}
      {image && (
        <Image
          source={{ uri: image }}
          style={{ width: '90%', height: 500, padding: 8 }}
          cache="only-if-cached"
          resizeMode="contain"
          fadeDuration={0}
        />
      )}

      {/* Menu strip */}
      <MenuStrip setMode={setMode} />

      {/* Slider for exposure or shadows */}
      <Text style={[styles.body, { marginTop: 24 }]}>{mode}</Text>
      <Slider
        style={{ width: '80%', height: 24 }}
        minimumValue={
          mode === 'Exposure' ? 0.5 : mode === 'Mid Tones' ? -10 : mode === 'Shadows' ? 0.0 : mode === 'Highlights' ? -1.0 : mode === 'Noise' ? 25.0 : mode === 'Unsharp' ? 25.0 : 15.0
        }
        maximumValue={
          // if mode is Exposure, set maximum value to 1.5
          mode === 'Exposure' ? 1.5: mode === 'Mid Tones' ? +10 : mode === 'Shadows' ? 2.0 : mode === 'Highlights' ? 1.0 : mode === 'Noise' ?95.0 : mode === 'Unsharp' ? 75.0 : 15.0
        }
        thumbTintColor="#7E84F7"
        minimumTrackTintColor="#7E84F7"
        maximumTrackTintColor="#FFFFFF"
        // onSlidingComplete={mode === 'Exposure' ? handleExposureChange : handleMidToneChange}
        onSlidingComplete={
          mode === 'Exposure' 
            ? handleExposureChange 
            : mode === 'Mid Tones' 
              ? handleMidToneChange
              : mode === 'Shadows' 
                ? handleShadowChange
              : mode === 'Highlights'
                ? handleHighlightChange  
              : mode === 'Noise'
                ? handleNoiseRemoval
              : mode === 'Unsharp'
                ? handleUnsharpMasking
              : undefined
        }
      />
    </View>
  );
};

export { Editor };

