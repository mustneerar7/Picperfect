import React, {useEffect, useState} from 'react';
import {Text, TouchableOpacity, View} from 'react-native';
import {launchImageLibrary} from 'react-native-image-picker';

import {IconSet} from '../../hooks/useCustomIcons';
import {styles} from '../../styles';

/**
 * Renders the Home screen component.
 * Allow picking an image from the gallery.
 * Navigate to the Editor screen with the selected image.
 *
 * @param {object} navigation - To navigate to other screens.
 * @returns {JSX.Element} The rendered Home screen component.
 */

const Home = ({navigation}) => {
  const [result, setResult] = useState(null);

  // Get image uri and path from the gallery.
  const handleSelectPhoto = async () => {
    const options = {
      mediaType: 'photo',
      includeBase64: true,
    };

    await launchImageLibrary(options).then(result => {
    setResult(result);
    }).catch(err => {
      console.log('Image selection error: ', err);
    });
  };

  useEffect(() => {
    // If the result is null, return.
    if (!result) return;
    
    // If the user cancels the image selection, return.
    if(result.didCancel) return;

    // Navigate to the Editor screen with the selected image.
    // pass the image uri and path as route params.
    navigation.navigate('Editor', {
      uri: result.assets['0'].uri,
      imagePath: result.assets['0'].originalPath,
    });

  }, [result]);

  // Presentation
  return (
    <View style={styles.container}>
      <View
        style={{
          alignItems: 'center',
          justifyContent: 'center',
          marginTop: 20,
          marginBottom: 20,
        }}>
        <IconSet name="photo" color="#2b2b2b" size={48} />
        <Text style={styles.heading}>Welcome</Text>
        <Text style={styles.body}>
          Select a photo from gallery to start editing. In future a gallery
          preview would be added to this screen to make accessing pictures more
          convinient.
        </Text>
      </View>

      <TouchableOpacity style={styles.button} onPress={handleSelectPhoto}>
        <Text style={styles.buttonText}>Select Photo</Text>
      </TouchableOpacity>
    </View>
  );
};

export {Home};
