/*
The MIT License (MIT)

Copyright (c) 2015 Loris Securo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

package customButton;

import org.eclipse.jface.resource.ColorRegistry;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;
import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.Timeline.TimelineState;
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertySetter;

import de.jaret.util.swt.TextRenderer;

public class CustomButton extends Canvas {

	/* PRIVATE STUFF */

	String text;
	Image backgroundImage;
	Image image;
	int marginWidth = 1;
	int marginHeight = 1;

	Color background;
	Color backgroundHover;
	Color backgroundPressed;
	Color backgroundDisabled;
	Color backgroundSelected;
	Color backgroundToPaint;

	Color border;
	Color borderHover;
	Color borderPressed;
	Color borderDisabled;
	Color borderSelected;
	Color borderToPaint;
	int borderWidth = 1;
	int borderWidthHover = 1;
	int borderWidthPressed = 1;
	int borderWidthDisabled = 1;
	int borderWidthSelected = 1;

	Color border2;
	Color border2Hover;
	Color border2Pressed;
	Color border2Disabled;
	Color border2Selected;
	Color border2ToPaint;
	int border2Width = 1;
	int border2WidthHover = 1;
	int border2WidthPressed = 1;
	int border2WidthDisabled = 1;
	int border2WidthSelected = 1;

	Color foreground;
	Color foregroundHover;
	Color foregroundPressed;
	Color foregroundDisabled;
	Color foregroundSelected;
	Color foregroundToPaint;

	Color textBackground;
	Color textBackgroundHover;
	Color textBackgroundPressed;
	Color textBackgroundDisabled;
	Color textBackgroundSelected;
	Color textBackgroundToPaint;

	long durationNormalToHover = 240;
	long durationSelectedToHover = 240;
	long durationHoverToPressed = 120;
	long durationDisabledToNormal = 190;
	long durationDisabledToSelected = 190;
	long durationNormalToSelected = 0;
	long durationSelectedToPressed = 120;

	Timeline transition;
	final int BACKGROUND_TRANSITION = 0;
	final int BORDER_TRANSITION = 1;
	final int BORDER2_TRANSITION = 2;
	final int FOREGROUND_TRANSITION = 3;

	Color background2;

	boolean roundedCorners = false;

	Font font;

	boolean colorTransition = true;

	boolean forceRedraw;

	enum State {
		NORMAL, PRESSED, HOVER, DISABLED, SELECTED
	};

	public static int CB_IMAGE_STRETCH_KEEP_PROPORTIONS = 0;
	public static int CB_IMAGE_STRETCH = 1;
	public static int CB_IMAGE_ORIGINAL = 2;
	public static int CB_IMAGE_TILE = 3;

	int backgroundImageStyle = CB_IMAGE_TILE;
	int imageStyle = CB_IMAGE_STRETCH_KEEP_PROPORTIONS;

	public static int CB_ALIGNMENT_CENTER = 0;
	public static int CB_ALIGNMENT_LEFT = 1;
	public static int CB_ALIGNMENT_RIGHT = 2;
	public static int CB_ALIGNMENT_TOP = 3;
	public static int CB_ALIGNMENT_BOTTOM = 4;

	int backgroundImageHorizontalAlignment = CB_ALIGNMENT_CENTER;
	int backgroundImageVerticalAlignment = CB_ALIGNMENT_CENTER;
	int imageHorizontalAlignment = CB_ALIGNMENT_CENTER;
	int imageVerticalAlignment = CB_ALIGNMENT_CENTER;
	int textHorizontalAlignment = CB_ALIGNMENT_CENTER;
	int textVerticalAlignment = CB_ALIGNMENT_CENTER;

	public static int CB_IMAGETEXT_NONE = 0;
	public static int CB_IMAGETEXT_LEFT_RIGHT = 1;
	public static int CB_IMAGETEXT_RIGHT_LEFT = 2;
	public static int CB_IMAGETEXT_TOP_BOTTOM = 3;
	public static int CB_IMAGETEXT_BOTTOM_TOP = 4;

	int imageAndTextMode = CB_IMAGETEXT_LEFT_RIGHT;
	double imageToTextRatio = 0.5;

	State state = State.NORMAL;

	public CustomButton(Composite parent, int style) {
		super(parent, style | SWT.DOUBLE_BUFFERED);

		// set default colors
		setDefaultColors();

		transition = new Timeline(this);
		transition.addPropertyToInterpolate(Timeline
				.<Color> property("backgroundToPaint").from(background)
				.to(background).setWith(backgroundToPaintSetter));
		transition.addPropertyToInterpolate(Timeline
				.<Color> property("borderToPaint").from(border).to(border)
				.setWith(borderToPaintSetter));
		transition.addPropertyToInterpolate(Timeline
				.<Color> property("border2ToPaint").from(border2).to(border2)
				.setWith(border2ToPaintSetter));
		transition.addPropertyToInterpolate(Timeline
				.<Color> property("foregroundToPaint").from(foreground)
				.to(foreground).setWith(foregroundToPaintSetter));

		addListeners();
	}

	private void addListeners() {
		addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				CustomButton.this.paintControl(e);
			}
		});

		addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (e.button == 1) {
					pressing();
				}
			}
		});

		addListener(SWT.MouseUp, new Listener() {
			@Override
			public void handleEvent(Event e) {
				if (e.button == 1) {
					releasePress(getClientArea().contains(e.x, e.y));
				}
			}
		});

		addListener(SWT.MouseEnter, new Listener() {
			@Override
			public void handleEvent(Event e) {
				State prevState = state;
				state = State.HOVER;
				if (colorTransition) {
					playTransition(prevState, state);
				} else {
					redraw();
				}
			}
		});
		addListener(SWT.MouseExit, new Listener() {
			@Override
			public void handleEvent(Event e) {

				if (!getEnabled()) {
					return;
				}

				State prevState = state;
				if (!isFocusControl()) {
					state = State.NORMAL;
				} else {
					state = State.SELECTED;
				}

				if (colorTransition) {
					playTransition(prevState, state);
				} else {
					redraw();
				}
			}
		});

		addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				if (!getEnabled()) {
					return;
				}

				State prevState = state;
				if (isMouseHovering()) {
					state = State.HOVER;
				} else {
					state = State.NORMAL;
				}

				if (colorTransition) {
					playTransition(prevState, state);
				} else {
					redraw();
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				State prevState = state;
				if (isMouseHovering()) {
					state = State.HOVER;
				} else {
					state = State.SELECTED;
				}
				if (colorTransition) {
					playTransition(prevState, state);
				} else {
					redraw();
				}
			}
		});

		addTraverseListener(new TraverseListener() {
			@Override
			public void keyTraversed(TraverseEvent e) {
				switch (e.detail) {
				case SWT.TRAVERSE_RETURN:
					e.doit = true;
					doButtonClicked();
					break;
				case SWT.TRAVERSE_ESCAPE:
				case SWT.TRAVERSE_TAB_NEXT:
				case SWT.TRAVERSE_TAB_PREVIOUS:
				case SWT.TRAVERSE_PAGE_NEXT:
				case SWT.TRAVERSE_PAGE_PREVIOUS:
					e.doit = true;
					break;
				}
			}
		});

		addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.character == ' ') {
					releasePress(true);
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if ((e.character == ' ') && (state != State.PRESSED)) {
					pressing();
				}
			}
		});

	}

	private void pressing() {
		State prevState = state;
		state = State.PRESSED;
		if (colorTransition) {
			playTransition(prevState, state);
		} else {
			redraw();
		}
	}

	private void releasePress(boolean doPress) {
		if (doPress) {
			doButtonClicked();
		}

		State prevState = state;
		if (isMouseHovering()) {
			state = State.HOVER;
		} else {
			state = State.SELECTED;
		}
		if (colorTransition) {
			playTransition(prevState, state);
		} else {
			redraw();
		}
	}

	private boolean isMouseHovering() {
		Control controlUnderMouse = Display.getCurrent().getCursorControl();
		return (controlUnderMouse instanceof CustomButton)
				&& ((CustomButton) controlUnderMouse == CustomButton.this);
	}

	private void doButtonClicked() {
		Event e = new Event();
		e.item = this;
		e.widget = this;
		e.type = SWT.Selection;
		notifyListeners(SWT.Selection, e);
	}

	private void playTransition(State from, State to) {

		if (from == to) {
			return;
		}

		if (transition.getState() != TimelineState.IDLE) {
			transition.abort();
		}

		long duration = getTransitionDuration(from, to);
		if (duration > 0) {
			transition.setPropertyValues(BACKGROUND_TRANSITION,
					backgroundToPaint, getBackgroundState());
			transition.setPropertyValues(BORDER_TRANSITION, borderToPaint,
					getBorderState());
			transition.setPropertyValues(BORDER2_TRANSITION, border2ToPaint,
					getBorder2State());
			transition.setPropertyValues(FOREGROUND_TRANSITION,
					foregroundToPaint, getForegroundState());
			transition.setDuration(duration);
			transition.play();
		} else {
			forceRedraw();
		}
	}

	private long getTransitionDuration(State from, State to) {
		if (((from == State.NORMAL) && (to == State.HOVER))
				|| ((to == State.NORMAL) && (from == State.HOVER))) {
			return durationNormalToHover;
		}
		if (((from == State.SELECTED) && (to == State.HOVER))
				|| ((to == State.SELECTED) && (from == State.HOVER))) {
			return durationSelectedToHover;
		}
		if (((from == State.HOVER) && (to == State.PRESSED))
				|| ((to == State.HOVER) && (from == State.PRESSED))) {
			return durationHoverToPressed;
		}
		if (((from == State.DISABLED) && (to == State.NORMAL))
				|| ((to == State.DISABLED) && (from == State.NORMAL))) {
			return durationDisabledToNormal;
		}
		if (((from == State.DISABLED) && (to == State.SELECTED))
				|| ((to == State.DISABLED) && (from == State.SELECTED))) {
			return durationDisabledToSelected;
		}
		if (((from == State.NORMAL) && (to == State.SELECTED))
				|| ((to == State.NORMAL) && (from == State.SELECTED))) {
			return durationNormalToSelected;
		}
		if (((from == State.SELECTED) && (to == State.PRESSED))
				|| ((to == State.SELECTED) && (from == State.PRESSED))) {
			return durationSelectedToPressed;
		}
		return 0;
	}

	// set default colors
	private void setDefaultColors() {

		background = getSavedColor(225, 225, 225);
		backgroundHover = getSavedColor(229, 241, 251);
		backgroundPressed = getSavedColor(204, 228, 247);
		backgroundDisabled = getSavedColor(204, 204, 204);
		backgroundSelected = getSavedColor(225, 225, 225);
		backgroundToPaint = background;

		border = getSavedColor(173, 173, 173);
		borderHover = getSavedColor(0, 120, 215);
		borderPressed = getSavedColor(0, 84, 153);
		borderDisabled = getSavedColor(191, 191, 191);
		borderSelected = getSavedColor(0, 120, 215);
		borderToPaint = border;

		border2 = background;
		border2Hover = backgroundHover;
		border2Pressed = backgroundPressed;
		border2Disabled = backgroundDisabled;
		border2Selected = getSavedColor(0, 120, 215);
		border2ToPaint = border2;

		foreground = getSavedColor(0, 0, 0);
		foregroundHover = getSavedColor(0, 0, 0);
		foregroundPressed = getSavedColor(0, 0, 0);
		foregroundDisabled = getSavedColor(131, 131, 131);
		foregroundSelected = getSavedColor(0, 0, 0);
		foregroundToPaint = foreground;

		textBackground = null;
		textBackgroundHover = null;
		textBackgroundPressed = null;
		textBackgroundDisabled = null;
		textBackgroundSelected = null;

		background2 = getSavedColor(122, 209, 255);
	}

	// get colors
	private Color getSavedColor(int r, int g, int b) {
		String colorString = "COLOR:" + r + "-" + g + "-" + b;
		ColorRegistry colorRegistry = JFaceResources.getColorRegistry();
		if (!colorRegistry.hasValueFor(colorString)) {
			colorRegistry.put(colorString, new RGB(r, g, b));
		}
		return colorRegistry.get(colorString);
	}

	// paint the button
	private void paintControl(PaintEvent e) {

		// get the size
		Point size = getSize();

		GC gc = e.gc;

		Rectangle insideRectangle = new Rectangle(marginWidth, marginHeight,
				size.x - (2 * marginWidth), size.y - (2 * marginHeight));

		if (roundedCorners) {
			gc.setAdvanced(true);
			gc.setAntialias(SWT.ON);

			// border
			int borderWidthToPaint = getBorderWidthState();
			if (borderWidthToPaint > 0) {

				if (!colorTransition || forceRedraw) {
					borderToPaint = getBorderState();
				}
				gc.setBackground(borderToPaint);

				int arcHeight = Math.max(5, insideRectangle.height / 8);
				int arcWidth = arcHeight;
				gc.fillRoundRectangle(insideRectangle.x, insideRectangle.y,
						insideRectangle.width, insideRectangle.height,
						arcWidth, arcHeight);

				insideRectangle = new Rectangle(insideRectangle.x
						+ borderWidthToPaint, insideRectangle.y
						+ borderWidthToPaint, insideRectangle.width
						- (2 * borderWidthToPaint), insideRectangle.height
						- (2 * borderWidthToPaint));

				// border 2
				int border2WidthToPaint = getBorder2WidthState();
				if (border2WidthToPaint > 0) {

					if (!colorTransition || forceRedraw) {
						border2ToPaint = getBorder2State();
					}
					gc.setBackground(border2ToPaint);

					arcHeight = Math.max(4, insideRectangle.height / 9);
					arcWidth = arcHeight;
					gc.fillRoundRectangle(insideRectangle.x, insideRectangle.y,
							insideRectangle.width, insideRectangle.height,
							arcWidth, arcHeight);
					insideRectangle = new Rectangle(insideRectangle.x
							+ border2WidthToPaint, insideRectangle.y
							+ border2WidthToPaint, insideRectangle.width
							- (2 * border2WidthToPaint), insideRectangle.height
							- (2 * border2WidthToPaint));
				}
			}

			if (!colorTransition || forceRedraw) {
				backgroundToPaint = getBackgroundState();
			}
			gc.setBackground(backgroundToPaint);
			int arcHeight = Math.max(3, insideRectangle.height / 10);
			int arcWidth = arcHeight;
			gc.fillRoundRectangle(insideRectangle.x, insideRectangle.y,
					insideRectangle.width, insideRectangle.height, arcWidth,
					arcHeight);

			gc.setAntialias(SWT.DEFAULT);
			gc.setAdvanced(false);
		} else {

			if (!colorTransition || forceRedraw) {
				backgroundToPaint = getBackgroundState();
			}
			gc.setBackground(backgroundToPaint);
			gc.fillRectangle(insideRectangle);

			// border
			int borderWidthToPaint = getBorderWidthState();
			if (borderWidthToPaint > 0) {
				gc.setLineWidth(borderWidthToPaint);

				if (!colorTransition || forceRedraw) {
					borderToPaint = getBorderState();
				}
				gc.setForeground(borderToPaint);

				Rectangle borderRectangle = new Rectangle(insideRectangle.x
						+ (borderWidthToPaint / 2), insideRectangle.y
						+ (borderWidthToPaint / 2), insideRectangle.width
						- borderWidthToPaint, insideRectangle.height
						- borderWidthToPaint);
				gc.drawRectangle(borderRectangle);

				insideRectangle = new Rectangle(insideRectangle.x
						+ borderWidthToPaint, insideRectangle.y
						+ borderWidthToPaint, insideRectangle.width
						- (2 * borderWidthToPaint), insideRectangle.height
						- (2 * borderWidthToPaint));

				// border 2
				int border2WidthToPaint = getBorder2WidthState();
				if (border2WidthToPaint > 0) {
					gc.setLineWidth(border2WidthToPaint);

					if (!colorTransition || forceRedraw) {
						border2ToPaint = getBorder2State();
					}
					gc.setForeground(border2ToPaint);

					Rectangle border2Rectangle = new Rectangle(
							insideRectangle.x + (border2WidthToPaint / 2),
							insideRectangle.y + (border2WidthToPaint / 2),
							insideRectangle.width - border2WidthToPaint,
							insideRectangle.height - border2WidthToPaint);
					gc.drawRectangle(border2Rectangle);

					insideRectangle = new Rectangle(insideRectangle.x
							+ border2WidthToPaint, insideRectangle.y
							+ border2WidthToPaint, insideRectangle.width
							- (2 * border2WidthToPaint), insideRectangle.height
							- (2 * border2WidthToPaint));
				}
			}
		}

		if (backgroundImage != null) {
			drawImage(gc, backgroundImage, backgroundImageStyle,
					backgroundImageHorizontalAlignment,
					backgroundImageVerticalAlignment, insideRectangle);
		}

		Rectangle textRectangle = new Rectangle(insideRectangle.x,
				insideRectangle.y, insideRectangle.width,
				insideRectangle.height);
		Rectangle imageRectangle = new Rectangle(insideRectangle.x,
				insideRectangle.y, insideRectangle.width,
				insideRectangle.height);
		if ((image != null) && (text != null)) {
			if (imageAndTextMode == CB_IMAGETEXT_LEFT_RIGHT) {
				imageRectangle.width = (int) (insideRectangle.width * imageToTextRatio);
				textRectangle.x += imageRectangle.width;
				textRectangle.width -= imageRectangle.width;
			} else if (imageAndTextMode == CB_IMAGETEXT_RIGHT_LEFT) {
				imageRectangle.width = (int) (insideRectangle.width * imageToTextRatio);
				imageRectangle.x += insideRectangle.width
						- imageRectangle.width;
				textRectangle.width -= imageRectangle.width;
			} else if (imageAndTextMode == CB_IMAGETEXT_TOP_BOTTOM) {
				imageRectangle.height = (int) (insideRectangle.height * imageToTextRatio);
				textRectangle.y += imageRectangle.height;
				textRectangle.height -= imageRectangle.height;
			} else if (imageAndTextMode == CB_IMAGETEXT_BOTTOM_TOP) {
				imageRectangle.height = (int) (insideRectangle.height * imageToTextRatio);
				imageRectangle.y += insideRectangle.height
						- imageRectangle.height;
				textRectangle.height -= imageRectangle.height;
			}
		}

		if (image != null) {
			drawImage(gc, image, imageStyle, imageHorizontalAlignment,
					imageVerticalAlignment, imageRectangle);
		}

		// text
		if (text != null) {
			// text color
			if (!colorTransition || forceRedraw) {
				foregroundToPaint = getForegroundState();
			}
			gc.setForeground(foregroundToPaint);

			// text background color
			textBackgroundToPaint = getTextBackgroundState();
			if (textBackgroundToPaint != null) {
				gc.setBackground(textBackgroundToPaint);
			}

			// text font
			gc.setFont(font);

			int textRendererHorizontalAlignment = TextRenderer.CENTER;
			int textRendererVerticalAlignment = TextRenderer.CENTER;
			if (textHorizontalAlignment == CB_ALIGNMENT_LEFT) {
				textRendererHorizontalAlignment = TextRenderer.LEFT;
			}
			if (textHorizontalAlignment == CB_ALIGNMENT_RIGHT) {
				textRendererHorizontalAlignment = TextRenderer.RIGHT;
			}
			if (textVerticalAlignment == CB_ALIGNMENT_TOP) {
				textRendererVerticalAlignment = TextRenderer.TOP;
			}
			if (textVerticalAlignment == CB_ALIGNMENT_BOTTOM) {
				textRendererVerticalAlignment = TextRenderer.BOTTOM;
			}
			TextRenderer.renderText(gc, textRectangle, true, false, text,
					textRendererHorizontalAlignment,
					textRendererVerticalAlignment,
					textBackgroundToPaint == null);
		}

		forceRedraw = false;
	}

	private void drawImage(GC gc, Image image, int style,
			int horizontalAlignment, int verticalAlignment, Rectangle rectangle) {
		gc.setAdvanced(true);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);

		gc.setClipping(rectangle);

		Image original = image;
		if (state == State.DISABLED) {
			image = new Image(getDisplay(), image, SWT.IMAGE_DISABLE);
		}

		if (style == CB_IMAGE_TILE) {
			Point imgSize = new Point(image.getImageData().width,
					image.getImageData().height);
			Point imgPosition = new Point(0, 0);
			for (imgPosition.x = rectangle.x; imgPosition.x < (rectangle.x + rectangle.width); imgPosition.x += imgSize.x) {
				for (imgPosition.y = rectangle.y; imgPosition.y < (rectangle.y + rectangle.height); imgPosition.y += imgSize.y) {
					gc.drawImage(image, 0, 0, imgSize.x, imgSize.y,
							imgPosition.x, imgPosition.y, imgSize.x, imgSize.y);
				}
			}
		}

		if (style == CB_IMAGE_ORIGINAL) {
			Point imgSize = new Point(image.getImageData().width,
					image.getImageData().height);
			Point imgPosition = calculatePosition(imgSize, rectangle,
					horizontalAlignment, verticalAlignment);

			gc.drawImage(image, 0, 0, imgSize.x, imgSize.y, imgPosition.x,
					imgPosition.y, imgSize.x, imgSize.y);
		}

		if (style == CB_IMAGE_STRETCH) {
			gc.drawImage(image, 0, 0, image.getImageData().width,
					image.getImageData().height, rectangle.x, rectangle.y,
					rectangle.width, rectangle.height);
		}

		if (style == CB_IMAGE_STRETCH_KEEP_PROPORTIONS) {
			Point imgSize = resizeAndKeepAspectRatio(
					image.getImageData().width, image.getImageData().height,
					rectangle.width, rectangle.height);

			Point imgPosition = calculatePosition(imgSize, rectangle,
					horizontalAlignment, verticalAlignment);

			gc.drawImage(image, 0, 0, image.getImageData().width,
					image.getImageData().height, imgPosition.x, imgPosition.y,
					imgSize.x, imgSize.y);
		}

		if (state == State.DISABLED) {
			image.dispose();
			image = original;
		}

		gc.setClipping((Rectangle) null);

		gc.setInterpolation(SWT.DEFAULT);
		gc.setAntialias(SWT.DEFAULT);
		gc.setAdvanced(false);
	}

	private Point calculatePosition(Point size, Rectangle rectangle,
			int horizontalAlignment, int verticalAlignment) {

		int imgPositionX = 0;
		int imgPositionY = 0;

		if (horizontalAlignment == CB_ALIGNMENT_CENTER) {
			imgPositionX = (rectangle.x + (rectangle.width / 2)) - (size.x / 2);
		} else if (horizontalAlignment == CB_ALIGNMENT_LEFT) {
			imgPositionX = rectangle.x;
		} else if (horizontalAlignment == CB_ALIGNMENT_RIGHT) {
			imgPositionX = (rectangle.x + rectangle.width) - size.x;
		}

		if (verticalAlignment == CB_ALIGNMENT_CENTER) {
			imgPositionY = (rectangle.y + (rectangle.height / 2))
					- (size.y / 2);
		} else if (verticalAlignment == CB_ALIGNMENT_TOP) {
			imgPositionY = rectangle.y;
		} else if (verticalAlignment == CB_ALIGNMENT_BOTTOM) {
			imgPositionY = (rectangle.y + rectangle.height) - size.y;
		}

		if (imgPositionX < rectangle.x) {
			imgPositionX = rectangle.x;
		}
		if (imgPositionY < rectangle.y) {
			imgPositionY = rectangle.y;
		}

		return new Point(imgPositionX, imgPositionY);
	}

	private Color getBorderState() {
		switch (state) {
		case NORMAL:
			return border;
		case PRESSED:
			return borderPressed;
		case HOVER:
			return borderHover;
		case DISABLED:
			return borderDisabled;
		case SELECTED:
			return borderSelected;
		default:
			break;
		}
		return null;
	}

	private int getBorderWidthState() {
		switch (state) {
		case NORMAL:
			return borderWidth;
		case PRESSED:
			return borderWidthPressed;
		case HOVER:
			return borderWidthHover;
		case DISABLED:
			return borderWidthDisabled;
		case SELECTED:
			return borderWidthSelected;
		default:
			break;
		}
		return 0;
	}

	private Color getBorder2State() {
		switch (state) {
		case NORMAL:
			return border2;
		case PRESSED:
			return border2Pressed;
		case HOVER:
			return border2Hover;
		case DISABLED:
			return border2Disabled;
		case SELECTED:
			return border2Selected;
		default:
			break;
		}
		return null;
	}

	private int getBorder2WidthState() {
		switch (state) {
		case NORMAL:
			return border2Width;
		case PRESSED:
			return border2WidthPressed;
		case HOVER:
			return border2WidthHover;
		case DISABLED:
			return border2WidthDisabled;
		case SELECTED:
			return border2WidthSelected;
		default:
			break;
		}
		return 0;
	}

	private Color getBackgroundState() {
		switch (state) {
		case NORMAL:
			return background;
		case PRESSED:
			return backgroundPressed;
		case HOVER:
			return backgroundHover;
		case DISABLED:
			return backgroundDisabled;
		case SELECTED:
			return backgroundSelected;
		default:
			break;
		}
		return null;
	}

	private Color getForegroundState() {
		switch (state) {
		case NORMAL:
			return foreground;
		case PRESSED:
			return foregroundPressed;
		case HOVER:
			return foregroundHover;
		case DISABLED:
			return foregroundDisabled;
		case SELECTED:
			return foregroundSelected;
		default:
			break;
		}
		return null;
	}

	private Color getTextBackgroundState() {
		switch (state) {
		case NORMAL:
			return textBackground;
		case PRESSED:
			return textBackgroundPressed;
		case HOVER:
			return textBackgroundHover;
		case DISABLED:
			return textBackgroundDisabled;
		case SELECTED:
			return textBackgroundSelected;
		default:
			break;
		}
		return null;
	}

	// transform a size keeping the original proportions
	private Point resizeAndKeepAspectRatio(int originalSizeWidth,
			int originalSizeHeight, int newWidth, int newHeight) {

		if (newHeight <= 0) {
			newHeight = 1;
		}

		float aspectRatioHeightf = originalSizeHeight;
		float aspectRatioWidthf = originalSizeWidth;
		float newHeightf = newHeight;
		float newWidthf = newWidth;

		float factor = aspectRatioHeightf / newHeightf;

		int dest_width = (int) Math.floor(aspectRatioWidthf / factor);
		int dest_height = (int) newHeightf;

		if (dest_width > newWidthf) {
			dest_width = (int) newWidthf;
			factor = aspectRatioWidthf / dest_width;
			dest_height = (int) Math.floor(aspectRatioHeightf / factor);

		}

		return new Point(dest_width, dest_height);
	}

	/* PUBLIC STUFF */

	@Override
	public void setEnabled(boolean enabled) {

		if (enabled != getEnabled()) {
			super.setEnabled(enabled);
			State prevState = state;
			if (enabled) {
				if (isMouseHovering()) {
					state = State.HOVER;
				} else {
					state = State.NORMAL;
				}
				if (colorTransition) {
					playTransition(prevState, state);
				} else {
					redraw();
				}
			} else {
				state = State.DISABLED;
				if (colorTransition) {
					playTransition(prevState, state);
				} else {
					redraw();
				}
			}
		}

	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		redraw();
	}

	@Override
	public Image getBackgroundImage() {
		return backgroundImage;
	}

	@Override
	public void setBackgroundImage(Image backgroundImage) {
		this.backgroundImage = backgroundImage;
		redraw();
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
		redraw();
	}

	public Color getTextBackground() {
		return textBackground;
	}

	public void setTextBackground(Color textBackground) {
		this.textBackground = textBackground;
		redraw();
	}

	public Color getTextBackgroundHover() {
		return textBackgroundHover;
	}

	public void setTextBackgroundHover(Color textBackgroundHover) {
		this.textBackgroundHover = textBackgroundHover;
		redraw();
	}

	public Color getTextBackgroundPressed() {
		return textBackgroundPressed;
	}

	public void setTextBackgroundPressed(Color textBackgroundPressed) {
		this.textBackgroundPressed = textBackgroundPressed;
		redraw();
	}

	public Color getTextBackgroundDisabled() {
		return textBackgroundDisabled;
	}

	public void setTextBackgroundDisabled(Color textBackgroundDisabled) {
		this.textBackgroundDisabled = textBackgroundDisabled;
		redraw();
	}

	public Color getTextBackgroundSelected() {
		return textBackgroundSelected;
	}

	public void setTextBackgroundSelected(Color textBackgroundSelected) {
		this.textBackgroundSelected = textBackgroundSelected;
		redraw();
	}

	@Override
	public Color getForeground() {
		return foreground;
	}

	@Override
	public void setForeground(Color foreground) {
		this.foreground = foreground;
		forceRedraw();
	}

	public Color getForegroundHover() {
		return foregroundHover;
	}

	public void setForegroundHover(Color foregroundHover) {
		this.foregroundHover = foregroundHover;
		forceRedraw();
	}

	public Color getForegroundPressed() {
		return foregroundPressed;
	}

	public void setForegroundPressed(Color foregroundPressed) {
		this.foregroundPressed = foregroundPressed;
		forceRedraw();
	}

	public Color getForegroundDisabled() {
		return foregroundDisabled;
	}

	public void setForegroundDisabled(Color foregroundDisabled) {
		this.foregroundDisabled = foregroundDisabled;
		forceRedraw();
	}

	public Color getForegroundSelected() {
		return foregroundSelected;
	}

	public void setForegroundSelected(Color foregroundSelected) {
		this.foregroundSelected = foregroundSelected;
		forceRedraw();
	}

	@Override
	public Color getBackground() {
		return background;
	}

	@Override
	public void setBackground(Color background) {
		this.background = background;
		forceRedraw();
	}

	public Color getBackgroundHover() {
		return backgroundHover;
	}

	public void setBackgroundHover(Color backgroundHover) {
		this.backgroundHover = backgroundHover;
		forceRedraw();
	}

	public Color getBackgroundPressed() {
		return backgroundPressed;
	}

	public void setBackgroundPressed(Color backgroundPressed) {
		this.backgroundPressed = backgroundPressed;
		forceRedraw();
	}

	public Color getBackgroundDisabled() {
		return backgroundDisabled;
	}

	public void setBackgroundDisabled(Color backgroundDisabled) {
		this.backgroundDisabled = backgroundDisabled;
		forceRedraw();
	}

	public Color getBackgroundSelected() {
		return backgroundSelected;
	}

	public void setBackgroundSelected(Color backgroundSelected) {
		this.backgroundSelected = backgroundSelected;
		forceRedraw();
	}

	public Color getBorder() {
		return border;
	}

	public void setBorder(Color border) {
		this.border = border;
		forceRedraw();
	}

	public Color getBorderHover() {
		return borderHover;
	}

	public void setBorderHover(Color borderHover) {
		this.borderHover = borderHover;
		forceRedraw();
	}

	public Color getBorderPressed() {
		return borderPressed;
	}

	public void setBorderPressed(Color borderPressed) {
		this.borderPressed = borderPressed;
		forceRedraw();
	}

	public Color getBorderDisabled() {
		return borderDisabled;
	}

	public void setBorderDisabled(Color borderDisabled) {
		this.borderDisabled = borderDisabled;
		forceRedraw();
	}

	public Color getBorderSelected() {
		return borderSelected;
	}

	public void setBorderSelected(Color borderSelected) {
		this.borderSelected = borderSelected;
		forceRedraw();
	}

	@Override
	public int getBorderWidth() {
		return borderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		this.borderWidth = borderWidth;
		redraw();
	}

	public int getBorderWidthHover() {
		return borderWidthHover;
	}

	public void setBorderWidthHover(int borderWidthHover) {
		this.borderWidthHover = borderWidthHover;
		redraw();
	}

	public int getBorderWidthPressed() {
		return borderWidthPressed;
	}

	public void setBorderWidthPressed(int borderWidthPressed) {
		this.borderWidthPressed = borderWidthPressed;
		redraw();
	}

	public int getBorderWidthDisabled() {
		return borderWidthDisabled;
	}

	public void setBorderWidthDisabled(int borderWidthDisabled) {
		this.borderWidthDisabled = borderWidthDisabled;
		redraw();
	}

	public int getBorderWidthSelected() {
		return borderWidthSelected;
	}

	public void setBorderWidthSelected(int borderWidthSelected) {
		this.borderWidthSelected = borderWidthSelected;
		redraw();
	}

	public Color getBorder2() {
		return border2;
	}

	public void setBorder2(Color border2) {
		this.border2 = border2;
		forceRedraw();
	}

	public Color getBorder2Hover() {
		return border2Hover;
	}

	public void setBorder2Hover(Color border2Hover) {
		this.border2Hover = border2Hover;
		forceRedraw();
	}

	public Color getBorder2Pressed() {
		return border2Pressed;
	}

	public void setBorder2Pressed(Color border2Pressed) {
		this.border2Pressed = border2Pressed;
		forceRedraw();
	}

	public Color getBorder2Disabled() {
		return border2Disabled;
	}

	public void setBorder2Disabled(Color border2Disabled) {
		this.border2Disabled = border2Disabled;
		forceRedraw();
	}

	public Color getBorder2Selected() {
		return border2Selected;
	}

	public void setBorder2Selected(Color border2Selected) {
		this.border2Selected = border2Selected;
		forceRedraw();
	}

	public int getBorder2Width() {
		return border2Width;
	}

	public void setBorder2Width(int border2Width) {
		this.border2Width = border2Width;
		redraw();
	}

	public int getBorder2WidthHover() {
		return border2WidthHover;
	}

	public void setBorder2WidthHover(int border2WidthHover) {
		this.border2WidthHover = border2WidthHover;
		redraw();
	}

	public int getBorder2WidthPressed() {
		return border2WidthPressed;
	}

	public void setBorder2WidthPressed(int border2WidthPressed) {
		this.border2WidthPressed = border2WidthPressed;
		redraw();
	}

	public int getBorder2WidthDisabled() {
		return border2WidthDisabled;
	}

	public void setBorder2WidthDisabled(int border2WidthDisabled) {
		this.border2WidthDisabled = border2WidthDisabled;
		redraw();
	}

	public int getBorder2WidthSelected() {
		return border2WidthSelected;
	}

	public void setBorder2WidthSelected(int border2WidthSelected) {
		this.border2WidthSelected = border2WidthSelected;
		redraw();
	}

	public int getMarginWidth() {
		return marginWidth;
	}

	public void setMarginWidth(int marginWidth) {
		this.marginWidth = marginWidth;
		redraw();
	}

	public int getMarginHeight() {
		return marginHeight;
	}

	public void setMarginHeight(int marginHeight) {
		this.marginHeight = marginHeight;
		redraw();
	}

	@Override
	public Font getFont() {
		return font;
	}

	@Override
	public void setFont(Font font) {
		this.font = font;
		redraw();
	}

	public Color getBackground2() {
		return background2;
	}

	public void setBackground2(Color background2) {
		this.background2 = background2;
		redraw();
	}

	public void setColorTransition(boolean enable) {
		colorTransition = enable;
	}

	public boolean getColorTransition() {
		return colorTransition;
	}

	public long getDurationNormalToHover() {
		return durationNormalToHover;
	}

	public void setDurationNormalToHover(long durationNormalToHover) {
		this.durationNormalToHover = durationNormalToHover;
	}

	public long getDurationSelectedToHover() {
		return durationSelectedToHover;
	}

	public void setDurationSelectedToHover(long durationSelectedToHover) {
		this.durationSelectedToHover = durationSelectedToHover;
	}

	public long getDurationHoverToPressed() {
		return durationHoverToPressed;
	}

	public void setDurationHoverToPressed(long durationHoverToPressed) {
		this.durationHoverToPressed = durationHoverToPressed;
	}

	public long getDurationDisabledToNormal() {
		return durationDisabledToNormal;
	}

	public void setDurationDisabledToNormal(long durationDisabledToNormal) {
		this.durationDisabledToNormal = durationDisabledToNormal;
	}

	public long getDurationDisabledToSelected() {
		return durationDisabledToSelected;
	}

	public void setDurationDisabledToSelected(long durationDisabledToSelected) {
		this.durationDisabledToSelected = durationDisabledToSelected;
	}

	public long getDurationNormalToSelected() {
		return durationNormalToSelected;
	}

	public void setDurationNormalToSelected(long durationNormalToSelected) {
		this.durationNormalToSelected = durationNormalToSelected;
	}

	public long getDurationSelectedToPressed() {
		return durationSelectedToPressed;
	}

	public void setDurationSelectedToPressed(long durationSelectedToPressed) {
		this.durationSelectedToPressed = durationSelectedToPressed;
	}

	public boolean isRoundedCorners() {
		return roundedCorners;
	}

	public void setRoundedCorners(boolean roundedCorners) {
		this.roundedCorners = roundedCorners;
		redraw();
	}

	public int getBackgroundImageStyle() {
		return backgroundImageStyle;
	}

	public void setBackgroundImageStyle(int backgroundImageStyle) {
		this.backgroundImageStyle = backgroundImageStyle;
		redraw();
	}

	public int getImageStyle() {
		return imageStyle;
	}

	public void setImageStyle(int imageStyle) {
		this.imageStyle = imageStyle;
		redraw();
	}

	public int getBackgroundImageHorizontalAlignment() {
		return backgroundImageHorizontalAlignment;
	}

	public void setBackgroundImageHorizontalAlignment(
			int backgroundImageHorizontalAlignment) {
		this.backgroundImageHorizontalAlignment = backgroundImageHorizontalAlignment;
		redraw();
	}

	public int getBackgroundImageVerticalAlignment() {
		return backgroundImageVerticalAlignment;
	}

	public void setBackgroundImageVerticalAlignment(
			int backgroundImageVerticalAlignment) {
		this.backgroundImageVerticalAlignment = backgroundImageVerticalAlignment;
		redraw();
	}

	public int getImageHorizontalAlignment() {
		return imageHorizontalAlignment;
	}

	public void setImageHorizontalAlignment(int imageHorizontalAlignment) {
		this.imageHorizontalAlignment = imageHorizontalAlignment;
		redraw();
	}

	public int getImageVerticalAlignment() {
		return imageVerticalAlignment;
	}

	public void setImageVerticalAlignment(int imageVerticalAlignment) {
		this.imageVerticalAlignment = imageVerticalAlignment;
		redraw();
	}

	public int getTextHorizontalAlignment() {
		return textHorizontalAlignment;
	}

	public void setTextHorizontalAlignment(int textHorizontalAlignment) {
		this.textHorizontalAlignment = textHorizontalAlignment;
		redraw();
	}

	public int getTextVerticalAlignment() {
		return textVerticalAlignment;
	}

	public void setTextVerticalAlignment(int textVerticalAlignment) {
		this.textVerticalAlignment = textVerticalAlignment;
		redraw();
	}

	public int getImageAndTextMode() {
		return imageAndTextMode;
	}

	public void setImageAndTextMode(int imageAndTextMode) {
		this.imageAndTextMode = imageAndTextMode;
		redraw();
	}

	public double getImageToTextRatio() {
		return imageToTextRatio;
	}

	public void setImageToTextRatio(double imageToTextRatio) {
		this.imageToTextRatio = imageToTextRatio;
		redraw();
	}

	public void forceRedraw() {
		forceRedraw = true;
		redraw();
	}

	/**
	 * SelectionListeners are notified when the button is clicked
	 *
	 * @param listener
	 */
	public void addSelectionListener(SelectionListener listener) {
		addListener(SWT.Selection, new TypedListener(listener));
	}

	public void removeSelectionListener(SelectionListener listener) {
		removeListener(SWT.Selection, listener);
	}

	PropertySetter<Color> backgroundToPaintSetter = new PropertySetter<Color>() {
		@Override
		public void set(Object obj, String fieldName, Color value) {
			backgroundToPaint = value;
			redraw();
		}
	};

	PropertySetter<Color> borderToPaintSetter = new PropertySetter<Color>() {
		@Override
		public void set(Object obj, String fieldName, Color value) {
			borderToPaint = value;
			redraw();
		}
	};

	PropertySetter<Color> border2ToPaintSetter = new PropertySetter<Color>() {
		@Override
		public void set(Object obj, String fieldName, Color value) {
			border2ToPaint = value;
			redraw();
		}
	};

	PropertySetter<Color> foregroundToPaintSetter = new PropertySetter<Color>() {
		@Override
		public void set(Object obj, String fieldName, Color value) {
			foregroundToPaint = value;
			redraw();
		}
	};
}
