// A wrapper for the native module LightingControls

import {NativeModules} from 'react-native';

const useLightingControls = () => {
  const {LightingControls} = NativeModules;
  return {LightingControls};
};

export {useLightingControls};