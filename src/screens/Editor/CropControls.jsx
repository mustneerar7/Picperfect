import React from 'react';
import {ScrollView, StyleSheet, Text, TouchableOpacity} from 'react-native';

/**
 * Renders the crop controls component.
 *
 * @param {Object} props - The component props.
 * @param {Function} props.setCrop - The function to set the crop value.
 * @returns {JSX.Element} The crop controls component.
 */
const CorpControls = ({setCrop}) => {
  return (
    <ScrollView
      horizontal
      showsHorizontalScrollIndicator={false}
      style={{
        flexDirection: 'row',
        width: '90%',
        marginTop: 16,
      }}
      contentContainerStyle={{
        justifyContent: 'space-between',
        paddingLeft: 8,
        paddingRight: 8,
        alignItems: 'center',
      }}>
      <TouchableOpacity
        style={styles.cropButton}
        onPress={() => {
          setCrop(1);
        }}>
        <Text style={{color: 'white'}}>1:1</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.cropButton}
        onPress={() => {
          setCrop(0.75);
        }}>
        <Text style={{color: 'white'}}>3:4</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.cropButton}
        onPress={() => {
          setCrop(1.33);
        }}>
        <Text style={{color: 'white'}}>4:3</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.cropButton}
        onPress={() => {
          setCrop(1.77);
        }}>
        <Text style={{color: 'white'}}>16:9</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.cropButton}
        onPress={() => {
          setCrop(1.5);
        }}>
        <Text style={{color: 'white'}}>3:2</Text>
      </TouchableOpacity>
    </ScrollView>
  );
};

const styles = StyleSheet.create({
  cropButton: {
    width: 64,
    height: 64,
    borderRadius: 4,
    alignItems: 'center',
    justifyContent: 'center',
    marginLeft: 8,
    marginRight: 8,
  },
});

export {CorpControls};
