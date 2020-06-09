package session;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import targets.DragTarget;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import fragments.Fragment;

public class KHintController implements Initializable {


	@FXML
	private ListView<DragTarget> orderList;
	
	@FXML
    private TextArea descBox = new TextArea();


	private ObservableList<DragTarget> targets;
	private KHintProblem myProblem;

	public KHintController(KHintProblem problem) {
		this.targets = FXCollections.observableArrayList(problem.getTargets());
		this.myProblem = problem;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		orderList.setItems(targets);
		orderList.setCellFactory(param -> new TargetCell());
		
		this.descBox.setText(myProblem.getDescription());
		//orderList.setPrefWidth(180);

        //VBox layout = new VBox(orderList);
        //layout.setPadding(new Insets(10));
	}
	
	private class TargetCell extends ListCell<DragTarget> {

        public TargetCell() {
            ListCell<DragTarget> thisCell = this;

            setContentDisplay(ContentDisplay.TEXT_ONLY);
            setAlignment(Pos.CENTER_LEFT);

            setOnDragDetected(event -> {
                if (getItem() == null) { 
                	event.consume();
                    return;
                }

                ObservableList<DragTarget> items = getListView().getItems();

                Dragboard dragboard = startDragAndDrop(TransferMode.MOVE);
                ClipboardContent content = new ClipboardContent();
                content.putString(String.valueOf(items.indexOf(getItem())));
                //dragboard.setDragView();  Maybe I'll find some icon to use here, but it appears broken on Linux so I can't test

                dragboard.setContent(content);

                event.consume();
                
            });

            setOnDragOver(event -> {
                if (event.getGestureSource() != thisCell &&
                       event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.MOVE);
                }

                event.consume();
            });

            setOnDragEntered(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    setOpacity(0.3);
                }
            });

            setOnDragExited(event -> {
                if (event.getGestureSource() != thisCell &&
                        event.getDragboard().hasString()) {
                    setOpacity(1);
                }
            });

            setOnDragDropped(event -> {
                if (getItem() == null) { 
                	event.consume();
                    return;
                }                
            
                Dragboard db = event.getDragboard();	                
                boolean success = false;
                
                if (db.hasString()) {
                    int draggedIdx = Integer.valueOf(db.getString());


                    DragTarget tempTarg = targets.get(draggedIdx);
                    Fragment tempFrag = tempTarg.getCurrentFrag();
                    tempTarg.setCurrentFrag(getItem().getCurrentFrag());
                    getItem().setCurrentFrag(tempFrag);

                    // For some reason using getListView().refresh() was highlighting random rows
                    // Doing a copy and set seems to resolve this issue
                    List<DragTarget> itemscopy = new ArrayList<>(getListView().getItems());
                    getListView().getItems().setAll(itemscopy);

                    success = true;
                }

                event.setDropCompleted(success);
            	
                event.consume();
            });            

            setOnDragDone(DragEvent::consume);
        }

        @Override
        protected void updateItem(DragTarget item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
            	setText(null);
            } else {
                setText(item.toString());
                if (item.isSolved()) {
                	setStyle("-fx-control-inner-background: green;");
                	// It's solved, so no more dragging and dropping here
                	this.setOnDragDetected(null);
                	this.setOnDragOver(null);
                	this.setOnDragDropped(null);
                } else {
                	setStyle("-fx-control-inner-background: white;");
                }
                	
            }
        }
    }

}