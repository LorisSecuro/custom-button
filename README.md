# CustomButton

A customizable SWT button.

Try the test application now: [Download page] (https://github.com/LorisSecuro/custom-button/releases)

![alt tag](Screenshot.png)

Features:

- For every state of the button (normal, hover, pressed, selected, disabled) you can change:
  - background color
  - text color
  - text background color
  - first border color and width
  - second border color and width

- The color transitions between the various states are made smooth, you can change their duration and even make them immediate

- You can show 2 images, one on the front and one in the background, in both cases you can choose 4 different styles to display them:
  - original size
  - streched
  - streched keeping proportions
  - tiled

- If you specify a text as well as an image to display, you can organize their position in 5 different ways and decide the size ratio between them:
  - image left and text right
  - image right and text left
  - image top and text bottom
  - image bottom and text top
  - both image and text centered

- You can specify the horizontal and vertical alignments for both the text and the image

- The text is automatically wrapped if the button is not big enough

- You can set rounded corners and change their radius
 
- The button can be a toggle button

- You can specify margins along the borders, fixed and percentage, for both the text and the image to have more control over the content disposition

- You can enable automatic text resizing which make the text dynamically grow and shrink depending on the size of the button and its margins 

- The default style of CustomButton resemble the standard Windows 10 button.

A test application is avaible to showcase the various features: [Download page] (https://github.com/LorisSecuro/custom-button/releases)

The button is composed of:
- CustomButton, the main project;
- a largely modified version of TextRenderer from jaretutil (v0.32), for the text wrapping functionality;
- a slightly modified version of Trident (v1.3), for smooth color transitions.

Thanks to:
- jaredutils (http://www.jaret.de/jaretutil/index.html) author Peter Kliem;
- Trident (https://kenai.com/projects/trident/pages/Home) author Kirill Grouchnikov;
- Julian Robichaux for SquareButton (https://github.com/jrobichaux/SquareButton) which I used as guideline for my CustomButton.
