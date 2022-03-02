package mesa.gui.controls.input;

import java.util.ArrayList;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.HitInfo;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import mesa.gui.controls.Font;

public class RichInput extends StackPane {
	private StringProperty textProperty;

	private ArrayList<State> history = new ArrayList<>();
	private int historyPos = 0;

	private StringBuilder text;
	private IntegerProperty caretPos;
	private ObjectProperty<IndexRange> selection;

	private Font defaultFont = new Font(32);

	private Canvas selectionPane;
	private TextFlow textDisp;
	private Label promptText;

	private Rectangle caret;

	private Timeline hideCaret;
	private Timeline showCaret;

	private ObjectProperty<Paint> fill;

	private Runnable action;

	public RichInput() {
		setPadding(new Insets(10));
		setFocusTraversable(true);
		setPickOnBounds(true);
		setAlignment(Pos.TOP_LEFT);
		setCursor(Cursor.TEXT);

		textProperty = new SimpleStringProperty("");

		text = new StringBuilder();
		caretPos = new SimpleIntegerProperty(0);
		caretPos.addListener((obs, ov, nv) -> positionCaret(nv.intValue()));

		selection = new SimpleObjectProperty<>(new IndexRange(-1, -1));

		saveState();

		selectionPane = new Canvas();
		textDisp = new TextFlow();
		textDisp.setCache(true);
		caret = new Rectangle();
		caret.setY(1);

		promptText = new Label("");

		maxHeightProperty().bind(Bindings.createDoubleBinding(
				() -> textHeight() + getPadding().getTop() + getPadding().getBottom(), textDisp.needsLayoutProperty()));

		setMinWidth(0);
		setMinHeight(0);

		selectionPane.widthProperty().bind(textDisp.widthProperty());
		selectionPane.heightProperty().bind(textDisp.heightProperty());

		textDisp.needsLayoutProperty().addListener((obs, ov, nv) -> {
			positionCaret(caretPos.get());
			displaySelection();
		});

		caret.setWidth(1);
		caret.visibleProperty().bind(focusedProperty());

		focusedProperty().addListener((obs, ov, nv) -> {
			if (nv.booleanValue()) {
				keepCaret();
			}
		});

		hideCaret = new Timeline(
				new KeyFrame(Duration.seconds(.5), new KeyValue(caret.opacityProperty(), 0, Interpolator.DISCRETE)));
		showCaret = new Timeline(
				new KeyFrame(Duration.seconds(.5), new KeyValue(caret.opacityProperty(), 1, Interpolator.DISCRETE)));

		hideCaret.setOnFinished(e -> showCaret.playFromStart());
		showCaret.setOnFinished(e -> hideCaret.playFromStart());

		hideCaret.playFromStart();

		fill = new SimpleObjectProperty<>(Color.BLACK);

		fill.addListener((obs, ov, nv) -> displaySelection());

		caret.fillProperty().bind(fill);
		promptText.textFillProperty().bind(fill);

		setOnKeyTyped(e -> {
			keepCaret();

			String codes = e.getCharacter();
			int code = codes.charAt(0);

			boolean canSaveHistory = true;

			String oldValue = text.toString();
			switch (code) {
			case 1:
				selectAll();
				break;
			case 3:
				copy();
				break;
			case 8:
				backspace();
				break;
			case 22:
				paste();
				break;
			case 24:
				cut();
				break;
			case 25:
				canSaveHistory = false;
				redo();
				break;
			case 26:
				canSaveHistory = false;
				undo();
				break;
			case 127:
				delete();
				break;
			default:
				if ((code == 13) && !e.isShiftDown()) {
					if (action != null) {
						action.run();
					}
				} else {
					append(e.getCharacter());
				}
			}

			String newValue = text.toString();

			if (!newValue.equals(oldValue) && canSaveHistory) {
				saveState();
			}
		});

		setOnKeyPressed(e -> {
			keepCaret();

			int code = e.getCode().getCode();

			switch (code) {
			case 35:
				clearSelection(-1);
				caretPos.set(text.length());
				break;
			case 36:
				clearSelection(-1);
				caretPos.set(0);
				break;
			case 37:
				int nexIndex = decrementCaretPos();
				if (e.isShiftDown()) {
					moveSelectionTo(nexIndex);
					caretPos.set(nexIndex);
				} else {
					if (selected()) {
						clearSelection(selection.get().getStart());
					} else {
						caretPos.set(nexIndex);
					}
				}
				break;
			case 39:
				int prevIndex = incrementCaretPos();
				if (e.isShiftDown()) {
					moveSelectionTo(prevIndex);
					caretPos.set(prevIndex);
				} else {
					if (selected()) {
						clearSelection(selection.get().getEnd());
					} else {
						caretPos.set(prevIndex);
					}
				}
				break;
			default:
				break;
			}

			e.consume();
		});

		setOnMousePressed(e -> {
			if (e.getButton().equals(MouseButton.PRIMARY)) {
				requestFocus();
				clearSelection(-1);
				int ind = indexForEvent(e);
				caretPos.set(ind);
			}
		});

		setOnMouseDragged(e -> {
			selection.set(IndexRange.normalize(caretPos.get(), indexForEvent(e)));
			displaySelection();
		});

		promptText.visibleProperty().bind(textProperty.isEmpty());
		promptText.setOpacity(.4);

		selectionPane.visibleProperty().bind(focusedProperty());

		getChildren().addAll(promptText, selectionPane, textDisp, caret);
	}

	public void setAction(Runnable action) {
		this.action = action;
	}

	public void setLineSpacing(double s) {
		textDisp.setLineSpacing(s);
		displaySelection();
	}

	public void selectAll() {
		caretPos.set(0);
		selection.set(new IndexRange(0, 0));
		moveSelectionTo(length());
		caretPos.set(length());
	}

	public void backspace() {
		if (selected()) {
			deleteSelection();
		} else {
			if (caretPos.get() > 0) {
				InputElement ie = (InputElement) textDisp.getChildren().get(caretPos.get() - 1);

				int delStart = posToPos(caretPos.get() - 1);
				text.delete(delStart, delStart + ie.getValue().length());
				applyText();
				caretPos.set(caretPos.get() - 1);
			}
		}
	}

	public void delete() {
		if (selected()) {
			deleteSelection();
		} else {
			if (caretPos.get() < length()) {
				InputElement ie = (InputElement) textDisp.getChildren().get(caretPos.get());

				int delStart = posToPos(caretPos.get());
				text.delete(delStart, delStart + ie.getValue().length());
				applyText();
			}
		}
	}

	public void cut() {
		if (selected()) {
			copy();
			deleteSelection();
		}
	}

	public void copy() {
		if (selected()) {
			IndexRange range = selection.get();
			ClipboardContent content = new ClipboardContent();
			String val = text.substring(Math.max(0, posToPos(range.getStart())),
					Math.min(text.length(), posToPos(range.getEnd())));
			content.putString(val);
			Clipboard.getSystemClipboard().setContent(content);
		}
	}

	public String getSelectedText() {
		if (selected()) {
			IndexRange range = selection.get();
			return text.substring(Math.max(0, posToPos(range.getStart())),
					Math.min(text.length(), posToPos(range.getEnd())));
		} else {
			return "";
		}
	}

	public void paste() {
		String val = Clipboard.getSystemClipboard().getString();
		if (val != null)
			append(val);
	}

	public void undo() {
		if (historyPos > 0)
			loadState(historyPos - 1);
	}

	public void redo() {
		if (historyPos < history.size() - 1)
			loadState(historyPos + 1);
	}

	public void setFont(Font font) {
		defaultFont = font;
		promptText.setFont(font.getFont());
		applyText();
		positionCaret(caretPos.get());
		displaySelection();

		caret.setHeight(lineHeight());
	}

	public StringProperty textProperty() {
		return textProperty;
	}

	public String getText() {
		return textProperty.get();
	}

	public void setText(String text) {
		this.text = new StringBuilder(text);
		applyText();
		caretPos.set(Math.min(length(), caretPos.get()));
		displaySelection();
	}

	public void setPrompt(String string) {
		promptText.setText(string);
	}

	public Paint getTextFill() {
		return fill.get();
	}

	public void setTextFill(Paint fill) {
		this.fill.set(fill);
	}

	public ObjectProperty<Paint> textFillProperty() {
		return fill;
	}

	public ObjectProperty<IndexRange> selectionProperty() {
		return selection;
	}

	public IndexRange getSelection() {
		return selection.get();
	}

	private double lineHeight() {
		Text t = new Text("0");
		t.setFont(defaultFont.getFont());
		return t.getLayoutBounds().getHeight();
	}

	private double textHeight() {
		if (text.isEmpty()) {
			return lineHeight();
		}

		PathElement[] pathElements = textDisp.caretShape(length(), true);
		Path path = new Path(pathElements);
		return path.getLayoutBounds().getMaxY();
	}

	private int indexForEvent(MouseEvent e) {
		double x = e.getX() - getPadding().getLeft();
		double y = e.getY() - getPadding().getTop();

		HitInfo inf = textDisp.hitTest(new Point2D(x, y));

		if (y < 0) {
			return 0;
		}
		return Math.max(0, Math.min(length(), inf.getInsertionIndex()));
	}

	private void loadState(int index) {
		historyPos = index;
		State s = history.get(index);
		text = new StringBuilder(s.text);
		applyText();
		selection.set(s.selection);
		displaySelection();
		caretPos.set(s.caretPos);
	}

	private void saveState() {
		for (int p = historyPos + 1; p < history.size();) {
			history.remove(p);
		}

		State s = new State(text.toString(), selection.get(), caretPos.get());
		history.add(s);
		historyPos = history.size() - 1;
	}

	private synchronized void append(String s) {
		s = s.replaceAll("\\R", "\n");

		int oldSize = length();

		if (selected()) {
			IndexRange range = selection.get();
			text.replace(range.getStart(), range.getEnd(), s);
		} else {
			text.insert(posToPos(caretPos.get()), s);
		}

		applyText();

		int dSize = length() - oldSize;
		caretPos.set(caretPos.get() + dSize);

		clearSelection(-1);
	}

	private void displaySelection() {
		for (Node n : textDisp.getChildren()) {
			((InputElement) n).deselect(this);
		}

		GraphicsContext gc = selectionPane.getGraphicsContext2D();
		gc.clearRect(0, 0, selectionPane.getWidth(), selectionPane.getHeight());

		if (selected()) {
			IndexRange r = selection.get();
			IndexRange range = IndexRange.normalize(r.getStart(), r.getEnd());
			for (int i = range.getStart(); i < range.getEnd() && i < length(); i++) {
				InputElement n = (InputElement) textDisp.getChildren().get(i);
				n.select(this);

				Point2D point = indexToPoint(i);

				gc.setFill(Color.web("#328fff"));
				gc.fillRect(point.getX(), point.getY() - 2, Math.max(3, n.width()),
						lineHeight() + textDisp.getLineSpacing());
			}
		}
	}

	private void moveSelectionTo(int index) {
		if (selected()) {
			IndexRange r = selection.get();
			updateSelection(r.getStart() == caretPos.get() ? r.getEnd() : r.getStart(), index);
		} else {
			updateSelection(caretPos.get(), index);
		}
	}

	private void updateSelection(int start, int end) {
		if (start == end) {
			selection.set(new IndexRange(-1, -1));
		} else {
			selection.set(IndexRange.normalize(start, end));
		}

		displaySelection();
	}

	private void clearSelection(int pos) {
		selection.set(new IndexRange(-1, -1));
		displaySelection();
		if (pos != -1)
			caretPos.set(pos);
	}

	private void deleteSelection() {
		if (selected()) {
			IndexRange r = selection.get();
			text.delete(posToPos(r.getStart()), posToPos(r.getEnd()));
			applyText();
			clearSelection(r.getStart());
		}
	}

	private boolean selected() {
		return selection.get() != null && selection.get().getLength() != 0 && selection.get().getStart() != -1
				&& selection.get().getEnd() != -1;
	}

	private int decrementCaretPos() {
		return Math.max(0, caretPos.get() - 1);
	}

	private int incrementCaretPos() {
		return Math.min(length(), caretPos.get() + 1);
	}

	private void keepCaret() {
		hideCaret.stop();
		showCaret.stop();
		caret.setOpacity(1);
		hideCaret.playFromStart();
	}

	private int posToPos(int index) {
		int p = 0;
		int res = 0;

		for (Node n : textDisp.getChildren()) {
			if (p >= index) {
				break;
			}
			InputElement ie = (InputElement) n;
			res += ie.getValue().length();
			p++;
		}

		return res;
	}

	private void positionCaret(int pos) {
		Point2D point = indexToPoint(pos);
		caret.setTranslateX(point.getX());
		caret.setTranslateY(point.getY() - 1);
	}

	private Point2D indexToPoint(int index) {
		PathElement[] pathElements = textDisp.caretShape(index, true);
		Path path = new Path(pathElements);
		Bounds b = path.getLayoutBounds();
		return new Point2D((int) b.getMinX() + 2.0, (int) b.getMinY() + 2.0);
	}

	private int length() {
		return textDisp.getChildren().size();
	}

	private static class State {
		String text;
		IndexRange selection;
		int caretPos;

		public State(String text, IndexRange selection, int caretPos) {
			this.text = text;
			this.selection = selection;
			this.caretPos = caretPos;
		}

		@Override
		public String toString() {
			return text;
		}
	}
	
	private void applyText() {
		String txt = text.toString();
		
		textProperty.set(txt);
		
		RichText.applyText(textDisp, txt, defaultFont);
	}
}
