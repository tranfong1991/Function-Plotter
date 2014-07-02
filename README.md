Function-Plotter
================

Java application that plots and calculates polynomial functions.

In order to run this application, please download and install JFreeChart. Just to clarify, I don't use any built-in plotter 
from the JFreeChart library for this project, I only use its OverlayLayout() class for my GUI. 

Here is the link to download JFreeChart: http://sourceforge.net/projects/jfreechart/files/

- This program only handles the x variable name. 
- In some cases, for more accurate results, please add space between operators and operands. For example, (x ^ 2  - 3) instead of (x^2-3). 
- It can't handle unary negation with x. Please multiply with (-1) instead. For example, (-1)*x instead of -x.

Error handling is still being implemented. Possible errors include: illegal operator, illegal variable name.



