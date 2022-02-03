package mesa.gui.window.content;

//import javafx.collections.ObservableList;
//import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.paint.Color;
//import javafx.stage.Window;
//import mesa.app.utils.Threaded;

public class TransparentScene extends javafx.scene.Scene {
	public TransparentScene(Parent root, double width, double height) {
		super(root, width, height);

		focusOwnerProperty().addListener((obs, ov, nv) -> {
//			if (nv != null)
//				System.out.println(nv.getClass().getSimpleName());
//			else
//				System.out.println(nv);
		});

//		count();

		setFill(Color.TRANSPARENT);
	}

//	private void count() {
//		int sum = 0;
//		
//		for(Window window : Window.getWindows()) {
//			Parent root = window.getScene().getRoot();
//			sum += recursiveCount(root);
//		}
//		
//		System.out.println(sum);
//		
//		Threaded.runAfter(2000, this::count);
//	}
//	
//	private int recursiveCount(Parent parent) {
//		ObservableList<Node> children = parent.getChildrenUnmodifiable();
//		int res = children.size();
//		for(Node n : children) {
//			if(n instanceof Parent p) {
//				res += recursiveCount(p);
//			}
//		}
//		return res;
//	}
}
