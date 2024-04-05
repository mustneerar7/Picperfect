import React from 'react';
import {View, Text, StyleSheet} from 'react-native';

const Profile = () => {
  return (
    <View style={styles.container}>
      <Text style={styles.body}>Profile</Text>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000000',
    justifyContent: 'center',
    alignItems: 'center',
  },

  body: {
    color: '#ffffff',
    fontSize: 14,
    marginBottom: 16,
    fontWeight: '400',
  }
});

export {Profile};
