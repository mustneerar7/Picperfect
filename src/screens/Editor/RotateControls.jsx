import {TouchableOpacity, View, StyleSheet} from 'react-native';
import {IconSet} from '../../hooks/useCustomIcons';

/**
 * A component that displays rotate controls.
 *
 * @component
 * @param {Object} props - The component props.
 * @param {Function} props.setRotate - A function to set the rotation value.
 * @param {Function} props.setFlip - A function to set the flip value.
 * @returns {JSX.Element} The RotateControls component.
 */
const RotateControls = ({setRotate, setFlip}) => {
  return (
    <View
      horizontal
      showsHorizontalScrollIndicator={false}
      style={styles.container}
      contentContainerStyle={{
        justifyContent: 'space-between',
        paddingLeft: 8,
        paddingRight: 8,
      }}>
      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          setRotate(-90);
        }}>
        <IconSet name="counterclockwise" size={32} color="#FFFFFF" />
      </TouchableOpacity>
      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          setFlip();
        }}>
        <IconSet name="flip" size={32} color="#FFFFFF" />
      </TouchableOpacity>
      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          setRotate(90);
        }}>
        <IconSet name="clockwise" size={32} color="#FFFFFF" />
      </TouchableOpacity>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flexDirection: 'row',
    width: '90%',
    marginTop: 16,
    justifyContent: 'space-between',
  },
  button: {
    width: 64,
    height: 64,
    borderRadius: 4,
    alignItems: 'center',
    justifyContent: 'center',
    marginLeft: 8,
    marginRight: 8,
  },
});

export {RotateControls};
