import {
  View,
  Text,
  StyleSheet,
  StatusBar,
  Image,
  Pressable,
} from 'react-native';

const App = (): React.JSX.Element => {
  return (
    <View style={Styles.container}>
      <Image
        style={Styles.imageContainer}
        source={require('./src/assets/images/sample.jpg')}
      />
      <Text style={Styles.heading}>Awaken your inner artist</Text>
      <Text style={Styles.body}>
        Blend colors, illuminate scenes, recompose in post and much more
      </Text>
      <Pressable style={Styles.buttonContainer}>
        <Text style={Styles.buttonText}>Get Started</Text>
      </Pressable>
      <StatusBar translucent={true} backgroundColor={'transparent'} />
    </View>
  );
};

const Styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#111111',
  },
  imageContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    resizeMode: 'cover',
    width: '100%',
    height: '60%',
    marginBottom: 32,
  },
  buttonContainer: {
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#7E84F7',
    padding: 12,
    borderRadius: 8,
    margin: 16,
    width: '86%',
    height: 44,
    alignSelf: 'center',
    position: 'absolute',
    bottom: 64,
  },
  heading: {
    fontSize: 28,
    fontFamily: 'Inter-SemiBold',
    padding: 16,
    paddingBottom: 0,
    textAlign: 'center',
    color: '#ffffff',
  },
  body: {
    fontSize: 14,
    fontFamily: 'Inter-Regular',
    padding: 16,
    paddingTop: 12,
    textAlign: 'center',
    color: '#ffffff',
  },
  buttonText: {
    fontSize: 14,
    fontFamily: 'Inter-SemiBold',
    color: '#ffffff',
  },
});

export default App;
