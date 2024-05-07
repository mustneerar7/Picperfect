# Pic perfect
A photo editing app created as a semester project for the course `Digital Image Processing` at COMSATS University Lahore campus by *Mustneer Ahmad, Babar Shakeel and Muhammad Ayan.*

## Instructions for developers
1. Clone the repository.
2. Run `yarn install` to install the dependencies.
3. cd to `android` folder and run 
`.\gradlew assembleDebug` to build the android project
4. cd to root folder and run `yarn start` to start the metro development server.
5. If you want to build a release verison (.apk), cd to `android` folder and run `.\gradlew assembleRelease`

## Features
- Crop
- Rotate
- Flip
- Brightness
- Sharpness
- Noise Reduction
- Shadows
- Highlights
- Midtones
- Exporting edited image

**NOTE**: 
- You must have a working `android sdk` and `platform-tools` configured on your system. 

- You must enable usb debugging on your android device and connect it to your system via usb.