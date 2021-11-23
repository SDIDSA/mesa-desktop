package mesa.gui.controls.image;

import java.util.ArrayList;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class LayerIcon extends StackPane {
	private ArrayList<ColorIcon> layers;
	private double size;
	
	public LayerIcon(double size, String...layers) {
		this.size = size;
		
		this.layers = new ArrayList<>();
		
		for(String layer:layers) {
			addLayer(layer);
		}
	}
	
	public void addLayer(String name) {
		ColorIcon layer = new ColorIcon(name, size);
		
		setMaxSize(layer.getMaxWidth(), layer.getMaxHeight());
		
		layers.add(layer);
		getChildren().add(layer);
	}
	
	public void setFill(int layer, Color fill) {
		layers.get(layer).setFill(fill);
	}
}
