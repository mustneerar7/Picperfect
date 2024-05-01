import { createIconSet } from 'react-native-vector-icons';

/**
 * Map of custom icons and their corresponding glyphs.
 * @type {Object<string, string>}
 */
const glyphMap = {
  'heart': '',
  'play': '',
  'shuffle': '',
  'download': '',
  'share': '',
  'more': '',
  'home':'',
  'search':'',
  'profile': '',
  'bulb': '',
  'rocket': '',
  'eye': '',
  'photo': '',
  'export': '',
  'brightness': '',
  'contrast': '',
  'highlights': '',
  'shadows': '',
  'midtones': '',
  'crop':'',
  'rotate':'',
  'clockwise': '',
  'counterclockwise': '',
  'flip': '',
  'broom': '',
  'sharp': ''
};

const IconSet = createIconSet(glyphMap, 'FluentSystemIcons-Regular');

export { IconSet }