import React from 'react';
import {createBottomTabNavigator} from '@react-navigation/bottom-tabs';

import {Home} from '../screens/Home/Home';
import {Learn} from '../screens/Learn/Learn';
import {Profile} from '../screens/Profile/Profile';

import {IconSet} from '../hooks/useCustomIcons';

const Tab = createBottomTabNavigator();

/**
 * Represents the HomeStack component.
 * This component is responsible for rendering the navigation stack for the home screen.
 *
 * @returns {JSX.Element} The rendered HomeStack component.
 */
const HomeStack = () => {
  return (
    <Tab.Navigator
      screenOptions={{
        headerShown: false,
        tabBarStyle: {
          backgroundColor: '#111111',
          height: 64,
          borderTopWidth: 0,
          paddingBottom: 12,
          elevation: 0,
          fontFamily: 'Montserrat-Regular',
        },
        tabBarActiveTintColor: '#7E84F7',
      }}>
      <Tab.Screen
        name="Home"
        component={Home}
        options={{
          tabBarIcon: ({color, size}) => (
            <IconSet name="home" color={color} size={size} />
          ),
        }}
      />
      <Tab.Screen
        name="Learn"
        component={Learn}
        options={{
          tabBarIcon: ({color, size}) => (
            <IconSet name="bulb" color={color} size={size} />
          ),
        }}
      />
      <Tab.Screen
        name="Profile"
        component={Profile}
        options={{
          tabBarIcon: ({color, size}) => (
            <IconSet name="profile" color={color} size={size} />
          ),
        }}
      />
    </Tab.Navigator>
  );
};

export {HomeStack};
