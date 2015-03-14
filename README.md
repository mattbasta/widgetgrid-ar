# widgetgrid-ar

This is a project I built back in 2009 as an experiment. It was designed as a means of integrating augmented reality with the web. A user could point their phone at a specially-designed 2D barcode (based on QR and implemented with a fork of the Google ZXing library). The library would fetch an asset bundle from a server (widgetgrid.com, now defunct). This asset bundle could contain images, text, and associated actions. The image could be overlaid onto the barcode on the augmented reality canvas in realtime. Tapping the image would open a menu which presented the user with a set of configurable actions. A very buggy implementation of 3D objects (rather than images) was also available.

The ultimate goal of this technology was to make it quick and easy to take complex and interesting content and display it to the user in the simplest way possible. This involved the entire flow: content creation and upload all the way through consumption by the user.

This project is no longer maintained and is posted here for learning purposes.

## Licenses

Google's ZXing library is licensed under the [Apache 2.0 license](https://github.com/zxing/zxing/blob/master/COPYING).

Some example code (such as the code that renders a cube) was borrowed from publicly available sources.

All other code is released as [CC0](https://creativecommons.org/publicdomain/zero/1.0/).
