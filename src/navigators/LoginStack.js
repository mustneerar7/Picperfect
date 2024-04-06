import React from 'react';
import {createNativeStackNavigator} from '@react-navigation/native-stack';

import Login from '../screens/Login/Login';
import {HomeStack} from './HomeStack';
import { Editor } from '../screens/Editor/Editor';

const Stack = createNativeStackNavigator();

/**
 * Represents the login stack navigator.
 * @returns {JSX.Element} The login stack navigator component.
 */
const LoginStack = () => {
  return (
    <Stack.Navigator
      screenOptions={{
        headerShown: false,
      }}>
      <Stack.Screen name="Login" component={Login} />
      <Stack.Screen name="HomeStack" component={HomeStack} />
      <Stack.Screen name="Editor" component={Editor} />
    </Stack.Navigator>
  );
};

export {LoginStack};
