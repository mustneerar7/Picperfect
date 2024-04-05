import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

const Learn = () => {
  return (
    <View style={styles.container}>
      <Text style={styles.body}>Learn</Text>
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

  heading: {
    color: '#7E84F7',
    fontSize: 24,
    marginBottom: 16,
    fontWeight: '800',
  },

  body: {
    color: '#ffffff',
    fontSize: 14,
    marginBottom: 16,
    fontWeight: '400',
  },
});

export { Learn };

