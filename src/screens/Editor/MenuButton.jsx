import {Text, TouchableOpacity, View} from 'react-native';
import {IconSet} from '../../hooks/useCustomIcons';

import {styles} from '../../styles';

const MenuButton = ({onClick, icon, title}) => (
  <TouchableOpacity
    style={{
      height: 100,
    }}
    onPress={onClick}>
    <View
      style={[
        {
          margin: 8,
          width: 56,
          height: 56,
          borderRadius: 100,
          backgroundColor: '#2b2b2b',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
        },
      ]}>
      <IconSet name={icon} size={24} color="#FFFFFF" />
    </View>
    <Text style={[styles.body, {fontSize: 12}]}>{title}</Text>
  </TouchableOpacity>
);

export {MenuButton};