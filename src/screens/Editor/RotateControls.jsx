import {View, Text, TouchableOpacity, ScrollView} from 'react-native';
import {IconSet, ImageIcon} from '../../hooks/useCustomIcons';

const RotateControls = ({rotate, setRotate, setFlip}) => {
  return (
    <View
      horizontal
      showsHorizontalScrollIndicator={false}
      style={{
        flexDirection: 'row',
        width: '90%',
        marginTop: 16,
        justifyContent: 'space-between',
      }}
      contentContainerStyle={{
        justifyContent: 'space-between',
        paddingLeft: 8,
        paddingRight: 8,
      }}>
      <TouchableOpacity
        style={{
            width: 64,
            height: 64,
            borderRadius: 4,
            alignItems: 'center',
            justifyContent: 'center',
            marginLeft: 8,
            marginRight: 8,
        }}
        onPress={() => {
          setRotate(-90);
        }}>
        <IconSet name="counterclockwise" size={32} color="#FFFFFF" />
      </TouchableOpacity>
      <TouchableOpacity
        style={{
            width: 64,
            height: 64,
            borderRadius: 4,
            alignItems: 'center',
            justifyContent: 'center',
            marginLeft: 8,
            marginRight: 8,
        }}
        onPress={() => {
          setFlip();
        }}>

        <IconSet name="flip" size={32} color="#FFFFFF" />


      </TouchableOpacity>
      <TouchableOpacity
        style={{
          width: 64,
          height: 64,
          borderRadius: 4,
          alignItems: 'center',
          justifyContent: 'center',
          marginLeft: 8,
          marginRight: 8,
        }}
        onPress={() => {
          setRotate(90);
        }}>
            <IconSet name="clockwise" size={32} color="#FFFFFF" />
      </TouchableOpacity>
    </View>
  );
};

export {RotateControls};