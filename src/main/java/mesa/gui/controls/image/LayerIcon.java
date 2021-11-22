package mesa.gui.controls.image;

import java.util.ArrayList;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class LayerIcon extends StackPane {
	private ArrayList<ColorIcon> layers;
	private double size;
	
	public LayerIcon(double size) {
		this.size = size;
		
		layers = new ArrayList<>();
	}
	
	public void addLayer(String name) {
		ColorIcon layer = new ColorIcon(name, size);
		
		setMinSize(layer.getMinWidth(), layer.getMinHeight());
		setMaxSize(layer.getMaxWidth(), layer.getMaxHeight());
		
		layers.add(layer);
		getChildren().add(layer);
	}
	
	public void setFill(int layer, Color fill) {
		layers.get(layer).setFill(fill);
	}
}
