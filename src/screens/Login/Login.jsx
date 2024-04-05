import React from 'react';
import { Text, TextInput, TouchableOpacity, View } from 'react-native';

import { IconSet } from '../../hooks/useCustomIcons';
import { styles } from '../../styles';

const Login = ({navigation}) => {
  const handleNavigation = () => {
    navigation.reset({
      index: 0,
      routes: [{name: 'HomeStack'}],
    });
  };

  return (
    <View style={styles.container}>
      <Text style={styles.heading}>Login</Text>
      <Text style={styles.body}>Please enter your credentials to login</Text>
      <TextInput
        style={styles.input}
        placeholder="Email"
        cursorColor={'#7E84F7'}
      />
      <View
        style={{
          flexDirection: 'row',
          justifyContent: 'space-between',
          alignItems: 'center',
          width: '90%',
          marginBottom: 10,
        }}>
        <TextInput
          style={[styles.input, {width: '84%'}]}
          placeholder="Password"
          cursorColor={'#7E84F7'}
        />
        <TouchableOpacity
          style={styles.showPasswordButton}
          onPress={() => console.log('Show password pressed')}>
          <IconSet name="eye" color="#ffffff" size={24} />
        </TouchableOpacity>
      </View>
      <TouchableOpacity style={styles.button} onPress={handleNavigation}>
        <Text style={styles.buttonText}>Login</Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={styles.forgotPassword}
        onPress={() => console.log('Forgot password pressed')}>
        <Text style={styles.buttonText}>Forgot password</Text>
      </TouchableOpacity>
      <TouchableOpacity
        style={[styles.forgotPassword, {position: 'absolute', bottom: 32}]}
        onPress={() => console.log('Register button pressed')}>
        <Text style={styles.buttonText}>Donot have an account? Sign up</Text>
      </TouchableOpacity>
    </View>
  );
};

export default Login;
