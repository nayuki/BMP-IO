BMP I/O
=======


Introduction
------------

**BMP I/O** is a lightweight Java library for reading and writing Windows BMP files.

Supported reading formats:

* Paletted 1-bit
* Paletted 4-bit
* Paletted 8-bit
* Paletted 8-bit with RLE compression
* RGB 8,8,8
* RGBA 8,8,8,8

Bitmap files with image data in bottom-to-top order (common) and top-to-bottom order (unusual) are supported.

Supported writing formats:

* RGB 8,8,8


Demo
----

In the `demo` directory, run the class `SimpleDemo` with the command `java SimpleDemo`. It will create a sample file `Demo.bmp` in the current directory.

Full listing of demos:

* SimpleDemo: Draws a square for each of the 8 extreme colors, and four 2D gradient squares. This shows that the image is in true color (rather than paletted 256-color, etc.).
* MandelbrotDemo: Draws a black-and-white image of the main part of the Mandelbrot set. The image is computed on the fly when each pixel is being requested for writing.
* ReadDemo: Reads Demo.bmp (which can be in any color format) and writes Demo2.bmp with the same image contents but in RGB 8,8,8 true color.


License
-------

Copyright © 2011 Nayuki Minase

(MIT License)

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
