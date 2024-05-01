import {ScrollView} from 'react-native';
import {MenuButton} from './MenuButton';

const MenuStrip = ({setMode}) => {
  return (
    <ScrollView
      horizontal
      showsHorizontalScrollIndicator={false}
      style={{
        flexDirection: 'row',
        width: '90%',
        marginTop: 16,
        maxHeight: 120,
        paddingTop: 16,
      }}>
      <MenuButton
        icon="brightness"
        onClick={() => {
          console.log('Exposure clicked');
          setMode('Exposure');
        }}
        title={'Exposure'}
      />
      <MenuButton
        icon="midtones"
        onClick={() => {
          console.log('Mid Tones clicked');
          setMode('Mid Tones');
        }}
        title={'Mid Tones'}
      />
      <MenuButton
        icon="shadows"
        onClick={() => {
          console.log('Shadows clicked');
          setMode('Shadows');
        }}
        title={'Shadows'}
      />
      <MenuButton
        icon="highlights"
        onClick={() => {
          console.log('Highlights clicked');
          setMode('Highlights');
        }}
        title={'Highlights'}
      />
      <MenuButton
        icon="crop"
        onClick={() => {
          console.log('Crop clicked');
          setMode('Crop');
        }}
        title={'Crop'}
      />
    </ScrollView>
  );
};

export {MenuStrip};
