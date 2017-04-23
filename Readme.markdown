BMP I/O
=======


Introduction
------------

**BMP I/O** is a lightweight Java library for reading and writing image files in the Windows BMP format.

Supported reading formats:

* BITMAPINFOHEADER header
* Paletted 1-bit
* Paletted 4-bit
* Paletted 4-bit with RLE compression
* Paletted 8-bit
* Paletted 8-bit with RLE compression
* RGB 8,8,8
* RGBA 8,8,8,8
* Rows in bottom-to-top order (common)
* Rows in top-to-bottom order (uncommon)

Supported writing formats:

* RGB 8,8,8
* Rows in bottom-to-top order


Demos
-----

In the demo directory, run any one of these classes with zero command-line arguments, and it will create a sample .bmp file in the current directory:

* SimpleDemo: Draws a square for each of the 8 extreme colors, and four 2D gradient squares. This shows that the image is in true color (rather than paletted 256-color, etc.).

* MandelbrotDemo: Draws a black-and-white image of the main part of the Mandelbrot set. The image is computed on the fly when each pixel is being requested for writing.

* ReadDemo: Reads from Demo.bmp (which can be in any color format) and writes to Demo2.bmp with the same image contents but in RGB 8,8,8 true color.


License
-------

Copyright © 2016 Project Nayuki. (MIT License)  
[https://www.nayuki.io/page/bmp-io-library-java](https://www.nayuki.io/page/bmp-io-library-java)

Permission is hereby granted, free of charge, to any person obtaining a copy of
this software and associated documentation files (the "Software"), to deal in
the Software without restriction, including without limitation the rights to
use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

* The above copyright notice and this permission notice shall be included in
  all copies or substantial portions of the Software.

* The Software is provided "as is", without warranty of any kind, express or
  implied, including but not limited to the warranties of merchantability,
  fitness for a particular purpose and noninfringement. In no event shall the
  authors or copyright holders be liable for any claim, damages or other
  liability, whether in an action of contract, tort or otherwise, arising from,
  out of or in connection with the Software or the use or other dealings in the
  Software.
