import { StyleSheet } from 'react-native';

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#000000',
    justifyContent: 'center',
    alignItems: 'center',
    padding: 16,
  },

  heading: {
    fontSize: 24,
    marginBottom: 16,
    fontFamily: 'Montserrat-Bold',
  },

  body: {
    color: '#ffffff',
    fontSize: 14,
    marginBottom: 16,
    fontFamily: 'Montserrat-Regular',
    textAlign: 'center',
    lineHeight: 20,
    color: 'gray',
  },

  button: {
    backgroundColor: '#7E84F7',
    padding: 10,
    borderRadius: 28,
    width: '40%',
    height: 40,
    marginTop: 24,
  },

  buttonText: {
    color: '#ffffff',
    textAlign: 'center',
    fontFamily: 'Montserrat-SemiBold',
    fontSize: 12,
  },

  input: {
    backgroundColor: '#333333',
    color: '#ffffff',
    width: '90%',
    height: 44,
    padding: 12,
    marginBottom: 8,
    borderRadius: 8,
    fontFamily: 'Montserrat-Regular',
  },

  forgotPassword: {
    color: '#7E84F7',
    marginTop: 32,
  },

  showPasswordButton: {
    backgroundColor: '#333333',
    color: '#ffffff',
    width: '14%',
    height: 44,
    padding: 8,
    marginBottom: 8,
    borderRadius: 8,
    alignItems: 'center',
    justifyContent: 'center',
    display: 'flex',
  },
});

export { styles };

