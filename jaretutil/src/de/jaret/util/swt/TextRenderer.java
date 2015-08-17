/*
 *  File: TextRenderer.java
 *  Copyright (c) 2004-2007  Peter Kliem (Peter.Kliem@jaret.de)
 *  A commercial license is available, see http://www.jaret.de.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
package de.jaret.util.swt;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

/**
 * Simple utility class for rendering a multiline text.
 *
 * @author Peter Kliem
 * @version $Id: TextRenderer.java 623 2007-11-01 15:23:45Z kliem $
 */
public class TextRenderer {
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int CENTER = 2;
	public static final int TOP = 0;
	public static final int BOTTOM = 1;

	public static void renderText(GC gc, Rectangle rect, boolean wrap,
			boolean ellipsis, String text) {
		renderText(gc, rect, wrap, ellipsis, text, LEFT, TOP);
	}

	public static void renderText(GC gc, Rectangle rect, boolean wrap,
			boolean ellipsis, String text, int halign, int valign) {
		renderText(gc, rect, wrap, ellipsis, text, LEFT, TOP, true);
	}

	public static void renderText(GC gc, Rectangle rect, boolean wrap,
			boolean ellipsis, String text, int halign, int valign,
			boolean isTransparent) {
		Rectangle clipSave = gc.getClipping();
		gc.setClipping(rect.intersection(clipSave));
		List<String> lines = breakInLines(gc, rect.width, wrap, text);

		// for (String string : lines) {
		// System.out.println(string);
		// }
		//
		int height = getHeight(gc, rect.width, wrap, lines);
		int offy = 0;
		if (height < rect.height) {
			if (valign == CENTER) {
				offy = (rect.height - height) / 2;
			}
			else if (valign == BOTTOM) {
				offy = rect.height - height;
			}
		}

		int lineheight = SwtGraphicsHelper.getStringDrawingHeight(gc, "WgyAqQ");
		int lineSpacing = 3;
		for (int row = 0; row < lines.size(); row++) {
			int y = rect.y + (row * lineheight) + (row * lineSpacing);
			drawLine(gc, rect.x, y + offy, rect.width, lines.get(row), halign,
					isTransparent);
			// System.out.println("Line "+row+"y"+y+"'"+(String)lines.get(row)+"'");
		}
		gc.setClipping(clipSave);
	}

	public static int getHeight(GC gc, int width, boolean wrap, String text) {
		List<String> lines = breakInLines(gc, width, wrap, text);
		return getHeight(gc, width, wrap, lines);
	}

	private static int getHeight(GC gc, int width, boolean wrap,
			List<String> lines) {
		if (lines.size() == 0) {
			return 0;
		}
		int lineheight = SwtGraphicsHelper.getStringDrawingHeight(gc,
				lines.get(0));
		int lineSpacing = 3;
		int height = (lines.size() * lineheight)
				+ ((lines.size() - 1) * lineSpacing);
		return height;
	}

	private static void drawLine(GC gc, int x, int y, int width, String string,
			int halign, boolean isTransparent) {
		int xx;
		int textWidth = SwtGraphicsHelper.getStringDrawingWidth(gc, string);
		switch (halign) {
		case LEFT:
			xx = x;
			break;
		case RIGHT:
			xx = x + (width - textWidth);
			break;
		case CENTER:
			xx = x + ((width - textWidth) / 2);
			break;
		default:
			throw new RuntimeException("illegal alignment");
		}
		gc.drawText(string, xx, y, isTransparent);

	}

	public static List<String> breakInLines(GC gc, int width, boolean wrap,
			String text) {
		List<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(text, "\n", false);
		while (tokenizer.hasMoreTokens()) {
			result.add(tokenizer.nextToken());
		}
		if (wrap) {
			List<String> brokenLines = new ArrayList<String>();
			Iterator it = result.iterator();
			while (it.hasNext()) {
				String line = (String) it.next();
				List<String> brLines = wrapLines(gc, width, line);
				brokenLines.addAll(brLines);
			}

			return brokenLines;
		}
		else {
			return result;
		}
	}

	/**
	 * @param gc
	 * @param width
	 * @param text
	 * @return
	 */
	private static List<String> wrapLines(GC gc, int width, String text) {
		List<String> result = new ArrayList<String>();
		StringTokenizer tokenizer = new StringTokenizer(text, " ", false);
		StringBuffer buf = new StringBuffer();
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			addToken(gc, width, buf, token, result);
		}

		if (buf.length() > 0) {
			result.add(buf.toString());
		}

		return result;
	}

	private static void addToken(GC gc, int width, StringBuffer buf,
			String token, List<String> result) {
		String testString;
		if (buf.length() > 0) {
			testString = " " + token;
		}
		else {
			testString = token;
		}

		if (SwtGraphicsHelper.getStringDrawingWidth(gc, buf.toString()
				+ testString) < width) {
			buf.append(testString);
		}
		else if (buf.length() == 0) {
			// a single word did not fit
			List<String> brWord = breakWord(gc, width, token);
			if (brWord.size() == 1) {
				result.add(brWord.get(0));
			}
			else {
				for (int i = 0; i < (brWord.size() - 1); i++) {
					result.add(brWord.get(i));
				}
				addToken(gc, width, buf, brWord.get(brWord.size() - 1), result);
			}
		}
		else {
			result.add(buf.toString());
			buf.setLength(0);
			addToken(gc, width, buf, token, result);
		}

	}

	/**
	 * break a word into strings fitting into width.
	 *
	 * @param gc
	 * @param width
	 * @param word
	 * @return
	 */
	private static List<String> breakWord(GC gc, int width, String word) {
		List<String> result = new ArrayList<String>();

		for (int bidx = 0, eidx = 1; eidx <= word.length(); eidx++) {
			String wordPart = word.substring(bidx, eidx);
			int wordPartExtent = SwtGraphicsHelper.getStringDrawingWidth(gc,
					wordPart);
			if (wordPartExtent > width) {
				if ((eidx - 1) > bidx) {
					eidx--;
				}
				result.add(word.substring(bidx, eidx));
				bidx = eidx;
			}
			else if (eidx == word.length()) {
				result.add(word.substring(bidx, eidx));
			}
		}

		return result;
	}
}
