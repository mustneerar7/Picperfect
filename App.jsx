import React from 'react';

import { NavigationContainer } from '@react-navigation/native';
import { LoginStack } from './src/navigators/LoginStack';

const App = () => {
  return (
    <NavigationContainer>
      <LoginStack />
    </NavigationContainer>
  );
};

export default App;
