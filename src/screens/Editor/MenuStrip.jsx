import { ScrollView } from 'react-native';
import { MenuButton } from './MenuButton';

const MenuStrip = () => {

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
      <MenuButton icon="brightness" onClick={() => {}} title={'Exposure'} />
      <MenuButton icon="contrast" onClick={() => {}} title={'Contrast'} />
      <MenuButton icon="highlights" onClick={() => {}} title={'Highlights'} />
      <MenuButton icon="shadows" onClick={() => {}} title={'Shadows'} />
      <MenuButton icon="midtones" onClick={() => {}} title={'Midtones'} />
    </ScrollView>
  );
};

export { MenuStrip };

