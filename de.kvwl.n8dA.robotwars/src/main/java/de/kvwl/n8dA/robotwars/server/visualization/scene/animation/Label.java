package de.kvwl.n8dA.robotwars.server.visualization.scene.animation;

import game.engine.stage.scene.object.LabelObject;
import game.engine.stage.scene.object.Orientation.HorizontalOrientation;
import game.engine.stage.scene.object.Orientation.VerticalOrientation;
import game.engine.stage.scene.object.Point;
import game.engine.stage.scene.object.ScaleStrategy;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * Benutzt ein {@link LabelObject} zur Anzeige
 */
public class Label extends AnimatedSceneObject {

	private LabelObject lbl = new LabelObject();

	public Label() {

		lbl.setTopLeftPosition(new Point(0, 0));
	}

	@Override
	protected void paint(Graphics2D g, long elapsedTime) {
		super.paint(g, elapsedTime);

		lbl.setSize(getSize());
		lbl.paintOnScene(g, elapsedTime);
	}

	public Font getFont() {
		return lbl.getFont();
	}

	public int getFontFlags() {
		return lbl.getFontFlags();
	}

	public Paint getOutlinePaint() {
		return lbl.getOutlinePaint();
	}

	public Paint getPaint() {
		return lbl.getPaint();
	}

	public ScaleStrategy getScaleStrategy() {
		return lbl.getScaleStrategy();
	}

	public Stroke getStroke() {
		return lbl.getStroke();
	}

	public String getText() {
		return lbl.getText();
	}

	public void setFont(Font font) {

		lbl.setFont(font);
	}

	public void setColor(Color color) {
		lbl.setColor(color);
	}

	public void setFontFlags(int fontFlags) {
		lbl.setFontFlags(fontFlags);
	}

	public void setHorizontalTextOrientation(
			HorizontalOrientation horizontalTextOrientation) {
		lbl.setHorizontalTextOrientation(horizontalTextOrientation);
	}

	public void setOutlineColor(Color outlineColor) {
		lbl.setOutlineColor(outlineColor);
	}

	public void setOutlinePaint(Paint outlinePaint) {
		lbl.setOutlinePaint(outlinePaint);
	}

	public void setPaint(Paint paint) {
		lbl.setPaint(paint);
	}

	public void setScaleStrategy(ScaleStrategy scaleStrategy) {
		lbl.setScaleStrategy(scaleStrategy);
	}

	public void setStroke(Stroke stroke) {
		lbl.setStroke(stroke);
	}

	public void setText(String text) {
		lbl.setText(text);
	}

	public void setVerticalTextOrientation(
			VerticalOrientation verticalTextOrientation) {
		lbl.setVerticalTextOrientation(verticalTextOrientation);
	}

}
