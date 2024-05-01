import {ScrollView} from 'react-native';
import {MenuButton} from './MenuButton';

/**
 * A component that renders a menu strip with various menu buttons.
 *
 * @component
 * @param {function} setMode - A function to set the mode when a menu button is clicked.
 * @returns {JSX.Element} The rendered menu strip component.
 */
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
        title={'Brightness'}
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
      <MenuButton
        icon="rotate"
        onClick={() => {
          console.log('Rotate clicked');
          setMode('Rotate');
        }}
        title={'Rotate'}
      />
      <MenuButton
        icon="broom"
        onClick={() => {
          console.log('Noise remover clicked');
          setMode('Noise');
        }}
        title={'Noise'}
      />
      <MenuButton
        icon="sharp"
        onClick={() => {
          console.log('Unsharp clicked');
          setMode('Unsharp');
        }}
        title={'Sharpen'}
      />
    </ScrollView>
  );
};

export {MenuStrip};
