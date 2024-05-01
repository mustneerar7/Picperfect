import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import {IconSet} from '../../hooks/useCustomIcons';

import {styles} from '../../styles';

/**
 * MenuButton component.
 *
 * @component
 * @param {Object} props - The component props.
 * @param {Function} props.onClick - The function to be called when the button is clicked.
 * @param {string} props.icon - The name of the icon to be displayed.
 * @param {string} props.title - The title text to be displayed.
 * @returns {JSX.Element} The rendered MenuButton component.
 */
const MenuButton = ({onClick, icon, title}) => (
  <TouchableOpacity
    style={{
      height: 100,
    }}
    onPress={onClick}>
    <View style={LocalStyles.menuButton}>
      <IconSet name={icon} size={24} color="#FFFFFF" />
    </View>
    <Text style={[styles.body, {fontSize: 12}]}>{title}</Text>
  </TouchableOpacity>
);

const LocalStyles = StyleSheet.create({
  menuButton: {
    margin: 8,
    width: 56,
    height: 56,
    borderRadius: 100,
    backgroundColor: '#2b2b2b',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
  },
});

export {MenuButton};
