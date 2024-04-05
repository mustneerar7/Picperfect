import React from 'react';
import { Text, TouchableOpacity, View } from 'react-native';

import { IconSet } from '../../hooks/useCustomIcons';
import { styles } from '../../styles';

/**
 * Renders the Home screen component.
 *
 * @returns {JSX.Element} The rendered Home screen component.
 */
const Home = () => {
  return (
    <View style={styles.container}>
      <IconSet name="photo" color="#2b2b2b" size={48} />
      <Text style={styles.heading}>Welcome</Text>
      <Text style={styles.body}>
        Select a photo from gallery to start editing. In future a gallery
        preview would be added to this screen to make accessing pictures more
        convinient.
      </Text>
      <TouchableOpacity style={styles.button}>
        <Text style={styles.buttonText}>Select Photo</Text>
      </TouchableOpacity>
    </View>
  );
};

export { Home };
