package test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import customButton.CustomButton;

public class Test {

	static int yPos = 0;
	static int yMargin = 5;
	static int xSize = 200;
	static int ySize = 50;
	static Display display;
	static TabFolder tabFolder;
	static Image backgroundImage;
	static Image image;
	static Image image2;

	public static void main(String[] args) {

		Shell shell = new Shell();
		shell.setSize(480, 480);
		shell.setText("CustomButton test");
		shell.setLayout(new FillLayout());

		display = Display.getCurrent();
		
		backgroundImage = new Image(display, Test.class.getClassLoader().getResourceAsStream("test/eclipse32.png"));
		image = new Image(display, Test.class.getClassLoader().getResourceAsStream("test/overview-select.png"));
		image2 = new Image(display, Test.class.getClassLoader().getResourceAsStream("test/eclipse16.png"));

		tabFolder = new TabFolder(shell, SWT.TOP);

		colorsTab();
		imagesTab();
		text_imagesTab();
		alignmentsTab();
		layoutTab();

		shell.open();
		while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch()) {
				shell.getDisplay().sleep();
			}
		}
	}

	private static void colorsTab() {

		TabItem colorsTab = new TabItem(tabFolder, 0);
		colorsTab.setText("Colors");
		ScrolledComposite colorsContentScroll = new ScrolledComposite(
				tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		colorsTab.setControl(colorsContentScroll);

		Composite colorsContent = new Composite(colorsContentScroll, 0);
		colorsContentScroll.setContent(colorsContent);

		yPos = 0;

		final CustomButton standardCustomButton = new CustomButton(
				colorsContent, 0);
		standardCustomButton.setText("Default CustomButton");
		standardCustomButton.setBounds(0, yPos, xSize, ySize);

		yPos += (ySize + yMargin);

		final CustomButton standardNoTransition = new CustomButton(
				colorsContent, 0);
		standardNoTransition.setText("No animated color transitions");
		standardNoTransition.setBounds(0, yPos, xSize, ySize);
		standardNoTransition.setColorTransition(false);

		yPos += (ySize + yMargin);

		final CustomButton customButton1 = new CustomButton(colorsContent, 0);
		customButton1.setText("Messing with colors");
		customButton1.setBounds(0, yPos, 200, ySize);
		setColors(customButton1);

		yPos += (ySize + yMargin);

		final CustomButton customButton2 = new CustomButton(colorsContent, 0);
		customButton2.setText("No animated color transitions");
		customButton2.setBounds(0, yPos, 200, ySize);
		customButton2.setColorTransition(false);
		setColors(customButton2);

		yPos += (ySize + yMargin);

		final CustomButton customButton3 = new CustomButton(colorsContent, 0);
		customButton3.setText("Increased color transitions duration");
		customButton3.setBounds(0, yPos, 200, ySize);
		customButton3.setBackgroundHover(display
				.getSystemColor(SWT.COLOR_WHITE));
		customButton3.setBorderHover(display.getSystemColor(SWT.COLOR_RED));
		customButton3.setBorder2Hover(display.getSystemColor(SWT.COLOR_GREEN));
		customButton3.setForegroundHover(display.getSystemColor(SWT.COLOR_RED));
		customButton3.setDurationDisabledToNormal(1000);
		customButton3.setDurationDisabledToSelected(1000);
		customButton3.setDurationHoverToPressed(1000);
		customButton3.setDurationNormalToHover(1000);
		customButton3.setDurationNormalToSelected(1000);
		customButton3.setDurationSelectedToHover(1000);
		customButton3.setDurationSelectedToPressed(1000);

		CustomButton disableButton = new CustomButton(colorsContent, 0);
		disableButton.setText("Enable/disable the other buttons");
		disableButton.setBounds(xSize + 50, 0, xSize, ySize);
		disableButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				standardCustomButton.setEnabled(!standardCustomButton
						.getEnabled());
				standardNoTransition.setEnabled(!standardNoTransition
						.getEnabled());
				customButton1.setEnabled(!customButton1.getEnabled());
				customButton2.setEnabled(!customButton2.getEnabled());
				customButton3.setEnabled(!customButton3.getEnabled());
			}
		});

		colorsContent.pack();
	}

	private static void imagesTab() {
		TabItem imagesTab = new TabItem(tabFolder, 0);
		imagesTab.setText("Images");
		ScrolledComposite imagesContentScroll = new ScrolledComposite(
				tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		imagesTab.setControl(imagesContentScroll);

		Composite imagesContent = new Composite(imagesContentScroll, 0);
		imagesContentScroll.setContent(imagesContent);

		Label info = new Label(imagesContent, 0);
		info.setText("Check the tooltips for more infos!");
		info.setBounds(5, 5, xSize, 20);

		yPos = 30;

		final CustomButton backgroundImageButton = new CustomButton(
				imagesContent, 0);
		backgroundImageButton.setBounds(0, yPos, xSize, ySize);
		backgroundImageButton.setBackgroundImage(backgroundImage);
		backgroundImageButton
				.setToolTipText("Background image default style (CB_IMAGE_TILE)");

		yPos += (ySize + yMargin);

		final CustomButton backgroundImageButton2 = new CustomButton(
				imagesContent, 0);
		backgroundImageButton2.setBounds(0, yPos, xSize, ySize);
		backgroundImageButton2.setBackgroundImage(backgroundImage);
		backgroundImageButton2
				.setBackgroundImageStyle(CustomButton.CB_IMAGE_STRETCH_KEEP_PROPORTIONS);
		backgroundImageButton2
				.setToolTipText("Background image CB_IMAGE_STRETCH_KEEP_PROPORTIONS");

		yPos += (ySize + yMargin);

		final CustomButton backgroundImageButton3 = new CustomButton(
				imagesContent, 0);
		backgroundImageButton3.setBounds(0, yPos, xSize, ySize);
		backgroundImageButton3.setBackgroundImage(backgroundImage);
		backgroundImageButton3
				.setBackgroundImageStyle(CustomButton.CB_IMAGE_STRETCH);
		backgroundImageButton3
				.setToolTipText("Background image CB_IMAGE_STRETCH");

		yPos += (ySize + yMargin);

		final CustomButton backgroundImageButton4 = new CustomButton(
				imagesContent, 0);
		backgroundImageButton4.setBounds(0, yPos, xSize, ySize);
		backgroundImageButton4.setBackgroundImage(backgroundImage);
		backgroundImageButton4
				.setBackgroundImageStyle(CustomButton.CB_IMAGE_ORIGINAL);
		backgroundImageButton4
				.setToolTipText("Background image CB_IMAGE_ORIGINAL");

		yPos += (ySize + yMargin);

		final CustomButton bkimg_imageButton = new CustomButton(imagesContent,
				0);
		bkimg_imageButton.setBounds(0, yPos, xSize, ySize);
		bkimg_imageButton.setBackgroundImage(backgroundImage);
		bkimg_imageButton.setImage(image);
		bkimg_imageButton
				.setToolTipText("Background image + image default style (CB_IMAGE_STRETCH_KEEP_PROPORTIONS)");

		yPos += (ySize + yMargin);

		final CustomButton bkimg_imageButton2 = new CustomButton(imagesContent,
				0);
		bkimg_imageButton2.setBounds(0, yPos, xSize, ySize);
		bkimg_imageButton2.setBackgroundImage(backgroundImage);
		bkimg_imageButton2.setImage(image);
		bkimg_imageButton2.setImageStyle(CustomButton.CB_IMAGE_STRETCH);
		bkimg_imageButton2
				.setToolTipText("Background image + image CB_IMAGE_STRETCH");

		yPos += (ySize + yMargin);

		final CustomButton bkimg_imageButton3 = new CustomButton(imagesContent,
				0);
		bkimg_imageButton3.setBounds(0, yPos, xSize, ySize);
		bkimg_imageButton3.setBackgroundImage(backgroundImage);
		bkimg_imageButton3.setImage(image);
		bkimg_imageButton3.setImageStyle(CustomButton.CB_IMAGE_ORIGINAL);
		bkimg_imageButton3
				.setToolTipText("Background image + image CB_IMAGE_ORIGINAL");

		CustomButton disableButton = new CustomButton(imagesContent, 0);
		disableButton.setText("Enable/disable the other buttons");
		disableButton.setBounds(xSize + 50, 0, xSize, ySize);
		disableButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				backgroundImageButton.setEnabled(!backgroundImageButton
						.getEnabled());
				backgroundImageButton2.setEnabled(!backgroundImageButton2
						.getEnabled());
				backgroundImageButton3.setEnabled(!backgroundImageButton3
						.getEnabled());
				backgroundImageButton4.setEnabled(!backgroundImageButton4
						.getEnabled());
				bkimg_imageButton.setEnabled(!bkimg_imageButton.getEnabled());
				bkimg_imageButton2.setEnabled(!bkimg_imageButton2.getEnabled());
				bkimg_imageButton3.setEnabled(!bkimg_imageButton3.getEnabled());
			}
		});

		imagesContent.pack();
	}

	private static void text_imagesTab() {
		TabItem textImagesTab = new TabItem(tabFolder, 0);
		textImagesTab.setText("Images + text");
		ScrolledComposite textImagesContentScroll = new ScrolledComposite(
				tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		textImagesTab.setControl(textImagesContentScroll);

		Composite textImagesContent = new Composite(textImagesContentScroll, 0);
		textImagesContentScroll.setContent(textImagesContent);

		Label info = new Label(textImagesContent, 0);
		info.setText("Check the tooltips for more infos!");
		info.setBounds(5, 5, xSize, 20);

		yPos = 30;

		final CustomButton textImageButton = new CustomButton(
				textImagesContent, 0);
		textImageButton.setBounds(0, yPos, xSize, ySize);
		textImageButton.setText("Default");
		textImageButton.setImage(image);
		textImageButton
				.setToolTipText("Text + image, default (CB_IMAGETEXT_LEFT_RIGHT, imageToTextRatio = 0.5)");

		yPos += (ySize + yMargin);

		final CustomButton textImageButton2 = new CustomButton(
				textImagesContent, 0);
		textImageButton2.setBounds(0, yPos, xSize, ySize);
		textImageButton2.setText("Right/left, 0.2");
		textImageButton2.setImage(image);
		textImageButton2
				.setImageAndTextMode(CustomButton.CB_IMAGETEXT_RIGHT_LEFT);
		textImageButton2.setImageToTextRatio(0.2);
		textImageButton2
				.setToolTipText("Text + image, CB_IMAGETEXT_RIGHT_LEFT, imageToTextRatio = 0.2");

		yPos += (ySize + yMargin);

		final CustomButton textImageButton3 = new CustomButton(
				textImagesContent, 0);
		textImageButton3.setBounds(0, yPos, xSize, ySize);
		textImageButton3.setText("Top/bottom, 0.7");
		textImageButton3.setImage(image);
		textImageButton3
				.setImageAndTextMode(CustomButton.CB_IMAGETEXT_TOP_BOTTOM);
		textImageButton3.setImageToTextRatio(0.7);
		textImageButton3
				.setToolTipText("Text + image, CB_IMAGETEXT_TOP_BOTTOM, imageToTextRatio = 0.7");

		yPos += (ySize + yMargin);

		final CustomButton textImageButton4 = new CustomButton(
				textImagesContent, 0);
		textImageButton4.setBounds(0, yPos, xSize, ySize);
		textImageButton4.setText("Bottom/top, 0.5");
		textImageButton4.setImage(image);
		textImageButton4
				.setImageAndTextMode(CustomButton.CB_IMAGETEXT_BOTTOM_TOP);
		textImageButton4.setImageToTextRatio(0.5);
		textImageButton4
				.setToolTipText("Text + image, CB_IMAGETEXT_BOTTOM_TOP, imageToTextRatio = 0.5");

		yPos += (ySize + yMargin);

		final CustomButton textImageButton5 = new CustomButton(
				textImagesContent, 0);
		textImageButton5.setBounds(0, yPos, xSize, ySize);
		textImageButton5.setText("No mode");
		textImageButton5.setImage(image);
		textImageButton5.setImageAndTextMode(CustomButton.CB_IMAGETEXT_NONE);
		textImageButton5.setToolTipText("Text + image, CB_IMAGETEXT_NONE");

		yPos += (ySize + yMargin);

		textImagesContent.pack();
	}

	private static void alignmentsTab() {
		TabItem alignmentsTab = new TabItem(tabFolder, 0);
		alignmentsTab.setText("Alignments");
		ScrolledComposite alignmentsContentScroll = new ScrolledComposite(
				tabFolder, SWT.H_SCROLL | SWT.V_SCROLL);
		alignmentsTab.setControl(alignmentsContentScroll);

		Composite alignmentsContent = new Composite(alignmentsContentScroll, 0);
		alignmentsContentScroll.setContent(alignmentsContent);

		Label info = new Label(alignmentsContent, 0);
		info.setText("Check the tooltips for more infos!");
		info.setBounds(5, 5, xSize, 20);

		yPos = 30;

		final CustomButton alignmentsButton = new CustomButton(
				alignmentsContent, 0);
		alignmentsButton.setBounds(0, yPos, xSize, ySize);
		alignmentsButton.setText("Top/left");
		alignmentsButton
				.setTextHorizontalAlignment(CustomButton.CB_ALIGNMENT_LEFT);
		alignmentsButton
				.setTextVerticalAlignment(CustomButton.CB_ALIGNMENT_TOP);
		alignmentsButton.setToolTipText("Text alignment top/left");

		yPos += (ySize + yMargin);

		final CustomButton alignmentsButton2 = new CustomButton(
				alignmentsContent, 0);
		alignmentsButton2.setBounds(0, yPos, xSize, ySize);
		alignmentsButton2.setText("Bottom");
		alignmentsButton2
				.setTextVerticalAlignment(CustomButton.CB_ALIGNMENT_BOTTOM);
		alignmentsButton2.setToolTipText("Text alignment bottom");

		yPos += (ySize + yMargin);

		final CustomButton alignmentsButton3 = new CustomButton(
				alignmentsContent, 0);
		alignmentsButton3.setBounds(0, yPos, xSize, ySize);
		alignmentsButton3.setImage(image2);
		alignmentsButton3
				.setImageHorizontalAlignment(CustomButton.CB_ALIGNMENT_LEFT);
		alignmentsButton3.setImageStyle(CustomButton.CB_IMAGE_ORIGINAL);
		alignmentsButton3.setToolTipText("Image alignment left");

		yPos += (ySize + yMargin);

		final CustomButton alignmentsButton4 = new CustomButton(
				alignmentsContent, 0);
		alignmentsButton4.setBounds(0, yPos, xSize, ySize);
		alignmentsButton4.setImage(image2);
		alignmentsButton4
				.setImageHorizontalAlignment(CustomButton.CB_ALIGNMENT_RIGHT);
		alignmentsButton4
				.setImageVerticalAlignment(CustomButton.CB_ALIGNMENT_TOP);
		alignmentsButton4.setImageStyle(CustomButton.CB_IMAGE_ORIGINAL);
		alignmentsButton4.setToolTipText("Image alignment top/right");

		yPos += (ySize + yMargin);

		final CustomButton alignmentsButton5 = new CustomButton(
				alignmentsContent, 0);
		alignmentsButton5.setBounds(0, yPos, xSize, ySize);
		alignmentsButton5.setText("Right");
		alignmentsButton5
				.setTextHorizontalAlignment(CustomButton.CB_ALIGNMENT_RIGHT);
		alignmentsButton5.setImage(image2);
		alignmentsButton5
				.setImageHorizontalAlignment(CustomButton.CB_ALIGNMENT_LEFT);
		alignmentsButton5
				.setImageVerticalAlignment(CustomButton.CB_ALIGNMENT_BOTTOM);
		alignmentsButton5.setImageStyle(CustomButton.CB_IMAGE_ORIGINAL);
		alignmentsButton5
				.setToolTipText("Image alignment bottom/left + text alignment right");

		yPos += (ySize + yMargin);

		alignmentsContent.pack();

	}

	private static void layoutTab() {
		TabItem layoutTab = new TabItem(tabFolder, 0);
		layoutTab.setText("Layout");

		Composite layoutContent = new Composite(tabFolder, 0);
		layoutContent.setLayout(new FillLayout(SWT.VERTICAL));
		layoutTab.setControl(layoutContent);

		final CustomButton defaultButton = new CustomButton(layoutContent, 0);
		defaultButton.setText("Default");

		final CustomButton customButton1 = new CustomButton(layoutContent, 0);
		customButton1.setText("Messing with colors");
		setColors(customButton1);

		final CustomButton bkimg_imageButton = new CustomButton(layoutContent,
				0);
		bkimg_imageButton.setBackgroundImage(backgroundImage);
		bkimg_imageButton.setImage(image);

		final CustomButton textImageButton2 = new CustomButton(layoutContent, 0);
		textImageButton2.setText("Right/left, 0.2");
		textImageButton2.setImage(image);
		textImageButton2
				.setImageAndTextMode(CustomButton.CB_IMAGETEXT_RIGHT_LEFT);
		textImageButton2.setImageToTextRatio(0.2);

		final CustomButton alignmentsButton5 = new CustomButton(layoutContent,
				0);
		alignmentsButton5.setText("Right");
		alignmentsButton5
				.setTextHorizontalAlignment(CustomButton.CB_ALIGNMENT_RIGHT);
		alignmentsButton5.setImage(image2);
		alignmentsButton5
				.setImageHorizontalAlignment(CustomButton.CB_ALIGNMENT_LEFT);
		alignmentsButton5
				.setImageVerticalAlignment(CustomButton.CB_ALIGNMENT_BOTTOM);
		alignmentsButton5.setImageStyle(CustomButton.CB_IMAGE_ORIGINAL);

	}

	private static void setColors(CustomButton customButton) {
		// normal
		customButton.setBackground(display.getSystemColor(SWT.COLOR_RED));
		customButton.setBorder(display.getSystemColor(SWT.COLOR_BLACK));
		customButton.setBorderWidth(5);
		customButton.setBorder2(display.getSystemColor(SWT.COLOR_GREEN));
		customButton.setBorder2Width(5);
		customButton.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
		customButton.setTextBackground(null);

		// hover
		customButton.setBackgroundHover(display
				.getSystemColor(SWT.COLOR_MAGENTA));
		customButton.setBorderHover(display.getSystemColor(SWT.COLOR_GRAY));
		customButton.setBorderWidthHover(3);
		customButton.setBorder2Hover(display.getSystemColor(SWT.COLOR_GREEN));
		customButton.setBorder2WidthHover(0);
		customButton.setForegroundHover(display.getSystemColor(SWT.COLOR_GRAY));
		customButton.setTextBackgroundHover(display
				.getSystemColor(SWT.COLOR_BLACK));

		// pressed
		customButton.setBackgroundPressed(display
				.getSystemColor(SWT.COLOR_WHITE));
		customButton.setBorderPressed(display.getSystemColor(SWT.COLOR_RED));
		customButton.setBorderWidthPressed(1);
		customButton.setBorder2Pressed(display.getSystemColor(SWT.COLOR_GREEN));
		customButton.setBorder2WidthPressed(0);
		customButton
				.setForegroundPressed(display.getSystemColor(SWT.COLOR_RED));
		customButton.setTextBackgroundPressed(null);

		// selected
		customButton.setBackgroundSelected(display
				.getSystemColor(SWT.COLOR_YELLOW));
		customButton.setBorderSelected(display.getSystemColor(SWT.COLOR_CYAN));
		customButton.setBorderWidthSelected(2);
		customButton.setBorder2Selected(display
				.getSystemColor(SWT.COLOR_MAGENTA));
		customButton.setBorder2WidthSelected(3);
		customButton.setForegroundSelected(display
				.getSystemColor(SWT.COLOR_GREEN));
		customButton.setTextBackgroundSelected(display
				.getSystemColor(SWT.COLOR_GRAY));

		// disabled
		customButton.setBackgroundDisabled(display
				.getSystemColor(SWT.COLOR_CYAN));
		customButton.setBorderDisabled(display.getSystemColor(SWT.COLOR_BLUE));
		customButton.setBorderWidthDisabled(2);
		customButton
				.setBorder2Disabled(display.getSystemColor(SWT.COLOR_GREEN));
		customButton.setBorder2WidthDisabled(0);
		customButton.setForegroundDisabled(display
				.getSystemColor(SWT.COLOR_WHITE));
		customButton.setTextBackgroundDisabled(display
				.getSystemColor(SWT.COLOR_BLUE));
	}

}
